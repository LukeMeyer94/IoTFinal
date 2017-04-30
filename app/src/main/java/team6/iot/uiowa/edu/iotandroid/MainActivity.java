package team6.iot.uiowa.edu.iotandroid;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDataRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private static final String TAG = "AnonAuth";
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mStorageRef = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_main);
        mDataRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        signInAnon();
    }

    private void signInAnon () {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser f){

    }

    public void testFunction(View view){
        EditText e = (EditText)findViewById(R.id.editText1);
        String userEmail = e.getText().toString();
        String userID = randomString();
        DatabaseReference userDBRef = mDataRef.child("users/" + userID);
        userDBRef.child("email").setValue(userEmail);
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
    }
}
