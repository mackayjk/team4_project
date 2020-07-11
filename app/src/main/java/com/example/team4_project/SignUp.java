//package com.example.team4_project;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.annotations.Nullable;
//
//
//public class SignUp extends AppCompatActivity {
//    private static final String TAG = "AddItemsToDatabase";
//
//    private Button mAddToDB;
//
//    private EditText mNewFood;
//    private FirebaseDatabase mFirebaseDatabase;
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//    private DatabaseReference myRef;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//        mAddToDB = (Button) findViewById(R.id.registerUser);
//        mNewFood = (EditText) findViewById(R.id.email);
//
//        mAuth = FirebaseAuth.getInstance();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = mFirebaseDatabase.getReference();
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    toastMessage("Successfully signed in with: " + user.getEmail());
//                } else {
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                    toastMessage("Successfully signed out.");
//                }
//            }
//        };
//
//        // READ FROM DATABASE
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Object value = dataSnapshot.getValue();
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                toastMessage("Failed to alter database.");
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
//            }
//        });
//
//        // ADD TO DATABASE
//        mAddToDB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Attempting to add object to database.");
//                String newFood = mNewFood.getText().toString();
//                if (!newFood.equals("")) {
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    assert user != null;
//                    String userId = user.getUid();
//                    myRef.child("users").child(userId).child("Food").child(newFood).setValue("true");
//                    toastMessage("Adding " + newFood + " to database...");
//                    mNewFood.setText("");
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
//
//    private void toastMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//}