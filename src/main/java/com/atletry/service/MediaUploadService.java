package com.atletry.service;

import com.atletry.entity.MediaUpload;
import com.atletry.entity.User;
import com.atletry.enums.MediaUploadEntityType;
import com.atletry.repository.MediaUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class MediaUploadService {

    private final S3UploadService      s3UploadService;
    private final MediaUploadRepository mediaUploadRepo;

    /**
     * Validates, uploads to S3, persists a MediaUpload record, and returns the public URL.
     *
     * @param image      the file to upload (must not be null/empty)
     * @param entityType which domain entity this image belongs to
     * @param entityId   the ID of that entity (may be null when entity is not yet persisted — update after save)
     * @param uploadedBy the authenticated user performing the upload (nullable for system uploads)
     * @return public S3 download URL
     */
    public String uploadAndRecord(MultipartFile image,
                                  MediaUploadEntityType entityType,
                                  Long entityId,
                                  User uploadedBy) {
        S3UploadService.UploadResult result =
                s3UploadService.uploadImage(image, entityType.getFolderPath());

        mediaUploadRepo.save(MediaUpload.builder()
                .entityType(entityType)
                .entityId(entityId)
                .s3Key(result.s3Key())
                .s3Url(result.url())
                .uploadedBy(uploadedBy)
                .contentType(image.getContentType())
                .fileSizeBytes(image.getSize())
                .build());

        log.info("MediaUpload recorded: type={} entityId={} url={}", entityType, entityId, result.url());
        return result.url();
    }

    public List<String> uploadAllAndRecord(List<MultipartFile> images,
                                           MediaUploadEntityType entityType,
                                           Long entityId,
                                           User uploadedBy) {
        List<String> urls = new ArrayList<>();
        if (images == null) return urls;
        for (MultipartFile image : images) {
            if (image != null && !image.isEmpty()) {
                urls.add(uploadAndRecord(image, entityType, entityId, uploadedBy));
            }
        }
        return urls;
    }
}
