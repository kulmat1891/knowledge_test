package usersController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressCircleFXMLController implements Initializable {

    @FXML
    private Circle circle;
    @FXML
    private Label number;

    public void setNumber(Integer number) {
        this.number.setText(number.toString());
    }

    public void setDefaultColor(){
        circle.setFill(Color.web("#758283"));
        number.setTextFill(Color.web("#CAD5E2"));
    }

    public void setCurrentQuestionColor(){
        circle.setFill(Color.web("#242B2E"));
        number.setTextFill(Color.web("#CAD5E2"));
    }

    public void setWrongAnswerColor(){
        circle.setFill(Color.web("#B4161B"));
        number.setTextFill(Color.web("#CAD5E2"));
    }

    public void setWrightAnswerColor(){
        circle.setFill(Color.web("#3DBE29"));
        number.setTextFill(Color.web("#CAD5E2"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
