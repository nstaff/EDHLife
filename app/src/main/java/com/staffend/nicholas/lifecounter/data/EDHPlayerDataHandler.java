package com.staffend.nicholas.lifecounter.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.staffend.nicholas.lifecounter.models.Player;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data layer abstraction class
 * Created by Nicholas on 1/1/2016.
 */
public class EDHPlayerDataHandler {

    private SQLiteDatabase database;
    private DataHelper dbHelper;
    private String[] allColumns = {DataHelper.COLUMN_ID,
            DataHelper.COLUMN_PLAYER_EDH};
    private final String TABLE_NAME = DataHelper.TABLE_PLAYERS_EDH;

    private static final String LOG_TAG = "PlayerDataHandler";

    public EDHPlayerDataHandler(Context context){
        dbHelper = new DataHelper(context);
    }

    /**
     * Get a writable instance of the database
     * @throws SQLException
     */
    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    /**
     * close the database
     */
    public void close(){
        dbHelper.close();
    }

    /**
     * Updates the player name
     * @param id the id number of the player
     * @param name the new name for the player
     */
    public void updatePlayerName(String id, String name){
        String sql = "UPDATE "+TABLE_NAME+" SET edh_player = '"
                + name + "' WHERE _id = " + id +";";
        try{
            open();
            database.execSQL(sql);
            close();
        }catch(SQLException ex){
            Log.v(LOG_TAG, ex.toString());
        }

    }

    /**
     * gets all players stored in the database
     *
     * @deprecated - use getXPlayers instead
     * @return ArrayList of Player
     */
    public ArrayList<Player> getAllPlayers(){
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM "+TABLE_NAME+";", null);
        cursor.moveToFirst();
        ArrayList<Player> players = new ArrayList<>();
        while(!cursor.isAfterLast()){
            players.add(cursorToPlayer(cursor));
            cursor.moveToNext();
        }
        dbHelper.close();
        return players;
    }

    /**
     * Gets the first X players in the database.
     * @param numPlayers
     * @return ArrayList of Player objects
     */
    public ArrayList<Player> getXPlayers(int numPlayers){
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM "+TABLE_NAME+";", null);
        cursor.moveToFirst();
        ArrayList<Player> players = new ArrayList<>();
        int count = 0;
        while(!cursor.isAfterLast() && count < numPlayers){
            count++;
            players.add(cursorToPlayer(cursor));
            cursor.moveToNext();
        }
        dbHelper.close();
        return players;
    }

    /**
     * get a specified Player object based on the player ID number
     * @param playerId
     * @return
     */
    public Player getPlayerById(long playerId){
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE _id ="+playerId+";", null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            return cursorToPlayer(cursor);
        }else{
            return null;
        }
    }

    /**
     * Helper method to build a player object from a row of player data
     * @param cursor
     * @return
     */
    private Player cursorToPlayer(Cursor cursor){
        Player player = new Player();
        player.setId(cursor.getLong(0));
        Log.v("EDHPlayerHandLng", String.valueOf(cursor.getLong(0)));
        player.setName(cursor.getString(1));
        Log.v("EDHPlayerHandStr", cursor.getString(1));

        return player;
    }

    /**
     * return just the player name from the database ID number
     * @param playerId
     * @return
     */
    public String getPlayerName(long playerId){
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM "+TABLE_NAME+" where _id="+playerId+";", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            return cursorToPlayer(cursor).toString();
        }
        return null;
    }
}
