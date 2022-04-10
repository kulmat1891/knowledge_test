package usersController;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import models.Question;
import models.Quiz;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class QuizResultFXMLController implements Initializable {

    @FXML
    private PieChart attemptedChart;
    @FXML
    private PieChart scoreChart;
    @FXML
    private VBox questionsContainer;

    // not fxml variable
    private Map <Question , String> userAnswers;
    private Integer numberOfWrightAnswers = 0;
    private Quiz quiz;
    private List <Question> questionList;
    private Integer notAttemped = 0;
    private Integer attemped = 0;


    public void setValues(Map<Question, String> userAnswers, Integer numberOfWrightAnswers,
                          Quiz quiz, List<Question> questionList) {
        this.userAnswers = userAnswers;
        this.numberOfWrightAnswers = numberOfWrightAnswers;
        this.quiz = quiz;
        this.questionList = questionList;

        this.attemped = this.userAnswers.keySet().size();
        this.notAttemped = this.questionList.size() - attemped;

        setValuesToChart();
        renderQuestions();
    }

    private void renderQuestions(){
        for (int i = 0; i < this.questionList.size(); i++){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/QuizResultSingleQuestionFXML.fxml"));

            try {
                Node node = fxmlLoader.load();
                QuizResultSingleQuestionFXMLController controller = fxmlLoader.getController();
                controller.setValues(this.questionList.get(i) , this.userAnswers.get(this.questionList.get(i)));
                questionsContainer.getChildren().add(node);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setValuesToChart(){
        ObservableList <PieChart.Data> attempedData = this.attemptedChart.getData();
        attempedData.add(new PieChart.Data(String.format("Attemped (%d)", this.attemped) , this.attemped));
        attempedData.add(new PieChart.Data(String.format("Not Attemped (%d)", this.notAttemped) , this.notAttemped));

        ObservableList <PieChart.Data> scoreChartData = this.scoreChart.getData();
        scoreChartData.add(new PieChart.Data
                (String.format("Wright Answers (%d)", this.numberOfWrightAnswers) , this.numberOfWrightAnswers));
        scoreChartData.add(new PieChart.Data
                (String.format("Wrong Answers (%d)", this.attemped - this.numberOfWrightAnswers) ,
                        this.attemped - this.numberOfWrightAnswers));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
