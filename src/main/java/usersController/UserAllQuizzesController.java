package usersController;

import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import listeners.NewScreenListener;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserAllQuizzesController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private StackPane stackPanel;

    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void setUser(User user) {
        this.user = user;
        addQuizListScreen();
    }

    private void addScreenToStackPane(Node node) {
        this.stackPanel.getChildren().add(node);
    }


    private void addQuizListScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                getResource("/QuizListFXML.fxml"));

        try {
            Node node = fxmlLoader.load();
            QuizListController quizListController = fxmlLoader.getController();
            quizListController.setUser(this.user);
            quizListController.setScreenListener(new NewScreenListener() {
                @Override
                public void changeScreen(Node node) {
                    addScreenToStackPane(node);
                }

                @Override
                public void removeTopScreen() {
                    stackPanel.getChildren().remove(stackPanel.getChildren().size() - 1);
                }

                @Override
                public void handle(Event event) {

                }
            });
            quizListController.setCards();
            stackPanel.getChildren().add(node);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent actionEvent) {
        ObservableList<Node> nodes = this.stackPanel.getChildren();
        if (nodes.size() == 1) {
            return;
        }
        this.stackPanel.getChildren().remove(nodes.size() - 1);
    }
}
