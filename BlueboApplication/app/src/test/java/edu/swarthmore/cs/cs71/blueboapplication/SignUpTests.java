package edu.swarthmore.cs.cs71.blueboapplication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SignUpTests {

    SignupActivity signUpObject = new SignupActivity();

    @Test
    public void signUpInfoIsValid() {
        String bad_email = "dave@gmail.com";
        // assertFalse("Not a Swarthmore email", meetsCharacterRequirements);
        String good_email = "dave@swarthmore.edu";
        // TODO: write tests like below ex:
        // Assert.assertEquals(ExpectedOutput, ActualOutput);
    }


    @Test
    public void createFirebaseAuth() {


    }
}
