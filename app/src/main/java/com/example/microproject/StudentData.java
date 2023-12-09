package com.example.microproject;

public class StudentData {
    public static final String PROFILE_URL_BASE = "https://online.gppune.ac.in/gpp_s20/student/photo/";
    String name, profileExtension;
    long enrollmentNo;
    boolean isPresent = true;

    public StudentData(String name, String profileExtension, long enrollmentNo) {
        this.name = name;
        this.profileExtension = profileExtension;
        this.enrollmentNo = enrollmentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileExtension() {
        return profileExtension;
    }

    public void setProfileExtension(String profileExtension) {
        this.profileExtension = profileExtension;
    }

    public long getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(long enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public String getProfileUrl() {
        if (profileExtension.equals(""))
            return null;
        return PROFILE_URL_BASE + String.valueOf(enrollmentNo) + "." + profileExtension;
    }
}
