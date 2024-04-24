package com.example.csit228_f1_v2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Stack;

public class HomeController {
    private Scene prevscene;
    private Scene scene;
    private TextArea taStatus;
    private TextArea postInput;
    private Label lblUsername;
    private ScrollPane spPosts;
    private VBox vbPosts;
    public void setPrevScene(Scene s){prevscene = s;}
    public void setCurrScene(Scene s){
        scene = s;
        taStatus = (TextArea) scene.lookup("#tastatus");
        lblUsername = (Label) scene.lookup("#lblusername");
        spPosts = (ScrollPane) scene.lookup("#spPosts");
        postInput = (TextArea) scene.lookup("#postInput");
    }
    public void setvals(){
        taStatus.setText(MainUser.status);
        lblUsername.setText(MainUser.username);

        vbPosts = new VBox();
        spPosts.setContent(vbPosts);

        ArrayList<Post> posts = CRUD.getPosts();
        for(int i = posts.size()-1; i>=0; i--){
            vbPosts.getChildren().add(displayPost(posts.get(i)));
        }
    }
    public void btnDeletePressed(ActionEvent event){
        CRUD.deleteAccount(MainUser.id);
        MainUser.logOut();
        Stage stage =HelloApplication.thestage;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(prevscene);
        stage.show();
    }
    public void btnLogOutPressed(ActionEvent event){
        MainUser.logOut();
        Stage stage =HelloApplication.thestage;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(prevscene);
        stage.show();
    }
    public void btnSetFavoriteThingPressed(ActionEvent event){
        String newStatus = taStatus.getText();
        CRUD.updateAccount(MainUser.id, newStatus);
        setvals();
    }
    public void btnPublishPost(){
        CRUD.createPost(MainUser.id,postInput.getText());
        setvals();
    }
    private Node displayPost(Post post){
        VBox container = new VBox();
        User user = CRUD.getAccount(post.getAccountid());
        String edited = "";
        if(post.isEdited())
            edited = " - (EDITED POST)";

        HBox header = new HBox();
        Label username = new Label(user.getUsername()+"   ");
        username.setFont(Font.font("Verdana", FontWeight.BOLD,12));
        header.getChildren().add(username);
        header.getChildren().add(new Label(user.getStatus()));
        Label editedTxt = new Label(edited);
        editedTxt.setFont(Font.font("Verdana",FontWeight.NORMAL, FontPosture.ITALIC,12));
        header.getChildren().add(editedTxt);
        container.getChildren().add(header);
        container.getChildren().add(new Label(post.getContents()));
        HBox buttons = new HBox();
        VBox editui = new VBox();
        TextArea taedit = new TextArea(post.getContents());
        editui.getChildren().add(taedit);
        Button confirmEdit = new Button("Confirm Edit");
        confirmEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CRUD.editPost(post.getId(),taedit.getText());
                setvals();
            }
        });
        editui.getChildren().add(confirmEdit);
        editui.setVisible(false);
        editui.setManaged(false);
        container.getChildren().add(editui);
        Button edit = new Button("Edit");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            private boolean shown = false;
            @Override
            public void handle(ActionEvent actionEvent) {
                //System.out.println("EDITED "+post.getId());;
                if(shown) edit.setText("Edit Post");
                else edit.setText("Cancel Edit");
                shown = !shown;
                editui.setVisible(shown);
                editui.setManaged(shown);
            }
        });
        Button delete = new Button("Delete Post");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //System.out.println("DELETED "+post.getId());
                if(CRUD.deletePost(post.getId()))
                    setvals();
            }
        });
        buttons.getChildren().add(edit);
        buttons.getChildren().add(delete);
        if(MainUser.id == user.getId())
            container.getChildren().add(buttons);
        container.setPadding(new Insets(0,0,10,0));
        return container;
    }
}
