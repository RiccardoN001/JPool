package game.controller;

import game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsSceneController {

    @FXML
    private Pagination tableMenu;
    @FXML
    private Pagination cueMenu;
    @FXML
    private Pagination modeMenu;

    @FXML
    private Button playButton;

    @FXML //Stavo provando questo ma lo devo riguardare meglio domani
    public VBox createPage(int index){
        ImageView imageView = new ImageView();



        VBox pageBox = new VBox();
        pageBox.getChildren().add(imageView);
        return pageBox;
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
