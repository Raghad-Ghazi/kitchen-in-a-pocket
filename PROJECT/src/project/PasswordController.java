
package project;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import org.hibernate.Query;
import org.hibernate.Session;

//comments by Shahad
/**
 * FXML Controller class
 *
 * @author ighoo
 */
public class PasswordController implements Initializable {


    @FXML
    private Label emailstar;
    @FXML
    private Label msg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    

    @FXML
    private void back(MouseEvent event) throws IOException {
        try{// go to log in page
           Parent pane =  (Pane) FXMLLoader.load(getClass().getResource("login.fxml"));
           Scene s = new Scene (pane);
           Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
           window.setScene(s);
           window.show();
        } catch(IOException e){e.printStackTrace();}
    }
    
    public void passEmail(String email){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> sList = null;
        String queryStr = "from User";
        Query query = session.createQuery(queryStr);
        sList = query.list();
        session.close();
        boolean keep=false;
        for(User c: sList){
            // if user has an account
            if(c.getEmailS().equals(email)){
                keep=true;
                String stars="";
                //encript email address
                for (int i = 4; i < email.indexOf('@'); i++) {
                    stars+="*";
                }
                msg.setText("The istructions to reset your password have been sent to the email :");
                emailstar.setText(email.substring(0, 4)+stars+email.substring(email.indexOf('@'), email.length()));
            }
        }
        // if user does not have an account
        if(keep==false)
            msg.setText("This email was not found in our records !");
    }
    
}
