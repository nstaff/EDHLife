package com.staffend.nicholas.lifecounter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.staffend.nicholas.lifecounter.data.EDHSettingsDataHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEDHGameStartListener} interface
 * to handle interaction events.
 * Use the {@link EDHGameSetup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EDHGameSetup extends Fragment implements CompoundButton.OnCheckedChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "EDHGAMESETUP";

    Spinner spn;
    EditText etxStartingLife, etxMaxPoison;
    CheckBox chbLinkDamage, chbAutoDelete;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnEDHGameStartListener mListener;

    public EDHGameSetup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EDHGameSetup.
     */
    // TODO: Rename and change types and number of parameters
    public static EDHGameSetup newInstance(String param1, String param2) {
        EDHGameSetup fragment = new EDHGameSetup();
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
        View v = inflater.inflate(R.layout.fragment_edhgame_setup, container, false);
        spn = (Spinner) v.findViewById(R.id.spnNumPlayers);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.num_players,
                R.layout.spinner_setup_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        etxMaxPoison = (EditText) v.findViewById(R.id.etxMaxPoison);
        etxStartingLife = (EditText) v.findViewById(R.id.etxStartingLife);
        chbLinkDamage = (CheckBox) v.findViewById(R.id.chbLinkDamage);
        chbAutoDelete = (CheckBox) v.findViewById(R.id.chbAutoDeleteDead);

        EDHSettingsDataHandler handler = new EDHSettingsDataHandler(getActivity());
        chbLinkDamage.setChecked(handler.isCommanderDamageLinked());
        chbAutoDelete.setChecked(handler.isAutoDeleteOn());
        etxMaxPoison.setText(String.valueOf(handler.getMaxPoison()));
        etxStartingLife.setText(String.valueOf(handler.getStartingLife()));
        spn.setSelection(handler.getNumPlayers() - 2);
        handler.close();

        chbAutoDelete.setOnCheckedChangeListener(this);
        chbLinkDamage.setOnCheckedChangeListener(this);

        ImageView btnGameStart = (ImageView) v.findViewById(R.id.imgGameStartyStart);
        btnGameStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onButtonPressed(v);
            }
        });
        Log.v(TAG, "set listener");
        //Button btn = (Button) v.findViewById(R.)
        return v;
    }

    // Load the parameters into a bundle and pass the bundle to the listener.
    public void onButtonPressed(View v) {

        if (mListener != null) {
            //Add 2 to spinner selection to get appropriate value for position.

            EDHSettingsDataHandler handler = new EDHSettingsDataHandler(getActivity());
            handler.setNumPlayers(spn.getSelectedItemPosition() + 2);
            handler.setStartingLife(Integer.parseInt(etxStartingLife.getText().toString()));
            handler.setMaxPoison(Integer.parseInt(etxMaxPoison.getText().toString()));
            handler.close();
            Log.v(TAG, "poison: "+ etxMaxPoison.getText().toString());

            mListener.onEDHGameStart();
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEDHGameStartListener) {
            mListener = (OnEDHGameStartListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEDHGameStartListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnEDHGameStartListener) {
            mListener = (OnEDHGameStartListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEDHGameStartListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        EDHSettingsDataHandler handler = new EDHSettingsDataHandler(getActivity());
        if(buttonView.getId() == chbAutoDelete.getId()){
            handler.setAutoDeletePlayers(isChecked);
        }else if(buttonView.getId() == chbLinkDamage.getId()){
            handler.setCommanderDamageLinked(isChecked);
        }
        handler.close();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnEDHGameStartListener {
        // TODO: Update argument type and name
        void onEDHGameStart();
    }
}
