// source: // source: https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/EmailPasswordActivity.java#L123-L147

package edu.swarthmore.cs.cs71.blueboapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Continuation;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Firebase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    // UI references
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mSignupButton;
    // Firebase
    private Firebase fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirstName = findViewById(R.id.new_first_name);
        mLastName = findViewById(R.id.new_last_name);
        mEmailView = findViewById(R.id.new_email);
        mPasswordView = findViewById(R.id.new_password);
        mSignupButton = findViewById(R.id.signup_button);

        // create account if sign up info is valid
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetCharacterRequirements()) {
                    createAccount();
                }
            }
        });
    }

    /**
     * Checks that new account information is valid.
     * @return true if info is valid, false otherwise.
     */
    private boolean meetCharacterRequirements() {
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (firstName.isEmpty()) {
            mFirstName.setError("required");
            return false;
        }
        if (lastName.isEmpty()) {
            mLastName.setError("required");
            return false;
        }
        if (email.isEmpty()) {
            mEmailView.setError("required");
            return false;
        }
        if (!email.contains("@swarthmore.edu")) {
            mEmailView.setError("must be a Swarthmore email");
            return false;
        }
        if (password.length() < 8) {
            mPasswordView.setError("must be at least 8 characters");
            return false;
        }
        return true;    }





    /**
     * Create a Bluebo Account for the user, and register the user as a FirebaseUser.
     */
    private void createAccount() {
        final String firstName = mFirstName.getText().toString();
        final String lastName = mLastName.getText().toString();
        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString(); // TODO: I think this is a security flaw? -k
        // init firebase
        this.fb = new Firebase();
        final FirebaseFirestore db = fb.getDb();

        Log.d(TAG, "createAccount:" + email);

        Continuation<FirebaseAuth> createUser = new Continuation<FirebaseAuth>() {
            @Override
            public void run(FirebaseAuth firebaseAuth) {

                // TODO: instead of this, fetch user's version of the app and go to that main activity
                Intent homepageIntent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(homepageIntent);
                fb.sendVerficationEmail(firebaseAuth.getCurrentUser(), SignupActivity.this);

                // inside Android settings
                fb.changeUserDisplayName(firebaseAuth.getCurrentUser(), firstName, lastName);


                // Create a new user
                Map<String, Object> userData = new HashMap<>();
                userData.put("first_name", firstName);
                userData.put("last_name", lastName);
                userData.put("email", email);
                userData.put("Uid", firebaseAuth.getUid());

                Date dateJoined = new Date();
                userData.put("date_joined", dateJoined);
                // not going to store their password in plaintext lol

               fb.addUserToUserDatabase(userData); // no continuation callback
            }
        };
        // Call continuation above.
        fb.createUser(email, password, SignupActivity.this, createUser);
    }

    @Override
    public void onDestroy(){
        // method no longer necessary, but might be later -k
    }
}