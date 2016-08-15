package com.staffend.nicholas.lifecounter.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.staffend.nicholas.lifecounter.models.Player;

import java.sql.SQLException;
import java.util.ArrayList;

/**
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

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

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

    private Player cursorToPlayer(Cursor cursor){
        Player player = new Player();
        player.setId(cursor.getLong(0));
        Log.v("EDHPlayerHandLng", String.valueOf(cursor.getLong(0)));
        player.setName(cursor.getString(1));
        Log.v("EDHPlayerHandStr", cursor.getString(1));

        return player;
    }

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
