package com.coigniez.resumebuilder.file;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.io.File.separator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String userId) {
        final String fileUploadSubPath = "users" + separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    public void deleteFile(
            @Nonnull String filePath) {
        Path targetPath = Paths.get(filePath);
        try {
            Files.delete(targetPath);
            log.info("File deleted: " + filePath);
        } catch (IOException e) {
            log.error("File was not deleted", e);
        }
    }

    private String uploadFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String fileUploadSubPath) {
        try {
            final Path finalUploadPath = Paths.get(fileUploadPath, fileUploadSubPath);
            
            // Create directories with proper permissions
            Files.createDirectories(finalUploadPath);
            
            // Generate unique file path
            final String originalFilename = sourceFile.getOriginalFilename();
            final String fileExtension = getFileExtension(originalFilename);
            final String baseName = originalFilename != null && originalFilename.lastIndexOf(".") > 0 
                ? originalFilename.substring(0, originalFilename.lastIndexOf(".")) 
                : "file";
            
            Path targetPath = createUniqueFilePath(finalUploadPath, baseName, fileExtension);
            
            // Write file
            Files.write(targetPath, sourceFile.getBytes(), StandardOpenOption.CREATE_NEW);
            log.info("File saved to: {}", targetPath);
            return targetPath.toString();
            
        } catch (IOException e) {
            log.error("Failed to save file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save file", e);
        }
    }
    
    private Path createUniqueFilePath(Path directory, String baseName, String extension) {
        Path filePath = directory.resolve(baseName + "." + extension);
        int counter = 1;
        
        while (Files.exists(filePath)) {
            filePath = directory.resolve(baseName + "_" + counter + "." + extension);
            counter++;
        }
        
        return filePath;
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
