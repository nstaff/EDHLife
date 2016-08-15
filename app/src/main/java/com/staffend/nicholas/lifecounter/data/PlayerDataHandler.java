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
public class PlayerDataHandler {

    private SQLiteDatabase database;
    private DataHelper dbHelper;
    private String[] allColumns = {DataHelper.COLUMN_ID,
            DataHelper.COLUMN_PLAYER};

    private static final String LOG_TAG = "PlayerDataHandler";

    public PlayerDataHandler(Context context){
        dbHelper = new DataHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public void updatePlayerName(String id, String name){
        String sql = "UPDATE players SET player = '"
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
                .rawQuery("SELECT * FROM players;", null);
        cursor.moveToFirst();
        ArrayList<Player> players = new ArrayList<>();
        int count = 0;
        while(!cursor.isAfterLast() && count < 2){
            count++;
            players.add(cursorToPlayer(cursor));
            cursor.moveToNext();
        }
        dbHelper.close();
        return players;
    }

    private Player cursorToPlayer(Cursor cursor){
        Player player = new Player();
        player.setId(cursor.getLong(0));
        player.setName(cursor.getString(1));
        return player;
    }

    public String getPlayerName(long playerId){
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM players where _id="+playerId+";", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            return cursorToPlayer(cursor).toString();
        }
        return null;
    }
}
