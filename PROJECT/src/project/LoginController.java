
package project;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
public class LoginController implements Initializable {

    
    @FXML
    private TextField emilLog;
    @FXML
    private PasswordField passwordlog;
    @FXML
    private Label createaccount;
    @FXML
    private Label errormsg;
    @FXML
    private Label forgetpassword;


    @Override
    public void initialize(URL url, ResourceBundle rb) {}    

    @FXML
    private void login(ActionEvent event) throws IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> sList = null;
        String queryStr = "from User";
        Query query = session.createQuery(queryStr);
        sList = query.list();
        session.close();
        // ensure entred email is not empty
        if(emilLog.getText().equals(""))
            showMsg("Please enter the e-mail");
        
        // ensure entred email is valid
        else if (isValidEmailAddress(emilLog.getText())==false)
            showMsg("Please enter valid e-mail");
        
        else{
            boolean keep =false;        
            for(User c: sList){
                //ensure user already have account, ensure email already rigestred
               if(c.getEmailS().equals(emilLog.getText())){
                   keep=true;
                   // ensure entred password is not empty
                   if(passwordlog.getText().equals(""))
                       showMsg("Please enter the passowrd");
                   // ensure entred password is valid, password = password registred for this email
                   else if(c.getPasswordS().equals(passwordlog.getText()))
                       {showMsg("");
                        try{// enter successfully! go to home page
                           FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
                           Parent root= (Parent)loader.load();
                           HomeController HC =loader.getController();
                           //every user has seprated list page, so ensure to send user to his page
                           HC.passUser(c.getUsernameS(),c.getEmailS());   
                           Scene s = new Scene ((Pane)root);
                           Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                           window.setScene(s);
                           window.show();
            }
            catch(IOException e){e.printStackTrace();}
            }
        else
            showMsg("You entered incorrect passowrd !!");}}
        
        if(keep==false)
            showMsg("you do not hava an account !! ");}
    }
    
    @FXML
    private void showpass(MouseEvent event) {
        if(emilLog.getText().equals(""))
            showMsg("Please enter the e-mail");
        else if (isValidEmailAddress(emilLog.getText())==false)
            showMsg("Please enter valid e-mail");
        else 
            try{// if clicked on forget password, go to password page
               FXMLLoader loader = new FXMLLoader(getClass().getResource("password.fxml"));
               Parent root= (Parent)loader.load();
               PasswordController PC =loader.getController();
               PC.passEmail(emilLog.getText());   
               Scene s = new Scene ((Pane)root);
               Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
               window.setScene(s);
               window.show();
            }
           catch(IOException e){e.printStackTrace();}
    }

    @FXML
    private void toSignUp(MouseEvent event) throws IOException {
        try{//if clicked on creatAccount lable, go to sign up page
           Parent pane =  (Pane) FXMLLoader.load(getClass().getResource("signup.fxml"));
           Scene s = new Scene (pane);
           Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
           window.setScene(s);
           window.show(); 
        }catch(IOException e){e.printStackTrace();}
    }
  
    public boolean isValidEmailAddress(String email) {
        // valid email address condition
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void showMsg(String msg){
        //error massages method
        errormsg.setText(msg);
        errormsg.setVisible(true);  
        FadeTransition fadeOut= new FadeTransition(javafx.util.Duration.seconds(5),errormsg );
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();
    }
}
