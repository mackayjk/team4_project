package com.example.team4_project;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestaurantLoader {
    PlacesClient placesClient;
    public final static String TAG = "RestaurantLoader";
    private static final String API_KEY_FILE = "AIzaSyB2Ul315SoD2GRH-xCMvpYUQ3sOlsx7u-Q";
    public final Restaurant restaurant = new Restaurant();
    private String apiCharset;

    private static final String URL_ENDPOINT_PLACEID = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json";

    public RestaurantLoader(PlacesClient placesClient) {
        this.placesClient = placesClient;
    }

    /**
     * This Method will use the PlaceId for a place in Google API to get detailed information of the place and store it in
     * a restaurant class.
     * @param placeId
     * @param activity
     * @return restaurant
     * @throws InterruptedException
     */
    public Restaurant getRestaurant(String placeId, MainActivity activity) throws InterruptedException {
        //Restaurant restaurant = new Restaurant();

        //specify the fields to return from the Places API
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.RATING, Place.Field.TYPES, Place.Field.WEBSITE_URI);
        //start a new fetch Place request and crete a new instance with specified id and fields
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {

            //get the place a save the info to restaurant.
            Place place = response.getPlace();
            restaurant.setRestaurantId(place.getId());
            restaurant.setRestaurantName(place.getName());
            restaurant.setRestaurantAddress(place.getAddress());
            restaurant.setRestaurantRating(place.getRating());
            restaurant.setRestaurantType(place.getTypes());
            restaurant.setRestaurantWebsiteUri(place.getWebsiteUri());


            //When it done, now I have access to the info, so I can show it in the MainActivity screen...
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This is code that will now run on the UI thread. Call the function in
                    // MainActivity that will update the UI correctly.
                    activity.showData(restaurant);
                }
            });

            //here in the Log cat message it show the correct restaurant info that is pulled from the API
            Log.d(TAG, "Restaurant Name: " + restaurant.getRestaurantName());
            Log.d(TAG, "Restaurant Address: " + restaurant.getRestaurantAddress());
            Log.d(TAG, "Restaurant Rating: " + restaurant.getRestaurantRating());
            Log.d(TAG, "Restaurant Type: " + restaurant.getRestaurantType());
            Log.d(TAG, "Restaurant Website: " + restaurant.getRestaurantWebsiteUri());


        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.d(TAG, "Place not found: " + exception.getMessage());
            }
        });
        Log.v(TAG, "Restaurant Name: " + restaurant.getRestaurantName());
        return restaurant;
    }

    public Restaurant getRestaurant2(String placeId, UserInfo activity, TextView restaurantSlot) throws InterruptedException {
        //Restaurant restaurant = new Restaurant();

        //specify the fields to return from the Places API
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.RATING, Place.Field.TYPES, Place.Field.WEBSITE_URI);
        //start a new fetch Place request and crete a new instance with specified id and fields
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {

            //get the place a save the info to restaurant.
            Place place = response.getPlace();
            restaurant.setRestaurantId(place.getId());
            restaurant.setRestaurantName(place.getName());
            restaurant.setRestaurantAddress(place.getAddress());
            restaurant.setRestaurantRating(place.getRating());
            restaurant.setRestaurantType(place.getTypes());
            restaurant.setRestaurantWebsiteUri(place.getWebsiteUri());


            //When it done, now I have access to the info, so I can show it in the MainActivity screen...
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This is code that will now run on the UI thread. Call the function in
                    // MainActivity that will update the UI correctly.
                    activity.showData(restaurant, restaurantSlot);
                }
            });

            //here in the Log cat message it show the correct restaurant info that is pulled from the API
            Log.d(TAG, "Restaurant Name: " + restaurant.getRestaurantName());
            Log.d(TAG, "Restaurant Address: " + restaurant.getRestaurantAddress());
            Log.d(TAG, "Restaurant Rating: " + restaurant.getRestaurantRating());
            Log.d(TAG, "Restaurant Type: " + restaurant.getRestaurantType());
            Log.d(TAG, "Restaurant Website: " + restaurant.getRestaurantWebsiteUri());


        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.d(TAG, "Place not found: " + exception.getMessage());
            }
        });
        Log.v(TAG, "Restaurant Name: " + restaurant.getRestaurantName());
        return restaurant;
    }

}
