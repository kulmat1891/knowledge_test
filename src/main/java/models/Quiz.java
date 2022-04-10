package models;

import constants.DatabaseConstants;

import java.sql.*;
import java.util.*;

public class Quiz {

    // Properties
    private Integer quizId;
    private String title;

    public static class MetaData {
        public static final String TABLE_NAME = "quizes";
        public static final String QUIZ_ID = "quiz_id";
        public static final String TITLE = "title";

    }

    // Constructors
    public Quiz() {
    }

    public Quiz(String title) {
        this.title = title;
    }

    // Getters and Setters
    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }


    // Other Methods
    public static void createTable() {
        String row = "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(500))";
        String query = String.format(row, MetaData.TABLE_NAME, MetaData.QUIZ_ID, MetaData.TITLE);
        System.out.println(query);
        try {
            String connectionUrl = DatabaseConstants.CONNECTION_URL;
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("models.Quiz.createTable()");
            System.out.println(b);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int save() {

        String row = "INSERT INTO %s (%s) values (?)";
        String query = String.format(row, MetaData.TABLE_NAME, MetaData.TITLE);
        String connectionUrl = DatabaseConstants.CONNECTION_URL;
        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);

            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, this.title);
                int i = ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }


    public boolean save(List<Question> questions) {
        boolean flag = true;
        this.quizId = this.save();

        for (Question q : questions) {
            flag = flag && q.save();
            System.out.println(flag);
        }

        return flag;
    }

    public static Map<Quiz, List<Question>> getAll() {
        Map<Quiz, List<Question>> quizes = new HashMap<>();
        Quiz key = null;

        String query = String.format("SELECT %s.%s, %s, %s, %s, %s, %s, %s, %s, " +
                        "%s FROM %s JOIN %s on %s.%s = %s.%s;",
                MetaData.TABLE_NAME, MetaData.QUIZ_ID, MetaData.TITLE, Question.MetaData.QUESTION_ID,
                Question.MetaData.QUESTION, Question.MetaData.OPTION_A, Question.MetaData.OPTION_B,
                Question.MetaData.OPTION_C, Question.MetaData.OPTION_D, Question.MetaData.ANSWER,
                MetaData.TABLE_NAME, Question.MetaData.TABLE_NAME, Question.MetaData.TABLE_NAME, Question.MetaData.QUIZ_ID,
                MetaData.TABLE_NAME, MetaData.QUIZ_ID);
        String connectionUrl = DatabaseConstants.CONNECTION_URL;
        System.out.println(query);
        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);

            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);

                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Quiz temp = new Quiz();
                    temp.setQuizId(result.getInt(1));
                    temp.setTitle(result.getString(2));

                    Question tempQuestion = new Question();
                    tempQuestion.setQuestionId(result.getInt(3));
                    tempQuestion.setQuestion(result.getString(4));
                    tempQuestion.setOptionA(result.getString(5));
                    tempQuestion.setOptionB(result.getString(6));
                    tempQuestion.setOptionC(result.getString(7));
                    tempQuestion.setOptionD(result.getString(8));
                    tempQuestion.setAnswer(result.getString(9));

                    if (key != null && key.equals(temp)) {
                        quizes.get(key).add(tempQuestion);
                    } else {
                        ArrayList<Question> value = new ArrayList<>();
                        value.add(tempQuestion);
                        quizes.put(temp, value);
                    }

                    key = temp;
                }


            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return quizes;
    }

    public static Map<Quiz, Integer> getAllWithQuestionsCount() {
        Map<Quiz, Integer> quizes = new HashMap<>();
        Quiz key = null;

        String query = String.format("SELECT %s , %s.%s, COUNT(*) AS questions_count FROM %s \n" +
                        "INNER JOIN %s ON \n" +
                        "%s.%s = %s.%s\n" +
                        "GROUP BY %s.%s;",
                MetaData.TITLE, MetaData.TABLE_NAME, MetaData.QUIZ_ID, MetaData.TABLE_NAME,
                Question.MetaData.TABLE_NAME, MetaData.TABLE_NAME, MetaData.QUIZ_ID,
                Question.MetaData.TABLE_NAME, Question.MetaData.QUIZ_ID, MetaData.TABLE_NAME,
                MetaData.QUIZ_ID);
        String connectionUrl = DatabaseConstants.CONNECTION_URL;
        System.out.println(query);
        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);

            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Quiz temp = new Quiz();
                    temp.setTitle(result.getString(1));
                    temp.setQuizId(result.getInt(2));
                    int count = result.getInt(3);

                    quizes.put(temp, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return quizes;
    }


    // get questions using quiz
    public List<Question> getQuestions() {
        List <Question> quizes = new ArrayList<>();

        String query = String.format("SELECT  %s, %s, %s, %s, %s, %s, " +
                        "%s FROM %s where %s = ?;",
                Question.MetaData.QUESTION_ID,
                Question.MetaData.QUESTION, Question.MetaData.OPTION_A, Question.MetaData.OPTION_B,
                Question.MetaData.OPTION_C, Question.MetaData.OPTION_D, Question.MetaData.ANSWER,
                Question.MetaData.TABLE_NAME, Question.MetaData.QUIZ_ID);
        String connectionUrl = DatabaseConstants.CONNECTION_URL;
        System.out.println(query);
        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);

            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, this.quizId);
                ResultSet result = ps.executeQuery();

                while (result.next()) {

                    Question tempQuestion = new Question();
                    tempQuestion.setQuestionId(result.getInt(1));
                    tempQuestion.setQuestion(result.getString(2));
                    tempQuestion.setOptionA(result.getString(3));
                    tempQuestion.setOptionB(result.getString(4));
                    tempQuestion.setOptionC(result.getString(5));
                    tempQuestion.setOptionD(result.getString(6));
                    tempQuestion.setAnswer(result.getString(7));
                    tempQuestion.setQuiz(this);
                    quizes.add(tempQuestion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return quizes;
    }

    public Integer getNumberOfQuestions() {
        String raw = "SELECT count(*) from %s WHERE %s = ?";
        String query = String.format(raw, Question.MetaData.TABLE_NAME
                , Question.MetaData.QUIZ_ID);


        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(DatabaseConstants.CONNECTION_URL);

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, this.quizId);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                return result.getInt(1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static Map<Quiz , Integer> getUserCount() {
        Map<Quiz , Integer> data = new HashMap<>();
        String raw = "SELECT quizes.ID , \n" +
                "quizes.title  , \n" +
                "count(*) ,\n" +
                "quiz_results.id\n" +
                "FROM quizes Left Join \n" +
                "quiz_results on \n" +
                "quiz_results.quiz_id = quizes.ID \n" +
                "GROUP BY quizes.id";
        String query = String.format(
                raw,
                MetaData.TABLE_NAME,
                MetaData.QUIZ_ID,

                MetaData.TABLE_NAME,
                MetaData.TITLE,

                QuizResult.MetaData.TABLE_NAME ,
                QuizResult.MetaData.ID,

                MetaData.TABLE_NAME,
                QuizResult.MetaData.TABLE_NAME,

                QuizResult.MetaData.TABLE_NAME,
                QuizResult.MetaData.QUIZ_ID,

                MetaData.TABLE_NAME,
                MetaData.QUIZ_ID,

                MetaData.TABLE_NAME,
                MetaData.QUIZ_ID

        );
        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(DatabaseConstants.CONNECTION_URL);

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizId(result.getInt(1));
                quiz.setTitle(result.getString(2));
                int count = 0 ;
                Integer resultID = result.getInt(4);
                if(resultID > 0){
                    count = result.getInt(3);
                }
                data.put(quiz , count);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Quiz)) {
            return false;
        }

        Quiz quizOfObject = (Quiz) obj;

        if (this.quizId == quizOfObject.quizId) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, title);
    }
}
