package librarian.model;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * Created with IntelliJ IDEA.
 * User: ayld
 * Date: 11/23/13
 * Time: 6:35 PM
 */
public class VersionInfo {
    private final boolean isLatest;
    private final String current;
    private final String latest;
    private final String artifactName;

    private VersionInfo(boolean isLatest, String current, String latest, String artifactName) {
        this.isLatest = isLatest;
        this.current = current;
        this.latest = latest;
        this.artifactName = artifactName;
    }

    public boolean isLatest() {
        return isLatest;
    }

    public String getCurrent() {
        return current;
    }

    public String getLatest() {
        return latest;
    }

    public String getArtifactName() {
        return artifactName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isLatest, latest, current, artifactName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return  false;
        }
        if (!(obj instanceof VersionInfo)) {
            return false;
        }

        final VersionInfo other = (VersionInfo) obj;

        return Objects.equal(isLatest, other.isLatest)
               &&
               Objects.equal(artifactName, other.getArtifactName())
               &&
               Objects.equal(current, other.getCurrent())
               &&
               Objects.equal(latest, other.getLatest());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", artifactName)
                .add("isLatest", isLatest)
                .add("latest", latest)
                .add("current", current)
                .toString();

    }

    public static class VersionInfoBuilder {

        private boolean isLatest;
        private String current;
        private String latest;
        private String artifactName;

        public VersionInfoBuilder() {
        }

        public VersionInfoBuilder isLatest(boolean latest) {
            isLatest = latest;
            return this;
        }

        public VersionInfoBuilder setCurrent(String current) {
            this.current = current;
            return this;
        }

        public VersionInfoBuilder setLatest(String latest) {
            this.latest = latest;
            return this;
        }

        public VersionInfoBuilder setArtifactName(String artifactName) {
            this.artifactName = artifactName;
            return this;
        }

        public VersionInfo build() {
            if (Strings.isNullOrEmpty(current)) {
                throw new IllegalArgumentException("current version not set");
            }
            if (Strings.isNullOrEmpty(latest)) {
                throw new IllegalArgumentException("latest not set");
            }
            if (Strings.isNullOrEmpty(artifactName)) {
                throw new IllegalArgumentException("artifact name not set");
            }
            return new VersionInfo(isLatest, current, latest, artifactName);
        }
    }
}
