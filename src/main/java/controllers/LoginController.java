package controllers;

import constants.AdminEmailPassword;
import exceptions.LoginException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import org.controlsfx.control.Notifications;
import usersController.UserMainScreenFXMLController;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField adminEmail;
    @FXML
    private PasswordField adminPassword;
    @FXML
    private Button adminLoginButton;
    @FXML
    private TextField userEmail;
    @FXML
    private PasswordField userPassword;
    @FXML
    private Button userLoginButton;

    @FXML
    private void adminLogin(ActionEvent event) {
        String email = adminEmail.getText();
        String password = adminPassword.getText();
        if (email.trim().equalsIgnoreCase(AdminEmailPassword.email)
                && password.trim().equalsIgnoreCase(AdminEmailPassword.password)) {

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/AdminHomePageFXML.fxml"));
                Stage stage = (Stage) userPassword.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setFullScreen(true);

                System.out.println("successful admin login");

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

        } else {
            System.out.println("unsuccessful admin login");
        }
        System.out.println(email + " ---> " + password);
        System.out.println("LoginController.adminLogin()");
    }

    @FXML
    private void userLogin(ActionEvent event) {

        System.out.println("LoginController.userLogin()");
        User user = new User(this.userEmail.getText(), this.userPassword.getText());
        try {
            user.login();
            System.out.println(user);

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UserMainScreenFXML.fxml"));
                Parent root = fxmlLoader.load();
                UserMainScreenFXMLController controller = fxmlLoader.getController();
                controller.setUser(user);
                Stage stage = (Stage) userPassword.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setFullScreen(true);

                System.out.println("successful admin login");

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

        } catch (Exception e) {
            if (e instanceof LoginException) {
                Notifications
                        .create()
                        .position(Pos.TOP_CENTER)
                        .title("Login failed")
                        .text("Email or Password incorrect")
                        .darkStyle()
                        .showError();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
