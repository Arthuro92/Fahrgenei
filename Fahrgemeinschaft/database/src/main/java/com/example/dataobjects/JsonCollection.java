package com.example.dataobjects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lennart on 18.06.2016.
 */
public class JsonCollection {
    public static Groups jsonToGroup(String jsonInString) {
        return new Gson().fromJson(jsonInString, Groups.class);
    }

    public static User jsonToUser(String jsonInString) {
        return new Gson().fromJson(jsonInString, User.class);
    }


    public static Chat jsonToChat(String jsonInString) {
        return new Gson().fromJson(jsonInString, Chat.class);
    }

    public static Appointment jsonToAppointment(String appointment) {
        return new Gson().fromJson(appointment, Appointment.class);
    }

    public static String[] jsonToStringArray(String jsonInString) {
        return new Gson().fromJson(jsonInString,String[].class);
    }

    public static ArrayList<User> jsonToUserList(String jsonInString) {
        return new Gson().fromJson(jsonInString, new TypeToken<ArrayList<User>>(){}.getType() );
    }

    public static UserInGroup jsonToUserInGroup(String userInGroup) {
        return new Gson().fromJson(userInGroup, UserInGroup.class);
    }

    public static ArrayList<UserInGroup> jsonToUserInGroupList(String userList) {
        return new Gson().fromJson(userList, new TypeToken<ArrayList<UserInGroup>>(){}.getType());
    }

    public static ArrayList<Groups> jsonToGroupList(String groupList) {
       return new Gson().fromJson(groupList, new TypeToken<List<Groups>>() {}.getType());
    }

    public static String objectToJson(Object object) {
        return new Gson().toJson(object);
    }
}

