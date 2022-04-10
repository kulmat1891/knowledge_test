package start;

import models.*;
import org.json.JSONException;
import util.DataCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Test {


    public static void main(String[] args) throws Exception {

//        Quiz.createTable();
//        Question.createTable();
//        User.createTable();
//        QuizResult.createTable();
//        QuizResultDetails.createTable();
//        DataCollector.readAndSaveQuizesData();
//        DataCollector.readAndSaveUsersData();
//        totalSec = 75;

    }
}

//        CREATE TABLE quiz_result_details (
//  id INT NOT NULL PRIMARY KEY,
//  quiz_result_id INT NOT NULL,
//  question_id INT NOT NULL,
//  wright_answer VARCHAR(1000) NOT NULL,
//  FOREIGN KEY (quiz_result_id) REFERENCES quiz_results(quiz_result_id),
//  FOREIGN KEY (question_id) REFERENCES questions(id));