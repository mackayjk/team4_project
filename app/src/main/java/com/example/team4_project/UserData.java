package com.example.team4_project;

import java.util.ArrayList;

public class UserData {
    private String username;
    private String email;

    public ArrayList getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<String> places) {
        this.places = places;
    }

    private ArrayList places;

    public UserData() {
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

    public UserData(String username, String email, ArrayList places) {
        this.username = username;
        this.email = email;
        this.places = places;
    }

}