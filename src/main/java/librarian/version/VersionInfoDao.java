package librarian.version;

import librarian.model.VersionInfo;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ayld
 * Date: 11/23/13
 * Time: 6:33 PM
 */
public interface VersionInfoDao {

    Set<VersionInfo> read();

    void persist(Set<VersionInfo> infos);
}
