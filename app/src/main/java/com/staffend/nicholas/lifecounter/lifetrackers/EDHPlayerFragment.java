package com.staffend.nicholas.lifecounter.lifetrackers;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.staffend.nicholas.lifecounter.R;
import com.staffend.nicholas.lifecounter.models.Player;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EDHPlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EDHPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EDHPlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PLAYER_NAME = "param1";
    private static final String PLAYER_ID = "paramID";
    private static final String STARTING_LIFE = "param2";
    private static final String ALL_PLAYER_IDS = "playerNames";
    private static final String MAX_POISON_TAG = "maxPoison";
    public static final String CDR_DMG_FRAG_TAG_PREFIX = "cdrDamage";
    public static final String PLAYER_LIFE_FRAG_TAG_PREFIX = "playerLife";

    private static final String LOG_TAG = "-*-EDHPlayerFragment";

    // TODO: Rename and change types of parameters
    private String mPlayerName;
    private String mStartingLife;
    private long[] mAllPlayers;
    private int mMaxPoison;
    private long mPlayerId;
    private LinearLayout cdrDmgContainer;
    private LinearLayout stdPlayerLifeContainer;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param playerName this player's name.
     * @param startingLife the starting life total.
     * @param allPlayers an array of all the player names
     * @return A new instance of fragment EDHPlayerFragment.
     */
    // TODO: DELETE ME!
    public static EDHPlayerFragment newInstance(String playerName, String startingLife, String[] allPlayers) {
        EDHPlayerFragment fragment = new EDHPlayerFragment();
        Bundle args = new Bundle();
        args.putString(PLAYER_NAME, playerName);
        args.putString(STARTING_LIFE, startingLife);
        args.putStringArray(ALL_PLAYER_IDS, allPlayers);
        fragment.setArguments(args);
        return fragment;
    }
    // TODO: DELETE ME!
    public static EDHPlayerFragment newInstance(String playerName, String startingLife, int maxPoison, String[] allPlayers) {
        EDHPlayerFragment fragment = new EDHPlayerFragment();
        Bundle args = new Bundle();
        args.putString(PLAYER_NAME, playerName);
        args.putString(STARTING_LIFE, startingLife);
        args.putStringArray(ALL_PLAYER_IDS, allPlayers);
        args.putInt(MAX_POISON_TAG, maxPoison);
        fragment.setArguments(args);
        return fragment;
    }

    //TODO: Refactor to this constructor
    public static EDHPlayerFragment newInstance(Player player, String startingLife, int maxPoison, long[] allPlayers) {
        EDHPlayerFragment fragment = new EDHPlayerFragment();
        Bundle args = new Bundle();
        args.putString(PLAYER_NAME, player.toString());
        args.putString(STARTING_LIFE, startingLife);
        args.putLongArray(ALL_PLAYER_IDS, allPlayers);
        args.putInt(MAX_POISON_TAG, maxPoison);
        args.putLong(PLAYER_ID, player.getId());
        fragment.setArguments(args);
        return fragment;
    }

    public EDHPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mPlayerName = getArguments().getString(PLAYER_NAME);
            mStartingLife = getArguments().getString(STARTING_LIFE, "0");
            mAllPlayers = getArguments().getLongArray(ALL_PLAYER_IDS);
            mMaxPoison = getArguments().getInt(MAX_POISON_TAG, 10);
            mPlayerId = getArguments().getLong(PLAYER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //get the views and rename the reference
        //This prevents all players being added to the first found R.id.player_life_holder
        View v = inflater.inflate(R.layout.fragment_edhplayer, container, false);
        stdPlayerLifeContainer = (LinearLayout)v.findViewById(R.id.player_life_holder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            stdPlayerLifeContainer.setId(View.generateViewId());
        }
        cdrDmgContainer = (LinearLayout)v.findViewById(R.id.commanderDamageContainer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            cdrDmgContainer.setId(View.generateViewId());
        }

        Log.v(LOG_TAG, "onCreateView(). id: " + mPlayerName);
        initGeneratedUIComponents(savedInstanceState);

        return v;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy()");
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String lifeTag = PLAYER_LIFE_FRAG_TAG_PREFIX + mPlayerId;
        StandardPlayerLifeCounter counter = (StandardPlayerLifeCounter) getFragmentManager().
                findFragmentByTag(lifeTag);
        outState.putInt(lifeTag, counter.getLifeTotal());

        lifeTag = lifeTag + "Poison";
        outState.putInt(lifeTag, counter.getPoisonTotal());
        for(int i =0; i<mAllPlayers.length; i++) {
            String tag = CDR_DMG_FRAG_TAG_PREFIX + mAllPlayers[i] + mPlayerId;
            EDHPlayerCommanderDamageCounter cdrFrag = (EDHPlayerCommanderDamageCounter)
                    getFragmentManager().findFragmentByTag(tag);
            outState.putInt(tag, cdrFrag.getCommanderDamageTotal());
        }
        outState.putLongArray(ALL_PLAYER_IDS, mAllPlayers);
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

    //This functions creates components on each onCreateView()
    private void initGeneratedUIComponents(Bundle savedInstanceState){
        String lifeTag = PLAYER_LIFE_FRAG_TAG_PREFIX + mPlayerId;
        Log.v(LOG_TAG, "tagAttempt: " + lifeTag);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        StandardPlayerLifeCounter lifeFrag;
        if(savedInstanceState == null){
            Log.v(LOG_TAG, "StdPCreate -" +mPlayerName);
            lifeFrag = StandardPlayerLifeCounter.newInstance(Integer.valueOf(mStartingLife), mMaxPoison, mPlayerName, mPlayerId, lifeTag);
            transaction.add(stdPlayerLifeContainer.getId(), lifeFrag, lifeTag);
            transaction.commit();
        }


        for(int i =0; i<mAllPlayers.length; i++){
            String tag = CDR_DMG_FRAG_TAG_PREFIX + mAllPlayers[i] + mPlayerId;
            //Log.v(TAG, tag);
            transaction = getFragmentManager().beginTransaction();
            //make this ifSavedInstanceState and pass the conditional fragment
            EDHPlayerCommanderDamageCounter cdrCounter;
            if(savedInstanceState== null){
                cdrCounter = EDHPlayerCommanderDamageCounter.newInstance(mAllPlayers[i], String.valueOf(0), mPlayerId);
                //temporary stuff
                transaction.add(cdrDmgContainer.getId(), cdrCounter, tag);
                    transaction.commit();
            }
        }
    }

    public boolean removeCommanderDamageTracker(String commanderToRemoveName){
        if((this.getTag()).equalsIgnoreCase(commanderToRemoveName))
        {
            //DELETE ME!
            return false;
        }else{
            //DeleteMyPeople
            String tag = CDR_DMG_FRAG_TAG_PREFIX + commanderToRemoveName + mPlayerName;
            //Log.v(TAG, tag);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(getFragmentManager().findFragmentByTag(tag));
            transaction.commit();

            return true;
        }
    }

    public boolean removeCommanderDamageTracker(long commanderToRemoveId){
        //DeleteMyPeople
        String tag = CDR_DMG_FRAG_TAG_PREFIX + commanderToRemoveId + mPlayerId;
        //Log.v(TAG, tag);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(getFragmentManager().findFragmentByTag(tag));
        transaction.commit();

        return true;

    }

    public void updatePlayersList(long[] newPlayersArray){
        this.mAllPlayers = newPlayersArray;
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
     *
     * REMOVED FOR NOW
     *
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
