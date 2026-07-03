package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;


@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyContactInfo {

    @Column(name = "contact_person_name", length = 100)
    private String contactPersonName;

    @Column(name = "contact_designation", length = 100)
    private String designation;

    @Column(name = "contact_phone", length = 20)
    private String phone;

    @Column(name = "contact_email", length = 150)
    private String email;

    @Column(name = "contact_whatsapp", length = 20)
    private String whatsapp;

    @Column(name = "contact_website_url", length = 300)
    private String websiteUrl;

    @Column(name = "contact_instagram_handle", length = 100)
    private String instagramHandle;
}
