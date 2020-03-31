package com.example.registrationform;

import java.io.Serializable;

public class User implements Serializable {
    public String id;
    public String email;
    String userName;
    String password;
    String time;
    String mobile;
    String address;
    String gender;
    String language;
    byte[] image;

    User(String id, String userName, String email, String password, String time, String mobile, String address, String gender, String language, byte[] image) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.time = time;
        this.mobile = mobile;
        this.address = address;
        this.gender = gender;
        this.language = language;
        this.image = image;
    }

    String getMobile() {
        return mobile;
    }

    String getAddress() {
        return address;
    }

    String getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    String getGender() {
        return gender;
    }

    String getLanguage() {
        return language;
    }

    byte[] getImage() {
        return image;
    }

}
