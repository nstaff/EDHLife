package com.staffend.nicholas.lifecounter.util;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.staffend.nicholas.lifecounter.R;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiceFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mDiceValue;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * Note: This factory method exists with parameters for future use, however null values
     * should be passed at the moment.
     *
     * @param param1 Parameter 1. - null not used
     * @param param2 Parameter 2. - null not used
     * @return A new instance of fragment DiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiceFragment newInstance(String param1, String param2) {
        DiceFragment fragment = new DiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DiceFragment() {
        // Required empty public constructor
    }

    /**
     * OnCreate method. Standard build.
     * Arguments not currently uses
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {// do nothing
        }
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dice, container, false);

        //instantiate the spinner and set the adapter to the array resource values
        Spinner diceSpinner = (Spinner)v.findViewById(R.id.spnNumberSides);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.dice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diceSpinner.setAdapter(adapter);

        //Set the spinner to the default value
        int defaultPosition = 4;
        diceSpinner.setSelection(defaultPosition);
        String[] array = getResources().getStringArray(R.array.dice_array);
        mDiceValue = array[defaultPosition];

        //find the button and set the listener
        ImageButton btnRollDice = (ImageButton)v.findViewById(R.id.ibtRollDice);
        btnRollDice.setOnClickListener(this);

        //Done!
        return v;

    }

    /**
     * EMPTY
     * Implemented for building of an alternate view. Framework still exists here for future development
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //make sure its not selecting the custom feature
        if(position == parent.getCount())
        {
            Toast.makeText(getContext(), "Getting there.. but not yet", Toast.LENGTH_SHORT).show();
            parent.setSelection(0);
        }
        else{
            mDiceValue = (String) parent.getItemAtPosition(position);
        }

    }

    /**
     * Do nothing. Overridden to ensure that nothing is done.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }


    /**
     * Generates a die roll result for display
     * @param v the associated view
     */
    @Override
    public void onClick(View v) {
        Spinner spinner = (Spinner)getView().findViewById(R.id.spnNumberSides);
        String diceSidesStr = ((String) spinner.getSelectedItem()).trim();
        int n;
        try{
            n = Integer.parseInt(diceSidesStr);
        }//catch for something. Probably text.
        catch(NumberFormatException e){
            Toast.makeText(getContext(), "n can't be words... ;) Rolling a 12-sider", Toast.LENGTH_SHORT).show();
            n=12;
        }

        Random r = new Random();
        int result = r.nextInt(n) + 1;
        //generate a die roll and show results
        TextView txvResult = (TextView) this.getView().findViewById(R.id.txvResult);

        //shift results into older queue
        if(txvResult.getText().toString() != null)
        {
            String oldRoll = txvResult.getText().toString();
            TextView txvOldResult = (TextView) this.getView().findViewById(R.id.txvPrevResult);
            txvOldResult.setText(oldRoll);
        }
        txvResult.setText(String.valueOf(result));
    }
}
