package com.staffend.nicholas.lifecounter;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.staffend.nicholas.lifecounter.util.DiceFragment;
import com.staffend.nicholas.lifecounter.util.GameState;
import com.staffend.nicholas.lifecounter.controllers.ThemeManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements EDHGameSetup.OnEDHGameStartListener,
        NavigationView.OnNavigationItemSelectedListener{

    private GameState mState;

    private static final String STATE_TAG = "MainActivityStateTag";
    private static final String TAG = "-*-MainActivity";
    private static final String ACTIVE_FRAG_TAG = "thisIsTheActiveFragment";


    private ArrayList<String> playerNames = new ArrayList<>();


    /**
     * Main create view. Load navigation items and menus. Load default menu if no game state exists.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init shared preference
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        //Load custom theme information
        ThemeManager themeManager = new ThemeManager(this);
        setTheme(themeManager.getTheme());

        setContentView(R.layout.activity_main);

        //Set navdrawer color
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setBackgroundResource(themeManager.getDrawableBackground());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(themeManager.getDrawableBackground());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            initSplashScreen();
            mState = GameState.SPLASH;
        }else{
            mState = (GameState) savedInstanceState.getSerializable(STATE_TAG);
        }
        if(mState == GameState.EDH){
            setupEDH();
        }


    }

    /**
     * Load the game instance state
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mState = (GameState) savedInstanceState.getSerializable(STATE_TAG);
    }

    /**
     * Make sure that back pressed closes drawer if open, instead of popping back stack
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**Handle action bar menu item selection
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.dice_roller) {
            DiceFragment.newInstance(null, null).show(getSupportFragmentManager(), null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Serialize instance state
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_TAG, mState);
    }


    /**
     * Control navigation from the menu
     * @param item - the menu item selected
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new SettingsActivity.GeneralPreferenceFragment())
                    .commit();
            /*Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);*/
        }
        if (id == R.id.nav_standard) {
            mState = GameState.STANDARD;
            Intent i = new Intent(this, StandardActivity.class);
            i.putExtra(StandardActivity.PLAYER_NAMES_TAG, playerNames);
            startActivity(i);

        } else if (id == R.id.nav_edh){
            setupEDH();

        } else if (id == R.id.nav_goldfish){
            mState = GameState.GOLDFISH;
            Intent i = new Intent(this, GoldfishActivity.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Loads the EDH Game mode setup menu
     */
    private void setupEDH(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, EDHGameSetup.newInstance(null, null));
        transaction.commit();
        this.mState = GameState.EDH;
    }

    /**
     * Loads the initial splash screen
     */
    private void initSplashScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, WelcomeFragment.newInstance(null, null), ACTIVE_FRAG_TAG);
        //transaction.addToBackStack(null);
        transaction.commit();
        mState = GameState.SPLASH;
    }



    /**
     * Interface Override from EDHGameSetup Fragment
     * Trigger start of an EDHActivity
     */
    @Override
    public void onEDHGameStart() {
        if(mState == GameState.EDH){
            Intent i = new Intent(getApplicationContext(), EDHActivity.class);
            startActivity(i);
        }

    }
}
