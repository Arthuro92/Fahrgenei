package com.android.cows.fahrgemeinschaft.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dataobjects.Appointment;
import com.dataobjects.Chat;
import com.dataobjects.Group;
import com.dataobjects.User;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by david on 29.05.2016.
 */
public class SQLiteDBHandler extends SQLiteOpenHelper {
    //new new new
    private static final int DATABASE_VERSION = 75;
    private static final String TAG = "SQLiteDbHandler";
    private static final String DATABASE_NAME = "chat.db";
    private static final String TABLE_CHAT_MESSAGE = "CREATE TABLE chat_message(id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR(400));";
    private static final String TABLE_APPOINTMENTS = "CREATE TABLE appointments(aid INTEGER , gid VARCHAR(255), isParticipant INTEGER, jsonInString VARCHAR(400), PRIMARY KEY(aid, gid), FOREIGN KEY(gid) REFERENCES groups(gid));";
    private static final String TABLE_GROUPS = "CREATE TABLE groups(gid VARCHAR(255) PRIMARY KEY, isJoined INTEGER, jsonInString VARCHAR(400));";
    //    private static final String TABLE_IS_IN_APPOINTMENT =
//            "CREATE TABLE is_in_appointment (aid INTEGER, gid VARCHAR(255), uid VARCHAR(255), isParticipant INTEGER, " +
//                    "PRIMARY KEY(aid, gid, uid)," +
//                    "FOREIGN KEY(aid) REFERENCES appointments(aid), " +
//                    "FOREIGN KEY(gid) REFERENCES appointments(gid) " +
//                    "FOREIGN KEY(uid) REFEREMCES ";
    private static final String TABLE_USERS = "CREATE TABLE user(uid VARCHAR(255) PRIMARY KEY, jsonInString VARCHAR(400));";
    private static final String TABLE_IS_IN_GROUP = "CREATE TABLE is_in_group(gid VARCHAR(255) , uid VARCHAR(255), isJoined INTEGER , PRIMARY KEY(gid, uid));";
    /// Constraints für IsInGroup        ",  CONSTRAINT gid FOREIGN KEY (gid) REFERENCES groups(gid), CONSTRAINT uid FOREIGN KEY (uid) REFERENCES user(userid));";
    private static final String GET_CHAT_MESSAGES = "SELECT * FROM chat_message";
    private static final String GET_GROUPS = "SELECT * FROM groups";
    private static final String GET_APPOINTMENTS = "SELECT * FROM appointments WHERE gid = ";
    private static final String GET_APPOINTMENT_1 = "SELECT * FROM appointments WHERE aid = ";
    private static final String GET_APPOINTMENT_2 = " AND gid = ";
    private static final String GET_GROUP = "SELECT * FROM groups WHERE gid = ";
    private static final String GET_USER_IN_GROUP = "SELECT uid FROM  is_in_group WHERE gid = ";
    private static final String GET_USERS = "SELECT * FROM user";
    private static final String GET_HIGHEST_ID_1 = "SELECT aid FROM appointments WHERE gid = ";
    private static final String GET_HIGHEST_ID_2 = " ORDER BY aid DESC LIMIT 1 ";

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

    private User jsonToUser(String jsonInString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonInString, User.class);
    }

    private String groupToJson(Group group) {
        Gson gson = new Gson();
        return gson.toJson(group);
    }

    private String userToJson(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
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
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("message")) != null) {
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
        db.update("groups", cv, "gid = '" + group.getGid() + "'" , null);
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

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uid", user.getId());
        cv.put("jsonInString",  userToJson(user));
        db.insert("user", null, cv);
        db.close();
    }

    public void addIsInGroup(String gid, String uid, int isJoined){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("gid", gid);
        cv.put("isJoined", isJoined);
        cv.put("uid", uid);
        db.insert("is_in_group", null, cv);
        db.close();
    }


    public ArrayList<User> getUserListOfGroup(){
        ArrayList<User> userArrayList = new ArrayList<User>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_USERS, null);
        cur.moveToFirst();
        while(!cur.isAfterLast()) {
            if(cur.getString(cur.getColumnIndex("uid")) != null) {

                userArrayList.add(jsonToUser(cur.getString(cur.getColumnIndex("jsonInString"))));
                System.out.println("DATABASE GET: " + cur.getString(cur.getColumnIndex("jsonInString")));
                cur.moveToNext();
            }
        }
        db.close();
        return userArrayList;

    }

    public ArrayList<Group> getGroups() {
        ArrayList<Group> groupArrayList = new ArrayList<Group>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_GROUPS, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("jsonInString")) != null) {
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
        if (cur.getString(cur.getColumnIndex("jsonInString")) != null) {
            db.close();
            return jsonToGroup(cur.getString(cur.getColumnIndex("jsonInString")));
        }
        db.close();
        return null;
    }


    public SQLiteDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    private Appointment jsonToAppointment(String appointment) {
        Gson gson = new Gson();
        return gson.fromJson(appointment, Appointment.class);
    }

    private String appointmentToJson(Appointment appointment) {
        Gson gson = new Gson();
        return gson.toJson(appointment);
    }

    public void addAppointment(Appointment appointment, int isParticipant) {
        if(getAppointment(appointment.getAid(), appointment.getGid()) == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("jsonInString", appointmentToJson(appointment));
            cv.put("gid", appointment.getGid());
            cv.put("aid", appointment.getAid());
            cv.put("isParticipant", isParticipant);
            db.insert("appointments", null, cv);
            db.close();
        } else {
            Log.i(TAG, "Appointment already in Database");
        }

    }


    public ArrayList<Appointment> getAppointments(String gid) {
        ArrayList<Appointment> appointmentArrayList = new ArrayList<Appointment>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_APPOINTMENTS + "'" + gid + "'", null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("jsonInString")) != null) {
                appointmentArrayList.add(jsonToAppointment(cur.getString(cur.getColumnIndex("jsonInString"))));
                System.out.println("DATABASE GET: " + cur.getString(cur.getColumnIndex("jsonInString")));
                cur.moveToNext();
            }
        }
        db.close();
        return appointmentArrayList;
    }

    public Appointment getAppointment(int aid, String gid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_APPOINTMENT_1 + aid + GET_APPOINTMENT_2 + "'" + gid + "'", null);
        cur.moveToFirst();
        if (!cur.isAfterLast()) {
            db.close();
            return  jsonToAppointment(cur.getString(cur.getColumnIndex("jsonInString")));
        }
        db.close();
        return null;
    }

    public int getNextAppointmentID(String gid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_HIGHEST_ID_1 + "'" +gid + "'" +GET_HIGHEST_ID_2, null);
        cur.moveToFirst();
        if (!cur.isAfterLast()) {
            db.close();
            return cur.getInt(cur.getColumnIndex("aid"));
        }
        db.close();
        return 0;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CHAT_MESSAGE);
        db.execSQL(TABLE_APPOINTMENTS);
        db.execSQL(TABLE_GROUPS);
        db.execSQL(TABLE_IS_IN_GROUP);
        db.execSQL(TABLE_USERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chat_message;");
        db.execSQL("DROP TABLE IF EXISTS appointments;");
        db.execSQL("DROP TABLE IF EXISTS groups;");
        db.execSQL("DROP TABLE IF EXISTS is_in_group;");
        db.execSQL("DROP TABLE IF EXISTS user;");
        onCreate(db);
    }

}


