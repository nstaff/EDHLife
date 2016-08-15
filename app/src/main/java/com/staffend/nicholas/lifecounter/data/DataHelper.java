package com.staffend.nicholas.lifecounter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nicholas on 1/1/2016.
 */
public class DataHelper extends SQLiteOpenHelper {
    public static final String TABLE_PLAYERS = "players";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYER = "player";

    public static final String TABLE_PLAYERS_EDH = "edh_players";
    public static final String COLUMN_PLAYER_EDH = "edh_player";

    public static final String TABLE_EDH_SETTINGS = "edh_settings";
    public static final String COLUMN_EDH_SETTINGS = "edh_value";
    public static final int ID_DMG_LINKED = 1;
    public static final int ID_AUTO_DELETE = 2;
    public static final int ID_POISON = 3;
    public static final int ID_STARTING_LIFE = 4;
    public static final int ID_NUM_PLAYERS = 5;




    private static final String DATABASE_NAME = "players.db";
    private static final int DATABASE_VERSION = 10;



    //Database creation sql statment
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PLAYERS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PLAYER
            + " text not null);";
    private static final String DATABASE_CREATE_EDH = "create table "
            + TABLE_PLAYERS_EDH + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PLAYER_EDH
            + " text not null);";
    private static final String DATABASE_CREATE_EDH_SETTINGS = "create table "
            + TABLE_EDH_SETTINGS + "(" + COLUMN_ID
            + " integer primary key, " + COLUMN_EDH_SETTINGS
            + " integer not null);";


    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE_EDH);
        db.execSQL(DATABASE_CREATE_EDH_SETTINGS);

        db.execSQL("INSERT INTO " + TABLE_PLAYERS + " ("+COLUMN_PLAYER+") VALUES ('Player 1');");
        db.execSQL("INSERT INTO " + TABLE_PLAYERS + " ("+COLUMN_PLAYER+") VALUES ('Player 2');");

        db.execSQL("INSERT INTO " + TABLE_PLAYERS_EDH + " ("+COLUMN_PLAYER_EDH+") VALUES ('Player 1');");
        db.execSQL("INSERT INTO " + TABLE_PLAYERS_EDH + " ("+COLUMN_PLAYER_EDH+") VALUES ('Player 2');");
        db.execSQL("INSERT INTO " + TABLE_PLAYERS_EDH + " ("+COLUMN_PLAYER_EDH+") VALUES ('Player 3');");
        db.execSQL("INSERT INTO " + TABLE_PLAYERS_EDH + " ("+COLUMN_PLAYER_EDH+") VALUES ('Player 4');");
        db.execSQL("INSERT INTO " + TABLE_PLAYERS_EDH + " ("+COLUMN_PLAYER_EDH+") VALUES ('Player 5');");
        db.execSQL("INSERT INTO " + TABLE_PLAYERS_EDH + " ("+COLUMN_PLAYER_EDH+") VALUES ('Player 6');");
        db.execSQL("INSERT INTO " + TABLE_PLAYERS_EDH + " ("+COLUMN_PLAYER_EDH+") VALUES ('Player 7');");
        db.execSQL("INSERT INTO " + TABLE_PLAYERS_EDH + " (" + COLUMN_PLAYER_EDH + ") VALUES ('Player 8');");

        db.execSQL("INSERT INTO " + TABLE_EDH_SETTINGS + " ("+COLUMN_ID+", "+COLUMN_EDH_SETTINGS+") VALUES ("+ID_DMG_LINKED+",1);");
        db.execSQL("INSERT INTO " + TABLE_EDH_SETTINGS + " ("+COLUMN_ID+", "+COLUMN_EDH_SETTINGS+") VALUES ("+ID_AUTO_DELETE+",1);");
        db.execSQL("INSERT INTO " + TABLE_EDH_SETTINGS + " ("+COLUMN_ID+", "+COLUMN_EDH_SETTINGS+") VALUES ("+ID_POISON+",10);");
        db.execSQL("INSERT INTO " + TABLE_EDH_SETTINGS + " ("+COLUMN_ID+", "+COLUMN_EDH_SETTINGS+") VALUES ("+ID_STARTING_LIFE+",40);");
        db.execSQL("INSERT INTO " + TABLE_EDH_SETTINGS + " ("+COLUMN_ID+", "+COLUMN_EDH_SETTINGS+") VALUES ("+ID_NUM_PLAYERS+",2);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS_EDH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDH_SETTINGS);
        onCreate(db);
    }
}
