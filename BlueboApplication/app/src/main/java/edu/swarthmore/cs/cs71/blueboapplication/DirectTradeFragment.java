package edu.swarthmore.cs.cs71.blueboapplication;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import edu.swarthmore.cs.cs71.blueboapplication.Model.*;
import edu.swarthmore.cs.cs71.blueboapplication.Model.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DirectTradeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DirectTradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectTradeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "DirectTradeFragment";


    // Firestore access
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // UI references
    private TextInputEditText mSearchText;
    private LinearLayout mRequestContainer;
    private LinearLayout mOfferContainer;
    private TextInputEditText mOfferQuant;
    private TextInputEditText mRequestQuant;
    private Button mSendRequestButton;
    private Spinner mRequestCurrency;
    private Spinner mOfferCurrency;
    private TextInputEditText mNote;
    private Firebase dataBase;

    private OnFragmentInteractionListener mListener;

    public DirectTradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectTradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectTradeFragment newInstance(String param1, String param2) {
        DirectTradeFragment fragment = new DirectTradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_direct_trade, container, false);

        return view;
    }

    // onViewCreated is where these fields must be referenced from, because they won't be created till after onCreateView returns.
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBase = new Firebase();

        // assign private variables
        setupView(view);

        // set up listener for when "Send Request" is clicked
        mSendRequestButton.setOnClickListener(new View.OnClickListener() {
            // onclick of 'send request' button, save transaction to Firestore
            @Override
            public void onClick(View v) {
                // Save transaction to Firestore

                // check necessary fields are populated
                boolean validUser = isValidUsername();
                boolean requestSet = isRequestSet();
                if (!validUser || !requestSet) {
                    return;
                }

                // Declare our variables
                Transaction currTrans = createTransaction();

                //Write new Transaction to Firebase database of transactions
                writeToFirebase(currTrans, view);
            }
        });
    }

    private void setupView(View view) {
        mSearchText = (TextInputEditText) view.findViewById(R.id.search_bar);
        mSendRequestButton = (Button) view.findViewById(R.id.send_request_but);
        mRequestContainer = (LinearLayout) view.findViewById(R.id.request);
        mRequestQuant = (TextInputEditText) mRequestContainer.findViewById(R.id.quantity_txt);
        mOfferContainer = (LinearLayout) view.findViewById(R.id.offer);
        mOfferQuant = (TextInputEditText) mOfferContainer.findViewById(R.id.quantity_txt);
        mRequestCurrency = (Spinner) mRequestContainer.findViewById(R.id.currency_spinner);
        mOfferCurrency = (Spinner) mOfferContainer.findViewById(R.id.currency_spinner);
        mNote = (TextInputEditText) view.findViewById(R.id.offer_note_txt);

        // populate the spinner with currencies
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.currencies_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        mRequestCurrency.setAdapter(adapter);
        mOfferCurrency.setAdapter(adapter);
    }

    private void writeToFirebase(Transaction currTrans, final View view) {
        Continuation<String> userNotFound = new Continuation<String>() {
            @Override
            public void run(String s) {
                mSearchText.setError("username not found");
            }
        };

        Continuation<String> tradeSent = new Continuation<String>() {
            @Override
            public void run(String s) {
                Snackbar.make(view, "Trade request sent!", Snackbar.LENGTH_LONG).show();
            }
        };
        // Call continuations above
        dataBase.sendDirectTrade(currTrans, userNotFound, tradeSent);
    }

    private Transaction createTransaction() {
        User userRecipient  = new User(mSearchText.getText().toString());
        String username = dataBase.getCurrentUser().getEmail().split("@")[0];
        User userRequester = new User(username);
        userRequester.setUid(dataBase.getCurrentUser().getUid());
        Payment request = new Payment(new Currency(mRequestCurrency.getSelectedItem().toString()), Integer.parseInt(mRequestQuant.getText().toString()));
        Payment offer;
        // check optional fields. Push null if not set
        if (mOfferQuant.getText() == null) {
            offer = new Payment(new Currency(null), Integer.parseInt(null));
        } else {
            offer = new Payment(new Currency(mOfferCurrency.getSelectedItem().toString()), Integer.parseInt(mOfferQuant.getText().toString()));
        }

        Transaction currTrans = new Transaction();
        currTrans.setUserRequester(userRequester);
        currTrans.setUserRecipient(userRecipient);
        currTrans.setOffer(offer);
        currTrans.setRequest(request);

        if (mNote.getText() == null) {
            currTrans.setNote(null);
        } else {
            currTrans.setNote(mNote.getText().toString());
        }

        return currTrans;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private boolean isValidUsername() {
        String username = mSearchText.getText().toString();
        if (username.isEmpty()) {
            mSearchText.setError("username required");
            return false;
        }
        if (username.contains("@")) {
            mSearchText.setError("specify username not email");
            return false;
        }
        return true;
    }

    private boolean isRequestSet() {
        String requestQuantity = mRequestQuant.getText().toString();
        if (requestQuantity.isEmpty()) {
            mRequestQuant.setError("quantity of request required");
            return false;
        }
        return true;
    }

}
