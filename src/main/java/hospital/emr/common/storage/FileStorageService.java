package hospital.emr.common.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

/**
 * Minimal local-disk file storage. Returns public URLs under /uploads/**.
 * Swap the implementation for S3-compatible storage later without changing callers.
 */
@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload directory: " + root, e);
        }
    }

    public String store(MultipartFile file, String subdirectory) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        String original = StringUtils.cleanPath(Objects.requireNonNullElse(file.getOriginalFilename(), "file"));
        String extension = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            extension = original.substring(dot);
        }
        String filename = UUID.randomUUID() + extension;

        Path targetDir = root.resolve(subdirectory).normalize();
        if (!targetDir.startsWith(root)) {
            throw new IllegalArgumentException("Invalid upload path");
        }
        Path target = targetDir.resolve(filename);
        try {
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file: " + e.getMessage(), e);
        }
        return "/uploads/" + subdirectory + "/" + filename;
    }
}
