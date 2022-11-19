package game.controller;

import game.Main;
import game.utils.Sounds;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class SettingsController{
    @FXML
    private Pane pane;

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
    private Button coinButton;
    @FXML
    private Button menuButtonFromSettings;
    @FXML
    private Label soundLabel;
    @FXML
    private Label splitPlayerLabel;
    @FXML
    private Button soundButton;
    private boolean soundOff = false;
    private ImageView coin;
    Timeline timeline = new Timeline();
    int count = 1;
    int flip = 0;

    private int splitPlayer;

    // CONTROLLER-CONTROLLER COMMUNICATION
    private static SettingsController instance;
    public SettingsController() {
        instance = this;
    }
    public static SettingsController getController() {
        return instance;
    }
   
    @FXML
    public void initialize() throws Exception{
        
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

        playButton.setDisable(true);
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
    }
    @FXML
     void handleCoinButton(ActionEvent event) throws Exception{
        player1Nickname.setDisable(true);
        player2Nickname.setDisable(true);

        Sounds.playSound("CoinSound2");

        KeyFrame frame = new KeyFrame(
            Duration.seconds(0.03), 
            e->{
             if(count<10){
                pane.getChildren().remove(coin);
                coin = new ImageView(new Image("file:src/game/resources/Coin/coin"+flip+".png"));
                coin.setFitWidth(120);
                coin.setFitHeight(120);
                coin.setLayoutX(1200);
                coin.setLayoutY(425);
                coin.setPreserveRatio(true);
                pane.getChildren().add(coin);
                flip++;
                if(flip==10){
                    flip = 1;
                    count++;
                }
            } else {
                timeline.stop();
                splitPlayer();
            }
        });
        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        coinButton.setDisable(true);
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

    public void splitPlayer() {
        int coinToss = (int)(Math.random()*2);
        if(coinToss == 0) {
            splitPlayer = 0;
            if(player1Nickname.getText().isEmpty()) {
                splitPlayerLabel.setText("INIZIA GIOCATORE 1");
            } else {
                splitPlayerLabel.setText("INIZIA " + player1Nickname.getText());
            }
        } else {
            splitPlayer = 1;
            if(player1Nickname.getText().isEmpty()) {
                splitPlayerLabel.setText("INIZIA GIOCATORE 2");
            } else {
                splitPlayerLabel.setText("INIZIA " + player2Nickname.getText());
            }
        }
        playButton.setDisable(false);
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
    public int getSplitPlayer() {
        return splitPlayer;
    }

}
