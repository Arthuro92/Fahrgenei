package com.android.cows.fahrgemeinschaft.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dataobjects.Chat;
import com.dataobjects.Group;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by david on 29.05.2016.
 */
public class SQLiteDBHandler extends SQLiteOpenHelper{
    //new new new
    private static final int DATABASE_VERSION = 42;
    private static final String TAG = "SQLiteDbHandler";
    private static final String DATABASE_NAME = "chat.db";
    private static final String TABLE_CHAT_MESSAGE = "CREATE TABLE chat_message(id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR(400));";
    private static final String TABLE_APPOINTMENTS = "CREATE TABLE appointments(aid INTEGER PRIMARY KEY, gid VARCHAR(255), jsonInString VARCHAR(400));";
    private static final String TABLE_GROUPS = "CREATE TABLE groups(gid VARCHAR(255) PRIMARY KEY, isJoined INTEGER, jsonInString VARCHAR(400));";
    private static final String GET_CHAT_MESSAGES = "SELECT * FROM chat_message";
    private static final String GET_GROUPS = "SELECT * FROM groups";
    private static final String GET_GROUP = "SELECT * FROM groups WHERE gid = ";

    private String setChatMessage(Chat c) {
        Gson gson = new Gson();
        String cm = gson.toJson(c);
        return cm;
    }

    private Chat getChatMessage(String jsonInString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonInString, Chat.class);
    }


    private Group jsonToGroup(String jsonInString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonInString, Group.class);
    }

    private String groupToJson(Group group) {
        Gson gson = new Gson();
        return gson.toJson(group);
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

    public void joinGroup(Group group) {
        Log.i(TAG, "Joining Group");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("isJoined", 1);
        cv.put("jsonInString", groupToJson(group));
        db.update("groups",cv, "gid = '" + group.getGid() + "'", null);
        db.close();
    }

    public void addGroup(Group group) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("gid", group.getGid());
        cv.put("isJoined", group.getisJoined());
        cv.put("jsonInString", groupToJson(group));
        db.insert("groups", null, cv);
        db.close();
    }

    public ArrayList<Group> getGroups() {
        ArrayList<Group> groupArrayList = new ArrayList<Group>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_GROUPS, null);
        cur.moveToFirst();
        while(!cur.isAfterLast()) {
            if(cur.getString(cur.getColumnIndex("jsonInString")) != null) {
                groupArrayList.add(jsonToGroup(cur.getString(cur.getColumnIndex("jsonInString"))));
                System.out.println("DATABASE GET: " + cur.getString(cur.getColumnIndex("jsonInString")));
                cur.moveToNext();
            }
        }
        db.close();
        return groupArrayList;
    }

    public Group getGroup(String gid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_GROUP + "'" + gid + "'", null);
        cur.moveToFirst();
        if(cur.getString(cur.getColumnIndex("jsonInString")) != null) {
            db.close();
            return jsonToGroup(cur.getString(cur.getColumnIndex("jsonInString")));
        }
        db.close();
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CHAT_MESSAGE);
        db.execSQL(TABLE_APPOINTMENTS);
        db.execSQL(TABLE_GROUPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chat_message;");
        db.execSQL("DROP TABLE IF EXISTS appointments;");
        db.execSQL("DROP TABLE IF EXISTS groups;");
        onCreate(db);
    }

    public SQLiteDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
}


//private ArrayList<Appointment> jsonToAppointmentArray(String jsonInString) {
//        Gson gson = new Gson();
//        return gson.fromJson(jsonInString, new TypeToken<List<Appointment>>(){}.getType());
//        }
//
//private String appointmentToJson(Appointment appointment) {
//        Gson gson = new Gson();
//        return gson.toJson(appointment);
//        }
//public void addAppointments(String jsonInString) {
//        ArrayList<Appointment> applist = jsonToAppointmentArray(jsonInString);
//
//        SQLiteDatabase db = getWritableDatabase();
//        for(Appointment appointment : applist) {
//        ContentValues cv = new ContentValues();
//        cv.put("message", appointmentToJson(appointment));
//        cv.put("gid", appointment.getGid());
//        cv.put("aid", appointment.getAid());
//        db.insert("appointments", null, cv);
//        }
//        }