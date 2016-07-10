package com.android.cows.fahrgemeinschaft.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import de.dataobjects.Appointment;
import de.dataobjects.JsonCollection;
import de.dataobjects.Task;
import de.dataobjects.User;
import de.dataobjects.UserInAppointment;
import de.dataobjects.UserInGroup;

/**
 * Created by david on 29.05.2016.
 */
public class SQLiteDBHandler extends SQLiteOpenHelper {
    //new new new
    private static final int DATABASE_VERSION = 159;
    private static final String TAG = "SQLiteDbHandler";
    private static final String DATABASE_NAME = "chat.db";
    private static final String TABLE_CHAT_MESSAGE = "CREATE TABLE chat_message(id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR(400), gid VARCHAR(255));";
    private static final String TABLE_APPOINTMENTS = "CREATE TABLE appointments(aid INTEGER , gid VARCHAR(255), JsonInString VARCHAR(400), PRIMARY KEY(aid, gid), FOREIGN KEY(gid) REFERENCES groups(gid));";
    private static final String TABLE_GROUPS = "CREATE TABLE groups(gid VARCHAR(255) PRIMARY KEY, isJoined INTEGER, JsonInString VARCHAR(400));";
    private static final String TABLE_IS_IN_APPOINTMENT =
                    "CREATE TABLE is_in_appointment (aid INTEGER, gid VARCHAR(255), uid VARCHAR(255), isParticipant INTEGER, " +
                    "JsonInString VARCHAR(400), " +
                    "FOREIGN KEY(aid) REFERENCES appointments(aid), " +
                    "FOREIGN KEY(gid) REFERENCES appointments(gid), " +
                    "FOREIGN KEY(uid) REFERENCES user(uid), " +
                    "PRIMARY KEY(aid, gid, uid));";
    private static final String TABLE_IS_IN_GROUP = "CREATE TABLE is_in_group (uid VARCHAR(255), gid VARCHAR(255), isJoined INTEGER," +
                    "drivingcount INTEGER," +
                    "FOREIGN KEY(uid) REFERENCES users(uid), " +
                    "FOREIGN KEY(gid) REFERENCES groups(gid) " +
                    "PRIMARY KEY(uid, gid));" ;
    private static final String TABLE_USERS = "CREATE TABLE user(uid VARCHAR(255) PRIMARY KEY, JsonInString VARCHAR(400));";
    private static final String TABLE_TASK = "CREATE TABLE task(taskid INTEGER, aid INTEGER, gid VARCHAR(255), taskname VARCHAR(255), taskdescription VARCHAR(400), responsible VARCHAR(255), JsonInString VARCHAR(400), " +
            "FOREIGN KEY(aid) REFERENCES appointments(aid)," +
            "FOREIGN KEY(gid) REFERENCES appointments(gid)," +
            "PRIMARY KEY(taskid, aid , gid));";
    /// Constraints für IsInGroup        ",  CONSTRAINT gid FOREIGN KEY (gid) REFERENCES groups(gid), CONSTRAINT uid FOREIGN KEY (uid) REFERENCES user(userid));";

    private static final String GET_CHAT_MESSAGES = "SELECT * FROM chat_message WHERE gid = ";
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
    private static final String GET_TASKS_1 = "SELECT * FROM task WHERE aid = ";
    private static final String GET_TASKS_2 = "AND gid = ";
    private static final String GET_HIGHEST_TASKID_1 = "SELECT taskId FROM task WHERE gid = ";
    private static final String GET_HIGHEST_TASKID_2 = " AND aid = ";
    private static final String GET_HIGHEST_TASKID_3 = " ORDER BY taskId DESC LIMIT 1 ";
    private static final String GET_USER_IN_APPOINTMENT_1 = "SELECT * FROM is_in_appointment WHERE aid = ";
    private static final String GET_USER_IN_APPOINTMENT_2 = "AND gid = ";
    private static final String GET_USER_IN_APPOINTMENT_3 = "AND uid = ";

    private static final String GET_MY_APPOINTMENTS = "SELECT * FROM is_in_appointment WHERE uid = ";
    private static final String GET_ALL_APPOINTMENTS = "SELECT * FROM appointments";



    private static final String DELETE_USER_IN_GROUP = "DELETE FROM is_in_group WHERE gid =";
    private static final String DELETE_USER_IN_GROUP1 ="DELETE FROM is_in_group WHERE gid = ";
    private static final String DELETE_USER_IN_GROUP2 =" AND uid = ";
    private static final String DELETE_USER_IN_APPOINTMENT_1 = "DELETE FROM is_in_appointment WHERE gid = ";
    private static final String DELETE_USER_IN_APPOINTMENT_2 = "AND uid = ";
    private static final String DELETE_GROUP = "DELETE FROM groups WHERE gid =";
    private static final String DELETE_APPOINTMENT_1 = "DELETE FROM appointments WHERE aid =";
    private static final String DELETE_APPOINTMENT_2 = "AND gid = ";
    private static final String DELETE_TASK_1 = "DELETE FROM task WHERE taskid = ";
    private static final String DELETE_TASK_2 = "AND gid = ";
    private static final String DELETE_TASK_3 = "AND aid = ";


    /**
     * Add ChatMessage
     * @param c chat object to be added
     */
    public void addChatMessage(de.dataobjects.Chat c) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("message", c.getJsonInString());
        cv.put("gid", c.getGid());
        db.insertWithOnConflict("chat_message", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    /**
     * Get Chat Object
     * @param gid group id
     * @return chat object
     */
    public ArrayList<de.dataobjects.Chat> getChatMessages(String gid) {
        ArrayList<de.dataobjects.Chat> alc = new ArrayList<de.dataobjects.Chat>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_CHAT_MESSAGES  + "'" + gid + "'", null);
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

    /**
     * Change Table is_in_group
     * @param gid group id
     * @param uid user id
     * @param isJoined isUserJoined 1 or 0 1 yes, 0 no
     */
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

    /**
     * Add Group to db
     * @param group group object
     */
    public void addGroup(de.dataobjects.Groups group) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("gid", group.getGid());
        cv.put("JsonInString", group.getJsonInString());
        db.insertWithOnConflict("groups", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Add User to db
     * @param user user object
     */
    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uid", user.getId());
        cv.put("JsonInString", user.getJsonInString());
        db.insertWithOnConflict("user", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Add UserInGroup
     * @param gid gid
     * @param uid uid
     * @param isJoined is user joined 1 yes, 0 no
     * @param drivingcount counter for how often he was driver in appointment
     */
    public void addIsInGroup(String gid, String uid, int isJoined, int drivingcount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("gid", gid);
        cv.put("isJoined", isJoined);
        cv.put("uid", uid);
        cv.put("drivingcount", drivingcount);
        db.insertWithOnConflict("is_in_group", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Add UserInGroup Object
     * @param userInGroup userInGroup Object
     */
    public void addIsInGroup(UserInGroup userInGroup) {
        addIsInGroup(userInGroup.getGid(), userInGroup.getUid(), userInGroup.getIsJoined(), userInGroup.getDrivingCount());
    }

    /**
     * Get all UserinGroup objects with gid
     * @param gid gid
     * @return UserInGroup List
     */
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
                int drivercount = cur.getInt(cur.getColumnIndex("drivingcount"));

                UserInGroup userInGroup = new UserInGroup(userid, groupid, isJoined);
                userInGroup.setDrivingCount(drivercount);
                isInGroupList.add(userInGroup);
            }
            cur.moveToNext();
        }
        db.close();
        return isInGroupList;

    }

    /**
     * Get if User is Joined in Group
     * @param gid group id
     * @param uid user id
     * @return -1 error, 0 not joined, 1 joined
     */
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

    /**
     * Set is Joined
     * @param groupId groupId
     * @param userId userid
     * @param isJoined 1 joined, 0 not joined
     */
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

    /**
     * Delete Group from database
     * and delete user_in_group
     * @param groupId grpid
     */
    public void deleteGroup(String groupId){
        Log.i(TAG, "Delete Group ");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put("gid", groupId);
        //cv.put("uid", userId);
        String delete_group  = DELETE_GROUP + "'" + groupId +"'" ;
        String delete_user_in_group  = DELETE_USER_IN_GROUP + "'" + groupId +"'" ;
        db.execSQL(delete_group);
        db.execSQL(delete_user_in_group);
        //db.delete();
        db.close();
    }

    /**
     * Delete Task from local Database
     * @param tid task id
     * @param gid group id
     * @param aid appointment id
     */
    public void deleteTask(int tid, String gid, int aid) {
        Log.i(TAG, "Delete Task ");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put("gid", groupId);
        //cv.put("uid", userId);
        String delete_task  = DELETE_TASK_1 + "'" + tid + "'" + DELETE_TASK_2 + "'" + gid + "'" + DELETE_TASK_3 + "'" + aid + "'" ;
        db.execSQL(delete_task);
        //db.delete();
        db.close();
    }

    /**
     * Delete Appointment from database
     * @param aid appointment id
     * @param gid group id
     */
    public void deleteAppoinment(int aid, String gid){
        Log.i(TAG, "Delete Appointment  with aid: " + aid + " and gid: " + gid);
        SQLiteDatabase db = getWritableDatabase();
        String delete_appoint  = DELETE_APPOINTMENT_1 + "'" + aid +"'" + DELETE_APPOINTMENT_2 + "'" + gid + "'";
        db.execSQL(delete_appoint);
        //db.delete();
        db.close();
    }

    /**
     * Delete from is_in_appointment
     * @param gid  group id
     * @param uid user id
     */
    public void deleteUserInAppointment(String gid, String uid) {
        Log.i(TAG, "Delete User In Appointment  with gid: " + gid + " and uid: " + uid);
        SQLiteDatabase db = getWritableDatabase();
        String delete_user_in_appointment  = DELETE_USER_IN_APPOINTMENT_1 + "'" + gid +"'" + DELETE_USER_IN_APPOINTMENT_2 + "'" + uid + "'";
        db.execSQL(delete_user_in_appointment);
        //db.delete();
        db.close();
    }

    /**
     * Delete from is_in_group
     * @param gid group id
     * @param uid user id
     */
    public void deleteUserInGroup(String gid, String uid) {
        Log.i(TAG, "Delete User In Group ");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put("gid", groupId);
        //cv.put("uid", userId);
        String delete_is_in_group  = DELETE_USER_IN_GROUP1 + "'" + gid + "'" + DELETE_USER_IN_GROUP2 + "'" + uid + "'" ;
        db.execSQL(delete_is_in_group);
        //db.delete();
        db.close();
    }

    /**
     * Get all Groups
     * @return group ArrayList
     */
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

    /**
     * Get single Group
     * @param gid group id
     * @return Group or Null if none Exists
     */
    public de.dataobjects.Groups getGroup(String gid) {
        SQLiteDatabase db = getWritableDatabase();
        Log.i(TAG, "Gid " + gid);
        try {
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
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get User
     * @param uid user id
     * @return User or null if none exists
     */
    public User getUser(String uid) {
        try {
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
        } catch (CursorIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Add Appointment to db
     * @param appointment Appointment object
     */
    public void addAppointment(de.dataobjects.Appointment appointment) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("JsonInString", appointment.getJsonInString());
            cv.put("gid", appointment.getGid());
            cv.put("aid", appointment.getAid());
            db.insertWithOnConflict("appointments", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
    }

    /**
     * Gibt eine ArrayList mit alle Termine einer bestimmten Gruppe  aus
     * @param gid group id
     * @return appointmentArrayList
     */
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

    /**
     * Return all Appointments
     * @return Appointment ArrayList
     */
    public ArrayList<de.dataobjects.Appointment> getAllAppointments() {
        ArrayList<de.dataobjects.Appointment> appointmentArrayList = new ArrayList<de.dataobjects.Appointment>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_ALL_APPOINTMENTS, null);
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

    /**
     * Gibt ein ArrayList aus mit allen Terminen meiner Gruppen aus
     * @param uid
     * @return appointmentArrayList
     */
    public ArrayList<de.dataobjects.Appointment> getMyAppointments(String uid) {
        ArrayList<de.dataobjects.Appointment> appointmentArrayList = new ArrayList<de.dataobjects.Appointment>();
        SQLiteDatabase db = getWritableDatabase();
        /**
        Cursor cur = db.rawQuery(GET_MY_APPOINTMENTS + "'" + uid + "'", null);
        cur.moveToFirst();
        int columnCounter = cur.getColumnCount() - 1;
        Log.i("DIE ERSTE AID LAUTET ", Integer.toString(cur.getInt(cur.getColumnIndex("aid"))));
        String statment = GET_APPOINTMENT_1 + Integer.toString(cur.getInt(cur.getColumnIndex("aid")));


        while (!cur.isLast()){
            cur.moveToNext();
            Log.i("DIE NEXTE AID LAUTET ", Integer.toString(cur.getInt(cur.getColumnIndex("aid"))));
            statment = statment + " OR aid = " + cur.getInt(cur.getColumnIndex("aid"));
        }
        Log.i("DAS SQL LAUTET ", statment);*/
        String statment2 = buildReturnMyAppointmentStatment(uid);

        Cursor curr = db.rawQuery("SELECT * FROM appointments", null);
        curr.moveToFirst();
        while (!curr.isAfterLast()) {
            if (curr.getString(curr.getColumnIndex("JsonInString")) != null) {
                appointmentArrayList.add(JsonCollection.jsonToAppointment(curr.getString(curr.getColumnIndex("JsonInString"))));
                Log.i(TAG, "getAppointments " + curr.getString(curr.getColumnIndex("JsonInString")));
            }
            curr.moveToNext();
        }

        return appointmentArrayList;
    }


    public String buildReturnMyAppointmentStatment(String userid){
        SQLiteDatabase db = getWritableDatabase();
        Log.i("UserId" ,userid);
        Cursor cur = db.rawQuery(GET_MY_APPOINTMENTS + "'" + userid + "'", null);
        cur.moveToFirst();
        Log.i("Current Col Index", Integer.toString(cur.getColumnIndex("aid")));
        Log.i("Data of CurrIndex", Integer.toString(cur.getInt(cur.getColumnIndex("aid"))) );
        Log.i("DIE ERSTE AID LAUTET ", Integer.toString(cur.getInt(cur.getColumnIndex("aid"))));
        String statment = GET_APPOINTMENT_1 + Integer.toString(cur.getInt(cur.getColumnIndex("aid")));


        while (!cur.isLast()){
            cur.moveToNext();
            Log.i("DIE NEXTE AID LAUTET ", Integer.toString(cur.getInt(cur.getColumnIndex("aid"))));
            statment = statment + " OR aid = " + cur.getInt(cur.getColumnIndex("aid"));
        }
        Log.i("DAS SQL LAUTET ", statment);
        db.close();
        return statment;
    }

    /**
     * Get single Appointment
     * @param aid appointment id
     * @param gid group id
     * @return Appointment
     */
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

    /**
     * Return highest Appointment Id
     * @param gid group id
     * @return highes id or 0 if no Appointment exists
     */
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

    /**
     * Get highest TaskId
     * @param gid group id
     * @param aid appointment id
     * @return highest id or 0 if no Task exists
     */
    public int getNextTaskID(String gid, int aid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_HIGHEST_TASKID_1 + "'" + gid + "'" + GET_HIGHEST_TASKID_2  + aid  + GET_HIGHEST_TASKID_3, null);
        cur.moveToFirst();
        if (!cur.isAfterLast()) {
            db.close();
            Log.i(TAG, "getNextAppointmentID " + cur.getInt(cur.getColumnIndex("taskid")));
            return cur.getInt(cur.getColumnIndex("taskid"));
        }
        db.close();
        return 0;
    }

    /**
     * Add Task to db
     * @param task Task object
     */
    public void addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("taskid", task.getTaskId());
        cv.put("aid", task.getAid());
        cv.put("gid", task.getGid());
        cv.put("taskname", task.getTaskName());
        cv.put("taskdescription", task.getTaskdescription());
        cv.put("responsible", task.getResponsible());
        cv.put("JsonInString", task.getJsonInString());
        db.insertWithOnConflict("task", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Get Task list
     * @param aid appointment id
     * @param gid group id
     * @return ArrayList of Tasks
     */
    public ArrayList<Task> getTasks(int aid, String gid) {
        ArrayList<Task> taskArrayList = new ArrayList<Task>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_TASKS_1 + "'" + aid + "'" + GET_TASKS_2 + "'" + gid + "'", null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("JsonInString")) != null) {
                taskArrayList.add(JsonCollection.jsonToTask(cur.getString(cur.getColumnIndex("JsonInString"))));
                Log.i(TAG, "getTasks  " + cur.getString(cur.getColumnIndex("JsonInString")));
                cur.moveToNext();
            }
        }
        db.close();
        return taskArrayList;
    }

    /**
     * Add in is_in_appointment
     * @param userInAppointment    UserInAppointment to be added
     */
    public void addIsInAppointment(UserInAppointment userInAppointment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("aid", userInAppointment.getAid());
        cv.put("gid", userInAppointment.getGid());
        cv.put("uid", userInAppointment.getUid());
        cv.put("JsonInString", userInAppointment.getJsonInString());
        cv.put("isParticipant", userInAppointment.getIsParticipant());
        db.insertWithOnConflict("is_in_appointment", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Get single UserInAppointment
     * @param aid appointment id
     * @param gid group id
     * @param uid user id
     * @return UserInAppointment Object
     */
    public UserInAppointment getUserInAppointment(int aid, String gid, String uid) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            Cursor cur = db.rawQuery(GET_USER_IN_APPOINTMENT_1 + "'" + aid + "'" + GET_USER_IN_APPOINTMENT_2 + "'" + gid + "'" + GET_USER_IN_APPOINTMENT_3 + "'" + uid + "'", null);
            cur.moveToFirst();
            if (cur.getString(cur.getColumnIndex("JsonInString")) != null) {
                db.close();
                Log.i(TAG, "getUserInAppointment " + cur.getString(cur.getColumnIndex("JsonInString")));
                return JsonCollection.jsonToUserInAppointment(cur.getString(cur.getColumnIndex("JsonInString")));
            }
            db.close();
            return null;
        } catch(CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all Users who are Participants in specific Appointment
     * @param aid appointment id
     * @param gid group id
     * @return ArrayList of UserInAppointments Objects
     */
    public ArrayList<UserInAppointment> getParticipantsInAppointment(int aid, String gid ) {
        ArrayList<UserInAppointment> userInAppointmentArrayList = new ArrayList<UserInAppointment>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(GET_USER_IN_APPOINTMENT_1 + "'" + aid + "'" + GET_USER_IN_APPOINTMENT_2 + "'" + gid + "'", null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            if (cur.getString(cur.getColumnIndex("JsonInString")) != null) {
                UserInAppointment userInAppointment = JsonCollection.jsonToUserInAppointment(cur.getString(cur.getColumnIndex("JsonInString")));
                if(userInAppointment.getIsParticipant() == 1) {
                    userInAppointmentArrayList.add(userInAppointment);
                    Log.i(TAG, "getAppointments " + cur.getString(cur.getColumnIndex("JsonInString")));
                }
            }
            cur.moveToNext();
        }
        db.close();
        return userInAppointmentArrayList;
    }

    /**
     * Update is_in_appointment and Appointment for new Appointment
     * @param appointment new appointment
     */
    public void updateUserInAppointment(Appointment appointment) {
        ArrayList<UserInGroup> userInGroupArrayList = this.getUserList(appointment.getGid());
        for(UserInGroup userInGroup : userInGroupArrayList) {
            UserInAppointment userInAppointment = new UserInAppointment(appointment.getAid(), appointment.getGid(), userInGroup.getUid(), 0);
            this.addIsInAppointment(userInAppointment);
        }
    }

    /**
     * Construcotr
     * @param context context
     * @param factory factory
     */
    public SQLiteDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CHAT_MESSAGE);
        db.execSQL(TABLE_APPOINTMENTS);
        db.execSQL(TABLE_GROUPS);
        db.execSQL(TABLE_IS_IN_GROUP);
        db.execSQL(TABLE_USERS);
        db.execSQL(TABLE_IS_IN_APPOINTMENT);
        db.execSQL(TABLE_TASK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chat_message;");
        db.execSQL("DROP TABLE IF EXISTS appointments;");
        db.execSQL("DROP TABLE IF EXISTS groups;");
        db.execSQL("DROP TABLE IF EXISTS is_in_group;");
        db.execSQL("DROP TABLE IF EXISTS user;");
        db.execSQL("DROP TABLE IF EXISTS is_in_appointment;");
        db.execSQL("DROP TABLE IF EXISTS task;");
        onCreate(db);
    }
}


