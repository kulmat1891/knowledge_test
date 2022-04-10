package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


public class AdminHomePageFXMLController implements Initializable {
    @FXML
    public Tab dashboard;
    @FXML
    private TabPane adminTabPane;
    @FXML
    private Tab addQuizTab;
    @FXML
    private Tab addUserTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            Parent node = FXMLLoader.load(getClass().getResource("/AddQuizFXML.fxml"));
            addQuizTab.setContent(node);


            Parent studentTabNode = FXMLLoader.load(getClass()
                    .getResource("/AdminUserTab.fxml"));
            addUserTab.setContent(studentTabNode);


            Parent dash = FXMLLoader.load(getClass()
                    .getResource("/AdminDashboardFXML.fxml"));
            dashboard.setContent(dash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
