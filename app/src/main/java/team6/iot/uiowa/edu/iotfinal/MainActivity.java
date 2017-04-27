package team6.iot.uiowa.edu.iotfinal;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;
    private static final String TAG = "AnonAuth";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void testFunction(View view){
        mDataRef.push().setValue("test");

//        System.out.println(byteRef);
//        UploadTask uploadTask = byteRef.putBytes(bytes);
//        while(uploadTask.isInProgress()){
//            System.out.println("waiting...");
//        }
//        System.out.println(uploadTask.isSuccessful());



        System.out.println("BUTTON PRESSED");
    }

    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
//
//        TextView idView = (TextView) findViewById(R.id.anonymous_status_id);
//        TextView emailView = (TextView) findViewById(R.id.anonymous_status_email);
//        boolean isSignedIn = (user != null);
//
//        // Status text
//        if (isSignedIn) {
//            idView.setText(getString(R.string.id_fmt, user.getUid()));
//            emailView.setText(getString(R.string.email_fmt, user.getEmail()));
//        } else {
//            idView.setText(R.string.signed_out);
//            emailView.setText(null);
//        }
//
//        // Button visibility
//        findViewById(R.id.button_anonymous_sign_in).setEnabled(!isSignedIn);
//        findViewById(R.id.button_anonymous_sign_out).setEnabled(isSignedIn);
//        findViewById(R.id.button_link_account).setEnabled(isSignedIn);
    }
}
