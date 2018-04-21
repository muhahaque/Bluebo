package edu.swarthmore.cs.cs71.blueboapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Firebase;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView mTextMessage;
    private Button mSignOutButton;
    private FloatingActionButton fab;
    private Firebase dataBase;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_transactions:
                    //mTextMessage.setText(R.string.title_home);
                    changeFragment(new TransactionsFragment(), "transactions_fragment");
                    return true;
                case R.id.navigation_trade:
                    //mTextMessage.setText(R.string.title_dashboard);
                    changeFragment(new DirectTradeFragment(), "trade_fragment");
                    return true;
                case R.id.navigation_notifications:
                    //TODO RENAME NAVIGATION_NOTIFICATIONS TO NAVIGATION_MARKETPLACE IN ALL PLACES IT'S USED (iteration 5)
                    //mTextMessage.setText(R.string.title_notifications);
                    changeFragment(new MarketplaceFragment(), "notifications_fragment");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase = new Firebase();
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_transactions);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new DirectTradeFragment(), "trade_fragment");
            }
        });

        // TODO bump user to login page if not signed in
        // note: doesn't do what it should right now.
        // Note, might be helpful: currentUser.reauthenticate(@NonNull firebaseAuth);
        // https://firebase.google.com/docs/reference/android/com/google/firebase/auth/AuthCredential
        if (dataBase.getCurrentUser() == null){
            dataBase.signOut();
            Intent loginPageIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginPageIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.sign_out:
            // sign user out and return to login page
            if (dataBase.getCurrentUser() != null) {
                dataBase.signOut();
                Intent loginPageIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginPageIntent);
            }
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    private void changeFragment(Fragment fragment, String tag) {
        if (fragment == null) { return; }

        FragmentManager fm = getSupportFragmentManager();
        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.content, fragment, tag);
                ft.commit();
            }
        }

    }

}
