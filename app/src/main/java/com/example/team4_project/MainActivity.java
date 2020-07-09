package com.example.team4_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    PlacesClient placesClient;
    EditText editUsername;
    EditText editEmail;
    Button saveButton;
    Button userInfoButton;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editUsername = findViewById(R.id.usernameDisplay);
        editEmail = findViewById(R.id.email);
        saveButton = findViewById(R.id.saveButton);

        myRef.child("UserData");
        UserData userData = new UserData();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                userData.setUsername(username);
                userData.setEmail(email);
                userData.setPlaceId("");
                myRef.child(username).setValue(userData);
                Toast.makeText(MainActivity.this, "Successfully Created User", Toast.LENGTH_LONG).show();
            }
        });
        userInfoButton = findViewById(R.id.viewUser);
        userInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserInfo();
            }
        });
    }

    public void openUserInfo(){
        Intent intent = new Intent(this, UserInfo.class);
        startActivity(intent);
    }

    //This method will initialize the places client.
    public void initializePlaces() {
        Places.initialize(getApplicationContext(), "AIzaSyB2Ul315SoD2GRH-xCMvpYUQ3sOlsx7u-Q");
        placesClient = Places.createClient(this);
    }


    public void loadRestaurant(View view) throws InterruptedException {
        initializePlaces();
        String placeId = "ChIJf83U3auQTYcRcsZgTfnaQOg";
        RestaurantLoader restaurantLoader = new RestaurantLoader(placesClient);
        Restaurant r = restaurantLoader.getRestaurant(placeId, this);
        //Toast.makeText(this, "testing the toast message." + r.getRestaurantName(), Toast.LENGTH_SHORT).show();

    }

    public void showData(Restaurant r) {
        Toast.makeText(this, "testing the toast message." + r.getRestaurantName(), Toast.LENGTH_SHORT).show();
    }
}
