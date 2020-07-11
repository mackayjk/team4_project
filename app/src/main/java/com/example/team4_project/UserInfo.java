package com.example.team4_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UserInfo extends AppCompatActivity {

    UserData user = new UserData();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    PlacesClient placesClient;

//    TextView name = (TextView) findViewById(R.id.user_name_view);
    //TextView username = (TextView) findViewById(R.id.user_username_view);
//    TextView email = (TextView) findViewById(R.id.user_email_view);
//    TextView restaurant = (TextView) findViewById(R.id.user_restaurant_name_view);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        this.setTitle("Rando");
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setCustomView(R.layout.action_bar);

        Button userOpenDirectionButton = (Button) findViewById(R.id.user_restaurant_directions_view);

        userOpenDirectionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                openDirection2();
            }
        });


        Button synchronizeDataButton = (Button) findViewById(R.id.synchronize_data);

        synchronizeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUserInfo();
            }
        });
    }

    public void initializePlaces() {
        Places.initialize(getApplicationContext(), "AIzaSyB2Ul315SoD2GRH-xCMvpYUQ3sOlsx7u-Q");
        placesClient = Places.createClient(this);
    }

    public void loadRestaurant(String placeId) throws InterruptedException {
        initializePlaces();
        RestaurantLoader restaurantLoader = new RestaurantLoader(placesClient);
        Restaurant r = restaurantLoader.getRestaurant2(placeId, this);

    }

    public void displayUserInfo() {

        myRef.child("asdf").child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView username = (TextView) findViewById(R.id.user_username_view);
                username.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child("asdf").child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView username = (TextView) findViewById(R.id.user_email_view);
                username.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child("asdf").child("placeId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                try {
                    loadRestaurant(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child("asdf").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView username = (TextView) findViewById(R.id.user_name_view);
                username.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child("asdf").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView username = (TextView) findViewById(R.id.user_name_title);
                username.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void showData(Restaurant r) {
        TextView restaurantName = (TextView) findViewById(R.id.user_restaurant_name_view);
        restaurantName.setText(r.restaurantName);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openDirection2(View view, String placeId) throws UnsupportedEncodingException {
        TextView nameView = (TextView) findViewById(R.id.restaurant_name_view);
        String placeName = nameView.getText().toString();
        String encodedName = URLEncoder.encode(placeName, StandardCharsets.UTF_8.toString());
        String query = "https://www.google.com/maps/search/?api=1&query=" + encodedName + "&query_place_id=" + placeId;
        Intent directionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
        startActivity(directionIntent);
    }

}