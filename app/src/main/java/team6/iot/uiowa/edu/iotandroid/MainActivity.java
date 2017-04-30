package team6.iot.uiowa.edu.iotandroid;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDataRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private static final String TAG = "AnonAuth";
    private TextView header;
    private TextView status;
    private User user;
    private List<String> userEmails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mStorageRef = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_main);
        mDataRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        header = (TextView) findViewById(R.id.header);
        status = (TextView) findViewById(R.id.textView2);
        checkForUser();
        header.setText("Welcome " + User.userName);
        userEmails = new ArrayList<>();
        getAllUsers();
    }

    private void getAllUsers () {
        mDataRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    DataSnapshot d = (DataSnapshot) i.next();
                    String e = d.child("email").getValue().toString();
                    if(!userEmails.contains(e)){
                        userEmails.add(e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }


    private void checkForUser () {
        if(User.userEmail == null){
            Intent signInIntent = new Intent(MainActivity.this,GSignIn.class);
            startActivity(signInIntent);
            this.finish();
        }
        return;
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser f){

    }

    public void addUser(View view){
        System.out.println(userEmails);
        EditText e = (EditText)findViewById(R.id.editText1);
        String userEmail = e.getText().toString();

        if(userEmails.contains(userEmail)){
            status.setTextColor(Color.RED);
            status.setText("User already added, try again");
            e.setText("");
            return;
        }
        addUserToDB(userEmail);
        status.setTextColor(Color.GREEN);
        status.setText("User added!");
    }

    private void addUserToDB(String email){
        String userID = randomString();
        DatabaseReference userDBRef = mDataRef.child("users/" + userID);
        userDBRef.child("email").setValue(email);
        userDBRef.child("status").setValue("Pending");
        DatabaseReference locks = userDBRef.child("locks");
        locks.child("Lock 1").setValue("true");
    }

    private String randomString() {
        SecureRandom r = new SecureRandom();
        return new BigInteger(130,r).toString(32);
    }
    public void goToBeam(android.view.View v){
        Intent beamIntent = new Intent(MainActivity.this,Beam.class);
        startActivity(beamIntent);
        this.finish();
    }
}
