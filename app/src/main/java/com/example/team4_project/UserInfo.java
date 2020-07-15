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
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class UserInfo extends AppCompatActivity {

    UserData user = new UserData();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    PlacesClient placesClient;
    ArrayList<Restaurant> myRestaurants = new ArrayList<>();
    Restaurant myRestaurant = new Restaurant();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

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
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        //userID = user.getUid();
        userID = "v18TgOTe5lfNytp4wokOB5HSjDb2";

        this.setTitle("Rando");
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setCustomView(R.layout.action_bar);

        Button userOpenDirectionButton = (Button) findViewById(R.id.user_restaurant_directions_view1);

        userOpenDirectionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //openDirection2();
            }
        });


        Button synchronizeDataButton = (Button) findViewById(R.id.synchronize_data);

        synchronizeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUserInfo();
            }
        });

        Button testingButton = (Button) findViewById(R.id.button5);

        testingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testingThis();
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
                TextView username = (TextView) findViewById(R.id.user_username_view);
                username.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child(userID).child("email").addValueEventListener(new ValueEventListener() {
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

        TextView restaurantSlot1 = (TextView) findViewById(R.id.user_restaurant_name_view1);
        TextView restaurantSlot2 = (TextView) findViewById(R.id.user_restaurant_name_view2);
        TextView restaurantSlot3 = (TextView) findViewById(R.id.user_restaurant_name_view3);


        Integer number = 2;
        ArrayList<TextView> restaurantSlots = new ArrayList<>();
        restaurantSlots.add(restaurantSlot1);
        restaurantSlots.add(restaurantSlot2);
        restaurantSlots.add(restaurantSlot3);


        for (int i = 0; i < 3; i++) {
            int finalI = i;
            myRef.child(userID).child("places").child(number.toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    try {
                        loadRestaurant(value, restaurantSlots.get(finalI));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            number++;
        }

//        myRef.child(userID).child("places").child("1").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.class);
//                try {
//                    loadRestaurant(value);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        myRef.child(userID).child("name").addValueEventListener(new ValueEventListener() {
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

        myRef.child(userID).child("name").addValueEventListener(new ValueEventListener() {
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

//        myRef.child("asdf").child("places").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<String> userPlaces = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String placeId = snapshot.getValue(String.class);
//                    userPlaces.add(placeId);
//                }
//
//                ArrayList<String> userPlaceNames = new ArrayList<>();
//                for(String placeId: userPlaces){
//                    try {
//                        loadRestaurant(placeId);
//                        TextView restaurantName = (TextView) findViewById(R.id.user_restaurant_name_view);
//                        String placeName = restaurantName.getText().toString();
//                        userPlaceNames.add(placeName);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                ListView placesListView = findViewById(R.id.user_places_view);
//                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, userPlaces);
//                placesListView.setAdapter(adapter);
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });


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

}