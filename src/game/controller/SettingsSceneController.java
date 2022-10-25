package game.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SettingsSceneController{

    @FXML
    private Pagination tableMenu;
    @FXML
    private Pagination cueMenu;
    @FXML
    private Pagination modeMenu;
    @FXML
    private Button playButton;

    
   
    @FXML
    public void initialize(){
        tableMenu.setPageFactory(new Callback<Integer,Node>() {
            @Override
            public Node call(Integer pageIndex){
                ImageView imageView = new ImageView(new Image("file:src/game/resources/Table" + (pageIndex+1) + ".png"));
                imageView.setFitWidth(800);
                imageView.setFitHeight(400);
                imageView.setPreserveRatio(true);
                AnchorPane pane = new AnchorPane();
                pane.getChildren().add(imageView);
                return pane;
            }
        });
        cueMenu.setPageFactory(new Callback<Integer,Node>() {
            @Override
            public Node call(Integer pageIndex){
                ImageView imageView = new ImageView(new Image("file:src/game/resources/Cue" + (pageIndex+1) + ".png"));
                imageView.setFitWidth(800);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                AnchorPane pane = new AnchorPane();
                pane.getChildren().add(imageView);
                return pane;
            }
        });
    }
    public static int getCueIndex(){
        return cueMenu.getCurrentPageIndex();
    }
    
    @FXML
    void handleGameSceneChoice(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if(tableMenu.getCurrentPageIndex()==0) {
            stage = (Stage) playButton.getScene().getWindow();
            root = FXMLLoader.load(Main.class.getResource("view/GameScene1.fxml"));
        } else if(tableMenu.getCurrentPageIndex()==1) {
            stage = (Stage) playButton.getScene().getWindow();
            root = FXMLLoader.load(Main.class.getResource("view/GameScene2.fxml"));
        } else if(tableMenu.getCurrentPageIndex()==2) {
            stage = (Stage) playButton.getScene().getWindow();
            root = FXMLLoader.load(Main.class.getResource("view/GameScene3.fxml"));
        } else {
            stage = (Stage) playButton.getScene().getWindow();
            root = FXMLLoader.load(Main.class.getResource("view/GameScene4.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
