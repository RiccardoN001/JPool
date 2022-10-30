package game.controller;

import game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    private TextField player1Nickname;
    @FXML
    private TextField player2Nickname;
    
    @FXML
    private Button playButton;
    @FXML
    private Button backButton;

    // CONTROLLER-CONTROLLER COMMUNICATION
    private static SettingsSceneController instance;
    public SettingsSceneController() {
        instance = this;
    }
    public static SettingsSceneController getSettingsSceneController() {
        return instance;
    }
   
    @FXML
    public void initialize(){
        tableMenu.setPageFactory(new Callback<Integer,Node>() {
            @Override
            public Node call(Integer pageIndex){
                ImageView imageView = new ImageView(new Image("file:src/game/resources/Tables/Table" + (pageIndex+1) + ".png"));
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
                ImageView imageView = new ImageView(new Image("file:src/game/resources/Cues/Cue" + (pageIndex+1) + ".png"));
                imageView.setFitWidth(800);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                AnchorPane pane = new AnchorPane();
                pane.getChildren().add(imageView);
                return pane;
            }
        });
        modeMenu.setPageFactory(new Callback<Integer,Node>() {
            @Override
            public Node call(Integer pageIndex){
                ImageView imageView = new ImageView(new Image("file:src/game/resources/Modes/Mode"  + (pageIndex+1) + ".png"));
                imageView.setFitWidth(290);
                imageView.setFitHeight(240);
                imageView.setPreserveRatio(true);
                AnchorPane pane = new AnchorPane();
                pane.getChildren().add(imageView);
                return pane;
            }
        });
    }

    @FXML
    void handleMenuFromSettings(ActionEvent event) throws Exception{
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) backButton.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/MenuScene.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().addAll(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
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
        scene.getStylesheets().addAll(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public int cueMenuIndex () {
        return this.cueMenu.getCurrentPageIndex();
    }

    public int modeMenuIndex() {
        return this.modeMenu.getCurrentPageIndex();
    }

    public String getP1Nickname() {
        return this.player1Nickname.getText();
    }

    public String getP2Nickname() {
        return this.player2Nickname.getText();
    }

}
