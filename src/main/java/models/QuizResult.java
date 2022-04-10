package models;

import constants.DatabaseConstants;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class QuizResult {
    private Integer id;
    private Quiz quiz;
    private User user;
    private Integer wrightAnswers;
    private Timestamp timestamp;

    public static class MetaData {
        public static final String TABLE_NAME = "quiz_results";
        public static final String ID = "id";
        public static final String QUIZ_ID = "quiz_id";
        public static final String USER_ID = "user_id";
        public static final String WRIGHT_ANSWERS = "wright_answers";
        public static final String TIMESTAMP = "date_time";
    }

    {
        timestamp = new Timestamp(new Date().getTime());
    }

    public QuizResult() {
    }

    public QuizResult(Integer id, Quiz quiz, User user, Integer wrightAnswers) {
        this.id = id;
        this.quiz = quiz;
        this.user = user;
        this.wrightAnswers = wrightAnswers;
    }

    public QuizResult(Quiz quiz, User user, Integer wrightAnswers) {
        this.quiz = quiz;
        this.user = user;
        this.wrightAnswers = wrightAnswers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getWrightAnswers() {
        return wrightAnswers;
    }

    public void setWrightAnswers(Integer wrightAnswers) {
        this.wrightAnswers = wrightAnswers;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public static void createTable() {

        String raw = "CREATE TABLE %s (\n" +
                "                %s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "                %s INT NOT NULL,\n" +
                "                %s INT NOT NULL,\n" +
                "                %s INT NOT NULL,\n" +
                "                %s TIMESTAMP NOT NULL,\n" +
                "                FOREIGN KEY (%s) REFERENCES %s(%s),\n" +
                "                FOREIGN KEY (%s) REFERENCES %s(%s))";

        String query = String.format(raw, MetaData.TABLE_NAME, MetaData.ID, MetaData.USER_ID, MetaData.QUIZ_ID,
                MetaData.WRIGHT_ANSWERS, MetaData.TIMESTAMP, MetaData.USER_ID,
                Quiz.MetaData.TABLE_NAME, Quiz.MetaData.QUIZ_ID, MetaData.USER_ID, User.MetaData.TABLE_NAME,
                User.MetaData.ID);

        System.out.println(query);
        try {
            String connectionUrl = DatabaseConstants.CONNECTION_URL;
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("models.QuizResult.createTable()");
            System.out.println(b);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean save(Map<Question, String> userAnswers) {

        String raw = "INSERT INTO %s (%s , %s , %s , %s) VALUES \n" +
                "(? , ? , ? , CURRENT_TIMESTAMP);";

        String query = String.format(raw, MetaData.TABLE_NAME, MetaData.USER_ID, MetaData.QUIZ_ID, MetaData.WRIGHT_ANSWERS,
                MetaData.TIMESTAMP);

        System.out.println(query);
        try {
            String connectionUrl = DatabaseConstants.CONNECTION_URL;
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, this.getUser().getId());
            ps.setInt(2, this.getQuiz().getQuizId());
            ps.setInt(3, this.getWrightAnswers());
            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    this.setId(keys.getInt(1));
                    // here we will save details
                    System.out.println(this);
                    return saveQuizResultDetails(userAnswers);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public Integer getNumberOfAttempedQuestions() {
        String raw = "SELECT COUNT(*) FROM %s  WHERE  %s = ?";
        String query = String.format(raw, QuizResultDetails.MetaData.TABLE_NAME
                , QuizResultDetails.MetaData.QUIZ_RESULT_ID
        );


        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(DatabaseConstants.CONNECTION_URL);

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, this.getId());
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

    public static Map<QuizResult , Quiz> getQuizzes(User user){

        Map<QuizResult ,  Quiz > data = new HashMap<>();
        String raw = "SELECT %s.%s ,  " +
                "%s.%s ," +
                " %s.%s as quiz_id , " +
                "%s.%s FROM %s " +
                "JOIN %s on " +
                "%s.%s = %s.%s WHERE %s = ?";

        String query = String.format(raw ,
                MetaData.TABLE_NAME,
                MetaData.ID,
                MetaData.TABLE_NAME,
                MetaData.WRIGHT_ANSWERS,
                Quiz.MetaData.TABLE_NAME,
                Quiz.MetaData.QUIZ_ID,
                Quiz.MetaData.TABLE_NAME,
                Quiz.MetaData.TITLE,
                MetaData.TABLE_NAME,
                Quiz.MetaData.TABLE_NAME,
                MetaData.TABLE_NAME,
                MetaData.QUIZ_ID ,
                Quiz.MetaData.TABLE_NAME,
                Quiz.MetaData.QUIZ_ID ,
                MetaData.USER_ID
        );

        try{
            String connectionUrl = DatabaseConstants.CONNECTION_URL;
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1 , user.getId());
            ResultSet result  =  ps.executeQuery();

            while (result.next()){
                QuizResult quizResult = new QuizResult();
                quizResult.setId(result.getInt(1));
                quizResult.setWrightAnswers(result.getInt(2));

                Quiz quiz = new Quiz();
                quiz.setQuizId(result.getInt(3));
                quiz.setTitle(result.getString(4));

                data.put(quizResult , quiz);
            }


        }catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return data;
    }

    public static List<User> getUsers(Quiz quiz){
        List<User> users = new ArrayList();
        String raw = "SELECT users.%s , users.%s ,\n" +
                "users.%s , users.%s ,\n" +
                "users.%s , users.%s, users.%s \n" +
                "FROM %s  As qr\n" +
                "join %s as users\n" +
                "on users.%s = qr.%s\n" +
                "WHERE %s = ? GROUP by %s";
        String query = String.format(raw ,
                User.MetaData.ID,
                User.MetaData.FIRST_NAME,
                User.MetaData.LAST_NAME,
                User.MetaData.MOBILE_NUMBER,
                User.MetaData.EMAIL,
                User.MetaData.GENDER,
                User.MetaData.COUNTRY,
                MetaData.TABLE_NAME,
                User.MetaData.TABLE_NAME,
                User.MetaData.ID,
                MetaData.USER_ID,
                MetaData.QUIZ_ID,
                MetaData.USER_ID
        );

        try{
            String connectionUrl = DatabaseConstants.CONNECTION_URL;
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1 , quiz.getQuizId());
            ResultSet result  =  ps.executeQuery();

            while (result.next()){
                User user = new User();
                user.setId(result.getInt(1));
                user.setFirstName(result.getString(2));
                user.setLastName(result.getString(3));
                user.setMobileNumber(result.getString(4));
                user.setEmail(result.getString(5));
                user.setGender(result.getString(6).charAt(0));
                user.setCountry(result.getString(7));
                users.add(user);
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return users;

    }

    private boolean saveQuizResultDetails(Map<Question, String> userAnswers) {

        return QuizResultDetails.saveQuizResultDetails(this, userAnswers);
    }

    public static List<QuizResult> getResult(User user){
        Map<QuizResult , Quiz > quizResultQuizMap = getQuizzes(user);
        return  new ArrayList(quizResultQuizMap.keySet());
    }

}
