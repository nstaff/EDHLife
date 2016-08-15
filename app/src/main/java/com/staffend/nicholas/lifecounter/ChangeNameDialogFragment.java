package com.staffend.nicholas.lifecounter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangeNameDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangeNameDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeNameDialogFragment extends DialogFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IDENTITY_TAG = "param1";
    private static final String CURRENT_NAME = "param2";
    private static final String LOG_TAG = "ChangeNameDlgFrg";

    // TODO: Rename and change types of parameters
    private String mTag;
    private String mName;

    private EditText mUserInput;
    private Button mOkButton, mCancelButton;


    private OnFragmentInteractionListener mListener;

    public ChangeNameDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tag The tag used to identify the
     * @param currentName Parameter 2.
     * @return A new instance of fragment ChangeNameDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeNameDialogFragment newInstance(String tag, String currentName) {
        ChangeNameDialogFragment fragment = new ChangeNameDialogFragment();
        Bundle args = new Bundle();
        args.putString(IDENTITY_TAG, tag);
        args.putString(CURRENT_NAME, currentName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTag = getArguments().getString(IDENTITY_TAG);
            mName = getArguments().getString(CURRENT_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_name_dialog, container, false);
        getDialog().setTitle(R.string.name_change_title);

        //setup edittext and set hint
        mUserInput = (EditText) v.findViewById(R.id.editTextDialogUserInput);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mUserInput.setHint(mName);

        //collect references for later use and set listeners
        mCancelButton = (Button) v.findViewById(R.id.btnCancel);
        mOkButton = (Button) v.findViewById(R.id.btnOk);
        mCancelButton.setOnClickListener(this);
        mOkButton.setOnClickListener(this);
        return v;
    }


    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity

            if(mListener != null){

                mListener.onNameChangeDialogConfirmed(mTag, mUserInput.getText().toString());
            }
            this.dismiss();
            return true;
        }
        return false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //handle OK click.
    @Override
    public void onClick(View v) {
        //get ID of caller
        if(v.getId() == mOkButton.getId()){
            //make sure activity is attached
            if(mListener != null){
                String finalName = mUserInput.getText().toString().trim();
                if(finalName.equals("") || finalName == null){
                    //if no input provided, keep original name
                    mListener.onNameChangeDialogConfirmed(mTag, mName);
                }else{
                    //update name callback
                    mListener.onNameChangeDialogConfirmed(mTag, mUserInput.getText().toString());
                }

            }
        }
        this.dismiss();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     */
    public interface OnFragmentInteractionListener {
        /**
         * * This interface is implemented to trigger the activity to set the new name
         * @param tag  the ID of the item to be changed. This object only operates as a carrier of this
         *            information
         * @param newName the name to set the life change object to.
         */
        void onNameChangeDialogConfirmed(String tag, String newName);
    }
}
