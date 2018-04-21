package edu.swarthmore.cs.cs71.blueboapplication;
//package edu.swarthmore.cs.cs71.blueboapplication.app.src.androidTest.java.edu.swarthmore.cs.cs71.blueboapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import edu.swarthmore.cs.cs71.blueboapplication.Model.*;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.crashlytics.android.beta.Beta.TAG;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FirebaseTests {
    private Context context;
    // rely upon our constant "test user"
    final private String testEmail = "test1@swarthmore.edu";
    final private String testPassword = "password123";
    final private String secondEmail = "test2@swarthmore.edu";
    final private String secondPassword = "password123";
    final private String newUser = "newUser@swarthmore.edu";
    final private String newUserPass = "password123";
    private String test1UID = "";


    private static final String REQUEST_QUANT = "request_quant";
    private static final String OFFER_QUANT = "offer_quant";
    private static final String NOTE = "note";
    private static final String OFFER_CURRENCY = "offer_currency";
    private static final String REQUEST_CURRENCY = "request_currency";
    private static final String USER_RECIPIENT = "user_recipient";
    private static final String USER_RECIPIENT_UID = "user_recipient_uid";
    private static final String USER_REQUESTER = "user_requester";
    private static final String USER_REQUESTER_UID = "user_requester_uid";
    private static final String USERS_KEY = "users";
    private static final String TRANSACTIONS_KEY = "transactions";
    private static final String POSTER = "user_seller";
    private static final String BUYING_CURRENCY = "buying_currency";
    private static final String BUYING_QUANT = "buying_quant";
    private static final String SELLING_CURRENCY = "selling_currency";
    private static final String SELLING_QUANT = "selling_quant";
    private static final String MARKETPLACE_KEY = "marketplace";


    // NOTE: YOU MUST BE SIGNED IN as 'test1@swarthmore.edu', password = 'password123'


    // Create two testing users
    @Before
    public void setup(){
        this.context  = InstrumentationRegistry.getContext();
        Firebase setupFirebase = new Firebase();

        Continuation<FirebaseAuth> success = new Continuation<FirebaseAuth>() {
            @Override
            public void run(FirebaseAuth auth) {
                // set test1@swarthmore.edu's UID
                test1UID = auth.getUid();
            }
        };
        setupFirebase.createUser(testEmail, testPassword, this.context, success);

         Continuation<FirebaseAuth> secondSuccess = new Continuation<FirebaseAuth>() {
            @Override
            public void run(FirebaseAuth auth) {
                // do nothing
            }
        };
        setupFirebase.createUser(secondEmail, secondPassword, this.context, secondSuccess);
    }

    // Cleanup testing users
    @After
    public void tearDown() {

        // delete two test users from databse.  Clean the slate for next test.

        // delete user 1

        // Can't delete user - it must be persistent!!

//        FirebaseUser test1User = FirebaseAuth.getInstance().getCurrentUser();
//        test1User.delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // OnComplete
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Failure
//                    }
//                });
    }


    @Test
    public void getCurrentUser() throws Exception {

        Firebase fb = new Firebase();
        FirebaseUser actualUser = fb.getCurrentUser();

        FirebaseUser expectedUser = FirebaseAuth.getInstance().getCurrentUser();

        // (expected, actual)
        //assertEquals(expectedUser, actualUser);
        Assert.assertEquals(expectedUser, actualUser);
    }

    @Test
    public void getDb() throws Exception {

        Firebase fb = new Firebase();
        FirebaseFirestore actualDB = fb.getDb();

        FirebaseFirestore expectedDB = FirebaseFirestore.getInstance();

        // (expected, actual)
        assertEquals(expectedDB, actualDB);
    }

    @Test
    public void signOut() throws Exception {
        Firebase fb = new Firebase();
        fb.signOut();

        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        fbAuth.signOut();

        // (expected, actual)
        assertEquals(fbAuth, fb.getAuth());
    }

    @Test
    public void attemptLogin() throws Exception {
        Firebase fb = new Firebase();

        // ACTUAL, from Firebase class
        Continuation<AuthResult> loginSuccess = new Continuation<AuthResult>() {
            @Override
            public void run(final AuthResult firebaseClassAuthResult) {

                // EXPECTED, direct from Firebase
                FirebaseAuth fbAuth = FirebaseAuth.getInstance();
                fbAuth.signInWithEmailAndPassword(testEmail, testPassword)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // (expected, actual)
                                assertEquals(authResult, firebaseClassAuthResult);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // (expected, actual)
                                // should never fail
                                Log.d(TAG, "ERROR: attemptLogin failed in testing suite");
                            }
                        });
            }
        };

        Continuation<String> loginFailure = new Continuation<String>() {
            @Override
            public void run(String s) {
                // Do nothing
            }
        };

        // call callbacks above.
        fb.attemptLogin(testEmail, testPassword, this.context, loginSuccess, loginFailure);
    }


    // Firebase Class method not implemented yet!
//    @Test
//    public void getUserDocument() throws Exception {
//
//
//        // (expected, actual)
//        assertEquals("", "");
//    }


    // Check if direct trades are properly created!
    @Test
    public void sendDirectTrade() throws Exception {
        Firebase fb = new Firebase();

        // TODO: Create direct trade object yourself. Compare that to what's actually contained in the Firebase Class.

        // Declare our variables
        User userRecipient  = new User("test1");
        User userRequester = new User("test2");

        Payment request = new Payment(new Currency("Points"), 3); // THIS IS WHAT WE WILL CHECK
        Payment offer = new Payment(new Currency("Meals"), 3); // THIS IS WHAT WE WILL CHECK

        Transaction currTrans = new Transaction();
        currTrans.setUserRequester(userRequester);
        currTrans.setUserRecipient(userRecipient);
        currTrans.setOffer(offer);
        currTrans.setRequest(request);

        Continuation<String> userNotFound = new Continuation<String>() {
            @Override
            public void run(String s) {
                // failure
            }
        };

        Continuation<String> tradeSent = new Continuation<String>() {
            @Override
            public void run(String s) {
                // Success
            }
        };
        // Call continuations above
        fb.sendDirectTrade(currTrans, userNotFound, tradeSent);

        // Check assertions
        Continuation<Map<String, Object>> thiscallback = new Continuation<Map<String, Object>>() {
            @Override
            public void run(Map<String, Object> stringObjectMap) {
                // was successful. Compare.
                assertEquals("Meals", stringObjectMap.get(OFFER_CURRENCY).toString());
                assertEquals("Points", stringObjectMap.get(REQUEST_CURRENCY).toString());
                assertEquals(3, Integer.parseInt(stringObjectMap.get(OFFER_QUANT).toString()));
                assertEquals(3, Integer.parseInt(stringObjectMap.get(REQUEST_QUANT).toString()));

                Log.d(TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ OFFER QUANT (3 expected):");
                Log.d(TAG, stringObjectMap.get(OFFER_CURRENCY).toString());
            }
        };

        fb.getTransactionFromUsername("test1", thiscallback); // 'test1' must be recipient rn
    }

    @Test
    public void addTransactionToDatabase() throws Exception {
        // set up the correct "environment"
        Firebase fb = new Firebase();
        Transaction transaction = new Transaction();
        transaction.setUserRecipient(new User("test1"));
        transaction.setUserRequester(new User("test2"));
        transaction.setOffer(new Payment(new Currency("meals"), 2));
        transaction.setRequest(new Payment(new Currency("dollars"), 15));
        transaction.setNote("TEST: addTransactionToDatabase()"); // unique note to ID this test transaction
        Map<String, Object> transactionMap = fb.transactionToMap(transaction);

        fb.addTransactionToDatabase(transactionMap);
        FirebaseFirestore db = fb.getDb();
        Query transactionQ = db.collection("transactions").whereEqualTo("note", transaction.getNote());
        transactionQ.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> ourDocs = task.getResult().getDocuments();
                        int numDocs = 0;
                        for (DocumentSnapshot document : ourDocs) {
                            assertTrue(document.exists());
                            numDocs += 1;
                        }
                        assertEquals(numDocs, 1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // TODO: testing implementation
//    @Test
//    public void getNegotiationsTransactions() throws Exception {
//        Firebase fb = new Firebase();
//        // (expected, actual)
//        assertEquals("", "");
//    }
//
//    @Test
//    public void getMarketplacePosts() throws Exception {
//        Firebase fb = new Firebase();
//        // (expected, actual)
//        assertEquals("", "");
//    }

    @Test
    public void createUser() throws Exception {
        Firebase fb = new Firebase();

        Continuation<FirebaseAuth> createdUser = new Continuation<FirebaseAuth>() {
            @Override
            public void run(FirebaseAuth firebaseAuth) {
                // check user exists now..

                FirebaseUser actualUser = firebaseAuth.getCurrentUser();

                // Check firebase user has correct email!!
                // (expected, actual)
                assertEquals(newUser, actualUser.getEmail());
            }
        };
        fb.createUser(newUser, newUserPass, this.context, createdUser);
    }

    @Test
    public void addUserToUserDatabase() throws Exception {
        Firebase fb = new Firebase();

        // Create a new user
        Map<String, Object> userData = new HashMap<>();
        userData.put("first_name", "testing");
        userData.put("last_name", "McTestFace");
        userData.put("email", newUser);
//        userData.put("Uid", firebaseAuth.getUid()); // Not able to do in testing conditions

        final Date dateJoined = new Date();
        userData.put("date_joined", dateJoined);

        fb.addUserToUserDatabase(userData);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query userRecipient = db.collection(USERS_KEY)
                .whereEqualTo("last_name", "McTestFace");

        // Retrieve current user's open trades
        userRecipient.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    // retrieve user document id
                    QuerySnapshot findingUserSnap = task.getResult();
                    List<DocumentSnapshot> userTrans = findingUserSnap.getDocuments();
                    Map<String, Object> oneDocument = userTrans.get(0).getData();

                    assertEquals("testing", oneDocument.get("first_name").toString());
                    assertEquals("McTestFace", oneDocument.get("last_name").toString());
                    assertEquals(dateJoined.toString(), oneDocument.get("date_joined").toString());
                } else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // (expected, actual)
        assertEquals("", "");
    }




    // Sending a verification email feels impossible to reasonably test (in this environment)
//    @Test
//    public void sendVerficationEmail() throws Exception {
//        Firebase fb = new Firebase();
//    }


    // TODO: Testing implementation
//    @Test
//    public void getUIDfromEmail() throws Exception {
//        Firebase fb = new Firebase();
//
//
//
//
//        // (expected, actual)
//        assertEquals("", "");
//    }
//
//        @Test
//    public void changeUserDisplayName() throws Exception {
//        Firebase fb = new Firebase();
//        // (expected, actual)
//        assertEquals("", "");
//    }
//
//    @Test
//    public void mapToTransaction() throws Exception {
//        Firebase fb = new Firebase();
//        // (expected, actual)
//        assertEquals("", "");
//    }
//
//    @Test
//    public void transactionToMap() throws Exception {
//        Firebase fb = new Firebase();
//        // (expected, actual)
//        assertEquals("", "");
//    }

}
