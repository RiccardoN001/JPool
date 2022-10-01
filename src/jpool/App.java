package jpool;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

 
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
     try{
        Parent root =  FXMLLoader.load(getClass().getResource("grafica.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
     }catch(Exception e){
        e.printStackTrace();
     }
    }

    public static void main(String[] args) {
        launch(args);
    }
}