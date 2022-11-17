package game.controller;

import game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    @FXML
    private Label soundLabel;
    @FXML
    private Button soundButton;
    private boolean soundOff = false;

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
                imageView.setFitWidth(500);
                imageView.setFitHeight(250);
                imageView.setPreserveRatio(true);
                StackPane pane = new StackPane();
                pane.getChildren().add(imageView);
                StackPane.setAlignment(imageView, Pos.CENTER);
                return pane;
            }
        });
        cueMenu.setPageFactory(new Callback<Integer,Node>() {
            @Override
            public Node call(Integer pageIndex){
                ImageView imageView = new ImageView(new Image("file:src/game/resources/Cues/Cue" + (pageIndex+1) + ".png"));
                imageView.setFitWidth(500);
                imageView.setFitHeight(250);
                imageView.setPreserveRatio(true);
                StackPane pane = new StackPane();
                pane.getChildren().add(imageView);
                StackPane.setAlignment(imageView, Pos.CENTER);
                return pane;
            }
        });
        modeMenu.setPageFactory(new Callback<Integer,Node>() {
            @Override
            public Node call(Integer pageIndex){
                ImageView imageView = new ImageView(new Image("file:src/game/resources/Modes/Mode"  + (pageIndex+1) + ".png"));
                imageView.setFitWidth(250);
                imageView.setFitHeight(250);
                imageView.setPreserveRatio(true);
                StackPane pane = new StackPane();
                pane.getChildren().add(imageView);
                StackPane.setAlignment(imageView, Pos.CENTER);
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
    void handleSoundButton(ActionEvent event) throws Exception{
        if(!soundOff){
            soundOff = true;
            soundLabel.setText("OFF");
        }
        else if(soundOff){
            soundOff = false;
            soundLabel.setText("ON");
        }
        System.out.println("Settings "+ soundOff);
    }
    
    @FXML
    void handlePlayButton(ActionEvent event) throws Exception {
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

        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> System.exit(0));
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
    public boolean getSoundOff(){
        return this.soundOff;
    }

}
