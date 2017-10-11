package be.algielen.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class FileStore {

    private static final Properties properties = loadProperties();
    private static final Path rootDirectory = loadRootDirectory();

    private static Properties loadProperties() {
        try {
            Properties properties = new Properties();
            InputStream resource = FileStore.class.getClassLoader()
                .getResourceAsStream("filestore.properties");
            properties.load(resource);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Filestore could not locate root directory", e);
        }
    }

    private static Path loadRootDirectory() {
        String rootdir = properties.getProperty("rootdir");
        rootdir = rootdir.replace("~", System.getProperty("user.home"));
        return Paths.get(rootdir);
    }

    public void addFile(String filename, InputStream inputStream) throws IOException {
        Path filepath = rootDirectory.resolve(filename);
        Files.copy(inputStream, filepath);
    }

    public byte[] getFile(String filename) throws IOException {
        Path filepath = rootDirectory.resolve(filename);
        byte[] content = Files.readAllBytes(filepath);
        return content;
    }
}
