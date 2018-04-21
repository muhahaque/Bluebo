package edu.swarthmore.cs.cs71.blueboapplication.Model;

public class Payment {

    private Currency currency;
    private int quantity;

    public Payment(Currency currency, int quantity) {
        this.currency = currency;
        this.quantity = quantity;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getQuantity() {
        return quantity;
    }


}
