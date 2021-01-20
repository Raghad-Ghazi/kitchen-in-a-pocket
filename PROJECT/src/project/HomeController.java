
package project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//comments by Shahad
/**
 *
 * @author merao
 */
public class HomeController implements Initializable {

    @FXML
    private Label homename;
    private String email;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    

    @FXML
    private void ClickOnList(MouseEvent event) throws IOException {
        try{// if clicked on list pane, go to list page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("list.fxml"));
            Parent root= (Parent)loader.load();
            ListController LC =loader.getController();
            System.out.println(email);
            LC.passUser(homename.getText(),email);
            LC.showTable();
            Scene s = new Scene ((Pane)root);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();}
        catch(IOException e){e.printStackTrace();}
    }
    
    @FXML
    private void ClickOnRecip(MouseEvent event) throws IOException {
        try{// if clicked on recipe pane, go to recipe page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("recipe.fxml"));
            Parent root= (Parent)loader.load();
            RecipeController RC =loader.getController();
            System.out.println(email);
            RC.passUser(homename.getText(),email);
            Scene s = new Scene ((Pane)root);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();}
        catch(IOException e){e.printStackTrace();}
    } 

    @FXML
    private void ClickOnTimer(MouseEvent event) throws IOException {
        try{ // if clicked on timer pane, go to timer page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("timer.fxml"));
            Parent root= (Parent)loader.load();
            TimerController TC =loader.getController();
            System.out.println(email);
            TC.passUser(homename.getText(),email);
            Scene s = new Scene ((Pane)root);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();  }
        catch(IOException e){e.printStackTrace();}
    }

    @FXML
    private void exit(MouseEvent event) throws IOException {
        try{// if clicked on exit icon, go to log in page
        Parent pane =  (Pane) FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene s = new Scene (pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.show();}
        catch(IOException e){e.printStackTrace();}
    }
    
    public void passUser(String n,String e){
       homename.setText(n);
       email=e;    
    }
        

}
