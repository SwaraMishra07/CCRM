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
     * Uses Files.walkFileTree (a strong demonstration of NIO.2).
     */
    public static Path backupData(Path dataFolder) throws IOException { // Changed return type to Path
        if (!Files.exists(dataFolder)) {
            System.out.println("No data folder found to backup.");
            return null;
        }

        String timestamp = LocalDateTime.now().format(formatter);
        Path backupFolder = dataFolder.resolveSibling("backup_" + timestamp);
        Files.createDirectories(backupFolder);

        // Recursively copy all files and subfolders using Files.walkFileTree (robust method)
        Files.walkFileTree(dataFolder, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // Ensure we don't try to copy the root data folder into itself if dataFolder is used directly
                if (dir.equals(dataFolder)) {
                    return FileVisitResult.CONTINUE;
                }
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

        System.out.println("Backup completed: " + backupFolder.getFileName());
        
        // Return the path for use in the CLI (to display size)
        return backupFolder; 
    }

    /**
     * Calculate the total size of a folder recursively (mandatory recursion/Stream API demo).
     * @param folder The folder to calculate size for (e.g., the root backup folder).
     * @return The total size in bytes.
     */
    public static long calculateSizeRecursive(Path folder) throws IOException {
        if (!Files.exists(folder)) return 0;
        
        // Use Files.walk() and the Stream API for aggregation (MANDATORY REQUIREMENT)
        try (var walk = Files.walk(folder)) {
            return walk
                // 1. Filter only regular files
                .filter(Files::isRegularFile) 
                // 2. Map path to file size (long)
                .mapToLong(p -> {
                    try {
                        return Files.size(p);
                    } catch (IOException e) {
                        // Exception handling within Stream pipeline
                        System.err.println("Warning: Could not get size for " + p + " during recursion.");
                        return 0L;
                    }
                })
                // 3. Aggregate the sum
                .sum(); 
        }
    }
}