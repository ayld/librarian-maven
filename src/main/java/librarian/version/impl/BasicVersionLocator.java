package librarian.version.impl;

import librarian.version.LatestVersionResolver;

import org.apache.maven.artifact.Artifact;

public class BasicVersionLocator implements LatestVersionResolver{

	@Override
	public Artifact getlatestVersion(Artifact of) {
		return null;
	}
}
