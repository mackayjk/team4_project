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
            FirebaseUser user = mAuth.getCurrentUser();
            assert user != null;
            saveUser(user, email);
        });

    }

    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
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

    public void saveUser(FirebaseUser currentUser, String email) {
        ArrayList<String> places = new ArrayList<>();
        places.add("You have none saved.");
        mDatabase.child(currentUser.getUid()).child("email").setValue(email);
        mDatabase.child(currentUser.getUid()).child("places").setValue(places);
        mDatabase.child(currentUser.getUid()).child("name").setValue("");//adding user info to database
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
