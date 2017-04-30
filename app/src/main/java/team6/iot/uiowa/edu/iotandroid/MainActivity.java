package team6.iot.uiowa.edu.iotandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;
    private TextView header;
    private TextView status;
    private User user;
    private List<String> userEmails;
    private EditText emailEntry;
    private Button addUserButton;
    private Button beamButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        header = (TextView) findViewById(R.id.header);
        status = (TextView) findViewById(R.id.textView2);
        emailEntry = (EditText)findViewById(R.id.editText1);
        addUserButton = (Button) findViewById(R.id.button);
        beamButton = (Button) findViewById(R.id.button2);
        checkForUser();
        userEmails = new ArrayList<>();
        getAllUsers();
        header.setText("Welcome " + User.userName);
    }

    private void validateUser() {
        if(userEmails.contains(User.userEmail)){
            status.setTextColor(Color.GREEN);
            status.setText("User validated!");
            emailEntry.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            mDataRef.child("users");
            return;
        } else{
            status.setTextColor(Color.RED);
            status.setText("User not validated");
            addUserButton.setEnabled(false);
            beamButton.setEnabled(false);
            emailEntry.setEnabled(false);
            emailEntry.setText("Login as a verified user to add new users");
        }

    }

    private boolean checkEmailValid( String email ) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailPattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
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
                        System.out.println("adding " + e + " to user email list");
                        userEmails.add(e);
                    }
                }
                validateUser();
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
//        EditText e = (EditText)findViewById(R.id.editText1);
        String userEmail = emailEntry.getText().toString();

        if(userEmails.contains(userEmail)){
            status.setTextColor(Color.RED);
            status.setText("User already added, try again");
            emailEntry.setText("");
            emailEntry.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            return;
        }
        if(!checkEmailValid(userEmail)){
            status.setTextColor(Color.RED);
            status.setText("Invalid email, try again");
            emailEntry.setText("");
            emailEntry.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            return;
        }
        addUserToDB(userEmail);
        status.setTextColor(Color.GREEN);
        status.setText("User added!");
        emailEntry.setText("");
        emailEntry.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
    }

    private void addUserToDB(String email){
        String userID = randomString();
        DatabaseReference userDBRef = mDataRef.child("users/" + userID);
        userDBRef.child("email").setValue(email);
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
