package usersController;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import listeners.NewScreenListener;
import models.Question;
import models.Quiz;
import models.QuizResult;
import models.User;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class QuestionScreenFXMLController implements Initializable {

    private class QuestionsObservable {
        Property<String> question = new SimpleStringProperty();
        Property<String> optionA = new SimpleStringProperty();
        Property<String> optionB = new SimpleStringProperty();
        Property<String> optionC = new SimpleStringProperty();
        Property<String> optionD = new SimpleStringProperty();
        Property<String> answer = new SimpleStringProperty();

        public void setQuestion(Question question) {
            this.question.setValue(question.getQuestion());
            this.optionA.setValue(question.getOptionA());
            this.optionB.setValue(question.getOptionB());
            this.optionC.setValue(question.getOptionC());
            this.optionD.setValue(question.getOptionD());
            this.answer.setValue(question.getAnswer());
        }
    }


    @FXML
    private Label title;
    @FXML
    private Label time;
    @FXML
    private Label question;
    @FXML
    private RadioButton optionA;
    @FXML
    private RadioButton optionB;
    @FXML
    private RadioButton optionC;
    @FXML
    private RadioButton optionD;
    @FXML
    private ToggleGroup options;
    @FXML
    private Button nextQuestionButton;
    @FXML
    private Button submitQuizButton;
    @FXML
    private FlowPane progressPane;

    // listeners
    private NewScreenListener screenListener;

    // NOT FXML FIELDS
    private Quiz quiz;
    private List<Question> questionList;
    private Question currentQuestion;
    int currentIndex = 0;
    private QuestionsObservable questionsObservable;
    private Map<Question, String> userAnswers = new HashMap<>();
    private Integer countOfWrightAnswers = 0;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void setScreenListener(NewScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    // timer fields
    private long min, sec, hr, totalSec = 0;
    private Timer timer;

    // METHODS AND CONSTRUCTOR
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.title.setText(this.quiz.getTitle());
        this.getData();
    }

    private String format(long value) {
        if (value < 10) {
            return 0 + "" + value;
        }

        return value + "";
    }

    public void convertTime() {
        min = TimeUnit.SECONDS.toMinutes(totalSec);
        sec = totalSec - (min * 60);
        hr = TimeUnit.MINUTES.toHours(min);
        min = min - (hr * 60);

        time.setText(format(hr) + ":" + format(min) + ":" + format(sec));

        totalSec--;
    }

    private void setTimer() {
        totalSec = this.questionList.size() * 120;
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        convertTime();
                        if (totalSec <= 0) {
                            timer.cancel();
                            time.setText("00:00:00");
                            // saving data to database
                            submitQuiz(null);
                            Notifications.create()
                                    .title("Error")
                                    .text("Time is over...")
                                    .position(Pos.BOTTOM_RIGHT)
                                    .showError();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void getData() {
        if (quiz != null) {
            this.questionList = quiz.getQuestions();
            Collections.shuffle(this.questionList);
            renderProgress();
            setNextQuestion();
            setTimer();
        }
    }

    private void renderProgress() {

        for (int i = 0; i < this.questionList.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgressCircleFXML.fxml"));
            try {
                Node node = fxmlLoader.load();
                ProgressCircleFXMLController progressCircleFXMLController = fxmlLoader.getController();
                progressCircleFXMLController.setNumber(i + 1);
                progressCircleFXMLController.setDefaultColor();
                progressPane.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void nextQuestion(ActionEvent event) {
        boolean isWright = false;
        {
            // checking answer
            RadioButton selectedButton = (RadioButton) options.getSelectedToggle();
            String userAnswer = selectedButton.getText();
            String wrightAnswer = this.currentQuestion.getAnswer();
            if (userAnswer.trim().equalsIgnoreCase(wrightAnswer.trim())) {
                isWright = true;
                this.countOfWrightAnswers++;
            }

            // saving Answer to HashMap
            userAnswers.put(this.currentQuestion, userAnswer);

        }
        Node circleNode = this.progressPane.getChildren().get(currentIndex - 1);
        ProgressCircleFXMLController controller = (ProgressCircleFXMLController) circleNode.getUserData();
        if (isWright) {
            controller.setWrightAnswerColor();
        } else {
            controller.setWrongAnswerColor();
        }
        this.setNextQuestion();
    }

    private void setNextQuestion() {
        if (!(currentIndex >= questionList.size())) {

            {
                // chaning the color
                Node circleNode = this.progressPane.getChildren().get(currentIndex);
                ProgressCircleFXMLController controller = (ProgressCircleFXMLController) circleNode.getUserData();
                controller.setCurrentQuestionColor();
            }


            this.currentQuestion = this.questionList.get(currentIndex);
            List<String> options = new ArrayList<>();
            options.add(this.currentQuestion.getOptionA());
            options.add(this.currentQuestion.getOptionB());
            options.add(this.currentQuestion.getOptionC());
            options.add(this.currentQuestion.getOptionD());
            Collections.shuffle(options);

            this.currentQuestion.setOptionA(options.get(0));
            this.currentQuestion.setOptionB(options.get(1));
            this.currentQuestion.setOptionC(options.get(2));
            this.currentQuestion.setOptionD(options.get(3));

//            this.question.setText(this.currentQuestion.getQuestion());
//            this.optionA.setText(options.get(0));
//            this.optionB.setText(options.get(1));
//            this.optionC.setText(options.get(2));
//            this.optionD.setText(options.get(3));

            this.questionsObservable.setQuestion(this.currentQuestion);
            currentIndex++;
        } else {
            hideNextQuestionButton();
            showSubmitQuizButton();
        }
    }

    private void hideNextQuestionButton() {
        this.nextQuestionButton.setVisible(false);
    }

    private void showNextQuestionButton() {
        this.nextQuestionButton.setVisible(true);
    }

    private void hideSubmitQuizButton() {
        this.submitQuizButton.setVisible(false);
    }

    private void showSubmitQuizButton() {
        this.submitQuizButton.setVisible(true);
    }

    private void bindFields() {
        this.question.textProperty().bind(this.questionsObservable.question);
        this.optionA.textProperty().bind(this.questionsObservable.optionA);
        this.optionB.textProperty().bind(this.questionsObservable.optionB);
        this.optionC.textProperty().bind(this.questionsObservable.optionC);
        this.optionD.textProperty().bind(this.questionsObservable.optionD);
    }

    @FXML
    private void submitQuiz(ActionEvent event) {
        System.out.println(this.userAnswers);
        System.out.println(this.user);
        QuizResult quizResult = new QuizResult(this.quiz, user, countOfWrightAnswers);
        boolean result = quizResult.save(this.userAnswers);
        if (result) {
            Notifications.create()
                    .title("Message")
                    .text("You Succesfully Attemped Quiz...")
                    .position(Pos.BOTTOM_RIGHT)
                    .showInformation();

            timer.cancel();
            openResultScreen();
        } else {
            Notifications.create()
                    .title("Error")
                    .text("Something going wrong...")
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
        }
    }

    private void openResultScreen(){

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/QuizResultFXML.fxml"));

        try {
            Node node = fxmlLoader.load();
            QuizResultFXMLController controller = fxmlLoader.getController();
            controller.setValues(this.userAnswers , countOfWrightAnswers , quiz , questionList);
            this.screenListener.removeTopScreen();
            this.screenListener.changeScreen(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.showNextQuestionButton();
        this.hideSubmitQuizButton();

        this.questionsObservable = new QuestionsObservable();
        bindFields();
    }
}
