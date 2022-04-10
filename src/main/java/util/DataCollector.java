package util;

import models.Question;
import models.Quiz;
import models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataCollector {
    public static void main(String[] args) throws IOException, JSONException {
        Quiz.createTable();
        Question.createTable();
        User.createTable();
        readAndSaveQuizesData();
        readAndSaveUsersData();
    }

    public static void readAndSaveQuizesData() throws IOException, JSONException {
        String folderPath = "src/main/java/util/sample_data/quizes";
        File folder = new File(folderPath);
        String[] fileNames = folder.list();

        for (String fileName : fileNames) {
            System.out.println(fileName);


            File file = new File(folder + "/" + fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            System.out.println(stringBuilder);

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray result = jsonObject.getJSONArray("results");
            Quiz quiz = new Quiz();
            List<Question> questions = new ArrayList<>();

            for (int i = 0; i < result.length(); i++) {
                String strObj = result.get(i).toString();
                JSONObject object = new JSONObject(strObj);

                quiz.setTitle(object.getString("category"));
                Question question = new Question();
                question.setQuestion(object.getString("question"));
                JSONArray jsonArrayOfIncorrectAnswer = object.getJSONArray("incorrect_answers");
                question.setOptionA(jsonArrayOfIncorrectAnswer.get(0).toString());
                question.setOptionB(jsonArrayOfIncorrectAnswer.get(1).toString());
                question.setOptionC(jsonArrayOfIncorrectAnswer.get(2).toString());
                question.setOptionD(object.getString("correct_answer"));
                question.setAnswer(object.getString("correct_answer"));
                questions.add(question);
                question.setQuiz(quiz);
                System.out.println(question);
                System.out.println(quiz);
            }
            quiz.save(questions);
        }
    }

    public static void readAndSaveUsersData() throws IOException, JSONException {

        // reading file
        File file = new File("src/main/java/util/users.json");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        System.out.println(stringBuilder);

        // parsing JSON
        JSONArray result = new JSONArray(stringBuilder.toString());

        for (int i = 0; i < result.length(); i++) {
            String strObj = result.get(i).toString();

            JSONObject object = new JSONObject(strObj);
            User user = new User();
            user.setFirstName(object.getString("firstName"));
            user.setLastName(object.getString("lastName"));
            user.setEmail(object.getString("email"));
            user.setPassword(object.getInt("password") + "");
            user.setMobileNumber(object.getInt("phone") + "");


            // saving Object
            user.save();

        }
    }
}



