package edu.swarthmore.cs.cs71.blueboapplication.Model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentTest {
    @Test
    public void getCurrency() throws Exception {
        Currency testCurr = new Currency("Meals");
        Payment testPayment = new Payment(testCurr, 4);
        assertEquals("Meals", testPayment.getCurrency().getCurrencyName());
    }

    @Test
    public void getQuantity() throws Exception {
        Currency testCurr = new Currency("Meals");
        Payment testPayment = new Payment(testCurr, 4);
        assertEquals(4, testPayment.getQuantity());
    }

}