package com.staffend.nicholas.lifecounter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.staffend.nicholas.lifecounter.data.EDHPlayerDataHandler;
import com.staffend.nicholas.lifecounter.data.EDHSettingsDataHandler;
import com.staffend.nicholas.lifecounter.lifetrackers.EDHPlayerCommanderDamageCounter;
import com.staffend.nicholas.lifecounter.lifetrackers.EDHPlayerFragment;
import com.staffend.nicholas.lifecounter.lifetrackers.StandardPlayerLifeCounter;
import com.staffend.nicholas.lifecounter.models.Player;
import com.staffend.nicholas.lifecounter.util.DiceFragment;
import com.staffend.nicholas.lifecounter.controllers.ThemeManager;
import com.staffend.nicholas.lifecounter.util.Util;

import java.util.ArrayList;

public class EDHActivity extends AppCompatActivity implements
        EDHPlayerFragment.OnFragmentInteractionListener,
        StandardPlayerLifeCounter.OnFragmentInteractionListener,
        EDHPlayerCommanderDamageCounter.OnFragmentInteractionListener,
ChangeNameDialogFragment.OnFragmentInteractionListener{

    public static final String PLAYER_NAMES_TAG = "PlayerNamesTagForEDHActivity";
    public static final String MAX_POISON_TAG = "maxPoisonTag";
    public static final String STARTING_LIFE_TAG = "startingLifeTag";
    public static final String NUM_PLAYERS_TAG = "numPlayersTag";


    public static final String EDH_FRAG_TAG_PREFIX = "plr";

    private static final String LOG_TAG = "EDHActivity";

    private boolean isCommanderDamageLinked = true;
    private boolean isAutoDeleteOn = true;

    private static final String LIFE_COUNTER_TAG_PREFIX = "lfCtr";
    private static final String CDR_DAMAGE_TAG_PREFIX ="cdrDmg";

    private ArrayList<String> mAllPlayers;
    private ArrayList<Player> mAllPlayersMaster;
    private EDHPlayerDataHandler mPlayerHandler;
    private ArrayList<Player> mPlayers;
    private String mStartingLife;
    private int mMaxPoison;
    private int mNumPlayers;
    private ThemeManager mThemeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load custom theme information
        mThemeManager = new ThemeManager(this);
        setTheme(mThemeManager.getTheme());

        setContentView(R.layout.activity_edh);

        //set custom theme background
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.backgroundLayout);
        layout.setBackgroundResource(mThemeManager.getDrawableBackground());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Keep screen on in this activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //load the data manager
        mPlayerHandler = new EDHPlayerDataHandler(getBaseContext());

        if(savedInstanceState == null) {
            Intent i = getIntent();
            Bundle bundle = i.getExtras();

            EDHSettingsDataHandler handler = new EDHSettingsDataHandler(this);

            //get base values
            isCommanderDamageLinked = handler.isCommanderDamageLinked();
            isAutoDeleteOn = handler.isAutoDeleteOn();

            mNumPlayers = handler.getNumPlayers();
            mStartingLife = String.valueOf(handler.getStartingLife());
            mMaxPoison = handler.getMaxPoison();
            handler.close();
            newGame();
        }

    }


    private void newGame(){
        mPlayers = mPlayerHandler.getXPlayers(mNumPlayers);
        long[] playerIds = Util.getIdArrayFromPlayersList(mPlayers);
        //String[] playerNames = Util.makeStringArrayFromPlayerList(mPlayers);
        for (Player p :
                mPlayers) {
            Log.v(LOG_TAG, "playerName: "+p.toString());
            addPlayer(p, playerIds);
        }

    }

    private void setTextDecorators(ThemeManager tm){
        //set custom theme text decorators
        for (Player p :
                mPlayers) {
            String tag = EDHPlayerFragment.PLAYER_LIFE_FRAG_TAG_PREFIX + p.getId();
            StandardPlayerLifeCounter counter = (StandardPlayerLifeCounter) getFragmentManager()
                    .findFragmentByTag(tag);
            Log.v(LOG_TAG, "tagAttempt: " + tag);
            //counter.setTextColor(tm);
        }
    }

    //TODO:Refactor to this new method
    public void addPlayer(Player player, long[] playerIdsArray){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.lifeCounterContainer,
                EDHPlayerFragment.newInstance(player, mStartingLife,
                        mMaxPoison, playerIdsArray), getEDHPlayerFragmentTag(player.getId()));
        transaction.commit();


    }

    @NonNull
    private String getEDHPlayerFragmentTag(long id) {
        return EDH_FRAG_TAG_PREFIX + id;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(PLAYER_NAMES_TAG, mAllPlayers);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Changes life amount based on if the commander damage is linked.
     * @param change
     * @param parentCommander
     */
    @Override
    public void onLifeChange(int change, long parentCommander) {

        if(isCommanderDamageLinked) {
            String fragTag = EDHPlayerFragment.PLAYER_LIFE_FRAG_TAG_PREFIX + parentCommander;
            StandardPlayerLifeCounter frag = (StandardPlayerLifeCounter) getFragmentManager().findFragmentByTag(fragTag);
            frag.updateLifeByIncrement(change);
        }
    }

    //Implementation of interface for a player dying. Delegates removal of that player from the view
    @Override
    public void onPlayerDeath(long playerId) {
        if(isAutoDeleteOn){
            dropFromPlayers(playerId, false);
        }
    }

    @Override
    public void onRequestNameChange(String tag, String name) {
        getFragmentManager().beginTransaction()
                .add(ChangeNameDialogFragment.newInstance(tag, name), null)
                .commit();
    }

    @Override
    public ThemeManager onCreateUpdateText() {
        return mThemeManager;
    }

    @Override
    public void onNameChangeDialogConfirmed(String tag, String newName) {
        changeName(tag, newName);
    }

    private void changeName(String tag, String newName){
        //get the player life counter to change the main name of
        StandardPlayerLifeCounter counter = (StandardPlayerLifeCounter) getFragmentManager()
                .findFragmentByTag(tag);
        //change the name
        counter.setPlayerName(newName);

        //Trim the fragment's tag from the name value to get the ID
        String id = tag.replace(EDHPlayerFragment.PLAYER_LIFE_FRAG_TAG_PREFIX, "");
        //get all commander damage counter fragments
        //for each commander damage counter fragment
        for (Player p :
                mPlayers) {
            //update the name of the effected fragment
            String cdrDamageTag = EDHPlayerFragment.CDR_DMG_FRAG_TAG_PREFIX + id + p.getId();
            EDHPlayerCommanderDamageCounter dmgCounter = (EDHPlayerCommanderDamageCounter)
                    getFragmentManager().findFragmentByTag(cdrDamageTag);
            dmgCounter.setCommanderName(newName);
        }




        //push update to the database
        mPlayerHandler.updatePlayerName(id, newName);
    }




    public void deleteCommanderDamageCounters(long commanderId){
        for (Player p:
                mPlayers) {
            EDHPlayerFragment player = (EDHPlayerFragment) getFragmentManager().findFragmentByTag(
                    getEDHPlayerFragmentTag(p.getId())
            );
            if(player != null){
                player.removeCommanderDamageTracker(commanderId);
                player.updatePlayersList(Util.getIdArrayFromPlayersList(mPlayers));
            }
        }
    }

    //Drops a player from the view based on the players name (which is the fragment tag)
    public void dropFromPlayers(long playerId, boolean suppressRequest){
        mPlayers.remove(mPlayerHandler.getPlayerById(playerId));
        Fragment f = getFragmentManager().findFragmentByTag(getEDHPlayerFragmentTag(playerId));
        getFragmentManager().beginTransaction().remove(f).commit();
        deleteCommanderDamageCounters(playerId);
        if(mPlayers.size() == 1 && !suppressRequest)
        {
            requestRestartGame(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.standard, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.dice_roller) {
            DiceFragment.newInstance(null, null).show(getSupportFragmentManager(), null);
            return true;
        } else if(id == R.id.restart_game){
            requestRestartGame(null);
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestRestartGame(String playerName){
        String message;
        if(playerName != null){
            message = playerName + " has died. Would you like to start a new game?";
        }else message = "Would you like to start a new game?";

        new AlertDialog.Builder(this)
                .setTitle("New Game?")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearOldGame();
                        newGame();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing. Close window.
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void clearOldGame() {
        long[] playersToDrop = Util.getIdArrayFromPlayersList(mPlayers);
        for(int i=0; i<playersToDrop.length; i++){
            dropFromPlayers(playersToDrop[i], true);
        }
    }
}
