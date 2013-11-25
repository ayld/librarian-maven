package librarian.plugin;

import librarian.version.VersionInfoDao;
import librarian.version.impl.JsonVersionInfoDao;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Created with IntelliJ IDEA.
 * User: siliev
 * Date: 11/25/13
 * Time: 11:37 AM
 */
@Mojo( name = "update", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class UpdateMojo extends AbstractMojo{

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    private VersionInfoDao dao;

    private final Log logger = getLog();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // FIXME, this needs to be injected ...
        // needed because I can't find a hook that executes after properties are injected ... stupid plexus
        // or at least a way for constructor injection ...
        dao = new JsonVersionInfoDao(project.getBasedir());


    }
}
