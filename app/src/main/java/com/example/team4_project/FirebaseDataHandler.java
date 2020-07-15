package com.example.team4_project;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

class FirebaseDataHandler {
    private UserData data = new UserData();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    private UserData userData = new UserData();


    UserData returnUserData() {
        return userData;
    }

    UserData readData(String username) {
        UserData data = new UserData();
        myRef.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "TAG";

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(username);
                Log.w(TAG, "" + ref.getKey());
                Log.w(TAG, "" + ref.child(username).getKey());
                Log.w(TAG, "" + Objects.requireNonNull(dataSnapshot.child(username).getValue()));
                for (DataSnapshot y : dataSnapshot.getChildren()) {
                    UserData userData = y.getValue(UserData.class);
                    assert userData != null;
                    try {
                        if (userData.getUsername().equals(username)) {
                            data.setEmail(userData.getEmail());
                            data.setUsername(userData.getUsername());
                        }
                    } catch (java.lang.NullPointerException e) {
                        Log.w(TAG, "Whoops");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Taco Paco", error.toException());
            }
        });
        return data;
    }


    void saveUserData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child("Different");
        myRef.child("Name").setValue("adfasdf");
        myRef.child("Age").setValue("33");
        myRef.child("Team").setValue("Dolden Gragons");
        myRef.child("Position").setValue("Fadgadfrd");
        myRef.child("Favorite Foods").child("Papas Fritas").setValue("¯\\_(ツ)_/¯");
        myRef.child("Favorite Foods").child("Fries").setValue("¯\\_(ツ)_/¯");
//        myRef.child("James").child("email").child("TRY").setValue("fiveoat@gmail.com");
//        myRef.child("McKay").child("email").setValue("Tacocat");
//        myRef.child("Another").child("email").setValue("hugh@gmail.com");
//        myRef.child("Coty").child("Words").setValue("Tacocat");
    }

    void saveTry(String username, String email, String restaurant) {
        userData.setUsername(username);
        userData.setEmail(email);
        myRef.child(username).setValue(userData);
    }

    void saveThree(String username, String email, String restaurant) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        userData.setUsername(username);
        userData.setEmail(email);
        myRef.child(username).push().setValue(userData);
    }


    void saveInitialUserData(String username, String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        String user_name = username.toLowerCase();
        myRef.setValue(user_name);
        myRef.child(user_name).child("email").setValue(email);
        myRef.child(user_name).child("restaurants").setValue("none");
    }

    void addNewRestaurant(String username, String place_id, String place_name, String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.getKey();
    }

    void addRestaurantToUser(String username, String placeName, String placeID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference("users/" + username + "/restaurants");
        myRef2.child(placeName).setValue(placeID);
    }

    void getSavedRestaurants(String username) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/Fiveoat/restaurants");
        myRef.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "TAG";

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Taco Paco", error.toException());
            }
        });
    }

}

