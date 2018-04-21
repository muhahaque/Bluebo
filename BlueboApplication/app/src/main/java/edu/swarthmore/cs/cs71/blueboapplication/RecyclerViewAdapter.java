package edu.swarthmore.cs.cs71.blueboapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Transaction;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    List<Transaction> items;

    public RecyclerViewAdapter(List<Transaction> items) {
        this.items = items;
    }

    @Override
    public TextItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list_item, parent, false);
        return new TextItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextItemViewHolder myHolder = (TextItemViewHolder) holder;
        myHolder.bind(items.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
