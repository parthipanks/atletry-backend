package com.atletry.service;

import com.atletry.config.AtletryProperties;
import com.atletry.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

    private static final Set<String> ALLOWED_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp", "image/svg+xml");
    private static final long MAX_BYTES = 5L * 1024 * 1024;

    private final S3Client          s3;
    private final AtletryProperties props;

    public record UploadResult(String url, String s3Key) {}

    /** Uploads a validated image to the given S3 folder and returns URL + key. */
    public UploadResult uploadImage(MultipartFile file, String folder) {
        validate(file);

        String key    = folder + "/" + UUID.randomUUID() + extension(file.getContentType());
        String bucket = props.getAws().getS3().getBucket();

        try {
            s3.putObject(
                PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build(),
                RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        }

        String url = "https://%s.s3.%s.amazonaws.com/%s"
                .formatted(bucket, props.getAws().getRegion(), key);
        log.info("Image uploaded to {} → {}", folder, url);
        return new UploadResult(url, key);
    }

    /** Kept for backward compatibility with SportService. */
    public String uploadSportIcon(MultipartFile file) {
        return uploadImage(file, "sports/icons").url();
    }

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new BadRequestException("Image file must not be empty");
        if (!ALLOWED_TYPES.contains(file.getContentType()))
            throw new BadRequestException("Unsupported file type. Allowed: JPEG, PNG, WebP, SVG");
        if (file.getSize() > MAX_BYTES)
            throw new BadRequestException("Image file exceeds 5 MB limit");
    }

    private String extension(String contentType) {
        return switch (contentType) {
            case "image/jpeg"    -> ".jpg";
            case "image/png"     -> ".png";
            case "image/webp"    -> ".webp";
            case "image/svg+xml" -> ".svg";
            default -> "";
        };
    }
}
