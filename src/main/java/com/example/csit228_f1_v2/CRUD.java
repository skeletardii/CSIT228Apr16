package com.example.csit228_f1_v2;

import java.sql.*;

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
    public static int createUser(String username, String password) {
        try (Connection c = getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO accounts (username, password, favorite) VALUES (?,?,?)"
             )) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, "Nothing");
            int rowsInserted = statement.executeUpdate();
            return rowsInserted;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static boolean verifyLogin(String username, String password) {
        try(
                Connection c = getConnection();
                Statement statement = c.createStatement();
        ){
            String query = "SELECT * FROM accounts WHERE username='"+username+"'";
            ResultSet res = statement.executeQuery(query);
            while(res.next()){
                String setpassword = res.getString("password");
                if(password.matches(setpassword)){
                    User.id = res.getInt("id");
                    User.password=password;
                    User.username=res.getString("username");
                    return true;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static int deleteAccount(int id) {
        try(
                Connection c = getConnection();
                PreparedStatement statement = c.prepareStatement("DELETE FROM accounts WHERE id=?");
        ){
            statement.setInt(1,id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
    public static void updateAccount(int id, String newname) {
        try(
                Connection c = getConnection();
                PreparedStatement statement = c.prepareStatement("UPDATE accounts SET username=? WHERE id=?");
        ){
            statement.setString(1,newname);
            statement.setInt(2,id);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows Updated: "+rowsUpdated);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
