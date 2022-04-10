package controllers;

import java.net.URL;
import java.util.*;

import com.sun.source.doctree.SeeTree;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.util.Duration;
import models.Question;
import models.Quiz;
import org.controlsfx.control.Notifications;


public class AddQuizFXMLController implements Initializable {

    @FXML
    private TextField enterQuizTitle;
    @FXML
    private TextArea question;
    @FXML
    private TextArea optionA;
    @FXML
    private TextArea optionB;
    @FXML
    private TextArea optionC;
    @FXML
    private TextArea optionD;
    @FXML
    private RadioButton optionAradio;
    @FXML
    private RadioButton optionBradio;
    @FXML
    private RadioButton optionCradio;
    @FXML
    private RadioButton optionDradio;
    @FXML
    private Button addNewQuestionButton;
    @FXML
    private Button submitQuizButton;
    @FXML
    private Button setQuizTitleButton;
    @FXML
    private TreeView treeView;

    private ToggleGroup radioButtonGroup;

    //my variable
    private Quiz quiz = null;
    private ArrayList<Question> questions = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        radioButtonSetup();
        renderTreeView();
    }

    private void renderTreeView() {
        Map<Quiz, List<Question>> data = Quiz.getAll();
        Set<Quiz> quizes = data.keySet();

        TreeItem root = new TreeItem("quizes");
        for (Quiz q : quizes) {
            TreeItem quizTreeItem = new TreeItem(q);

            List<Question> questions = data.get(q);
            for (Question question : questions) {
                TreeItem questionTreeItem = new TreeItem(question);
                questionTreeItem.getChildren().add(new TreeItem("A : " + question.getOptionA()));
                questionTreeItem.getChildren().add(new TreeItem("B : " + question.getOptionB()));
                questionTreeItem.getChildren().add(new TreeItem("C : " + question.getOptionC()));
                questionTreeItem.getChildren().add(new TreeItem("D : " + question.getOptionD()));
                questionTreeItem.getChildren().add(new TreeItem("Answer : " + question.getAnswer()));
                quizTreeItem.getChildren().add(questionTreeItem);
            }

            quizTreeItem.setExpanded(true);
            root.getChildren().add(quizTreeItem);
        }

        root.setExpanded(true);
        this.treeView.setRoot(root);
    }

    private void radioButtonSetup() {
        radioButtonGroup = new ToggleGroup();
        optionAradio.setToggleGroup(radioButtonGroup);
        optionBradio.setToggleGroup(radioButtonGroup);
        optionCradio.setToggleGroup(radioButtonGroup);
        optionDradio.setToggleGroup(radioButtonGroup);

    }

    private boolean validateFields() {

        if (quiz == null) {
            Notifications.create()
                    .title("Quiz")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .hideAfter(Duration.millis(3000))
                    .text("Enter Quiz Title")
                    .showWarning();

            return false;
        }

        String quest = this.question.getText();
        String opA = this.optionA.getText();
        String opB = this.optionB.getText();
        String opC = this.optionC.getText();
        String opD = this.optionD.getText();

        Toggle selectedRadioButton = radioButtonGroup.getSelectedToggle();
        System.out.println(selectedRadioButton);

        if (quest.trim().isBlank() || opA.trim().isBlank() || opB.trim().isBlank()
                || opC.trim().isBlank() || opD.trim().isBlank()) {

            Notifications.create()
                    .title("Question")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .hideAfter(Duration.millis(3000))
                    .text("All Fields Are Required \n Fields: Question, option A, option B, option C, option D")
                    .showWarning();

            return false;

        } else {
            if (selectedRadioButton == null) {

                Notifications.create()
                        .title("Question")
                        .darkStyle()
                        .position(Pos.TOP_RIGHT)
                        .hideAfter(Duration.millis(3000))
                        .text("Select Wright Answer")
                        .showWarning();

                return false;
            } else {
                return true;
            }
        }
    }

    @FXML
    private void setQuizTitle(ActionEvent event) {

        String title = enterQuizTitle.getText();
        if (title.trim().isBlank()) {
            Notifications.create()
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .title("Quiz Title")
                    .text("Enter valid Quiz Title")
                    .hideAfter(Duration.millis(3000))
                    .showWarning();

        } else {
            enterQuizTitle.setEditable(false);
            System.out.println("Save Title");
            this.quiz = new Quiz(title);
        }
    }

    @FXML
    private void addNextQuestion(ActionEvent event) {
        addQuestions();
    }

    private boolean addQuestions() {
        boolean valid = validateFields();
        Question question = new Question();
        if (valid) {
            //save
            question.setOptionA(optionA.getText().trim());
            question.setOptionB(optionB.getText().trim());
            question.setOptionC(optionC.getText().trim());
            question.setOptionD(optionD.getText().trim());

            Toggle selected = radioButtonGroup.getSelectedToggle();
            String ans = null;

            if (selected == optionAradio) {
                ans = optionA.getText().trim();
            } else if (selected == optionBradio) {
                ans = optionB.getText().trim();
            } else if (selected == optionCradio) {
                ans = optionC.getText().trim();
            } else if (selected == optionDradio) {
                ans = optionD.getText().trim();
            }
            question.setAnswer(ans);
            question.setQuestion(this.question.getText().trim());


            this.question.clear();
            optionA.clear();
            optionB.clear();
            optionC.clear();
            optionD.clear();

            questions.add(question);

            question.setQuiz(quiz);


            System.out.println("Save Question");
            System.out.println(questions);
            System.out.println(quiz);

        }
        return valid;
    }

    @FXML
    private void submitQuiz(ActionEvent event) {
        boolean flag = addQuestions();
        if (flag) {
            flag = quiz.save(questions);
            if (flag) {
                //success
                Notifications.create()
                        .darkStyle()
                        .position(Pos.CENTER)
                        .title("Success")
                        .text("Quiz Successfully Saves")
                        .hideAfter(Duration.millis(3000))
                        .showInformation();
            } else {
                //error
                Notifications.create()
                        .darkStyle()
                        .position(Pos.TOP_RIGHT)
                        .title("Fail")
                        .text("Can't Save Quiz, Try Again")
                        .hideAfter(Duration.millis(3000))
                        .showError();
            }
        }
    }
}
