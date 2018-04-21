package edu.swarthmore.cs.cs71.blueboapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import edu.swarthmore.cs.cs71.blueboapplication.Model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class NegotiationsFragment extends Fragment {

    private RecyclerViewAdapter adapter;
    private List<Transaction> allTrans = new ArrayList<>();
    private RecyclerView recyclerView;
    private Firebase dataBase;

    public NegotiationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecyclerViewAdapter(allTrans);
        dataBase = new Firebase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_negotiations, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allTrans.clear();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Populate with transactions
        //TODO currently populates with ALL transactions, need to add transaction_state field to database so we can differentiate them
        Continuation<Transaction> transactionContinuation = new Continuation<Transaction>() {
            @Override
            public void run(Transaction oneTrans) {
                allTrans.add(oneTrans);
                adapter.notifyDataSetChanged();
            }
        };
        dataBase.getNegotiationsTransactions(transactionContinuation);

    }

}
