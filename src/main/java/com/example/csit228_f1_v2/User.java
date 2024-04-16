package com.example.csit228_f1_v2;

public class User {
    public static int id=0;
    public static String username=null;
    public static String password=null;
    public static String favorite=null;
    public static void logOut(){
        id = 0;
        username = null;
        password = null;
        favorite = null;
    }
}
