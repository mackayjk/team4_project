package com.example.team4_project;

public class UserData {
    private String placeId;
    private String username;
    private String email;

    public UserData() {
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserData(String username, String placeId, String email) {
        this.username = username;
        this.placeId = placeId;
        this.email = email;
    }

}