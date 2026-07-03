package com.atletry.repository;

import com.atletry.entity.MediaUpload;
import com.atletry.enums.MediaUploadEntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaUploadRepository extends JpaRepository<MediaUpload, Long> {

    List<MediaUpload> findByEntityTypeAndEntityIdOrderByCreatedDateDesc(
            MediaUploadEntityType entityType, Long entityId);
}
