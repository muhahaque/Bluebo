package edu.swarthmore.cs.cs71.blueboapplication.Model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TransactionTest {
    @Test
    public void setDateCreated() throws Exception {
        Transaction transaction = new Transaction();
        Date date = new Date();
        transaction.setDateCreated(date);
        assertEquals(date, transaction.getDateCreated());
    }

    @Test
    public void setUserRecipient() throws Exception {
        Transaction transaction = new Transaction();
        User recipient = new User("test_user");
        transaction.setUserRecipient(recipient);
        assertEquals(recipient, transaction.getUserRecipient());
    }

    @Test
    public void setUserRequester() throws Exception {
        Transaction transaction = new Transaction();
        User requester = new User("test_user");
        transaction.setUserRequester(requester);
        assertEquals(requester, transaction.getUserRequester());
    }

    @Test
    public void setRequest() throws Exception {
        Transaction transaction = new Transaction();
        Payment request = new Payment(new Currency("Meals"), 5);
        transaction.setRequest(request);
        assertEquals(request, transaction.getRequest());
    }

    @Test
    public void setOffer() throws Exception {
        Transaction transaction = new Transaction();
        Payment offer = new Payment(new Currency("Meals"), 5);
        transaction.setOffer(offer);
        assertEquals(offer, transaction.getOffer());
    }

    @Test
    public void setNote() throws Exception {
        Transaction transaction = new Transaction();
        String note = "hello, world!";
        transaction.setNote(note);
        assertEquals(note, transaction.getNote());
    }

    @Test
    public void getDateCreated() throws Exception {
        Transaction transaction = new Transaction();
        assertNotNull(transaction.getDateCreated());
        Date date = new Date();
        transaction.setDateCreated(date);
        assertEquals(date, transaction.getDateCreated());
    }

    @Test
    public void getUserRecipient() throws Exception {
        Transaction transaction = new Transaction();
        assertNull(transaction.getUserRecipient());
        User recipient = new User("test_user");
        transaction.setUserRecipient(recipient);
        assertEquals(recipient, transaction.getUserRecipient());
    }

    @Test
    public void getUserRequester() throws Exception {
        Transaction transaction = new Transaction();
        assertNull(transaction.getUserRequester());
        User requester = new User("test_user");
        transaction.setUserRequester(requester);
        assertEquals(requester, transaction.getUserRequester());
    }

    @Test
    public void getRequest() throws Exception {
        Transaction transaction = new Transaction();
        assertNull(transaction.getRequest());
        Payment request = new Payment(new Currency("Meals"), 5);
        transaction.setRequest(request);
        assertEquals(request, transaction.getRequest());
    }

    @Test
    public void getOffer() throws Exception {
        Transaction transaction = new Transaction();
        assertNull(transaction.getOffer());
        Payment offer = new Payment(new Currency("Meals"), 5);
        transaction.setOffer(offer);
        assertEquals(offer, transaction.getOffer());
    }

    @Test
    public void getNote() throws Exception {
        Transaction transaction = new Transaction();
        assertNull(transaction.getNote());
        String note = "hello, world!";
        transaction.setNote(note);
        assertEquals(note, transaction.getNote());
    }

    @Test
    public void getState() throws Exception {
        Transaction transaction = new Transaction();
        assertNotNull(transaction.getState());
    }

}