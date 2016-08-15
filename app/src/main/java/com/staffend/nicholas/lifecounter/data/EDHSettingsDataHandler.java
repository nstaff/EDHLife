package com.staffend.nicholas.lifecounter.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Nicholas on 1/5/2016.
 */
public class EDHSettingsDataHandler {
    private SQLiteDatabase database;
    private DataHelper dbHelper;
    private String[] allColumns = {DataHelper.COLUMN_ID,
            DataHelper.COLUMN_EDH_SETTINGS};
    private final String TABLE_NAME = DataHelper.TABLE_EDH_SETTINGS;

    public EDHSettingsDataHandler(Context context){
        dbHelper = new DataHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public boolean isCommanderDamageLinked(){
        int value = getIntFromId(DataHelper.ID_DMG_LINKED);
        if(value == 0){
            return false;
        }else {
            return true;
        }
    }

    public void setCommanderDamageLinked(boolean value){
        int valStored;
        if(value){
            valStored = 1;
        }
        else{
            valStored = 0;
        }
        updateIntValue(1, valStored);
    }

    public boolean isAutoDeleteOn(){
        int value = getIntFromId(DataHelper.ID_AUTO_DELETE);
        if(value == 0){
            return false;
        }else {
            return true;
        }
    }

    public void setAutoDeletePlayers(boolean value){
        int valStored;
        if(value){
            valStored = 1;
        }
        else{
            valStored = 0;
        }
        updateIntValue(2, valStored);
    }

    public int getMaxPoison(){
        return getIntFromId(DataHelper.ID_POISON);
    }

    public void setMaxPoison(int newMax){
        updateIntValue(DataHelper.ID_POISON, newMax);
    }

    public int getStartingLife(){
        return getIntFromId(DataHelper.ID_STARTING_LIFE);
    }

    public void setStartingLife(int newMax){
        updateIntValue(DataHelper.ID_STARTING_LIFE, newMax);
    }

    public int getNumPlayers(){
        return getIntFromId(DataHelper.ID_NUM_PLAYERS);
    }

    public void setNumPlayers(int newMax){
        updateIntValue(DataHelper.ID_NUM_PLAYERS, newMax);
    }

    private int getIntFromId(int id){
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT " + allColumns[1] + " FROM " +
                        TABLE_NAME + " WHERE _id = " + id + ";", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private void updateIntValue(int id, int value){
        dbHelper.getWritableDatabase().execSQL(
                "UPDATE " + TABLE_NAME + " SET " + allColumns[1] + " ="+value+
                        " WHERE _id="+id+";"
        );
    }
}
