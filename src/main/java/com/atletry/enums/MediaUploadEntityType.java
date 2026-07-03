package com.atletry.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MediaUploadEntityType {

    USER("users/profile-images"),
    COACH("coaches/images"),
    GROUND("grounds/images"),
    MATCH("matches/images"),
    TOURNAMENT("tournaments/images"),
    SPORT("sports/icons"),
    COACH_PROGRAM("coaches/programs/images"),
    ACADEMY("academies/images"),
    ACADEMY_BRANCH("academies/branches/images");

    private final String folderPath;
}
