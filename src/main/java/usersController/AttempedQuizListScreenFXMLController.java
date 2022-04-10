package usersController;

import javafx.fxml.FXML;
import usersController.AttempedQuizListItemFXMLController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import listeners.AttempedQuizItemClickListener;
import models.Quiz;
import models.QuizResult;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class AttempedQuizListScreenFXMLController implements Initializable {

    @FXML
    public VBox list;
    @FXML
    public Label total;
    @FXML
    public Label aq;
    @FXML
    public Label ra;
    @FXML
    public Label wa;
    @FXML
    public PieChart attempedChart;
    @FXML
    public PieChart rightWrongChart;

    private User user;

    public void setUser(User user) {
        this.user = user;

        setList();
    }


    private void setList(){
        Map<QuizResult, Quiz> map = QuizResult.getQuizzes(user);
        Set<QuizResult> quizzesData = map.keySet();

        for(QuizResult quizResult : quizzesData){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                    getResource("/AttempedQuizListItemFXML.fxml"));
            try {
                Parent root = fxmlLoader.load();
                AttempedQuizListItemFXMLController attempedQuizListItemFXML = fxmlLoader.getController();
                attempedQuizListItemFXML.setItemClickListener(new AttempedQuizItemClickListener() {
                    @Override
                    public void itemClicked(Integer nof, Integer noa) {

                        int attemped = noa;
                        int nonAtemped = nof - attemped ;
                        int right = quizResult.getWrightAnswers() ;
                        int wrong = attemped - right ;

                        total.setText(nof.toString());
                        aq.setText(attemped + "");
                        ra.setText(right+"");
                        wa.setText((noa - quizResult.getWrightAnswers()) + "");


                        ObservableList<PieChart.Data> attempedData  = attempedChart.getData();
                        attempedData.clear();
                        attempedData.add(new PieChart.Data("Attemped Questions ("+attemped+")" , attemped));
                        attempedData.add(new PieChart.Data("Not Attemped Questions ("+nonAtemped+")" , nonAtemped));


                        ObservableList<PieChart.Data> answerData  = rightWrongChart.getData();
                        answerData.clear();
                        answerData.add(new PieChart.Data("Right Answers ("+right+")" , right));
                        answerData.add(new PieChart.Data("Wrong Answers ("+wrong+")" , wrong));


                    }
                });
                attempedQuizListItemFXML.setData(map.get(quizResult) , quizResult);
                this.list.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
