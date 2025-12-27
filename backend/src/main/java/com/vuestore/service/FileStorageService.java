package com.vuestore.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
  private final Path uploadPath;
  private final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

  public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
    this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
  }

  public String store(MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    Files.createDirectories(uploadPath);
    String originalName = file.getOriginalFilename() == null ? "image" : file.getOriginalFilename();
    originalName = originalName.replace("\\", "/");
    if (originalName.contains("/")) {
      originalName = originalName.substring(originalName.lastIndexOf('/') + 1);
    }
    String extension = "";
    int dot = originalName.lastIndexOf('.');
    if (dot > -1) {
      extension = originalName.substring(dot);
    }
    String fileName = UUID.randomUUID() + extension;
    Path target = uploadPath.resolve(fileName);
    try {
      Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ex) {
      logger.warn("Failed to store file {}: {}", originalName, ex.getMessage());
      throw ex;
    }
    return "/uploads/" + fileName;
  }
}
