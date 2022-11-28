package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("resources/gui/def/Menu.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(getClass().getResource("resources/gui/style/standard.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:src/game/resources/images/Menu/Logo.png"));
        stage.setTitle("JPool");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
