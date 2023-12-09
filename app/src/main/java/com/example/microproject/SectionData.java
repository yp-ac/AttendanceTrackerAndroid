package com.example.microproject;

public class SectionData {
    private String sectionName, sectionSem;

    public String getSectionName() {
        return sectionName;
    }

    public String getSectionSem() {
        return sectionSem;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setSectionSem(String sectionSem) {
        this.sectionSem = sectionSem;
    }

    public SectionData() {}

    public SectionData(String name, String sem) {
        sectionName = name;
        sectionSem = sem;
    }
}
