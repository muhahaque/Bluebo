package edu.swarthmore.cs.cs71.blueboapplication;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DirectTradeFragmentTests {

    @Test
    public void someTest() {
        /*
         * Tests:
         * - Check database auth
         * - Check database write and read
         * - Check database write and query
         */
    }

    @Test
    public void checkFirestoreAuthorization() {
        // Assert.assertEquals(ExpectedOutput, ActualOutput);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = firebaseAuth.createUserWithEmailAndPassword("testing@swarthmore.edu", "password1");
        FirebaseUser currUser = firebaseAuth.getCurrentUser();

        // Assert.assertEquals("kday1@swarthmore.edu", currUser.getEmail());
        currUser.getUid();
    }

    @Test
    public void checkFirestoreReadWrite() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Map<String, Object> transaction = new HashMap<>();
                // fieldName, data
        transaction.put("string_field", "I'm a string!");
        transaction.put("int_field", 1234);
        transaction.put("bool_field", true);


        // Write data to Firestore
        // save transaction to current user's transaction collection
        db.collection("testing").document("DirectTradeFragmentTests")
                .set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Document not saved with error: ", e);
            }
        });
    }
}

