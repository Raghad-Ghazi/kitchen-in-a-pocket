
package project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.PROJECT;

//comments by Shahad
/**
 *
 * @author s4380
 */
public class EnterController implements Initializable {
    
    @FXML
    private Pane root;
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if(!PROJECT.islogo){
              loadSreen();
        }
    }    

    @FXML
    private void login(ActionEvent event) throws IOException {
        // go to log in page
       try{
       Parent pane =  (Pane) FXMLLoader.load(getClass().getResource("login.fxml"));
       Scene s = new Scene (pane);
       Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
       window.setScene(s);
       window.show();    }
       catch(IOException e){e.printStackTrace();}
        
    }

    @FXML
    private void signup(ActionEvent event) throws IOException {
        // go to sign up page
       try{
       Parent pane =  (Pane) FXMLLoader.load(getClass().getResource("signup.fxml"));
       Scene s = new Scene (pane);
       Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
       window.setScene(s);
       window.show();}
        catch(IOException e){e.printStackTrace();}
    }
    
    private void loadSreen(){
        // logo animation
        try {
            PROJECT.islogo = true ;
            Pane pane = FXMLLoader.load(getClass().getResource("logoPage.fxml"));
            
            root.getChildren().setAll(pane);
            
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3),pane );
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);
            
            FadeTransition fadeOut= new FadeTransition(Duration.seconds(3),pane );
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);
            
            fadeIn.play();            
            
            fadeIn.setOnFinished(e->{            
            fadeOut.play();             
            });
            fadeOut.setOnFinished(e->{                 
                try {                   
                    Pane pane2 = FXMLLoader.load(getClass().getResource("enter.fxml"));                   
                    root.getChildren().setAll(pane2);
                } catch (IOException ex) {
                    Logger.getLogger(EnterController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(EnterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
