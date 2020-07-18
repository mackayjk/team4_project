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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class UserInfo extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    PlacesClient placesClient;
    ArrayList<Restaurant> myRestaurants = new ArrayList<>();
    Restaurant myRestaurant = new Restaurant();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    private String userID;
    public ArrayList<String> masterPlacesArray;

//    TextView name = (TextView) findViewById(R.id.user_name_view);
    //TextView username = (TextView) findViewById(R.id.user_username_view);
//    TextView email = (TextView) findViewById(R.id.user_email_view);
//    TextView restaurant = (TextView) findViewById(R.id.user_restaurant_name_view);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference();
        user = mAuth.getCurrentUser();
        assert user != null;
        userID = user.getUid();

        this.setTitle("Rando");
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setCustomView(R.layout.action_bar);

        displayUserInfo();

        ImageView userDataButton = (ImageView) findViewById(R.id.image_button_user);
        ImageView btnOpenRandom = (ImageView) findViewById(R.id.image_button_random);
        ImageView btnOpenHome = (ImageView) findViewById(R.id.image_button_home);

        btnOpenHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        btnOpenRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRandom();
            }
        });
    }

    public void initializePlaces() {
        Places.initialize(getApplicationContext(), "AIzaSyB2Ul315SoD2GRH-xCMvpYUQ3sOlsx7u-Q");
        placesClient = Places.createClient(this);
    }

    public void loadRestaurant(String placeId,  TextView restaurantSlot) throws InterruptedException {
        initializePlaces();
        RestaurantLoader restaurantLoader = new RestaurantLoader(placesClient);
        Restaurant r = restaurantLoader.getRestaurant2(placeId, this, restaurantSlot);

    }

    public void displayUserInfo() {

        myRef.child(userID).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child(userID).child("email").addValueEventListener(new ValueEventListener() {
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



            myRef.child(userID).child("places").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> placesArray = new ArrayList<>();
                    Map<String, String> placesMap = (Map<String, String>) dataSnapshot.getValue();
                    Iterator it = placesMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        placesArray.add(pair.getValue().toString());
                        it.remove();

                    }
                    masterPlacesArray = placesArray;
                    ListView listView = (ListView) findViewById(R.id.ListView);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserInfo.this, android.R.layout.simple_list_item_1, placesArray);
                    listView.setAdapter(adapter);
//                    try {
//                        loadRestaurant(value, restaurantSlots.get(finalI));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        myRef.child(userID).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child(userID).child("name").addValueEventListener(new ValueEventListener() {
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


    }

    public void showData(Restaurant r, TextView restaurantSlot) {
        restaurantSlot.setText(r.restaurantName);
        myRestaurant.setRestaurantId(r.restaurantId);
        myRestaurant.setRestaurantName(r.restaurantName);
        myRestaurant.setRestaurantAddress(r.restaurantAddress);
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

    public void testingThis() {
        Toast.makeText(this, "Restaurant found!\n" + myRestaurant.getRestaurantName(), Toast.LENGTH_SHORT).show();
    }

    public void openRandom() {
        Intent intent = new Intent(this, random_place.class);
        startActivity(intent);
    }

    public void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

//    public displayRestaurantNames(ArrayList<String> placesArray) {
//
//        Iterator it = placesArray.iterator();
//        Iterator it2 =
//        while (it.hasNext()) {
//            = (Map.Entry)it.next();
//            placesArray.add(pair.getValue().toString());
//            it.remove();
//
//        }
//
//        ListView lv = (ListView) findViewById(R.id.ListView);
//        Iterator
//    }

}