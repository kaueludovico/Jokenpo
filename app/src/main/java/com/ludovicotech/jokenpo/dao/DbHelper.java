package com.ludovicotech.jokenpo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ludovicotech.jokenpo.model.Player;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	5;
    private	static final String	DATABASE_NAME = "player";
    private	static final String TABLE_PLAYERS = "players";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "playerName";
    private static final String COLUMN_WINS = "wins";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String	CREATE_PLAYERS_TABLE = "CREATE	TABLE " + TABLE_PLAYERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_WINS + " INTEGER" + ")";
        db.execSQL(CREATE_PLAYERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(db);
    }

    public ArrayList<Player> listPlayers(){
        String sql = "SELECT * FROM " + TABLE_PLAYERS;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Player> storePlayers = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                int wins = cursor.getInt(2);
                storePlayers.add(new Player(id, name, wins));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storePlayers;
    }

    public void addPlayers(Player players) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, players.getName());
        values.put(COLUMN_WINS, players.getPoints());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PLAYERS, null, values);
    }

    public void updatePlayers(Player players) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, players.getName());
        values.put(COLUMN_WINS, players.getPoints());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PLAYERS, values, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(players.getId())});
    }

    public void deletePlayer(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYERS, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
}
