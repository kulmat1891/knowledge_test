package controllers;

import constants.Countries;
import constants.EmailConstraint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import models.User;
import org.controlsfx.control.Notifications;


import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AdminUserTabController implements Initializable {

    @FXML
    private VBox formContainer;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField mobileNumber;
    @FXML
    private TextField email;
    @FXML
    private ChoiceBox<String> country;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private PasswordField password;
    @FXML
    private Button saveButton;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> userIdColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> mobileNumberColumn;
    @FXML
    private TableColumn<User, Character> genderColumn;
    @FXML
    private TableColumn<User, String> countryColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;

    // Not FXML variables
    private ToggleGroup groupOfMaleAndFemale;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxCountriesAdd();
        initAll();
        radioButtonSetup();
        renderTable();
    }

    // show information on user page in app
    private void renderTable() {
        List<User> users = User.getAllUser();
        userTable.getItems().clear();

        this.userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.mobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        this.genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        this.countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        this.emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        userTable.getItems().addAll(users);
    }

    private void ChoiceBoxCountriesAdd() {
        country.setItems(new Countries().countriesExample);
    }

    private void initAll() {
        groupOfMaleAndFemale = new ToggleGroup();
    }

    private void radioButtonSetup() {
        this.maleRadioButton.setToggleGroup(groupOfMaleAndFemale);
        this.femaleRadioButton.setToggleGroup(groupOfMaleAndFemale);
    }

    private void resetForm() {
        this.password.clear();
        this.email.clear();
        this.firstName.clear();
        this.lastName.clear();
        this.mobileNumber.clear();
        this.country.valueProperty().set(null);
    }

    // constraint method
    private String validate(User user) {
        String message = null;
        if (user.getFirstName().length() < 2 || user.getFirstName().isBlank()) {
            message = "First Name Is to Short. First Name Must Be More Than 1 Character";
        } else if (user.getLastName().length() < 2 || user.getLastName().isBlank()) {
            message = "Last Name Is to Short. Last Name Must Be More Than 1 Character";
        } else if (user.getMobileNumber().length() != 12 || user.getMobileNumber() != user.getMobileNumber().replaceAll("[^\\d.]", "").trim()) {
            message = "The number must contain 12 characters. Example: 3-8099-000-000-0";
        } else if (!EmailConstraint.emailValidator(user.getEmail())) {
            message = "Enter Valid Email";
        } else if (user.getCountry() == null) {
            message = "Select Your Country";
        } else if (user.getGender() == null) {
            message = "Select Gender";
        } else if (user.getPassword().length() < 6) {
            message = "Password Is to Short. Password Must Be More Than 5 Character";
        }
        return message;
    }

    @FXML
    private void saveUser(ActionEvent event) {
        System.out.println("Save button clicked");

        // get values from app fields
        String firstName = this.firstName.getText().trim();
        String lastName = this.lastName.getText().trim();
        String mobile = this.mobileNumber.getText().trim();
        String email = this.email.getText().trim();
        String country = this.country.getValue();
        String password = this.password.getText().trim();
        Character gender = null;

        RadioButton genderRadioButton = (RadioButton) groupOfMaleAndFemale.getSelectedToggle();

        // choice for gender radio button
        if (genderRadioButton != null) {
            if (genderRadioButton == maleRadioButton) {
                gender = 'M';
            } else {
                gender = 'F';
            }
        }

        // message for error
        String message = null;

        // create our user
        User user = new User(firstName, lastName, mobile, gender, country, email, password);

        // initialize for our error message, return type of validated method
        message = this.validate(user);

        // return error if validated not confirm
        if (message != null) {
            Notifications.create()
                    .darkStyle()
                    .position(Pos.CENTER)
                    .title("Error Filling Out The Form")
                    .text(message)
                    .hideAfter(Duration.millis(5000))
                    .showWarning();
            return;
        }

        // Constraint. Checked user already registered or not
        if (user.isExists()) {
            Notifications.create()
                    .darkStyle()
                    .position(Pos.CENTER)
                    .title("Failed")
                    .text("User Already Registered")
                    .hideAfter(Duration.millis(5000))
                    .showError();
            return;
        }

        // saving user
        user = user.save();

        // information for console
        System.out.println(user);

        // information for user, user registered or not
        if (user != null) {
            Notifications.create()
                    .darkStyle()
                    .position(Pos.CENTER)
                    .title("Success")
                    .text("User Registered")
                    .hideAfter(Duration.millis(5000))
                    .showInformation();
            this.resetForm();

            // add user to our table on user page in app
            userTable.getItems().add(0, user);
        } else {
            Notifications.create()
                    .darkStyle()
                    .position(Pos.CENTER)
                    .title("Failed")
                    .text("User Registration Failed")
                    .hideAfter(Duration.millis(5000))
                    .showError();
        }
    }

}
