package com.atletry.entity;

import com.atletry.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;


@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachBankingDetails {

    @Column(name = "bank_account_holder_name", length = 100)
    private String accountHolderName;

    @Column(name = "bank_account_number_last4", length = 4)
    private String accountNumberLast4;

    @Column(name = "bank_account_number_full", length = 50)
    private String accountNumberFull;

    @Column(name = "bank_ifsc_code", length = 15)
    private String ifscCode;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_branch_name", length = 100)
    private String branchName;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_account_type", length = 20)
    private AccountType accountType;

    @Column(name = "bank_upi_id", length = 100)
    private String upiId;

    @Column(name = "bank_penny_drop_verified")
    @Builder.Default
    private boolean pennyDropVerified = false;

    @Column(name = "bank_payout_eligible")
    @Builder.Default
    private boolean payoutEligible = false;
}
