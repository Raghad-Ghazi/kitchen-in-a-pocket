
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
import org.hibernate.Transaction;

//comments by Shahad
/**
 *
 * @author ighoo
 */
public class SignupController implements Initializable {
   
    
    @FXML
    private Label haveaccount;
    @FXML
    private TextField passwordLog;
    @FXML
    private PasswordField passwordLog1;
    @FXML
    private TextField nameLog;
    @FXML
    private TextField emailLog;
    @FXML
    private Label errormsg;
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    

    @FXML
    private void signup(ActionEvent event) throws IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> sList = null;
        String queryStr = "from User";
        Query query = session.createQuery(queryStr);
        sList = query.list();
        session.close();
        //ensure username is not empty
        if(nameLog.getText().equals(""))
           showMsg("Please enter your name");
        //ensure email is not empty
        else if(emailLog.getText().equals(""))
           showMsg("Please enter the e-mail");
        // ensure email is valid
        else if (isValidEmailAddress(emailLog.getText())==false)
           showMsg("Please enter valid e-mail");
        //ensure password is not empty
        else if(passwordLog.getText().equals(""))
           showMsg("Please enter the passowrd");
        //ensure re-password is not empty
        else if(passwordLog1.getText().equals(""))
           showMsg("Please re-enter the passowrd");
        else{
           boolean keep=true;
           for(User c: sList){
               //ensure user does not have an account before
               if(c.getEmailS().equals(emailLog.getText())){
                   showMsg("You already have an account !!");
                   keep=false;
               }
           }// ensure password and confirmed password are equals
           if(passwordLog.getText().equals(passwordLog1.getText())==false){
               showMsg("the passowrds are not matching !!");
               keep=false;
               //ensure entred password is valid
           } else if(!isValidPassword(passwordLog.getText())){
               keep=false;
           }
           if(keep==true){
               showMsg("");
               User user= new User(nameLog.getText(),emailLog.getText(),passwordLog.getText()); 
               session = HibernateUtil.getSessionFactory().openSession();
               Transaction tx= null;
               try{
                   tx=session.beginTransaction();
                   //add user to database
                   session.save(user);
                   tx.commit();
               }catch(Exception e){
                   if(tx!=null)
                       tx.rollback();
                   e.printStackTrace();
               }finally{
                   session.close();
               }
               try{// entered successfully, go to home page
                   FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
                   Parent root= (Parent)loader.load();
                   HomeController HC =loader.getController();
                   HC.passUser(nameLog.getText(),emailLog.getText());   
                   Scene s = new Scene ((Pane)root);
                   Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                   window.setScene(s);
                   window.show();
               } catch(IOException e){e.printStackTrace();}       
           }
        }
    }

    @FXML
    private void toLogin(MouseEvent event) throws IOException {
       try{// go to log in page
           Parent pane =(Pane) FXMLLoader.load(getClass().getResource("login.fxml"));
           Scene s = new Scene (pane);
           Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
           window.setScene(s);
           window.show();
       } catch(IOException e){e.printStackTrace();}
    }

    public boolean isValidEmailAddress(String email) {
         // valid email address condition
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean isValidPassword(String password){     
         // valid password conditions
         //password length
        if (!((password.length() >= 8) && (password.length() <= 15))) { 
            showMsg("Password length should be between 8 to 15 !");
            return false; 
        } 
  
        // to check space 
        if (password.contains(" ")) { 
            showMsg("Password must not contain any space !");
            return false; 
        } 
        if (true) { 
            int count = 0; 
            // check digits from 0 to 9 
            for (int i = 0; i <= 9; i++) { 
                // to convert int to string 
                String str1 = Integer.toString(i); 
                if (password.contains(str1)) { 
                    count = 1; 
                } 
            } 
            if (count == 0) { 
                showMsg("Password must contain at least one digit !");
                return false; 
            } 
        } 
  
        if (true) { 
            int count = 0; 
            // checking capital letters 
            for (int i = 65; i <= 90; i++) { 
                // type casting 
                char c = (char)i; 
                String str1 = Character.toString(c); 
                if (password.contains(str1)) { 
                    count = 1; 
                } 
            } 
            if (count == 0) {
                showMsg("Password must contain at least one uppercase letter !");
                return false; 
            } 
        } 
  
        if (true) { 
            int count = 0;   
            // checking small letters 
            for (int i = 90; i <= 122; i++) {  
                // type casting 
                char c = (char)i; 
                String str1 = Character.toString(c);   
                if (password.contains(str1)) { 
                    count = 1; 
                } 
            } 
            if (count == 0) {
                showMsg("Password must contain at least one lowercase letter !");
                return false; 
            }                 
        }  
        // if all conditions fails 
          return true;
          //code source: https://www.geeksforgeeks.org/program-to-check-the-validity-of-a-password/
    }
        
    public void showMsg(String msg){
        //error massages
        errormsg.setText(msg);
        errormsg.setVisible(true);  
        FadeTransition fadeOut= new FadeTransition(javafx.util.Duration.seconds(5),errormsg );
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();
    }

}
