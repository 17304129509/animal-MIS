package com.campus.animal.controller;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.common.BusinessException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    @Value("${app.upload-dir}")
    private String uploadDir;

    @PostMapping
    public ApiResponse<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw BusinessException.badRequest("请选择文件");
        }
        if (file.getSize() > 5L * 1024 * 1024) {
            throw BusinessException.badRequest("文件大小不能超过5MB");
        }
        byte[] header;
        try (var input = file.getInputStream()) {
            header = input.readNBytes(16);
        }
        String extension = extension(header);
        Path directory = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(directory);
        String filename = UUID.randomUUID() + extension;
        file.transferTo(directory.resolve(filename));
        return ApiResponse.ok(Map.of("url", "/uploads/" + filename, "filename", filename));
    }

    private String extension(byte[] header) {
        if (matches(header, 0, 0xFF, 0xD8, 0xFF)) return ".jpg";
        if (matches(header, 0, 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)) return ".png";
        if (matches(header, 0, 'G', 'I', 'F', '8')) return ".gif";
        if (matches(header, 0, 'B', 'M')) return ".bmp";
        if (matches(header, 0, 'R', 'I', 'F', 'F') && matches(header, 8, 'W', 'E', 'B', 'P')) return ".webp";
        if (matches(header, 4, 'f', 't', 'y', 'p', 'a', 'v', 'i', 'f')) return ".avif";
        throw BusinessException.badRequest("仅支持 JPEG、PNG、GIF、BMP、WebP 和 AVIF 图片");
    }

    private boolean matches(byte[] header, int offset, int... signature) {
        if (header.length < offset + signature.length) return false;
        for (int index = 0; index < signature.length; index++) {
            if (Byte.toUnsignedInt(header[offset + index]) != signature[index]) return false;
        }
        return true;
    }
}
