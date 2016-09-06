package com.staffend.nicholas.lifecounter.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Data broker abstractino for the settings of a game
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

    /**
     * Open a copy of the settings data table
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Close the current database connection
     */
    public void close(){
        dbHelper.close();
    }

    /**
     * Determine if commander damage preference is set
     * @return
     */
    public boolean isCommanderDamageLinked(){
        int value = getIntFromId(DataHelper.ID_DMG_LINKED);
        if(value == 0){
            return false;
        }else {
            return true;
        }
    }

    /**
     * Updates commander damage preference
     * @param value
     */
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

    /**
     * Determines if auto deletion of players on death is enabled
     * @return
     */
    public boolean isAutoDeleteOn(){
        int value = getIntFromId(DataHelper.ID_AUTO_DELETE);
        if(value == 0){
            return false;
        }else {
            return true;
        }
    }

    /**
     * Sets auto deletion of players on death
     * @param value
     */
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

    /**
     * getter for max poison total in a game
     * @return
     */
    public int getMaxPoison(){
        return getIntFromId(DataHelper.ID_POISON);
    }

    /**
     * setter for max poison total for games
     * @param newMax
     */
    public void setMaxPoison(int newMax){
        updateIntValue(DataHelper.ID_POISON, newMax);
    }

    /**
     * gets the starting life total
     * @return
     */
    public int getStartingLife(){
        return getIntFromId(DataHelper.ID_STARTING_LIFE);
    }

    /**
     * Updates the database with new starting life total preference
     * @param newMax
     */
    public void setStartingLife(int newMax){
        updateIntValue(DataHelper.ID_STARTING_LIFE, newMax);
    }

    /**
     * gets the number of players for the game
     * @return
     */
    public int getNumPlayers(){
        return getIntFromId(DataHelper.ID_NUM_PLAYERS);
    }

    /**
     * Sets the number of players for the game
     * @param newMax
     */
    public void setNumPlayers(int newMax){
        updateIntValue(DataHelper.ID_NUM_PLAYERS, newMax);
    }

    /**
     * returns the int value of the row. Helper method
     * @param id
     * @return
     */
    private int getIntFromId(int id){
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT " + allColumns[1] + " FROM " +
                        TABLE_NAME + " WHERE _id = " + id + ";", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    /**
     * Updates the int value for a specified ID
     * @param id
     * @param value
     */
    private void updateIntValue(int id, int value){
        dbHelper.getWritableDatabase().execSQL(
                "UPDATE " + TABLE_NAME + " SET " + allColumns[1] + " ="+value+
                        " WHERE _id="+id+";"
        );
    }
}
