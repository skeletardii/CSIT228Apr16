package com.example.csit228_f1_v2;

public class User {
    private int id=0;
    private String username=null;
    private String status=null;
    public User(int id, String username, String status){
        this.id = id;
        this.username = username;
        this.status = status;
    }
    public int getId(){return id;}
    public String getUsername(){return username;}
    public String getStatus(){return status;}
}
