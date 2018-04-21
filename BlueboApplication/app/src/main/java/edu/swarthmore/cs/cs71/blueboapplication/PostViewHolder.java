package edu.swarthmore.cs.cs71.blueboapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Transaction;

public class PostViewHolder extends RecyclerView.ViewHolder {

    private TextView posterView;
    private TextView posterPlaceholder;
    private TextView requestCurrency;
    private TextView requestQuantity;
    private TextView offerCurrency;
    private TextView offerQuantity;
    private TextView note;
    private ImageView tradeImage;


    public PostViewHolder(View itemView) {
        super(itemView);
        posterView = (TextView) itemView.findViewById(R.id.poster);
        posterPlaceholder = (TextView) itemView.findViewById(R.id.poster_placeholder);
        requestQuantity = (TextView) itemView.findViewById(R.id.request_quantity);
        requestCurrency = (TextView) itemView.findViewById(R.id.request_currency);
        offerQuantity = (TextView) itemView.findViewById(R.id.offer_quantity);
        offerCurrency = (TextView) itemView.findViewById(R.id.offer_currency);
        note = (TextView) itemView.findViewById(R.id.note);
        tradeImage = (ImageView) itemView.findViewById(R.id.test_picasso);
        Picasso.with(itemView.getContext()).load("https://firebasestorage.googleapis.com/v0/b/bluebo-application.appspot.com/o/Frog_whip.jpg?alt=media&token=b18c334c-60d9-458a-8ef1-bd2c3e0fd591").into(tradeImage);

    }

    public void bind(Transaction transaction) {

        posterView.setText(transaction.getUserRequester().getUsername());
        posterPlaceholder.setText(transaction.getUserRequester().getUsername());
        requestQuantity.setText(String.valueOf(transaction.getRequest().getQuantity()));
        requestCurrency.setText(transaction.getRequest().getCurrency().getCurrencyName());
        offerQuantity.setText(String.valueOf(transaction.getOffer().getQuantity()));
        offerCurrency.setText(transaction.getOffer().getCurrency().getCurrencyName());
        note.setText(transaction.getNote());

    }
}
