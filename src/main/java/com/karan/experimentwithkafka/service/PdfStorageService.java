package com.karan.experimentwithkafka.service;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PdfStorageService {

    // Directory to store PDF files
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Documents/uploads/";

    public String savePdf(byte[] pdfBytes, String fileName) throws IOException {
        // Ensure the directory exists
        Path uploadPath = Path.of(UPLOAD_DIR);
        Files.createDirectories(uploadPath);

        // Full file path
        Path filePath = uploadPath.resolve(fileName);

        // Write PDF bytes to file
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(pdfBytes);
        }

        // Return file path as a string (you could return a download URL if hosted somewhere)
        return filePath.toAbsolutePath().toString();
    }
}
