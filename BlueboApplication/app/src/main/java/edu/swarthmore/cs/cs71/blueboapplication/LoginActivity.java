// source: https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/EmailPasswordActivity.java#L123-L147

package edu.swarthmore.cs.cs71.blueboapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Continuation;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Firebase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // UI references
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mLoginButton;
    private TextView mSignupLink;
    private Firebase dataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.user_email);
        mPasswordView = (EditText) findViewById(R.id.user_password);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mSignupLink = (TextView) findViewById(R.id.signup_link);
        dataBase = new Firebase();

        // log user in if log-in info is valid
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                attemptLogin(email, password);
            }
        });

        // option for new user to sign up
        mSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // send user to the right place if already signed in
        //FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (dataBase.getCurrentUser() != null) {
            // TODO: instead of this, fetch user's version of the app and go to that main activity
            Intent homepageIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(homepageIntent);
        }
    }

    /**
     * Attempt to log user in.
     * @param email user's email of type string
     * @param password user's password of type string
     */
    private void attemptLogin(String email, String password) {
        Log.d(TAG, "attemptLogin:" + email);

        // check if login information meets character requirements
        if (!meetsCharacterRequirements()) {
            return;
        }

        Continuation<AuthResult> successContinuation = new Continuation<AuthResult>() {
            @Override
            public void run(AuthResult s) {
                // TODO: instead of this, fetch user's version of the app and go to that main activity
                Intent homepageIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(homepageIntent);
            }
        };

        Continuation<String> failContinuation = new Continuation<String>() {
            @Override
            public void run(String s) {
                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
            }
        };
        dataBase.attemptLogin(email, password, LoginActivity.this, successContinuation, failContinuation);
    }

    /**
     * Checks that email and password meet the character requirements.
     * @return true if email and password are valid, false otherwise
     */
    private boolean meetsCharacterRequirements() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
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
        return true;
    }
}