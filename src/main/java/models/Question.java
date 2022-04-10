package models;

import constants.DatabaseConstants;

import java.sql.*;

public class Question {

    // Properties
    Quiz quiz;
    private Integer questionId;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;

    public static class MetaData {
        public static final String TABLE_NAME = "questions";
        public static final String QUESTION_ID = "id";
        public static final String QUESTION = "question";
        public static final String OPTION_A = "optionA";
        public static final String OPTION_B = "optionB";
        public static final String OPTION_C = "optionC";
        public static final String OPTION_D = "optionD";
        public static final String ANSWER = "answer";
        public static final String QUIZ_ID = "quiz_id";
    }

    // Constructors
    public Question() {
    }

    public Question(Quiz quiz, String question, String optionA, String optionB, String optionC, String optionD, String answer) {
        this.quiz = quiz;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
    }

    // Getters and Setters
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return this.question;
    }

    public static void createTable() {
        String raw = "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(5000), " +
                "%s VARCHAR(2500)," +
                "%s VARCHAR(2500)," +
                "%s VARCHAR(2500)," +
                "%s VARCHAR(2500)," +
                "%s VARCHAR(2500), " +
                "%s INTEGER, " +
                "FOREIGN KEY (%s) REFERENCES %s(%s))";

        String query = String.format(raw, MetaData.TABLE_NAME, MetaData.QUESTION_ID, MetaData.QUESTION,
                MetaData.OPTION_A, MetaData.OPTION_B,
                MetaData.OPTION_C, MetaData.OPTION_D, MetaData.ANSWER, MetaData.QUIZ_ID, MetaData.QUIZ_ID,
                Quiz.MetaData.TABLE_NAME, Quiz.MetaData.QUIZ_ID);

        System.out.println(query);
        try {
            String connectionUrl = DatabaseConstants.CONNECTION_URL;
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("models.Question.createTable()");
            System.out.println(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean save() {
        boolean flag = false;
        String row = "INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String query = String.format(row, MetaData.TABLE_NAME,
                MetaData.QUESTION, MetaData.OPTION_A, MetaData.OPTION_B, MetaData.OPTION_C, MetaData.OPTION_D,
                MetaData.ANSWER, MetaData.QUIZ_ID);
        System.out.println("Actual query - " + query);
        String connectionUrl = DatabaseConstants.CONNECTION_URL;
        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, this.question);
                ps.setString(2, this.optionA);
                ps.setString(3, this.optionB);
                ps.setString(4, this.optionC);
                ps.setString(5, this.optionD);
                ps.setString(6, this.answer);
                ps.setInt(7, this.quiz.getQuizId());

                int i = ps.executeUpdate();
                System.out.println("Updated rows - " + i);
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return flag;
    }
}
