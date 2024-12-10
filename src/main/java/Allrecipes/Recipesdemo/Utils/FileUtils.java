package Allrecipes.Recipesdemo.Utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileUtils {

    public static void saveFile(byte[] bytes, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        // Ensure parent directories exist
        Files.createDirectories(path.getParent());
        Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static byte[] readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new NoSuchFileException("File not found: " + filePath);
        }
        return Files.readAllBytes(path);
    }

    public static void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }

    public static boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

    /**
     * Lists all files in the specified directory.
     *
     * @param directoryPath The directory path.
     * @return List of file names.
     * @throws IOException if an I/O error occurs.
     */
    public static List<String> listFiles(String directoryPath) throws IOException {
        Path dirPath = Paths.get(directoryPath);
        if (!Files.isDirectory(dirPath)) {
            throw new NotDirectoryException("Not a directory: " + directoryPath);
        }
        return Files.list(dirPath)
                .filter(Files::isRegularFile)
                .map(path -> path.getFileName().toString())
                .toList();
    }

    /**
     * Copies a file from source to destination.
     *
     * @param sourcePath      The source file path.
     * @param destinationPath The destination file path.
     * @throws IOException if an I/O error occurs.
     */
    public static void copyFile(String sourcePath, String destinationPath) throws IOException {
        Path src = Paths.get(sourcePath);
        Path dest = Paths.get(destinationPath);
        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Moves a file from source to destination.
     *
     * @param sourcePath      The source file path.
     * @param destinationPath The destination file path.
     * @throws IOException if an I/O error occurs.
     */
    public static void moveFile(String sourcePath, String destinationPath) throws IOException {
        Path src = Paths.get(sourcePath);
        Path dest = Paths.get(destinationPath);
        Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Reads all lines from a text file.
     *
     * @param filePath The text file path.
     * @return List of lines.
     * @throws IOException if an I/O error occurs.
     */
    public static List<String> readAllLines(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path);
    }

    /**
     * Writes lines to a text file.
     *
     * @param lines    The lines to write.
     * @param filePath The destination file path.
     * @throws IOException if an I/O error occurs.
     */
    public static void writeAllLines(List<String> lines, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        // Ensure parent directories exist
        Files.createDirectories(path.getParent());
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // Additional file-related utility methods can be added here
}
