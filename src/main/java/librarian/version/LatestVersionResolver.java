package librarian.version;

import org.apache.maven.artifact.Artifact;

public interface LatestVersionResolver {

	Artifact getlatestVersion(Artifact of);
}
