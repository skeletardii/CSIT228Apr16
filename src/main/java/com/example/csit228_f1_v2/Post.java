package com.example.csit228_f1_v2;

public class Post {
    private int postid = -1;
    private int accountid = -1;
    private String contents = "";
    private boolean edited = false;

    public Post(int postid, int accountid, String contents, boolean edited){
        this.postid = postid;
        this.accountid = accountid;
        this.contents = contents;
        this.edited = edited;
    }
    public void edit(String newContents){
        contents = newContents;
        this.edited = true;
    }
    public boolean isEdited(){return edited;}
    public int getId(){return postid;}
    public int getAccountid(){return accountid;}
    public String getContents(){return contents;}
}
