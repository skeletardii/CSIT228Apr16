package com.example.csit228_f1_v2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

public class HomeController {
    public void btnDeletePressed(){
        CRUD.deleteAccount(User.id);
    }
    public void btnLogOutPressed(){
        User.logOut();
//        Parent p = null;
//        try {
//            p = FXMLLoader.load(getClass().getResource("homepage.fxml"));
//            Scene s = new Scene(p);
//            stage.setScene(s);
//            stage.show();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }
    public void btnSetFavoriteThingPressed(){
        CRUD.updateAccount(User.id, User.username+" is favorite");
    }
}
