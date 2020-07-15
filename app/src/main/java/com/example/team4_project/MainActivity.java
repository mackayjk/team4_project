package com.example.team4_project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    PlacesClient placesClient;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializePlaces();
        initializeAutocomplete();
        mAuth = FirebaseAuth.getInstance();

        this.setTitle("Rando");
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setCustomView(R.layout.action_bar);

        Button userDataButton = (Button) findViewById(R.id.open_user_data);
        Button signInButton = (Button) findViewById(R.id.open_signin);
        Button signUpButton = (Button) findViewById(R.id.open_signup);
        Button btnViewDatabase = (Button) findViewById(R.id.view_items_screen);
        Button save_restaurant = (Button) findViewById(R.id.save_button_view);
        TextView restaurant_id_view = (TextView) findViewById(R.id.restaurant_id_view);
        TextView name = (TextView) findViewById(R.id.restaurant_name_view);


        save_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRestaurant();
            }
        });
        Button btnOpenRandom = (Button) findViewById(R.id.open_random);

        userDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserData();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();
            }
        });

        btnViewDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewDatabase.class);
                startActivity(intent);
            }
        });

        btnOpenRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRandom();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        save_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to add object to database.");
                String Restaurant = restaurant_id_view.getText().toString();
                String RestName = name.getText().toString();
                if(!Restaurant.equals("")){
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    String userID = user.getUid();
                    myRef.child(userID).child("email").setValue(user.getEmail());
                    myRef.child(userID).child("places").child(Restaurant).setValue(Restaurant);
                    toastMessage("Adding " + RestName + " to favorites");
                    restaurant_id_view.setText("");
                }
            }
        });
    }


    public void initializeAutocomplete() {
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Collections.singletonList(Place.Field.ID));
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull @NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                try {
                    loadRestaurant(place.getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onError(@NonNull @NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    //This method will initialize the places client.
    public void initializePlaces() {
        Places.initialize(getApplicationContext(), "AIzaSyB2Ul315SoD2GRH-xCMvpYUQ3sOlsx7u-Q");
        placesClient = Places.createClient(this);
    }


    public void loadRestaurant(String placeId) throws InterruptedException {
        initializePlaces();
        //String placeId = "ChIJf83U3auQTYcRcsZgTfnaQOg";
        RestaurantLoader restaurantLoader = new RestaurantLoader(placesClient);
        Restaurant r = restaurantLoader.getRestaurant(placeId, this);
        //Toast.makeText(this, "testing the toast message." + r.getRestaurantName(), Toast.LENGTH_SHORT).show();

    }

    public void showData(Restaurant r) {
        Toast.makeText(this, "Restaurant found!\n" + r.getRestaurantName(), Toast.LENGTH_SHORT).show();
        TextView restaurantName = (TextView) findViewById(R.id.restaurant_name_view);
        TextView restaurantAddress = (TextView) findViewById(R.id.restaurant_address_view);
        TextView restaurantWebsite = (TextView) findViewById(R.id.restaurant_website_view);
        TextView restaurantId = (TextView) findViewById(R.id.restaurant_id_view);
        RatingBar restaurantRating = (RatingBar) findViewById(R.id.restaurant_rating_view);

        restaurantName.setText(r.restaurantName);
        restaurantAddress.setText(r.restaurantAddress);
        restaurantWebsite.setText(r.restaurantWebsiteUri.toString());
        restaurantId.setText(r.restaurantId);
        restaurantRating.setRating(r.restaurantRating.floatValue());

        CardView restaurantCard = (CardView) findViewById(R.id.restaurant_card);
        restaurantCard.setVisibility(View.VISIBLE);

        LinearLayout search_for_restaurant_massage = (LinearLayout) findViewById(R.id.search_for_restaurant_message);
        search_for_restaurant_massage.setVisibility(View.GONE);

    }

    public void openWebsite(View view) {

        TextView websiteView = (TextView) findViewById(R.id.restaurant_website_view);
        String website = websiteView.getText().toString();

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
        startActivity(browserIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openDirection(View view) throws UnsupportedEncodingException {
        TextView idView = (TextView) findViewById(R.id.restaurant_id_view);
        TextView nameView = (TextView) findViewById(R.id.restaurant_name_view);
        String placeId = idView.getText().toString();
        String placeName = nameView.getText().toString();
        String encodedName = URLEncoder.encode(placeName, StandardCharsets.UTF_8.toString());

        String query = "https://www.google.com/maps/search/?api=1&query=" + encodedName + "&query_place_id=" + placeId;
        Intent directionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
        startActivity(directionIntent);
    }



    public void saveRestaurant() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        TextView placeIdView = (TextView) findViewById(R.id.restaurant_id_view);
        String restaurantId = placeIdView.getText().toString();
        UserData userData = new UserData();
        ArrayList<String> places = new ArrayList<>();
        assert currentUser != null;
        places.add(restaurantId);
        userData.setPlaces(places);
        userData.setEmail(currentUser.getEmail());
        myRef.child(currentUser.getUid()).setValue(userData);
    }

    public void getUserData(String username) {
        UserData userData = new UserData();

    }


    public void openUserData() {
        Intent intent = new Intent(this, UserInfo.class);
        startActivity(intent);
    }

    public void openSignIn() {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void openSignUp() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void openRandom() {
        Intent intent = new Intent(this, random_place.class);
        startActivity(intent);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
