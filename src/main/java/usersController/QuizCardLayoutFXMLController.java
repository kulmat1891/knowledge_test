package usersController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import listeners.NewScreenListener;
import models.Quiz;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuizCardLayoutFXMLController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label countOfQuestions;
    @FXML
    private Button startButton;

    private Quiz quiz;

    private NewScreenListener screenListener;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuiz (Quiz quiz){
        this.quiz = quiz;
        this.title.setText(this.quiz.getTitle());
    }

    public void setScreenListener(NewScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    @FXML
    private void startQuiz(ActionEvent actionEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/QuestionScreenFXML.fxml"));

        try {
            Node node = fxmlLoader.load();
            QuestionScreenFXMLController questionScreenFXMLController = fxmlLoader.getController();
            questionScreenFXMLController.setUser(this.user);
            questionScreenFXMLController.setQuiz(this.quiz);
            questionScreenFXMLController.setScreenListener(this.screenListener);
            this.screenListener.changeScreen(node);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setCountOfQuestions(String value) {
        this.countOfQuestions.setText(value);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
