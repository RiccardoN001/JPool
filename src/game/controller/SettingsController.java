package game.controller;

import java.lang.ModuleLayer.Controller;

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

public class SettingsController{

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
    private Button menuButtonFromSettings;
    private GameController game = GameController.getController();

    // CONTROLLER-CONTROLLER COMMUNICATION
    private static SettingsController instance;
    public SettingsController() {
        instance = this;
    }
    public static SettingsController getController() {
        return instance;
    }
   
    @FXML
    public void initialize(){
        tableMenu.setPageFactory(new Callback<Integer,Node>() {
            @Override
            public Node call(Integer pageIndex){
                ImageView imageView = new ImageView(new Image("file:src/game/resources/Tables/Table" + (pageIndex+1) + ".png"));
                imageView.setFitWidth(600);
                imageView.setFitHeight(300);
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
                imageView.setFitWidth(600);
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
                imageView.setFitWidth(300);
                imageView.setFitHeight(300);
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
        stage = (Stage) menuButtonFromSettings.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/Menu.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void handleGameSceneChoice(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        stage = (Stage) playButton.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/Game.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("view/style.css").toExternalForm());

        if(tableMenu.getCurrentPageIndex()==0) {
            scene.getStylesheets().add(Main.class.getResource("view/table1.css").toExternalForm());
        } else if(tableMenu.getCurrentPageIndex()==1) {
            scene.getStylesheets().add(Main.class.getResource("view/table2.css").toExternalForm());
        } else if(tableMenu.getCurrentPageIndex()==2) {
            scene.getStylesheets().add(Main.class.getResource("view/table3.css").toExternalForm());
        } else {
            scene.getStylesheets().add(Main.class.getResource("view/table4.css").toExternalForm());
        }

       // stage.setOnHidden(e->game.shutdown());
        
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
