package com.example.team4_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class Register extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String TAG = "RegisterActivity";
    private String password;
    private String email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users");
        mAuth = FirebaseAuth.getInstance();
        Button registerButton = (Button) findViewById(R.id.registerUser);
        EditText emailEntry = (EditText) findViewById(R.id.emailEntry);
        EditText passwordEntry = (EditText) findViewById(R.id.password);

        registerButton.setOnClickListener(v -> {
            //insert data into firebase database
            password = passwordEntry.getText().toString();
            email = emailEntry.getText().toString();
            registerUser(email, password);
            mAuth.signInWithEmailAndPassword(email, password);
            FirebaseUser user = mAuth.getCurrentUser();
            assert user != null;
            saveUser(email);
        });
    }

    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        mAuth.signInWithEmailAndPassword(email, password);
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Register.this, "Success!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(Register.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void saveUser(String email) {
        ArrayList<String> places = new ArrayList<>();
        mAuth.signInWithEmailAndPassword(email, password);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        places.add("You have none saved.");
        assert currentUser != null;
        mDatabase.child(currentUser.getUid()).child("email").setValue(email);
        mDatabase.child(currentUser.getUid()).child("places").setValue(places);
        mDatabase.child(currentUser.getUid()).child("name").setValue("");//adding user info to database
        mDatabase.child(currentUser.getUid()).child("places").child("Favorite Foods").child("ooof").setValue("true");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveRestaurant(String restaurantID){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        mDatabase.child(currentUser.getUid()).child("places").setValue("ANOTHER");
    }
}
