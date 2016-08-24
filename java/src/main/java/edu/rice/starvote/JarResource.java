package edu.rice.starvote;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luej on 8/24/16.
 */
public class JarResource {

    private static Map<String, Path> resources = new ConcurrentHashMap<>();
    private static Path tempDir;

    static {
        try {
            tempDir = Files.createTempDirectory("starvote");
            tempDir.toFile().deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected I/O error");
        }
    }

    public static Path getResource(String path) {
        if (resources.containsKey(path)) {
            return resources.get(path);
        } else {
            final InputStream resourceStream = JarResource.class.getClassLoader().getResourceAsStream(path);
            if (resourceStream == null) {
                throw new RuntimeException("Resource failed to load");
            }
            try {
                final FileAttribute<Set<PosixFilePermission>> permissions = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-----"));

                final Path filePath = Files.createFile(tempDir.resolve(path), permissions);
                filePath.toFile().deleteOnExit();
                final BufferedInputStream reader = new BufferedInputStream(resourceStream);
                Files.copy(reader, filePath, StandardCopyOption.REPLACE_EXISTING);
                reader.close();
                resources.put(path, filePath);
                System.out.println("Extracted temporary resource to " + filePath.toAbsolutePath().toString());
                return filePath;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Unexpected I/O error");
            }
        }
    }
}
