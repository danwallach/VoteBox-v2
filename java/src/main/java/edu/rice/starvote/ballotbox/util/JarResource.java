package edu.rice.starvote.ballotbox.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class that simplifies access to resource files when the application is packaged in a JAR. When the class
 * is loaded, it creates a directory inside the default OS temporary file location (typically `/tmp` on Linux), to
 * which resources are extracted to on demand, where they can be accessed via the native filesystem. This directory
 * and its contents are deleted upon termination of the JVM.
 *
 * Although this class is designed for use in a JAR application, it can be used to access any resource on the resource
 * path regardless of whether or not the application is packaged in a JAR.
 *
 * @author luejerry
 */
public class JarResource {

    private static Map<String, Path> resources = new ConcurrentHashMap<>();
    private static Path tempDir;

    static {
        try {
            tempDir = Files.createTempDirectory("starvote");
            tempDir.toFile().deleteOnExit();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Access a resource file. On first access to a resource, the file is copied (whether from the filesystem or
     * application JAR) to a temporary directory. Subsequent requests for the same resource are directed to the copied
     * file. The file is deleted upon termination of the JVM.
     *
     * The path to the requested resource is relative to the resource path: `src/main/resources` in a default Gradle
     * project structure. For example, to access a resource `src/main/resources/test.py`, simply use
     * `getResource("test.py")`. The method of access is identical whether the application is unpackaged or in a
     * JAR.
     *
     * The extracted file is created with read/write/execute permissions for owner, read-only for group, and no access
     * for everyone.
     *
     * @param path Path to the requested resource file, relative to the resource path.
     * @return Path to the extracted copy of the resource file on the filesystem, or null if the resource could not be
     * found.
     * @throws UncheckedIOException If an error occurs copying the resource to disk.
     */
    public static Path getResource(String path) {
        if (resources.containsKey(path)) {
            return resources.get(path);
        } else {
            final InputStream resourceStream = JarResource.class.getClassLoader().getResourceAsStream(path);
            if (resourceStream == null) {
                return null;
            }
            try { // IOException
                final Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-----");
                final FileAttribute<Set<PosixFilePermission>> permissions = PosixFilePermissions.asFileAttribute(perms);
                final Path filePath = Files.createFile(tempDir.resolve(path), permissions);
                filePath.toFile().deleteOnExit();
                final BufferedInputStream reader = new BufferedInputStream(resourceStream);
                Files.copy(reader, filePath, StandardCopyOption.REPLACE_EXISTING);
                reader.close();
                Files.setPosixFilePermissions(filePath, perms);
                resources.put(path, filePath);
                System.out.println("Extracted temporary resource to " + filePath.toAbsolutePath().toString());
                return filePath;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
