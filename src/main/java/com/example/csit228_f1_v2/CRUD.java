package com.example.csit228_f1_v2;

import java.sql.*;
import java.util.ArrayList;

public class CRUD {
    public static final String URL = "jdbc:mysql://localhost:3306/dbz";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    static Connection getConnection(){
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return c;
    }
    public static void createTables(){
        try (Connection c = getConnection();
             PreparedStatement statement1 = c.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS `dbz`.`accounts` (" +
                             "`id` INT NOT NULL AUTO_INCREMENT , " +
                             "`username` VARCHAR(30) NOT NULL , " +
                             "`password` VARCHAR(30) NOT NULL , " +
                             "`status` VARCHAR(100) NOT NULL , " +
                             "PRIMARY KEY (`id`)" +
                             ") ENGINE = InnoDB;"
             );
             PreparedStatement statement2 = c.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS `dbz`.`posts` ( " +
                             "`postid` INT NOT NULL AUTO_INCREMENT , " +
                             "`accountid` INT NOT NULL , " +
                             "`contents` VARCHAR(200) NOT NULL , " +
                             "`edited` BOOLEAN NOT NULL default false, " +
                             "PRIMARY KEY (`postid`), " +
                             "FOREIGN KEY (`accountid`) REFERENCES accounts(`id`) ON DELETE CASCADE" +
                             ") ENGINE = InnoDB;"
             )
             ) {
            statement1.execute();
            statement2.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static int createUser(String username, String password) {
        try (Connection c = getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO accounts (username, password, status) VALUES (?,?,?)"
             )) {
            statement.setString(1, username);
            statement.setString(2, hashPassword(password));
            statement.setString(3, " ");
            int rowsInserted = statement.executeUpdate();
            return rowsInserted;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static boolean verifyLogin(String username, String password) {
        password = hashPassword(password);
        try(
                Connection c = getConnection();
                Statement statement = c.createStatement();
        ){
            String query = "SELECT * FROM accounts WHERE username='"+username+"'";
            ResultSet res = statement.executeQuery(query);
            while(res.next()){
                String setpassword = res.getString("password");
                if(password.matches(setpassword)){
                    MainUser.id = res.getInt("id");
                    MainUser.password=password;
                    MainUser.username=res.getString("username");
                    MainUser.status=res.getString("status");
                    return true;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static User getAccount(int id){
        User user = new User(-1,"NULL","NULL");
        try(
                Connection c = getConnection();
                Statement statement = c.createStatement();
        ){
            String query = "SELECT * FROM accounts WHERE id="+id;
            ResultSet res = statement.executeQuery(query);
            if(res.next()){
                user = new User(
                        id,
                        res.getString("username"),
                        res.getString("status")
                );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return user;
    }
    public static int deleteAccount(int id) {
//        int postsDeleted=-1;
//        try(
//                Connection c = getConnection();
//                PreparedStatement statement = c.prepareStatement("DELETE FROM accounts WHERE id=?");
//        ){
//            statement.setInt(1,id);
//            postsDeleted = statement.executeUpdate();
//        } catch(SQLException e){
//            e.printStackTrace();
//        }
//        if(postsDeleted>0){
//            try(
//                    Connection c = getConnection();
//                    PreparedStatement statement = c.prepareStatement("DELETE FROM posts WHERE accountid=?");
//            ){
//                statement.setInt(1,id);
//                postsDeleted = statement.executeUpdate();
//            } catch(SQLException e){
//                e.printStackTrace();
//            }
//        }
//        return postsDeleted;
        int postsDeleted = -1;
        try(
                Connection c = getConnection();
                PreparedStatement deleteUserStatement = c.prepareStatement("DELETE FROM accounts WHERE id=?");
                PreparedStatement deletePostStatement = c.prepareStatement("DELETE FROM posts WHERE accountid=?")
                ){
            c.setAutoCommit(false);
            deleteUserStatement.setInt(1,id);
            deleteUserStatement.execute();
            deletePostStatement.setInt(1,id);
            deletePostStatement.execute();
            c.commit();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return postsDeleted;
    }
    public static void updateAccount(int userid, String newStatus) {
        try(
                Connection c = getConnection();
                PreparedStatement statement = c.prepareStatement("UPDATE accounts SET status=? WHERE id=?");
        ){
            statement.setString(1,newStatus);
            statement.setInt(2,userid);
            int rowsUpdated = statement.executeUpdate();
            //System.out.println("Rows Updated: "+rowsUpdated);
            MainUser.status = newStatus;
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static int createPost(int userID, String text){
        try (Connection c = getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO posts (accountid,contents) VALUES (?,?)"
             )) {
            statement.setInt(1, userID);
            statement.setString(2, text);
            return statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<Post> getPosts(){
        ArrayList<Post> posts = new ArrayList<>();
        try(
                Connection c = getConnection();
                Statement statement = c.createStatement();
        ){
            String query = "SELECT * FROM posts";
            ResultSet res = statement.executeQuery(query);
            while(res.next()){
                int postid = res.getInt("postid");
                int accountid = res.getInt("accountid");
                String contents = res.getString("contents");
                boolean edited = res.getBoolean("edited");
                posts.add(new Post(postid,accountid,contents,edited));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return posts;
    }
    public static void editPost(int postID, String newContents){
        try(
                Connection c = getConnection();
                PreparedStatement statement = c.prepareStatement("UPDATE posts SET contents=?, edited=true WHERE postid=?");
        ){
            statement.setString(1,newContents);
            statement.setInt(2,postID);
            int rowsUpdated = statement.executeUpdate();
            //System.out.println("Posts Updated: "+rowsUpdated);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static boolean deletePost(int postID){
        try(
                Connection c = getConnection();
                PreparedStatement statement = c.prepareStatement("DELETE FROM posts WHERE postid=?");
        ){
            statement.setInt(1,postID);
            int rowsDeleted = statement.executeUpdate();
            return (rowsDeleted>0);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    private static String hashPassword(String pass){
        return ""+pass.hashCode();
    }
}
