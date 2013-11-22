package librarian.plugin;

import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo( name = "sync", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class SyncMojo extends AbstractMojo {
	
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

    @SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
    	final Set<Artifact> arts = project.getDependencyArtifacts();
    	for (Artifact artifact : arts) {
			try {
				
				final List<ArtifactVersion> versions = metadataSource.retrieveAvailableVersions(artifact, localRepository, remoteRepositories);
				System.out.println(versions);
				
			} catch (ArtifactMetadataRetrievalException e) {
				e.printStackTrace();
			}
		}
    }
}
