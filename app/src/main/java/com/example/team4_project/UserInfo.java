package com.example.team4_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class UserInfo extends AppCompatActivity {
    EditText usernameEntry;
    TextView emailDisplay;
    TextView usernameDisplay;
    TextView placeIdDisplay;
    Button findUser;
    Button setUsername;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    String path = "users";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(path);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        usernameEntry = findViewById(R.id.usernameEntry);
        setUsername = findViewById(R.id.setUsername);
        emailDisplay = findViewById(R.id.emailDisplay);
        usernameDisplay = findViewById(R.id.usernameDisplay);
        placeIdDisplay = findViewById(R.id.placeIdDisplay);
        findUser = findViewById(R.id.findUser);
        findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.w("TAG", path);
                        String username = Objects.requireNonNull(snapshot.child(("username")).getValue()).toString();
                        String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                        String placeId = Objects.requireNonNull(snapshot.child("placeId").getValue()).toString();
                        emailDisplay.setText(email);
                        usernameDisplay.setText(username);
                        placeIdDisplay.setText(placeId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        setUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEntry = findViewById(R.id.usernameEntry);
                String unE = usernameEntry.getText().toString().trim();
                setPath("users/" + unE);
            }
        });
    }
}
