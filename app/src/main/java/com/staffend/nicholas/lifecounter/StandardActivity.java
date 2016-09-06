package com.staffend.nicholas.lifecounter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.staffend.nicholas.lifecounter.data.PlayerDataHandler;
import com.staffend.nicholas.lifecounter.lifetrackers.StandardPlayerLifeCounter;
import com.staffend.nicholas.lifecounter.models.Player;
import com.staffend.nicholas.lifecounter.util.DiceFragment;
import com.staffend.nicholas.lifecounter.controllers.ThemeManager;

import java.util.ArrayList;

/**
 * A standard game instance activity
 */
public class StandardActivity extends AppCompatActivity
        implements StandardPlayerLifeCounter.OnFragmentInteractionListener,
ChangeNameDialogFragment.OnFragmentInteractionListener{

    public static final String PLAYER_NAMES_TAG = "playerNames";

    private static final String FRAGMENT_TAG = "LifeFragment";

    private ArrayList<String> mPlayerNames;
    private ArrayList<Player> mAllPlayers;
    private PlayerDataHandler mPlayerHandler;
    private ThemeManager mThemeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load cumstom theme information
        mThemeManager = new ThemeManager(this);
        setTheme(mThemeManager.getTheme());

        setContentView(R.layout.activity_standard);

        //set custom theme background
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.backgroundLayout);
        layout.setBackgroundResource(mThemeManager.getDrawableBackground());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Keep screen on in this activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPlayerHandler = new PlayerDataHandler(getBaseContext());
        if(savedInstanceState == null){
            newGame();
        }

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

    public void newGame(){
        mAllPlayers = mPlayerHandler.getAllPlayers();
        for (Player p :
                mAllPlayers) {
            Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG+p.getId());
            if (fragment != null) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        for (Player p :
                mAllPlayers) {
            String fragTag = FRAGMENT_TAG+p.getId();
            getFragmentManager().beginTransaction()
                    .add(R.id.stdActFragmentContainer, StandardPlayerLifeCounter.newInstance(20, p.getId(), fragTag), fragTag)
                    .commit();
        }
    }

    @Override
    public void onPlayerDeath(long playerId) {
        for (Player p :
                mAllPlayers) {
            if (p.getId() == playerId){
                requestRestartGame(p.toString());
                break;
            }
        }
    }

    @Override
    public void onRequestNameChange(String tag, String name) {
        getFragmentManager().beginTransaction()
                .add(ChangeNameDialogFragment.newInstance(tag, name), null)
                .commit();
        //ChangeNameDialogFragment.newInstance(null, null).show(getSupportFragmentManager(), null);

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
        StandardPlayerLifeCounter counter = (StandardPlayerLifeCounter) getFragmentManager()
                .findFragmentByTag(tag);
        counter.setPlayerName(newName);
        String id = tag.replace(FRAGMENT_TAG, "");
        mPlayerHandler.updatePlayerName(id, newName);
        mAllPlayers = mPlayerHandler.getAllPlayers();
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


}
