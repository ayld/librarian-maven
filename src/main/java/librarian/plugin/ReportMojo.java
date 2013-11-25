package librarian.plugin;

import com.google.common.collect.Sets;
import librarian.model.VersionInfo;
import librarian.version.VersionInfoDao;
import librarian.version.impl.JsonVersionInfoDao;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;
import java.util.Set;

@Mojo( name = "report", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class ReportMojo extends AbstractMojo {
	
    @Parameter(defaultValue = "${project}")
    private MavenProject project;
    
    @Parameter(defaultValue="${localRepository}")
    private ArtifactRepository localRepository;
    
    @Parameter(defaultValue="${project.remoteArtifactRepositories}")
    private List<ArtifactRepository> remoteRepositories;
    
    @Component
    private ArtifactResolver resolver;
    
    @Component
    private ArtifactMetadataSource metadataSource;

    private VersionInfoDao dao;

    private final Log logger = getLog();

    @SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
        // FIXME, this needs to be injected ...
        // needed because I can't find a hook that executes after properties are injected ... stupid plexus
        dao = new JsonVersionInfoDao(project.getBasedir());

        final Set<Artifact> arts = project.getDependencyArtifacts();
        final Set<VersionInfo> artifactVersions = Sets.newHashSetWithExpectedSize(arts.size());
        for (Artifact artifact : arts) {
			try {

				final List<ArtifactVersion> versions = metadataSource.retrieveAvailableVersions(artifact, localRepository, remoteRepositories);
                final String latestVersion = latest(versions).toString();

                artifactVersions.add(
                    new VersionInfo.VersionInfoBuilder()
                        .isLatest(artifact.getVersion().equals(latestVersion))
                        .setCurrent(artifact.getVersion())
                        .setLatest(latestVersion)
                        .setArtifactName(artifact.getId())
                        .build()
                );

			} catch (ArtifactMetadataRetrievalException e) {
				e.printStackTrace();
			}
		}

        dao.persist(artifactVersions);

        for (VersionInfo info : artifactVersions) {
            logger.info(info.toString());
        }
    }

    private ArtifactVersion latest(List<ArtifactVersion> versions) {
        return versions.get(versions.size() - 1);
    }

}
