package usersController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import listeners.NewScreenListener;
import models.Quiz;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class QuizListController implements Initializable {


    @FXML
    private FlowPane quizListContainer;

    Map<Quiz, Integer> quizes = null;

    private NewScreenListener screenListener;

    private Set<Quiz> keys;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void setScreenListener(NewScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    public void setCards(){
        for (Quiz q : keys) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/QuizCardLayoutFXML.fxml"));

            try {
                Node node = fxmlLoader.load();
                QuizCardLayoutFXMLController quizCardLayoutFXMLController = fxmlLoader.getController();
                quizCardLayoutFXMLController.setQuiz(q);
                quizCardLayoutFXMLController.setUser(this.user);
                quizCardLayoutFXMLController.setCountOfQuestions("Count of questions : " + quizes.get(q));
                quizCardLayoutFXMLController.setScreenListener(this.screenListener);
                quizListContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        quizes = Quiz.getAllWithQuestionsCount();
        keys = quizes.keySet();


    }
}
