package com.staffend.nicholas.lifecounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.staffend.nicholas.lifecounter.data.PlayerDataHandler;
import com.staffend.nicholas.lifecounter.lifetrackers.StandardPlayerLifeCounter;
import com.staffend.nicholas.lifecounter.models.Player;
import com.staffend.nicholas.lifecounter.util.DiceFragment;
import com.staffend.nicholas.lifecounter.controllers.ThemeManager;
import com.staffend.nicholas.lifecounter.util.Util;

import java.util.ArrayList;

public class GoldfishActivity extends AppCompatActivity implements
        StandardPlayerLifeCounter.OnFragmentInteractionListener,
        ChangeNameDialogFragment.OnFragmentInteractionListener{

    public final String fragmentTag = "goldfishLifeCounterTag";
    private long mPlayerId;
    private PlayerDataHandler mHandler;
    private TextView txvTurnCount;
    private TextView txvTurnLabel;
    private ThemeManager mThemeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load custom theme information
        mThemeManager = new ThemeManager(this);
        setTheme(mThemeManager.getTheme());

        setContentView(R.layout.activity_goldfish);

        //set custom background
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.backgroundLayout);
        layout.setBackgroundResource(mThemeManager.getDrawableBackground());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Keep screen on in this activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //build life counter, initialize widgets
        buildLifeCounter(savedInstanceState);
        txvTurnLabel = (TextView) findViewById(R.id.txvTurnLabel);
        txvTurnCount = (TextView) findViewById(R.id.txvTurnCount);
        txvTurnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.changeNumericTextViewValue(1, txvTurnCount);
            }
        });
    }

    /**
     * Builds the life counter.
     * Checks if instance state is null. if it is creates the transaction to load the life counter
     * @param savedInstanceState
     */
    private void buildLifeCounter(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            loadDefaultPlayerId();
            getFragmentManager().beginTransaction()
                    .add(R.id.fgmContainer, StandardPlayerLifeCounter.newInstance(20, mPlayerId, fragmentTag), fragmentTag)
                    .commit();
        }
    }

    /**
     * Gets the first playerId in the database. Should be 1, but the method would
     * find it out for sure
     */
    private void loadDefaultPlayerId() {
        mHandler = new PlayerDataHandler(this);
        ArrayList<Player> players = mHandler.getAllPlayers();
        mPlayerId = players.get(0).getId();
    }

    /**
     * Requests the user if they would like to start a new game. Called from public functions.
     * Creates a dialog box for input.
     */
    private void requestNewGame(){
        //build a dialog to ask user if they want a new game
        new AlertDialog.Builder(this)
                .setTitle("New Game?")
                .setMessage("Start a new game?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get the fragment and reset it.
                        StandardPlayerLifeCounter frag = (StandardPlayerLifeCounter) getFragmentManager().findFragmentByTag(fragmentTag);
                        frag.reset();
                        txvTurnCount.setText("1");
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

    /**
     * Inflate the menus
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.standard, menu);
        return true;
    }

    /**
     * Handle menu option selection
     * @param item
     * @return
     */
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
            requestNewGame();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback method implementation
     * @param playerId
     */
    @Override
    public void onPlayerDeath(long playerId) {
        requestNewGame();
    }

    /**
     * Callback method implementation
     * @param tag
     * @param name
     */
    @Override
    public void onRequestNameChange(String tag, String name) {
        getFragmentManager().beginTransaction()
                .add(ChangeNameDialogFragment.newInstance(tag, name), null)
                .commit();
    }

    @Override
    public ThemeManager onCreateUpdateText() {
        txvTurnCount.setTextColor(getResources().getColor(mThemeManager.getPrimaryLight()));
        txvTurnLabel.setTextColor(getResources().getColor(mThemeManager.getPrimaryLight()));
        return mThemeManager;
    }

    @Override
    public void onNameChangeDialogConfirmed(String tag, String newName) {
        StandardPlayerLifeCounter counter = (StandardPlayerLifeCounter) getFragmentManager()
                .findFragmentByTag(tag);
        counter.setPlayerName(newName);
        mHandler.updatePlayerName(String.valueOf(mPlayerId), newName);
    }
}
