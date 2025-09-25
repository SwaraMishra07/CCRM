package edu.ccrm.util;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Recursively backup the entire data folder to a timestamped folder.
     * Prints total backup size.
     */
    public static void backupData(Path dataFolder) throws IOException {
        if (!Files.exists(dataFolder)) {
            System.out.println("No data folder found to backup.");
            return;
        }

        String timestamp = LocalDateTime.now().format(formatter);
        Path backupFolder = dataFolder.resolveSibling("backup_" + timestamp);
        Files.createDirectories(backupFolder);

        // Recursively copy all files and subfolders
        Files.walkFileTree(dataFolder, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = backupFolder.resolve(dataFolder.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = backupFolder.resolve(dataFolder.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });

        long totalSize = calculateSize(backupFolder);
        System.out.println("Backup completed: " + backupFolder);
        System.out.println("Total backup size: " + totalSize + " bytes");
    }

    /**
     * Calculate the total size of a folder recursively.
     */
    private static long calculateSize(Path folder) throws IOException {
        final long[] size = {0};
        Files.walkFileTree(folder, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                size[0] += attrs.size();
                return FileVisitResult.CONTINUE;
            }
        });
        return size[0];
    }
}
