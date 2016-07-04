package com.android.cows.fahrgemeinschaft.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import de.dataobjects.JsonCollection;
import de.dataobjects.User;
import de.dataobjects.UserInGroup;

/**
 * Created by david on 29.05.2016.
 */
public class SQLiteDBHandler extends SQLiteOpenHelper {
    //new new new
    private static final int DATABASE_VERSION = 123;
    private static final String TAG = "SQLiteDbHandler";
    private static final String DATABASE_NAME = "chat.db";
    private static final String TABLE_CHAT_MESSAGE = "CREATE TABLE chat_message(id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR(400));";
    private static final String TABLE_APPOINTMENTS = "CREATE TABLE appointments(aid INTEGER , gid VARCHAR(255), isParticipant INTEGER, JsonInString VARCHAR(400), PRIMARY KEY(aid, gid), FOREIGN KEY(gid) REFERENCES groups(gid));";
    private static final String TABLE_GROUPS = "CREATE TABLE groups(gid VARCHAR(255) PRIMARY KEY, isJoined INTEGER, JsonInString VARCHAR(400));";
    private static final String TABLE_IS_IN_APPOINTMENT =
                    "CREATE TABLE is_in_appointment (aid INTEGER, gid VARCHAR(255), uid VARCHAR(255), isParticipant INTEGER, " +
                    "FOREIGN KEY(aid) REFERENCES appointments(aid), " +
                    "FOREIGN KEY(gid) REFERENCES appointments(gid), " +
                    "FOREIGN KEY(uid) REFERENCES user(uid), " +
                    "PRIMARY KEY(aid, gid, uid));";
    private static final String TABLE_IS_IN_GROUP = "CREATE TABLE is_in_group (uid VARCHAR(255), gid VARCHAR(255), isJoined INTEGER," +
                    "FOREIGN KEY(uid) REFERENCES users(uid), " +
                    "FOREIGN KEY(gid) REFERENCES groups(gid) " +
                    "PRIMARY KEY(uid, gid));" ;
    private static final String TABLE_USERS = "CREATE TABLE user(uid VARCHAR(255) PRIMARY KEY, JsonInString VARCHAR(400));";
    /// Constraints für IsInGroup        ",  CONSTRAINT gid FOREIGN KEY (gid) REFERENCES groups(gid), CONSTRAINT uid FOREIGN KEY (uid) REFERENCES user(userid));";

    private static final String GET_CHAT_MESSAGES = "SELECT * FROM chat_message";
    private static final String GET_GROUPS = "SELECT * FROM groups";
    private static final String GET_APPOINTMENTS = "SELECT * FROM appointments WHERE gid = ";
    private static final String GET_APPOINTMENT_1 = "SELECT * FROM appointments WHERE aid = ";
    private static final String GET_APPOINTMENT_2 = " AND gid = ";
    private static final String GET_GROUP = "SELECT * FROM groups WHERE gid = ";
    private static final String GET_USER_IN_GROUP = "SELECT uid FROM  is_in_group WHERE gid = ";
    private static final String GET_USER = "SELECT * FROM user WHERE uid =";
    private static final String GET_IS_IN_GROUP = "SELECT * FROM is_in_group WHERE gid = ";
    private static final String GET_IS_JOINT_1 = "SELECT * FROM is_in_group WHERE gid = ";
    private static final String GET_IS_JOINT_2 = " AND uid = ";
    private static final String GET_HIGHEST_ID_1 = "SELECT aid FROM appointments WHERE gid = ";
    private static final String GET_HIGHEST_ID_2 = " ORDER BY aid DESC LIMIT 1 ";

    private static final String DELETE_USER_IN_GROUP1 ="DELETE FROM is_in_group WHERE gid = ";
    private static final String DELETE_USER_IN_GROUP2 =" AND uid = ";

    private static final String DELETE_GROUP = "DELETE FROM groups WHERE gid =";
    private static final String DELETE_APPOINTMENT = "DELETE FROM appointments WHERE aid =";






    public void addChatMessage(de.dataobjects.Chat c) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("message", c.getJsonInString());
        db.insertWithOnConflict("chat_message", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public ArrayList<de.dataobjects.Chat> getChatMessages() {
        ArrayList<de.dataobjects.Chat> alc = new ArrayList<de.dataobjects.Chat>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_CHAT_MESSAGES, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("message")) != null) {
                alc.add(JsonCollection.jsonToChat(cur.getString(cur.getColumnIndex("message"))));
                Log.i(TAG, "getChatMessages " + cur.getString(cur.getColumnIndex("message")));
            }
            cur.moveToNext();
        }
        db.close();
        return alc;
    }

    public void joinGroup(String gid, String uid, int isJoined) {
        Log.i(TAG, "Joining Group");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("gid", gid);
        cv.put("uid", uid);
        cv.put("isJoined", isJoined);
        db.insertWithOnConflict("is_in_group", null, cv,SQLiteDatabase.CONFLICT_IGNORE );
        db.close();
    }

    public void addGroup(de.dataobjects.Groups group) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("gid", group.getGid());
        cv.put("JsonInString", group.getJsonInString());
        db.insertWithOnConflict("groups", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uid", user.getId());
        cv.put("JsonInString", user.getJsonInString());
        db.insertWithOnConflict("user", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addIsInGroup(String gid, String uid, int isJoined) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("gid", gid);
        cv.put("isJoined", isJoined);
        cv.put("uid", uid);
        db.insertWithOnConflict("is_in_group", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addIsInGroup(UserInGroup userInGroup) {
        addIsInGroup(userInGroup.getGid(), userInGroup.getUid(), userInGroup.getIsJoined());
    }

    public ArrayList<UserInGroup> getUserList(String gid) {
        ArrayList<UserInGroup> isInGroupList = new ArrayList<UserInGroup>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_IS_IN_GROUP + "'" + gid + "'", null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("uid")) != null) {


                String groupid = cur.getString(cur.getColumnIndex("gid"));
                String userid = cur.getString(cur.getColumnIndex("uid"));
                int isJoined = cur.getInt(cur.getColumnIndex("isJoined"));

                UserInGroup userInGroup = new UserInGroup(userid, groupid, isJoined);
                isInGroupList.add(userInGroup);
            }
            cur.moveToNext();
        }
        db.close();
        return isInGroupList;

    }


    public ArrayList<UserInGroup> getIsInGroupWithGroupId(String gid) {
        ArrayList<UserInGroup> isInGroupList = new ArrayList<UserInGroup>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_IS_IN_GROUP + "'" + gid + "'", null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("uid")) != null) {

                String groupid = cur.getString(cur.getColumnIndex("gid"));
                String userid = cur.getString(cur.getColumnIndex("uid"));
                int isJoined = cur.getInt(cur.getColumnIndex("isJoined"));

                UserInGroup userInGroup = new UserInGroup(userid, groupid, isJoined);
                isInGroupList.add(userInGroup);
            }
            cur.moveToNext();
        }
        db.close();
        return isInGroupList;
    }

    public int getIsJoint(String gid, String uid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_IS_JOINT_1 + "'" + gid + "'" + GET_IS_JOINT_2 + "'" + uid + "'", null);
        cur.moveToFirst();
        if (cur.getString(cur.getColumnIndex("isJoined")) != null) {
            db.close();
            Log.i(TAG, "isJoined " + cur.getString(cur.getColumnIndex("isJoined")));
            return cur.getInt(cur.getColumnIndex("isJoined"));
        }
        db.close();
        return -1;
    }

    public void setIsJoint(String groupId, String userId ,  int isJoined) {
        Log.i(TAG, "Set isJoined ");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("gid", groupId);
        cv.put("uid", userId);
        cv.put("isJoined",isJoined);
        db.insertWithOnConflict("is_in_group", null, cv,SQLiteDatabase.CONFLICT_REPLACE );
        db.close();
    }

    // DELETE USER FROM SQL LITE DATABASE
    public void deleteUserInGroup(String groupId, String userId){
        Log.i(TAG, "Delete UserinGroup ");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put("gid", groupId);
        //cv.put("uid", userId);
        String delete_is_in_group  = DELETE_GROUP + "'" + groupId +"'" ;
        db.execSQL(delete_is_in_group);
        //db.delete();
        db.close();
    }

    // DELETE USER FROM SQL LITE DATABASE
    public void deleteAppoinment(int aid){
        Log.i(TAG, "Delete Appointment ");
        SQLiteDatabase db = getWritableDatabase();
        String delete_appoint  = DELETE_APPOINTMENT + "'" + aid +"'" ;
        db.execSQL(delete_appoint);
        //db.delete();
        db.close();
    }

    public ArrayList<de.dataobjects.Groups> getGroups() {
        ArrayList<de.dataobjects.Groups> groupArrayList = new ArrayList<de.dataobjects.Groups>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_GROUPS, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("JsonInString")) != null) {
                groupArrayList.add(JsonCollection.jsonToGroup(cur.getString(cur.getColumnIndex("JsonInString"))));
                Log.i(TAG, "getGroups  " + cur.getString(cur.getColumnIndex("JsonInString")));

            }
            cur.moveToNext();
        }
        db.close();
        return groupArrayList;
    }

    public de.dataobjects.Groups getGroup(String gid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_GROUP + "'" + gid + "'", null);
        cur.moveToFirst();
        if (cur.getString(cur.getColumnIndex("JsonInString")) != null) {
            db.close();
            Log.i(TAG, "getGroup " + cur.getString(cur.getColumnIndex("JsonInString")));
            de.dataobjects.Groups group = JsonCollection.jsonToGroup(cur.getString(cur.getColumnIndex("JsonInString")));
            return group;
        }
        db.close();
        return null;
    }

    public User getUser(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_USER + "'" + uid + "'", null);
        cur.moveToFirst();
        if (cur.getString(cur.getColumnIndex("JsonInString")) != null) {
            db.close();
            Log.i(TAG, "getUser " + cur.getString(cur.getColumnIndex("JsonInString")));
            return JsonCollection.jsonToUser(cur.getString(cur.getColumnIndex("JsonInString")));
        }
        db.close();
        return null;
    }

    public SQLiteDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    public void addAppointment(de.dataobjects.Appointment appointment, int isParticipant) {
        if (getAppointment(appointment.getAid(), appointment.getGid()) == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("JsonInString", appointment.getJsonInString());
            cv.put("gid", appointment.getGid());
            cv.put("aid", appointment.getAid());
            cv.put("isParticipant", isParticipant);
            db.insertWithOnConflict("appointments", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
            db.close();
        } else {
            Log.i(TAG, "Appointment already in Database");
        }

    }


    public ArrayList<de.dataobjects.Appointment> getAppointments(String gid) {
        ArrayList<de.dataobjects.Appointment> appointmentArrayList = new ArrayList<de.dataobjects.Appointment>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_APPOINTMENTS + "'" + gid + "'", null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("JsonInString")) != null) {
                appointmentArrayList.add(JsonCollection.jsonToAppointment(cur.getString(cur.getColumnIndex("JsonInString"))));
                Log.i(TAG, "getAppointments " + cur.getString(cur.getColumnIndex("JsonInString")));
            }
            cur.moveToNext();
        }
        db.close();
        return appointmentArrayList;
    }

    public de.dataobjects.Appointment getAppointment(int aid, String gid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_APPOINTMENT_1 + aid + GET_APPOINTMENT_2 + "'" + gid + "'", null);
        cur.moveToFirst();
        if (!cur.isAfterLast()) {
            db.close();
            Log.i(TAG, "getAppointment " + cur.getString(cur.getColumnIndex("JsonInString")));
            return JsonCollection.jsonToAppointment(cur.getString(cur.getColumnIndex("JsonInString")));
        }
        db.close();
        return null;
    }

    public int getNextAppointmentID(String gid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_HIGHEST_ID_1 + "'" + gid + "'" + GET_HIGHEST_ID_2, null);
        cur.moveToFirst();
        if (!cur.isAfterLast()) {
            db.close();
            Log.i(TAG, "getNextAppointmentID " + cur.getInt(cur.getColumnIndex("aid")));
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
        db.execSQL(TABLE_IS_IN_APPOINTMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chat_message;");
        db.execSQL("DROP TABLE IF EXISTS appointments;");
        db.execSQL("DROP TABLE IF EXISTS groups;");
        db.execSQL("DROP TABLE IF EXISTS is_in_group;");
        db.execSQL("DROP TABLE IF EXISTS user;");
        db.execSQL("DROP TABLE IF EXISTS is_in_appointment;");
        onCreate(db);
    }

}


