package com.staffend.nicholas.lifecounter.lifetrackers;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.staffend.nicholas.lifecounter.R;
import com.staffend.nicholas.lifecounter.data.PlayerDataHandler;
import com.staffend.nicholas.lifecounter.controllers.ThemeManager;
import com.staffend.nicholas.lifecounter.util.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StandardPlayerLifeCounter.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StandardPlayerLifeCounter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardPlayerLifeCounter extends Fragment implements View.OnClickListener, ViewSwitcher.ViewFactory {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String STARTING_LIFE;
    private static String PLAYER_NAME;
    private static String STARTING_POISON;
    private static String MAX_POISON;
    private static String IDENTIFIER_TAG;
    private static String PLAYER_ID;

    private static final String BASE_STARTING_LIFE = "STARTING_LIFE";
    private static final String BASE_PLAYER_NAME = "PLAYER_NAME";
    private static final String BASE_STARTING_POISON = "STARTING_POISON";
    private static final String BASE_MAX_POISON = "MAX_POISON";
    private static final String BASE_IDENTIFIER_TAG = "IDENTIFIER_TAG";
    private static final String BASE_PLAYER_ID = "PLAYER_ID";

    private static final int DEFAULT_STARTING_LIFE = 20;
    private static final String LOG_TAG = "-*-StdPlayerLifeCounter";

    //Variables for the savedInstanceState
    private static final String LARGE_TXV = "largeTxv";
    private static final String SMALL_TXV = "smallTxv";
    private static final String IS_LIFE_VISIBLE = "isLifeVisible";

    // TODO: Rename and change types of parameters
    private int mStartingLife;
    private int mStartingPoison;
    private int mMaxPoison;
    private long mPlayerId;
    private String mTag;
    private String mPlayerName;
    private boolean isLifeVisible = true;

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton fabPlus;
    private FloatingActionButton fabMinus;
    private TextView txvName, txvLarge, txvSmall;
    private ImageSwitcher imgSmall, imgLarge;
    private LinearLayout llSmall;




    /**
     * Provides tag tracking for database support.
     * @param startingLife starting life total for the player
     * @param playerName the name to be used for the player
     * @param tag - the tag reference that is attached to this fragment
     * @return A new instance of fragment StandardPlayerLifeCounter.
     */
    public static StandardPlayerLifeCounter newInstance(int startingLife, String playerName, long playerId, String tag) {
        StandardPlayerLifeCounter fragment = new StandardPlayerLifeCounter();
        fragment.setTags(String.valueOf(playerId));
        Bundle args = new Bundle();
        args.putInt(fragment.BASE_STARTING_LIFE, startingLife);
        args.putString(fragment.BASE_PLAYER_NAME, playerName);
        args.putString(fragment.BASE_IDENTIFIER_TAG, tag);
        args.putLong(fragment.BASE_PLAYER_ID, playerId);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Preferred creation method for the LifeCounter. Auto generates playername from database ID.
     * sets starting poison to 0.
     * @param startingLife starting life total
     * @param playerId player database id number
     * @param tag the fragment tag associated with this fragment.
     * @return
     */
    public static StandardPlayerLifeCounter newInstance(int startingLife, long playerId, String tag) {
        StandardPlayerLifeCounter fragment = new StandardPlayerLifeCounter();
        fragment.setTags(String.valueOf(playerId));
        Bundle args = new Bundle();
        args.putInt(fragment.BASE_STARTING_LIFE, startingLife);
        args.putString(fragment.BASE_IDENTIFIER_TAG, tag);
        args.putLong(fragment.BASE_PLAYER_ID, playerId);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Preferred creation method for the LifeCounter. Auto generates playername from database ID.
     * sets starting poison to 0.
     * @param startingLife
     * @param maxPoison
     * @param playerName
     * @param playerId
     * @param tag
     * @return
     */
    public static StandardPlayerLifeCounter newInstance(int startingLife, int maxPoison, String playerName, long playerId, String tag) {
        StandardPlayerLifeCounter fragment = new StandardPlayerLifeCounter();
        fragment.setTags(String.valueOf(playerId));
        Bundle args = new Bundle();
        args.putInt(fragment.BASE_STARTING_LIFE, startingLife);
        args.putString(fragment.BASE_PLAYER_NAME, playerName);
        args.putInt(fragment.BASE_MAX_POISON, maxPoison);
        args.putString(fragment.BASE_IDENTIFIER_TAG, tag);
        args.putLong(fragment.BASE_PLAYER_ID, playerId);
        fragment.setArguments(args);
        Log.v(LOG_TAG, "recieved: " + playerName);
        return fragment;
    }


    public StandardPlayerLifeCounter() {
        //required empty constructor
    }

    /**
     * Set the tags for this object for access by callbacks. Concatenate the id num to the end of
     * the tag.
     * @param mID
     */
    private static void setTags(String mID){
        STARTING_LIFE = BASE_STARTING_LIFE + mID;
        PLAYER_NAME = BASE_PLAYER_NAME + mID;
        STARTING_POISON = BASE_STARTING_POISON + mID;
        MAX_POISON = BASE_MAX_POISON + mID;
        IDENTIFIER_TAG = BASE_IDENTIFIER_TAG + mID;
        PLAYER_ID = BASE_PLAYER_ID + mID;
    }

    /**
     * Create the object. Initialize values for the use in inflating views
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStartingLife = getArguments().getInt(BASE_STARTING_LIFE, DEFAULT_STARTING_LIFE);
            mPlayerId = getArguments().getLong(BASE_PLAYER_ID);
            mPlayerName = getArguments().getString(BASE_PLAYER_NAME, null);
            if(mPlayerName!= null){
                Log.v(LOG_TAG, mPlayerName);
            }
            if(mPlayerName == null){
                PlayerDataHandler handler = new PlayerDataHandler(getActivity().getBaseContext());
                mPlayerName = handler.getPlayerName(mPlayerId);
                Log.v(LOG_TAG, "withNull:"+mPlayerName);
            }
            mStartingPoison = getArguments().getInt(BASE_STARTING_POISON, 0);
            mMaxPoison = getArguments().getInt(BASE_MAX_POISON, 10);
            mTag = getArguments().getString(BASE_IDENTIFIER_TAG, "NaN");

            Log.v(LOG_TAG, "Created with poison: " +mMaxPoison);
        }
    }

    /**
     * Create the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.std_life_counter_layout, container, false);
        Log.v(LOG_TAG, "onCreateView():"+mPlayerName);
        //Declare our Views for interaction
        fabPlus = (FloatingActionButton)v.findViewById(R.id.fabPlus);
        fabMinus = (FloatingActionButton)v.findViewById(R.id.fabMinus);
        txvLarge = (TextView)v.findViewById(R.id.txvLarge);
        txvSmall = (TextView)v.findViewById(R.id.txvSmall);
        txvName = (TextView)v.findViewById(R.id.txvName);
        llSmall = (LinearLayout)v.findViewById(R.id.llSmall);
        imgSmall = (ImageSwitcher)v.findViewById(R.id.imgSmall);
        imgLarge = (ImageSwitcher)v.findViewById(R.id.imgLarge);

        initAnimation(v);

        //Declare the listeners
        llSmall.setOnClickListener(this);
        txvSmall.setOnClickListener(this);
        imgSmall.setOnClickListener(this);
        fabPlus.setOnClickListener(this);
        fabMinus.setOnClickListener(this);
        txvName.setOnClickListener(this);


        return v;
    }

    /**
     * Set the theme of the view based on preferences
     */
    @Override
    public void onStart() {
        super.onStart();
        if(mListener != null){
            ThemeManager tm = mListener.onCreateUpdateText();
            setTextColor(tm);
        }
    }

    /**
     * Serialize the game state for recall later
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String largeTxv = String.valueOf(txvLarge.getText());
        String smallTxv = String.valueOf(txvSmall.getText());
        boolean isLifeVisible = this.isLifeVisible;

        outState.putString(PLAYER_NAME, mPlayerName);
        outState.putString(LARGE_TXV, largeTxv);
        outState.putString(SMALL_TXV, smallTxv);
        outState.putBoolean(IS_LIFE_VISIBLE, isLifeVisible);
    }

    /**
     * Deserialize the game state
     * @param savedInstanceState
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            boolean isLifeVisible = savedInstanceState.getBoolean(IS_LIFE_VISIBLE);
            String largeTxvStr = savedInstanceState.getString(LARGE_TXV);
            String smallTxvStr = savedInstanceState.getString(SMALL_TXV);
            String name = savedInstanceState.getString(PLAYER_NAME);


            this.txvLarge.setText(largeTxvStr);
            this.txvSmall.setText(smallTxvStr);
            this.txvName.setText(name);
            if(isLifeVisible){
                setFocusOnLife();
                this.isLifeVisible = true;
            }else{
                setFocusOnPoison();
                this.isLifeVisible = false;
            }
        }else //initialize Values, else wait for onViewStateRestored
            if(savedInstanceState == null){
                txvLarge.setText(String.valueOf(mStartingLife));
                txvName.setText(mPlayerName);
                txvSmall.setText(String.valueOf(mStartingPoison));
            }
    }

    /**
     * Build the animation for swapping images
     * @param v
     */
    private void initAnimation(View v) {
        imgLarge.setFactory(this);
        imgSmall.setFactory(this);
        setFocusOnLife();
        Animation in = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);

        imgLarge.setInAnimation(in);
        imgSmall.setInAnimation(in);
    }

    /**
     * @deprecated
     * @return
     */
    public String getPlayerName(){
        return txvName.getText().toString();
    }

    /**
     * Set the player name for this view
     * @param newPlayerName
     */
    public void setPlayerName(String newPlayerName){
        this.mPlayerName = newPlayerName;
        this.txvName.setText(newPlayerName);
    }

    /**
     * Get the currently displayed life total of this view
     * @return
     */
    public int getLifeTotal(){
        if(isLifeVisible){
            return Util.changeNumericTextViewValue(0, txvLarge);
        }
        else
            return Util.changeNumericTextViewValue(0, txvSmall);
    }

    /**
     * Set the currently displayed life total of this view
     * @param newLifeTotal
     */
    public void setLifeTotal(int newLifeTotal){
        if(isLifeVisible){
            txvLarge.setText(String.valueOf(newLifeTotal));
        }else
            txvSmall.setText(String.valueOf(newLifeTotal));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy()" +mPlayerName);
    }

    /**
     * Get the current value of poison counters for this player
     * @return
     */
    public int getPoisonTotal(){
        if(isLifeVisible){
            return Util.changeNumericTextViewValue(0, txvSmall);
        }
        else
            return Util.changeNumericTextViewValue(0, txvLarge);
    }

    /**
     * Set the current poison counter value
     * @param newPoisonTotal
     */
    public void setPoisonTotal(int newPoisonTotal){
        if(!isLifeVisible){
            txvLarge.setText(String.valueOf(newPoisonTotal));
        }else
            txvSmall.setText(String.valueOf(newPoisonTotal));
    }

    /**
     * Handle pressing of the life increment/decrememnt buttons.
     * @param change
     */
    private void onLifeFabPressed(int change) {
        int lifeTotal = Util.changeNumericTextViewValue(change, txvLarge);
        if(isLifeVisible){
            checkDead(lifeTotal, 0);
        }else{
            //do some fancy math to invert the check if poison
            checkDead(lifeTotal*-1, -mMaxPoison);
        }

    }

    /**
     * Checks if the passed value is less than the passed threshold
     * @param lifeTotal
     * @param threshold
     */
    private void checkDead(int lifeTotal, int threshold) {
        if(lifeTotal <= threshold){
            mListener.onPlayerDeath(mPlayerId);
        }
    }

    /**
     * Updates the life counter from an external source.
     * @param change The amount of increment/decrement to apply to the life total.
     */
    public void updateLifeByIncrement(int change){
        int lifeTotal = 20;
        if(isLifeVisible){
            lifeTotal = Util.changeNumericTextViewValue(change, txvLarge);
        }else if(!isLifeVisible){
            lifeTotal=Util.changeNumericTextViewValue(change, txvSmall);
        }
        checkDead(lifeTotal, 0);
    }

    /**
     * Resets the game to beginning of the game totals
     */
    public void reset(){
        setLifeTotal(mStartingLife);
        setPoisonTotal(0);
    }

    /**
     * Updates the player name for this view
     * @return the new player name
     */
    public String updatePlayerName(){
        PlayerDataHandler handler = new PlayerDataHandler(getActivity().getBaseContext());
        String newName = handler.getPlayerName(mPlayerId);
        txvName.setText(newName);
        return newName;
    }

    /**
     * called from on create to pull text colors from the ThemeManager
     * @param tm
     */
    public void setTextColor(ThemeManager tm){
        txvName.setTextColor(getResources().getColor(tm.getPrimaryLight()));
        txvLarge.setTextColor(getResources().getColor(tm.getPrimaryLight()));
        txvSmall.setTextColor(getResources().getColor(tm.getPrimaryLight()));
    }

    /**
     * Changes which view is in focus (Larger).
     * @param view
     */
    public void onSmallLifePressed(View view){
        if(isLifeVisible){
            //swap the life totals
            String temp = String.valueOf(txvLarge.getText());
            txvLarge.setText(txvSmall.getText());
            txvSmall.setText(String.valueOf(temp));
            //swap the images
            setFocusOnPoison();
            isLifeVisible=false;
        } else
        {
            //swap the life totals
            String temp = String.valueOf(txvSmall.getText());
            txvSmall.setText(txvLarge.getText());
            txvLarge.setText(String.valueOf(temp));
            //swap the images
            setFocusOnLife();
            isLifeVisible=true;
        }

    }

    /**
     * Sets the focus in the view to the poison icon and value
     */
    private void setFocusOnPoison() {
        imgLarge.setImageResource(R.mipmap.ic_poison);
        imgSmall.setImageResource(R.mipmap.ic_heart);
    }

    /**
     * Sets the focus in the life counter to the hear icon and life total
     */
    private void setFocusOnLife() {
        imgLarge.setImageResource(R.mipmap.ic_heart);
        imgSmall.setImageResource(R.mipmap.ic_poison);
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

    /**
     * OnClickListener for the whole fragment. Determines the caller's id and calls the subsequent
     * function.
     * @param v The calling View.
     */
    @Override
    public void onClick(View v) {
        if(v.getId()==llSmall.getId() || v.getId() == txvSmall.getId() || v.getId() == imgSmall.getId()){
            onSmallLifePressed(v);
        } else if(v.getId()==fabMinus.getId()){
            onLifeFabPressed(-1);
        } else if(v.getId()==fabPlus.getId()){
            onLifeFabPressed(1);
        } else if(v.getId() == txvName.getId()){
            //Do something cool.... Ugh...
            //Send name to listener
            mListener.onRequestNameChange(mTag, mPlayerName);
            //Activity updates name holders
            //Activity pushes updated same to life counter
        }
    }

    /**
     * Makes the image view for the animation
     * @return
     */
    @Override
    public View makeView() {
        ImageView view = new ImageView(getActivity().getApplicationContext());
        return view;
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

        void onPlayerDeath(long playerId);

        void onRequestNameChange(String tag, String name);

        ThemeManager onCreateUpdateText();
    }


}
