package com.atletry.entity;

import com.atletry.enums.IdType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;


@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachDocuments {

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_id_type", length = 30)
    private IdType idType;

    @Column(name = "doc_id_number", length = 100)
    private String idNumber;

    @Column(name = "doc_id_front_uri", length = 500)
    private String idFrontUri;

    @Column(name = "doc_id_back_uri", length = 500)
    private String idBackUri;

    @Column(name = "doc_pan_number", length = 20)
    private String panNumber;

    @Column(name = "doc_pan_image_uri", length = 500)
    private String panImageUri;

    @Column(name = "doc_pan_verified")
    @Builder.Default
    private boolean panVerified = false;

    @Column(name = "doc_gstin", length = 20)
    private String gstin;

    @Column(name = "doc_gst_certificate_uri", length = 500)
    private String gstCertificateUri;

    @Column(name = "doc_police_verification_uri", length = 500)
    private String policeVerificationUri;

    @Column(name = "doc_police_verification_date")
    private LocalDate policeVerificationDate;

    @Column(name = "doc_first_aid_cert_uri", length = 500)
    private String firstAidCertUri;

    @Column(name = "doc_insurance_uri", length = 500)
    private String insuranceUri;

    @Column(name = "doc_agreement_signature_uri", length = 500)
    private String agreementSignatureUri;

    @Column(name = "doc_agreement_signed_at")
    private Instant agreementSignedAt;
}
