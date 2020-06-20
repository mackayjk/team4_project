package com.example.team4_project;

import android.net.Uri;

import com.google.android.libraries.places.api.model.Place;

import java.util.List;

public class Restaurant {

    public CharSequence restaurantName;
    public CharSequence restaurantAddress;
    public Double restaurantRating;
    public List<Place.Type> restaurantType;
    public Uri restaurantWebsiteUri;


    public Restaurant() {
//        restaurantName = placeName;
//        restaurantAddress = placeAddress;
//        restaurantRating = placeRating;
//        restaurantType = placeType;
//        restaurantWebsiteUri = placeWebsiteUri;
    }

    public void setRestaurantName(CharSequence restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantAddress(CharSequence restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public void setRestaurantRating(Double restaurantRating) {
        this.restaurantRating = restaurantRating;
    }

    public void setRestaurantType(List<Place.Type> restaurantType) {
        this.restaurantType = restaurantType;
    }

    public void setRestaurantWebsiteUri(Uri restaurantWebsiteUri) {
        this.restaurantWebsiteUri = restaurantWebsiteUri;
    }

    public CharSequence getRestaurantName() {
        return restaurantName;
    }

    public CharSequence getRestaurantAddress() {
        return restaurantAddress;
    }

    public Double getRestaurantRating() {
        return restaurantRating;
    }

    public List<Place.Type> getRestaurantType() {
        return restaurantType;
    }

    public Uri getRestaurantWebsiteUri() {
        return restaurantWebsiteUri;
    }


}
