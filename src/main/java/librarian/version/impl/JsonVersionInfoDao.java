package librarian.version.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import librarian.model.VersionInfo;
import librarian.version.VersionInfoDao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ayld
 * Date: 11/23/13
 * Time: 6:35 PM
 */
public class JsonVersionInfoDao implements VersionInfoDao {

    private static final String PERSISTED_FILE_NAME = "versions.json";

    private final File persistDir;
    private final Gson gson = new Gson();

    public JsonVersionInfoDao(File persistDir) {
        if (!persistDir.exists() || !persistDir.isDirectory()) {
            throw new IllegalArgumentException("persist directory doesn't exist or is not a directory");
        }
        this.persistDir = persistDir;
    }

    @Override
    public Set<VersionInfo> read() {
        final File versionsFile = getVersionsFile();
        if (!versionsFile.exists()) {
            throw new IllegalStateException(versionsFile.getAbsolutePath() + " not found");
        }

        final URL versionsFileUrl = Resources.getResource(versionsFile.getAbsolutePath());
        final String[] lines;
        try {

            lines = Resources.toString(versionsFileUrl, Charsets.UTF_8).split("\n");

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        final Set<VersionInfo> result = Sets.newHashSetWithExpectedSize(lines.length);
        for (String line : lines) {
            result.add(gson.fromJson(line, VersionInfo.class));
        }

        return result;
    }

    @Override
    public void persist(Set<VersionInfo> infos) {
        final Set<String> infosJson = Sets.newHashSetWithExpectedSize(infos.size());
        for (VersionInfo info : infos) {
            infosJson.add(gson.toJson(info));
        }

        FileWriter writer = null;
        try {

            final File versionsFile = getVersionsFile();
            Files.touch(versionsFile);

            writer = new FileWriter(versionsFile);
            for (String info : infosJson) {
                writer.write(info);
                writer.write("\n");
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

    private File getVersionsFile() {
        return new File(Joiner.on(File.separator).join(persistDir.getAbsolutePath(), PERSISTED_FILE_NAME));
    }
}
