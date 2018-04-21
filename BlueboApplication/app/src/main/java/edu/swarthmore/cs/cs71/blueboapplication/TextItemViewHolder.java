package edu.swarthmore.cs.cs71.blueboapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Transaction;

public class TextItemViewHolder extends RecyclerView.ViewHolder {
    private TextView requesterView;
    private TextView offererView;
    private TextView recipientView;
    private TextView requestCurrency;
    private TextView requestQuantity;
    private TextView offerCurrency;
    private TextView offerQuantity;
    private TextView note;


    public TextItemViewHolder(View itemView) {
        super(itemView);
        requesterView = (TextView) itemView.findViewById(R.id.requester);
        offererView = (TextView) itemView.findViewById(R.id.offerer);
        recipientView = (TextView) itemView.findViewById(R.id.recipient);
        requestQuantity = (TextView) itemView.findViewById(R.id.request_quantity);
        requestCurrency = (TextView) itemView.findViewById(R.id.request_currency);
        offerQuantity = (TextView) itemView.findViewById(R.id.offer_quantity);
        offerCurrency = (TextView) itemView.findViewById(R.id.offer_currency);
        note = (TextView) itemView.findViewById(R.id.note);
    }

    public void bind(Transaction transaction) {

        requesterView.setText(transaction.getUserRequester().getUsername());
        offererView.setText(transaction.getUserRequester().getUsername());
        recipientView.setText(transaction.getUserRecipient().getUsername());
        requestQuantity.setText(String.valueOf(transaction.getRequest().getQuantity()));
        requestCurrency.setText(transaction.getRequest().getCurrency().getCurrencyName());
        offerQuantity.setText(String.valueOf(transaction.getOffer().getQuantity()));
        offerCurrency.setText(transaction.getOffer().getCurrency().getCurrencyName());
        note.setText(transaction.getNote());
    }

}
