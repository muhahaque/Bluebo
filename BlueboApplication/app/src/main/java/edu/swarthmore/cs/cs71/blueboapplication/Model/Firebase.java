package edu.swarthmore.cs.cs71.blueboapplication.Model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.*;
import edu.swarthmore.cs.cs71.blueboapplication.MainActivity;
import edu.swarthmore.cs.cs71.blueboapplication.SignupActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Firebase {
    // field names in the Firestore database
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

    // class fields
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    // note: currentUser is auth.getCurrentUser()

    // TODO implement all methods

    /* Proposed methods:
    *   1. Get user document
        2. Get user trades:
            a. in negotiation
            b. in progress
            c. complete
            -- Return list<Transaction>
        3. Get authentication information...?
        4. convert MapToTransaction (as in NegotiationsFragment)
        5. Convert Transaction to Map :) -- Check
    *
    */

    /**
     * Constructor, initializes authentication
     */
    public Firebase(){

        // Firebase authentication
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Returns FirebaseAuth object
     * @return - current FirebaseAuth object
     */
    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void signOut() {
        this.auth.signOut();
    }

    public void attemptLogin(String email, String password, final Context context, final Continuation<AuthResult> successContinuation, final Continuation<String> failContinuation) {

        // check if login information is valid
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // successful login
                            Log.d(TAG, "signInWithEmail:success");
                            // TODO: instead of this, fetch user's version of the app and go to that main activity
                            //Intent homepageIntent = new Intent(LoginActivity.this, MainActivity.class);
                            //startActivity(homepageIntent);
                            successContinuation.run(task.getResult()); // authResult - useful for Unit Testing
                        } else {
                            // login failure
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            failContinuation.run("null");
                        }
                    }
                });
    }

    public User getUserDocument(){

        // TODO:
        // 1. get (query for) a document
        // 2. Populate the user class


        return null;
    }


    // TODO implement logical OR
    public void getTransactionFromUsername(String username, final Continuation<Map<String, Object>> continuation) {

        Query userRecipient = db.collection(Firebase.TRANSACTIONS_KEY)
                .whereEqualTo(USER_RECIPIENT_UID, username);

        // Retrieve current user's open trades
        userRecipient.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    // retrieve user document id
                    QuerySnapshot findingUserSnap = task.getResult();
                    List<DocumentSnapshot> userTrans = findingUserSnap.getDocuments();
                    Map<String, Object> oneDocument = userTrans.get(0).getData();

                    // 'return' the one document
                    continuation.run(oneDocument);

                } else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void sendDirectTrade(final Transaction transaction, final Continuation<String> userNotFound, final Continuation<String> tradeSent) {
        // Get the UID of the recipient
        String searchString = transaction.getUserRecipient().getUsername();
        // Query for userDoc of recipient
        Query findRecipient = db.collection(Firebase.USERS_KEY).whereEqualTo("email", searchString+"@swarthmore.edu");

        // Retrieve UID of recipient
        findRecipient.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    // get document snap of query
                    QuerySnapshot recipientSnap = task.getResult();

                    // list contains our user doc
                    List<DocumentSnapshot> recipientDocs = recipientSnap.getDocuments();

                    if (recipientDocs.size() == 0) {
                        userNotFound.run("null");
                        return;
                    } else if (recipientDocs.size() != 1){
                        Log.d(TAG, "FIRESTORE ERROR: Multiple users with that name found. Major error!");
                    }

                    // access UID of the recipient's userDoc
                    transaction.getUserRecipient().setUid(recipientDocs.get(0).get("Uid").toString());
                    String username = recipientDocs.get(0).get("email").toString();
                    username = username.split("@")[0]; // take email 'kday1@swarthmore' --> 'kday1'
                    transaction.getUserRecipient().setUsername(username);
                    Map<String, Object> transactionMap = transactionToMap(transaction);

                    // Add transaction to 'transaction' database!
                    db.collection("transactions")
                            .add(transactionMap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "Posted user transaction to the transaction database");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to post user transaction to transaction database", e);
                                }
                            });

                    tradeSent.run("null");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to find user of entered email.", e);
            }
        });
    }

    public void addTransactionToDatabase(Map<String, Object> transactionMap) {
        db.collection("transactions")
                .add(transactionMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Posted user transaction to the transaction database");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to post user transaction to transaction database", e);
                    }
                });
    }

    /**
     * Gets transactions in the negotiations state for the current user
     * @param continuation
     */
    public void getNegotiationsTransactions(final Continuation<Transaction> continuation) {
        // Create a query against the collection, searching for the current user.
        // will return transactions where current user is recipient OR requester
        Query userRecipient = db.collection(Firebase.TRANSACTIONS_KEY)
                .whereEqualTo(USER_RECIPIENT_UID, auth.getCurrentUser().getUid());
        final Query userRequester = db.collection(Firebase.TRANSACTIONS_KEY)
                .whereEqualTo(USER_REQUESTER_UID, auth.getCurrentUser().getUid());

        // Retrieve current user's open trades
        userRecipient.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    // retrieve user document id
                    QuerySnapshot recipientSnap = task.getResult();
                    List<DocumentSnapshot> negotiationRecipient = recipientSnap.getDocuments();
                    if (negotiationRecipient.size() == 0){
                        Log.d(TAG, "FIRESTORE: User has no 'in-negotiation' transactions");
                    } else {
                        for (int i = 0; i < negotiationRecipient.size(); i++) {
                            // convert data from Firestore to transaction Model information
                            Transaction oneTrans = mapToTransaction(negotiationRecipient.get(i).getData());
                            // "run" method is defined in NegotiationsFragment
                            continuation.run(oneTrans);
                        }
                    }

                    userRequester.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot requesterSnap = task.getResult();
                            List<DocumentSnapshot> negotiationRequest = requesterSnap.getDocuments();
                            if (negotiationRequest.size() == 0) {
                                Log.d(TAG, "FIRESTORE: User has no requester 'in-negotiation' transactions");
                            } else {
                                for (int i = 0; i < negotiationRequest.size(); i++) {
                                    // convert data from Firestore to transaction Model information
                                    Transaction oneTrans = mapToTransaction(negotiationRequest.get(i).getData());
                                    // "run" method is defined in NegotiationsFragment
                                    continuation.run(oneTrans);
                                }
                            }
                        }
                    });
                    
                } else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Get all posts in the market place
     * @param continuation
     */
    public void getMarketplacePosts (final Continuation<Transaction> continuation) {
        // Create a query against the marketplace collection
        Query marketPlace = db.collection(MARKETPLACE_KEY);

        // Retrieve current user's open trades
        marketPlace.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    // retrieve user document id
                    QuerySnapshot findingMarketSnap = task.getResult();
                    List<DocumentSnapshot> marketTrans = findingMarketSnap.getDocuments();
                    if (marketTrans.size() == 0){
                        Log.d(TAG, "FIRESTORE: User has no 'marketplace' transactions");
                    } else {
                        for (int i = 0; i < marketTrans.size(); i++) {
                            // convert data from Firestore to transaction Model information
                            Transaction oneTrans = mapToMarketpost(marketTrans.get(i).getData());
                            // add list enteries to a list of transactions
                            //allTrans.add(oneTrans);
                            //adapter.notifyDataSetChanged();
                            continuation.run(oneTrans);
                        }
                    }

                } else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    /**
     * Creates a "user" in firebase authenticacion
     * @param email
     * @param password
     * @param context - the context of the calling activity (usually SignupActivity)
     * @param continuation - continuation callback function (written by caller)
     */
    public void createUser(String email, String password, final Context context, final Continuation<FirebaseAuth> continuation){

        final FirebaseAuth fbAuth = FirebaseAuth.getInstance();

        fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // successful signup
                            Log.d(TAG, "createUserWithEmail:success");
                            continuation.run(fbAuth);
                        } else {
                            // signup failure
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            // Set error toast - authenticatino failed
                            Toast.makeText(context, "Creating user Auth Token failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Adds a user to the Firestore 'user' database
     * @param userData - Map of user information
     */
    public void addUserToUserDatabase(Map<String, Object> userData){

        final FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        // add 'user' document to collection, use currentUID as document ID!
                // 'user' doc ID == that user's UID is a database INVARIANT
                db.collection(USERS_KEY).document(fbAuth.getUid())
                        .set(userData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "new 'user' document added");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error setting document", e);
                            }
                        });
    }

    /**
     * Change the user's Firebase Display Name to be user's first and last name.
     * @param user a user of type FirebaseUser
     * @param firstName user's first name of type string
     * @param lastName user's last name of type string
     */
    public void changeUserDisplayName(FirebaseUser user, String firstName, String lastName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(firstName + " " + lastName)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated (display name change).");
                        }
                    }
                });
    }

    /**
     * Send an email to the user's email with a link for the user to verify Bluebo account.
     * @param user the user to email, user is of type FirebaseUser
     */
    public void sendVerficationEmail(FirebaseUser user, final Context context) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Verification email sent.");
                            Toast.makeText(context, // context is likely Signup Activity context
                                    "Verification email sent to your email.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    // input is full email address including @swarthmore.edu
    // returns that user's UID
    public void getUIDfromEmail(String userEmail, final Continuation<String> continuation){ // QUESTION: What is the thing in <here>?

        // Get the UID of the recipient
        // Query for userDoc of recipient
        Query findUser = db.collection(USERS_KEY).whereEqualTo("email", userEmail);

        // Retrieve UID of recipient
        findUser.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    // get document snap of query
                    QuerySnapshot userSnap = task.getResult();

                    // list contains our user doc
                    List<DocumentSnapshot> recipientDocs = userSnap.getDocuments();
                    // checks that just one document was retrieved
                    if (recipientDocs.size() == 0) {
                        Log.d(TAG, "FIRESTORE ERROR: NO USER with that name found. Major error!");
                        return;
                    } else if (recipientDocs.size() != 1){
                        Log.d(TAG, "FIRESTORE ERROR: Multiple users with that name found. Major error!");
                        return;
                    }
                    // this "returns" the UID of user with given email address
                    continuation.run(recipientDocs.get(0).get("Uid").toString());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to find user of entered email.", e);
            }
        });
    }


    // Convert a standard Firestore Map to a Transaction class object
    public Transaction mapToTransaction(final Map<String, Object> inputMap){

        Transaction outputTrans = new Transaction();
        User userRequester = new User(inputMap.get(USER_REQUESTER).toString());
        userRequester.setUid(inputMap.get(USER_REQUESTER_UID).toString());
        outputTrans.setUserRequester(userRequester);

        User userRecipient = new User(inputMap.get(USER_RECIPIENT).toString());
        userRecipient.setUid(inputMap.get(USER_RECIPIENT_UID).toString());
        outputTrans.setUserRecipient(userRecipient);

        Currency offerCurrency = new Currency(inputMap.get(OFFER_CURRENCY).toString());
        int offerQuantity = Integer.parseInt(inputMap.get(OFFER_QUANT).toString());
        Payment offer = new Payment(offerCurrency, offerQuantity);
        outputTrans.setOffer(offer);

        Currency requestCurrency = new Currency(inputMap.get(REQUEST_CURRENCY).toString());
        int requestQuantity = Integer.parseInt(inputMap.get(REQUEST_QUANT).toString());
        Payment request = new Payment(requestCurrency, requestQuantity);
        outputTrans.setRequest(request);

        outputTrans.setNote(inputMap.get(NOTE).toString());

        //TODO: set date created???

        return outputTrans;
    }

    // Convert a standard Firestore Map to a Transaction class object
    private Transaction mapToMarketpost(final Map<String, Object> inputMap){

        Transaction outputTrans = new Transaction();
        User userRequester = new User(inputMap.get(POSTER).toString());

        // TODO: Might need to fetch and set the users' dateJoined? Also UId

        Currency sellingCurrency = new Currency(inputMap.get(SELLING_CURRENCY).toString());
        Currency buyingCurrency = new Currency(inputMap.get(BUYING_CURRENCY).toString());
        int sellingQuantity = Integer.parseInt(inputMap.get(SELLING_QUANT).toString());
        int buyingQuantity = Integer.parseInt(inputMap.get(BUYING_QUANT).toString());

        Payment request = new Payment(buyingCurrency, buyingQuantity);
        Payment offer = new Payment(sellingCurrency, sellingQuantity);

        outputTrans.setUserRequester(userRequester);
        outputTrans.setUserRecipient(null);
        outputTrans.setOffer(offer);
        outputTrans.setRequest(request);
        outputTrans.setNote(inputMap.get(NOTE).toString());

        //TODO: set date created
        return outputTrans;
    }

    // Convert a Transaction class object to a standard Firestore map
    public Map<String, Object> transactionToMap(Transaction inputTransaction) {
        Map<String, Object> outputMap = new HashMap<>();

        outputMap.put(USER_RECIPIENT, inputTransaction.getUserRecipient().getUsername());
        outputMap.put(USER_RECIPIENT_UID, inputTransaction.getUserRecipient().getUid());

        outputMap.put(USER_REQUESTER, inputTransaction.getUserRequester().getUsername());
        outputMap.put(USER_REQUESTER_UID, inputTransaction.getUserRequester().getUid());

        outputMap.put(OFFER_CURRENCY, inputTransaction.getOffer().getCurrency().getCurrencyName());
        outputMap.put(OFFER_QUANT, inputTransaction.getOffer().getQuantity());

        outputMap.put(REQUEST_CURRENCY, inputTransaction.getRequest().getCurrency().getCurrencyName());
        outputMap.put(REQUEST_QUANT, inputTransaction.getRequest().getQuantity());

        outputMap.put(NOTE, inputTransaction.getNote());

        return outputMap;

    }
}
