
package project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//comments by Shahad
/**
 *
 * @author merao
 */
public class RecipeController implements Initializable {

    @FXML
    private TextArea testArea;
    @FXML
    private TextField textFiledSearch;
    @FXML
    private Label norec;
    private String word;
    private String name;
    private String email;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void searchResipes(KeyEvent event) {
        // search if "enter" key pressed
        if(event.getCode().equals(KeyCode.ENTER)) 
        {
            try {
               word = textFiledSearch.getText();//read from text fialed
               Scanner input = new Scanner( new java.io.File("src\\file\\Recipes.txt"));
               String result="";
               String lineStr="";
               String line="";
               while (input.hasNextLine()){ //check if file has next
                 do{
                 line=input.nextLine();
                 lineStr+=line+"\n";}
                 //every recipe end with "Enjoy!".
                 while(!line.endsWith("Enjoy!"));
                    // if search word found
                    if(lineStr.contains(word.toLowerCase()))
                        //save recipe in resulte.
                        result+=lineStr+"\n";
                        lineStr="";
                    // if found a result
                    if(!result.isEmpty())
                        //do NOT display errror massage 
                        {norec.setVisible(false);
                        //display recipes found "result"
                        testArea.setVisible(true);
                        testArea.setText(result);}    
                    else
                        //display errror massage 
                        {norec.setVisible(true);
                         testArea.setVisible(false);
                    }
                }                
                input.close();
            } catch (FileNotFoundException t) {
                System.out.println("Eror, Open File Filed.");}         
        }        
    }
    
    @FXML
    private void back(MouseEvent event) {
        try{//go back to hame page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root= (Parent)loader.load();
            HomeController HC =loader.getController();
            HC.passUser(name,email);   
            Scene s = new Scene ((Pane)root);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();
        } catch(IOException e){e.printStackTrace();}
    }
    
    public void passUser(String n,String e){
       name=n;
       email=e;    
    }

   
}
