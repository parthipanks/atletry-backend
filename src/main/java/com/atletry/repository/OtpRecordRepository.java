package com.atletry.repository;

import com.atletry.entity.OtpRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;


@Repository
public interface OtpRecordRepository extends JpaRepository<OtpRecord, Long> {

    Optional<OtpRecord> findTopByMobileAndIsUsedFalseAndExpiresAtAfterOrderByCreatedDateDesc(
            String mobile, ZonedDateTime now);

    @Modifying
    @Query("UPDATE OtpRecord o SET o.isUsed = true WHERE o.mobile = :mobile")
    void invalidateAllByMobile(String mobile);
}
