package usersController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import models.Question;

import java.net.URL;
import java.util.ResourceBundle;

public class QuizResultSingleQuestionFXMLController implements Initializable {

    @FXML
    private Label question;
    @FXML
    private Label optionA;
    @FXML
    private Label optionB;
    @FXML
    private Label optionC;
    @FXML
    private Label optionD;

    // not fxml variable
    private Question questionObject;
    private String userAnswer;

    public void setValues(Question questionObject, String userAnswer) {
        this.questionObject = questionObject;
        if (userAnswer == null){
            userAnswer = "";
        }
        else {
            this.userAnswer = userAnswer;
        }
        this.userAnswer = userAnswer;
        setTexts();
    }

    private void setTexts(){
        this.question.setText(this.questionObject.getQuestion());
        this.optionA.setText(this.questionObject.getOptionA());
        this.optionB.setText(this.questionObject.getOptionB());
        this.optionC.setText(this.questionObject.getOptionC());
        this.optionD.setText(this.questionObject.getOptionD());

        settingColors();
    }

    private void settingColors() {
        Label rightAnswer = null;

        if(optionA.getText().trim().equalsIgnoreCase(this.questionObject.getAnswer().trim())){
            rightAnswer = optionA;
        }
        else if(optionB.getText().trim().equalsIgnoreCase(this.questionObject.getAnswer().trim())){
            rightAnswer = optionB;
        }
        else if(optionC.getText().trim().equalsIgnoreCase(this.questionObject.getAnswer().trim())){
            rightAnswer = optionC;
        }
        else if(optionD.getText().trim().equalsIgnoreCase(this.questionObject.getAnswer().trim())){
            rightAnswer = optionD;
        }
        rightAnswer.setTextFill(Color.web("#26ae60"));
        rightAnswer.setText("✔ "+rightAnswer.getText());


        if(!userAnswer.trim().equalsIgnoreCase(this.questionObject.getAnswer().trim())){
            Label userAnswer = null;
            if(optionA.getText().trim().equalsIgnoreCase(this.userAnswer.trim())){
                userAnswer = optionA;
            }
            else if(optionB.getText().trim().equalsIgnoreCase(this.userAnswer.trim().trim())){
                userAnswer = optionB;
            }

            else if(optionC.getText().trim().equalsIgnoreCase(this.userAnswer.trim().trim())){
                userAnswer = optionC;
            }
            else if(optionD.getText().trim().equalsIgnoreCase(this.userAnswer.trim().trim())){
                userAnswer = optionD;
            }

            userAnswer.setTextFill(Color.web("#B83227"));
            userAnswer.setText("✖ " + userAnswer.getText());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
