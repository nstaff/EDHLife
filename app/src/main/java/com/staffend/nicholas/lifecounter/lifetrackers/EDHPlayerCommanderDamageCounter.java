package com.staffend.nicholas.lifecounter.lifetrackers;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.staffend.nicholas.lifecounter.R;
import com.staffend.nicholas.lifecounter.data.EDHPlayerDataHandler;
import com.staffend.nicholas.lifecounter.util.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EDHPlayerCommanderDamageCounter.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EDHPlayerCommanderDamageCounter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EDHPlayerCommanderDamageCounter extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String COMMANDER_NAME = "commanderName";
    private static final String STARTING_DAMAGE = "startingDamage";
    private static final String PARENT_COMMANDER = "parentCommander";
    private static final String PARENT_COMMANDER_ID = "parentCommanderID";

    // TODO: Rename and change types of parameters
    private String commanderName;
    private String startingDamage;
    private long parentCommander;
    private long mParentCommanderId;
    private TextView txvCommanderName, txvCommanderDamage;
    private ImageView btnIncrement, btnDecrement;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param commanderId the new Commander's Id.
     * @param startingDamage starting damage level.
     * @return A new instance of fragment EDHPlayerCommanderDamageCounter.
     */
    // TODO: Rename and change types and number of parameters


    public static EDHPlayerCommanderDamageCounter newInstance(long commanderId, String startingDamage, long parentCommanderID) {
        EDHPlayerCommanderDamageCounter fragment = new EDHPlayerCommanderDamageCounter();
        Bundle args = new Bundle();
        args.putLong(COMMANDER_NAME, commanderId);
        args.putString(STARTING_DAMAGE, startingDamage);
        args.putLong(PARENT_COMMANDER_ID, parentCommanderID);
        fragment.setArguments(args);
        return fragment;
    }

    public EDHPlayerCommanderDamageCounter() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        if (getArguments() != null) {
            EDHPlayerDataHandler handler = new EDHPlayerDataHandler(getActivity().getBaseContext());
            commanderName = handler.getPlayerName(getArguments().getLong(COMMANDER_NAME));
            startingDamage = getArguments().getString(STARTING_DAMAGE);
            mParentCommanderId = getArguments().getLong(PARENT_COMMANDER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_commander_damage, container, false);

        //get the UI components to interact with.
        txvCommanderName = (TextView)v.findViewById(R.id.txvCommanderName);
        txvCommanderDamage = (TextView)v.findViewById(R.id.txvCommanderDamage);
        btnDecrement = (ImageView)v.findViewById(R.id.ibtnDecrement);
        btnIncrement = (ImageView)v.findViewById(R.id.ibtnIncrement);


        //initialize the UI components' listeners
        txvCommanderName.setText(this.commanderName);
        txvCommanderDamage.setText(this.startingDamage);
        btnDecrement.setOnClickListener(this);
        btnIncrement.setOnClickListener(this);

        if(savedInstanceState != null){
            //this.parentCommander = savedInstanceState.getString(PARENT_COMMANDER);
            //this.txvCommanderDamage.setText(savedInstanceState.getString(STARTING_DAMAGE));
        }

        return v;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEDHGameStartListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        int finalValue = 0;
        //find the caller and increment/decrement the damage

        if(v.getId() == R.id.ibtnDecrement){
            finalValue = Util.changeNumericTextViewValue(-1, txvCommanderDamage);
            if (!isDead(finalValue)){
                mListener.onLifeChange(1, mParentCommanderId);
            }
            Log.v("Decrement", String.valueOf(finalValue));
        }
        else if (v.getId() == R.id.ibtnIncrement){
            finalValue = Util.changeNumericTextViewValue(1, txvCommanderDamage);
            isDead(finalValue);
            if (!isDead(finalValue)){
                mListener.onLifeChange(-1, mParentCommanderId);
            }
            Log.v("Increment", String.valueOf(finalValue));
        }

    }

    public int getCommanderDamageTotal(){
        return Util.changeNumericTextViewValue(0, txvCommanderDamage);
    }

    public void setCommanderName(String newName){
        txvCommanderName.setText(newName);
    }

    private boolean isDead(int value){
        if (value > 20){
            Log.v("EDHDmgCtr", String.valueOf(mParentCommanderId));
            mListener.onPlayerDeath(mParentCommanderId);
            return true;
        }
        else return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPlayerDeath(long parentCommanderId);

        void onLifeChange(int change, long parentCommanderId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(PARENT_COMMANDER, parentCommander);
        outState.putString(STARTING_DAMAGE, txvCommanderDamage.getText().toString());
    }


}
