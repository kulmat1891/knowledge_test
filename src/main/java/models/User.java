package models;

import constants.DatabaseConstants;
import exceptions.LoginException;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;

public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private Character gender;
    private String country;
    private String email;
    private String password;

    public static class MetaData {
        public static final String TABLE_NAME = "users";
        public static final String ID = "id";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String MOBILE_NUMBER = "mobileNumber";
        public static final String GENDER = "gender";
        public static final String COUNTRY = "country";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }

    // Constructors
    public User() {
    }

    public User(String firstName, String lastName, String phoneNumber, Character gender, String country, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = phoneNumber;
        this.gender = gender;
        this.country = country;
        this.email = email;
        this.password = password;
    }

    public User(Integer id, String firstName, String lastName, String phoneNumber, Character gender, String country, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = phoneNumber;
        this.gender = gender;
        this.country = country;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String phoneNumber) {
        this.mobileNumber = phoneNumber;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", gender=" + gender +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    // create table for user
    public static void createTable() {

        String row = "CREATE TABLE IF NOT EXISTS %s ( \n" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "%s VARCHAR(150),\n" +
                "%s VARCHAR(150),\n" +
                "%s VARCHAR (20),\n" +
                "%s CHAR,\n" +
                "%s VARCHAR(80),\n" +
                "%s VARCHAR(100),\n" +
                "%s VARCHAR(100))";
        String query = String.format(row, MetaData.TABLE_NAME, MetaData.ID, MetaData.FIRST_NAME, MetaData.LAST_NAME,
                MetaData.MOBILE_NUMBER, MetaData.GENDER, MetaData.COUNTRY, MetaData.EMAIL, MetaData.PASSWORD);

        System.out.println(query);

        try {
            String connectionUrl = DatabaseConstants.CONNECTION_URL;
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("models.Users.createTable()");
            System.out.println(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // save User
    public User save() {
        String row = "INSERT INTO %s (%s, %s, %s, %s, %s, %s , %s) VALUES\n" +
                "(?, ?, ?, ?, ?, ?, ?);";
        String query = String.format(row, MetaData.TABLE_NAME, MetaData.FIRST_NAME, MetaData.LAST_NAME, MetaData.MOBILE_NUMBER,
                MetaData.GENDER, MetaData.COUNTRY, MetaData.EMAIL, MetaData.PASSWORD);

        String connectionUrl = DatabaseConstants.CONNECTION_URL;
        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, this.firstName);
                ps.setString(2, this.lastName);
                ps.setString(3, this.mobileNumber);
                ps.setString(4, String.valueOf(this.gender));
                ps.setString(5, this.country);
                ps.setString(6, this.email);
                ps.setString(7, this.password);

                int i = ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    this.id = keys.getInt(1);
                }
                System.out.println("Updated rows - " + i);
                return this;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // get all users method
    public static ArrayList<User> getAllUser() {
        ArrayList<User> userArrayList = new ArrayList<>();

        String query = String.format("SELECT %s, %s, %s, %s, %s, %s, %s, %s from %s;",
                MetaData.ID, MetaData.FIRST_NAME, MetaData.LAST_NAME, MetaData.MOBILE_NUMBER,
                MetaData.GENDER, MetaData.COUNTRY, MetaData.EMAIL, MetaData.PASSWORD, MetaData.TABLE_NAME);

        String connectionUrl = DatabaseConstants.CONNECTION_URL;
        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                PreparedStatement ps = connection.prepareStatement(query);

                ResultSet result = ps.executeQuery();

                while (result.next()) {

                    User user = new User();
                    user.setId(result.getInt(1));
                    user.setFirstName(result.getString(2));
                    user.setLastName(result.getString(3));
                    user.setMobileNumber(result.getString(4));
                    user.setGender(result.getString(5).charAt(0));
                    user.setCountry(result.getString(6));
                    user.setEmail(result.getString(7));
                    user.setPassword(result.getString(8));

                    userArrayList.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userArrayList;
    }

    // Checked user already registered or not
    public boolean isExists() {
        String query = String.format("SELECT * from %s where %s = ?",
                MetaData.TABLE_NAME, MetaData.EMAIL);

        String connectionUrl = DatabaseConstants.CONNECTION_URL;
        try {
            Class.forName(DatabaseConstants.DRIVER_CLASS);
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, this.email);

                ResultSet result = ps.executeQuery();
                if (result.next()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void login() throws SQLException, ClassNotFoundException, LoginException {
        String query = String.format("SELECT %s, %s, %s, %s, %s, %s from %s where %s = ? AND %s = ?;",
                MetaData.ID, MetaData.FIRST_NAME, MetaData.LAST_NAME,
                MetaData.MOBILE_NUMBER, MetaData.GENDER, MetaData.COUNTRY,
                MetaData.TABLE_NAME, MetaData.EMAIL, MetaData.PASSWORD);

        System.out.println(query);
        String connectionUrl = DatabaseConstants.CONNECTION_URL;
        Class.forName(DatabaseConstants.DRIVER_CLASS);
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, this.email);
            ps.setString(2, this.password);

            ResultSet result = ps.executeQuery();
            if (result.next()) {
                this.setId(result.getInt(1));
                this.setFirstName(result.getString(2));
                this.setLastName(result.getString(3));
                this.setMobileNumber(result.getString(4));
                this.setGender(result.getString(5).charAt(0));
                this.setCountry(result.getString(6));
            } else {
                throw new LoginException("Login Failed");
            }
        }

    }
}
