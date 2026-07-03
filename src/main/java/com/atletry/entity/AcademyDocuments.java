package com.atletry.entity;

import com.atletry.enums.BusinessRegType;
import jakarta.persistence.*;
import lombok.*;


@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyDocuments {

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_business_reg_type", length = 30)
    private BusinessRegType businessRegType;

    @Column(name = "doc_registration_number", length = 100)
    private String registrationNumber;

    @Column(name = "doc_registration_doc_uri", length = 500)
    private String registrationDocUri;

    @Column(name = "doc_gstin", length = 20)
    private String gstin;

    @Column(name = "doc_pan_number", length = 20)
    private String panNumber;

    @Column(name = "doc_pocso_compliant")
    @Builder.Default
    private boolean pocsoCompliant = false;

    @Column(name = "doc_pocso_policy_doc_uri", length = 500)
    private String pocsoPolicyDocUri;

    @Column(name = "doc_has_insurance")
    @Builder.Default
    private boolean hasInsurance = false;

    @Column(name = "doc_insurance_doc_uri", length = 500)
    private String insuranceDocUri;

    @Column(name = "doc_has_safety_cert")
    @Builder.Default
    private boolean hasSafetyCert = false;

    @Column(name = "doc_bank_account_last4", length = 4)
    private String bankAccountLast4;

    @Column(name = "doc_bank_account_full", length = 50)
    private String bankAccountFull;

    @Column(name = "doc_ifsc_code", length = 15)
    private String ifscCode;

    @Column(name = "doc_bank_name", length = 100)
    private String bankName;

    @Column(name = "doc_upi_id", length = 100)
    private String upiId;
}
