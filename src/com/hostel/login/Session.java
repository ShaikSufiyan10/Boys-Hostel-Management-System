package com.hostel.login;

public class Session {

    private static int userId;
    private static String userName;
    private static String userType;

    public static void setUser(
            int id,
            String name,
            String type) {

        userId = id;
        userName = name;
        userType = type;
    }

    public static int getUserId() {

        return userId;
    }

    public static String getUserName() {

        return userName;
    }

    public static String getUserType() {

        return userType;
    }

    public static void logout() {

        userId = 0;
        userName = null;
        userType = null;
    }
}