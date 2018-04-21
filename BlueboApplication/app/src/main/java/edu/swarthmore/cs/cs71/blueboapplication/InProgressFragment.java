package edu.swarthmore.cs.cs71.blueboapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Transaction;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class InProgressFragment extends Fragment {

    RecyclerView recyclerView;


    public InProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  //String[] items = getResources().getStringArray(R.array.tab_in_progress);
        // TODO: GET RID OF THIS!!!!
        Transaction dummy1 = new Transaction();
        Transaction dummy2 = new Transaction();
        List<Transaction> dummyTransactions = new ArrayList<>();
        dummyTransactions.add(dummy1);
        dummyTransactions.add(dummy2);
        //
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dummyTransactions);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter); */
    }

}

