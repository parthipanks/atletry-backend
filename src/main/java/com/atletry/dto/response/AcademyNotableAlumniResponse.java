package com.atletry.dto.response;

import lombok.Data;

@Data
public class AcademyNotableAlumniResponse {
    private Long id;
    private String name;
    private String achievement;
    private Integer year;
    private String sport;
    private String photoUri;
    private boolean hasConsent;
}
