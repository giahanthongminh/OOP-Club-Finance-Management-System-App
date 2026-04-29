package com.example.demo;

public class UserSession {
    private static String username = "";
    private static String password = "";

    public static void set(String user, String pass) {
        username = user;
        password = pass;
    }

    public static String getUsername() { return username; }
    public static String getPassword() { return password; }
    public static void setPassword(String pass) { password = pass; }
    public static void clear() { username = ""; password = ""; }
}
