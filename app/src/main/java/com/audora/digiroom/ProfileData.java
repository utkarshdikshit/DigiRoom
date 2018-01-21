package com.audora.digiroom;

/**
 * Created by ud on 1/18/2018.
 */

public class ProfileData {
    private String name;
    private String department;
    private boolean isStudent;
    private String email;

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean getIsStudent(){
        return isStudent;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }
}
