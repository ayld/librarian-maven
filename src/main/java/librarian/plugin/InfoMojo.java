package librarian.plugin;

import com.google.common.collect.Sets;
import librarian.model.VersionInfo;
import librarian.version.VersionInfoPersister;
import librarian.version.impl.JsonVersionInfoPersister;
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

@Mojo( name = "info", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class InfoMojo extends AbstractMojo {
	
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

    private VersionInfoPersister persister;

    private final Log logger = getLog();

    @SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
        // FIXME, this needs to be injected ...
        // needed because I can't find a hook that executes after properties are injected ... stupid plexus
        persister = new JsonVersionInfoPersister(project);

    	final Set<Artifact> arts = project.getDependencyArtifacts();
        final Set<VersionInfo> artifactVersions = Sets.newHashSetWithExpectedSize(arts.size());
        for (Artifact artifact : arts) {
			try {

				final List<ArtifactVersion> versions = metadataSource.retrieveAvailableVersions(artifact, localRepository, remoteRepositories);
                artifactVersions.add(
                    new VersionInfo.VersionInfoBuilder()
                        .isLatest(artifact.getVersion().equals(latest(versions).toString()))
                        .setCurrent(artifact.getVersion())
                        .setLatest(latest(versions).toString())
                        .setArtifactName(artifact.getId())
                        .build()
                );

			} catch (ArtifactMetadataRetrievalException e) {
				e.printStackTrace();
			}
		}

        persister.persist(artifactVersions);

        for (VersionInfo info : artifactVersions) {
            logger.info(info.toString());
        }
    }

    private ArtifactVersion latest(List<ArtifactVersion> versions) {
        return versions.get(versions.size() - 1);
    }

}
