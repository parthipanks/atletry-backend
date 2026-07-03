package com.atletry.enums;

public enum SkillLevel {
    JUST_STARTING("Just Starting",
            "You are new to this sport and still learning the basics. No prior experience needed."),
    INTERMEDIATE("Intermediate",
            "You have a solid grasp of the rules and play/train regularly. Looking to improve."),
    COMPETITIVE("Competitive",
            "You play at a competitive level, enter tournaments or leagues, and train seriously.");

    private final String label;
    private final String description;

    SkillLevel(String label, String description) {
        this.label       = label;
        this.description = description;
    }

    public String getLabel()       { return label; }
    public String getDescription() { return description; }
}
