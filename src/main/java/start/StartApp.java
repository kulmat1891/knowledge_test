package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.*;


public class StartApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/AdminLogin.fxml"));
        createTables();
//        Parent root = FXMLLoader.load(getClass().getResource("/AdminHomePageFXML.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

    }
    private void createTables(){
        Quiz.createTable();
        Question.createTable();
        User.createTable();
        QuizResult.createTable();
        QuizResultDetails.createTable();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
