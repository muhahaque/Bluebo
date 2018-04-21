package edu.swarthmore.cs.cs71.blueboapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//import junit.framework.Assert;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static android.content.ContentValues.TAG;
//import static junit.framework.Assert.assertNotNull;
//
//public class DirectTradeFragmentTests {
//    String USERS_KEY = "users";
//    String TRANSACTIONS_KEY = "transactions";
//
//    @Rule
//    public ActivityTestRule<MainActivity> DirectTradeFragmentTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
//
//    private MainActivity mainActivity = null;
//
//    @Before
//    public void setUp() {
//
//        mainActivity = DirectTradeFragmentTestRule.getActivity();
//    }
//
//
//    /* Verifies that
//     * 1. writing to and reading from Firebase works as expected
//     * 2. All events happen sucessfully, and info matches up.
//    */
//    @Test
//    public void checkFirestoreReadWrite() {
//
//        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        final Map<String, Object> transaction = new HashMap<>();
//                // fieldName, data
//        transaction.put("string_field", "I'm a string!");
//        transaction.put("int_field", 800);
//        transaction.put("bool_field", true);
//
//
//        // Write data to Firestore
//        // save transaction to current user's transaction collection
//        db.collection("testing").document("DirectTradeFragmentTests")
//                .set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "Document has been saved!");
//
//
//                // Read data from Firestore
//
//                final DocumentReference docRef = db.collection("testing").document("DirectTradeFragmentTests");
//
//                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            // if it's sucessful, this call retrieves your document.
//                            DocumentSnapshot document = task.getResult();
//
//                            Assert.assertEquals("I'm a string!", document.get("string_field"));
//                            Assert.assertEquals(800, document.get("int_field"));
//                            Assert.assertEquals(true, document.get("bool_field"));
//
//                            if (document != null) {
//                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
//                            } else {
//                                Log.d(TAG, "No such document");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                        }
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Document not saved with error: ", e);
//            }
//        });
//    }
//
//    /* Verifies that
//     * 1. users can be created in authentication database.
//     * 2. auth info can be then added to Firestore database
//     * 3. All events happen sucessfully, and info matches up.
//    */
//    @Test
//    public void checkFirebaseAuthorization() {
//        // Assert.assertEquals(ExpectedOutput, ActualOutput);
//
//
//        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        //FirebaseUser user = firebaseAuth.createUserWithEmailAndPassword("testing@swarthmore.edu", "password1");
//
//        // create Firebase authentication item (not Firestore user)
//        firebaseAuth.createUserWithEmailAndPassword("testing@swarthmore.edu", "password1")
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // successful signup
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser currUser = firebaseAuth.getCurrentUser();
//                            // send welcome email to user
//
//                            // Add user info to database:
//
//                            // Create a new user in testing user database
//                            Map<String, Object> userData = new HashMap<>();
//                            userData.put("first_name", "demoName");
//                            userData.put("last_name", "finalName");
//                            userData.put("email", "testing@swarthmore.edu");
//                            userData.put("Uid", currUser.getUid());
//                            // not going to store their password in plaintext lol
//
//                            // add 'user' document to collection, use currentUID as document ID!
//                            // 'user' doc ID == that user's UID is a database INVARIANT
//                            db.collection("testing").document("test_auth")
//                                    .set(userData)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Log.d(TAG, "new 'user' document added");
//
//                                            // READ TO VERIFY INPUT:
//                                            DocumentReference docRef = db.collection("testing").document("test_auth");
//                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                    if (task.isSuccessful()) {
//                                                        // if it's sucessful, this call retrieves your document.
//                                                        DocumentSnapshot document = task.getResult();
//
//                                                        // CONDUCT TEST ASSERTIONS:
//                                                        Assert.assertEquals("testing@swarthmore.edu", document.get("email"));
//                                                        Assert.assertEquals("finalName", document.get("last_name"));
//                                                        Assert.assertEquals("demoName", document.get("first_name"));
//
//                                                        if (document != null) {
//                                                            Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
//                                                        } else {
//                                                            Log.d(TAG, "No such document");
//                                                        }
//                                                    } else {
//                                                        Log.d(TAG, "get failed with ", task.getException());
//                                                    }
//                                                }
//                                            });
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.d(TAG, "Error setting document", e);
//                                        }
//                                    });
//                        } else {
//                            // signup failure
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                        }
//                    }
//                });
//    }
//
//    @After
//    public void classCleanup() {
//        // delete created Firestore elements
//
//        // TODO: sign in with user above credentials, and delete account.
//        // ^^ Firebase is difficult bec normally built around idea of currentUser.
//
////        AuthCredential credential = EmailAuthProvider
////                .getCredential("testing@swarthmore.edu", "password1");
////
////        FirebaseAuth someAuth = FirebaseAuth
////                .signInWithEmailAndPassword("testing@swarthmore.edu", "password1")
////                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
////                    @Override
////                    public void onComplete(@NonNull Task<AuthResult> task) {
////
////                    }
////                }).addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////
////                    }
////                });
////
////        FirebaseUser user = FirebaseAuth.getInstance().signInWithEmailAndPassword("testing@swarthmore.edu", "password1");
////
////        user.delete()
////                .addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        if (task.isSuccessful()) {
////                            Log.d(TAG, "User account deleted.");
////                        }
////                    }
////                });
//
//        mainActivity = null;
//    }
//}
