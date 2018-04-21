package edu.swarthmore.cs.cs71.blueboapplication.Model;

import java.util.Date;

public class Transaction {

    private Date dateCreated;
    private User userRecipient;
    private User userRequester;
    private Payment request;
    private Payment offer;
    private TransactionState state;
    private String note;



    public Transaction() {

        this.dateCreated = new Date();
        this.state = new TransactionStateNegotiation();
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setUserRecipient(User userRecipient) {
        this.userRecipient = userRecipient;
    }

    public void setUserRequester(User userRequester) {
        this.userRequester = userRequester;
    }

    public void setRequest(Payment request) {
        this.request = request;
    }

    public void setOffer(Payment offer) {
        this.offer = offer;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public User getUserRecipient() {
        return userRecipient;
    }

    public User getUserRequester() {
        return userRequester;
    }

    public Payment getRequest() {
        return request;
    }

    public Payment getOffer() {
        return offer;
    }

    public String getNote() { return note; }

    public TransactionState getState() {
        return state;
    }

    private interface TransactionState {

        void accept(User userWhoClicked);

        void cancel();

    }

    private class TransactionStateNegotiation implements TransactionState {

        @Override
        public void accept(User userWhoClicked) {

            // state transitions to in-progress if person accepting is recipient
            if (userWhoClicked == userRecipient) {
                state = new TransactionStateInProgress();
            }

        }

        @Override
        public void cancel() {
            // TODO: how do you delete from firebase?
        }
    }

    private class TransactionStateInProgress implements TransactionState {

        boolean userRecipientDone;
        boolean userRequesterDone;

        @Override
        public void accept(User userWhoClicked) {
            if (userRecipient == userWhoClicked) {
                userRecipientDone = true;
            } else {
                userRequesterDone = true;
            }

            if (userRecipientDone && userRequesterDone) {
                state = new TransactionStateComplete();
            }
        }

        @Override
        public void cancel() {
            // Does nothing
        }
    }

    private class TransactionStateComplete implements TransactionState {

        @Override
        public void accept(User userWhoClicked) {
            // do nothing
        }

        @Override
        public void cancel() {
            // do nothing
        }
    }


}
