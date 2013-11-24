package librarian.version.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import librarian.model.VersionInfo;
import librarian.version.VersionInfoPersister;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ayld
 * Date: 11/23/13
 * Time: 6:35 PM
 */
public class JsonVersionInfoPersister implements VersionInfoPersister {

    private static final String PERSISTED_FILE_NAME = "versions.json";

    private final MavenProject project;
    private final Gson gson = new Gson();

    public JsonVersionInfoPersister(MavenProject project) {
        this.project = project;
    }

    @Override
    public void persist(Set<VersionInfo> infos) {
        final Set<String> infosJson = Sets.newHashSetWithExpectedSize(infos.size());
        for (VersionInfo info : infos) {
            infosJson.add(gson.toJson(info));
        }

        FileWriter writer = null;
        try {

            final File versionsFile = new File(Joiner.on(File.separator).join(project.getBasedir().getAbsolutePath(), PERSISTED_FILE_NAME));
            Files.touch(versionsFile);

            writer = new FileWriter(versionsFile);
            for (String info : infosJson) {
                writer.write(info);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
