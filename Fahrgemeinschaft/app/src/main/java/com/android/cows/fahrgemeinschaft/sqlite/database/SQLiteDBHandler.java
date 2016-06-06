package com.android.cows.fahrgemeinschaft.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by david on 29.05.2016.
 */
public class SQLiteDBHandler extends SQLiteOpenHelper{
    //new
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "chat.db";
    private static final String TABLE_CHAT_MESSAGE = "CREATE TABLE chat_message(id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR(255));";
    private static final String GET_CHAT_MESSAGES = "SELECT * FROM chat_message";

    private String setChatMessage(Chat c) {
        Gson gson = new Gson();
        String cm = gson.toJson(c);
        return cm;
    }

    private Chat getChatMessage(String jsonInString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonInString, Chat.class);
    }

    public void addChatMessage(Chat c) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("message", setChatMessage(c));
        db.insert("chat_message", null, cv);
        db.close();
    }

    public ArrayList<Chat> getChatMessages() {
        ArrayList<Chat> alc = new ArrayList<Chat>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_CHAT_MESSAGES, null);
        cur.moveToFirst();
        while(!cur.isAfterLast()) {
            if(cur.getString(cur.getColumnIndex("message")) != null) {
                alc.add(getChatMessage(cur.getString(cur.getColumnIndex("message"))));
                System.out.println("DATABASE GET: " + cur.getString(cur.getColumnIndex("message")));
                cur.moveToNext();
            }
        }
        db.close();
        return alc;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CHAT_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chat_message;");
        onCreate(db);
    }

    public SQLiteDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
}
