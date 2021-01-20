
package project;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

//comments by Shahad
/**
 * FXML Controller class
 *
 * @author ighoo
 */
public class AddController implements Initializable {

    @FXML
    private TextField tName;
    @FXML
    private ComboBox<Integer> tQuantity;
    @FXML
    private DatePicker tBDate;
    @FXML
    private DatePicker tEDate;
    @FXML
    private Label errormsg;
    @FXML
    private Label successmsg;
    private String name;
    private String email;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       ObservableList<Integer> quantity = FXCollections.observableArrayList();
       //specifying quantity range
       for(int i=1 ; i <=100 ; i++){
       quantity.add(new Integer(i));
       }
       tQuantity.setItems(quantity);
       tQuantity.setValue(1);}    

    @FXML //add to TableView and database
    void addItem(ActionEvent event) {
        boolean keep=true;
        //check if name is empty
        if(tName.getText().isEmpty())
        {showMsg("Enter the Item name !","e");}
        else
        {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Items> sList = null;
        String queryStr = "from Items";
        Query query = session.createQuery(queryStr);
        sList = query.list();
        session.close();
        // items number in list
        int i=sList.size();
        Items c1 = new Items();
        c1.setEmail(email);
        //set id value automaticly, new id= list size +1
        c1.setId(++i);
        c1.setName(tName.getText());
        c1.setQuantity(tQuantity.getValue());
        // set expired date = null if user buy somthing with no expired date, ex: plastic bags
        if(tEDate.getValue()==null)
            c1.setEDate(null);   
        else{// otherwise make sure the new item is not expired
        LocalDateTime now = LocalDateTime.now();
        LocalDate d2 = tEDate.getValue();
        Duration diff = Duration.between(now, d2.atStartOfDay());
        long diffDays = diff.toDays();
        if(diffDays<0)
        {showMsg("This item is Expired !","e");
        keep=false;
        }
        else{c1.setEDate(java.sql.Date.valueOf(tEDate.getValue())); }}
        // incase user did not enter buy date for his item
        if(tBDate.getValue()==null)
            c1.setBDate(null);   
        else{// otherwise make sure the entred buy date is not in the future.
        LocalDateTime now = LocalDateTime.now();
        LocalDate d2 = tBDate.getValue();
        Duration diff = Duration.between(now, d2.atStartOfDay());
        long diffDays = diff.toDays();
        if (diffDays>0) 
        {showMsg("Invalid buy date !","e");
        keep=false;}
        else{c1.setBDate(java.sql.Date.valueOf(tBDate.getValue()));}}
        
        boolean keep2=true;
        if(keep==true){
        for(Items c: sList ){
        // make sure the same item withe same name, buy,expired date is not repeated
        if(c1.getName().equals(c.getName())&&c1.getEmail().equals(c.getEmail())&&c1.getBDate()==null&&c.getBDate()==null&&c1.getEDate()!=null&&c.getEDate()!=null&&c1.getEDate().compareTo(c.getEDate())==0)
        {keep2=false;showMsg("This item already exists !","e");break;}   
        else if(c1.getName().equals(c.getName())&&c1.getEmail().equals(c.getEmail())&&c1.getBDate()==null&&c.getBDate()==null&&c1.getEDate()==null&&c.getEDate()==null)
        {keep2=false;showMsg("This item already exists !","e");break;}
        else if(c1.getName().equals(c.getName())&&c1.getEmail().equals(c.getEmail())&&c1.getBDate()!=null&&c.getBDate()!=null&&c1.getBDate().compareTo(c.getBDate())==0&&c1.getEDate()!=null&&c.getEDate()!=null&&c1.getEDate().compareTo(c.getEDate())==0)
        {keep2=false;showMsg("This item already exists !","e");break;}
        else if(c1.getName().equals(c.getName())&&c1.getEmail().equals(c.getEmail())&&c1.getBDate()!=null&&c.getBDate()!=null&&c1.getBDate().compareTo(c.getBDate())==0&&c1.getEDate()==null&&c.getEDate()==null)
        {keep2=false;showMsg("This item already exists !","e");break;}
        else 
        keep2=true;}}

         //add to item to database
        if(keep==true){
        if(keep2==true){
        Session session1 = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session1.beginTransaction();
            session1.save(c1);
        }catch(Exception e){
            if(tx!=null)
                tx.rollback();
            e.printStackTrace();
        }
        finally{
          tx.commit();
        session1.close();
        showMsg("Added successfully !","s");  
        }        
        }
        }}
        
    }
     
    @FXML
    private void back(MouseEvent event) {
        // go back to List page
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("list.fxml"));
            Parent root= (Parent)loader.load();
            ListController LC =loader.getController();
            LC.passUser(name,email); 
            LC.showTable();
            Scene s = new Scene ((Pane)root);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();
            }
            catch(IOException e){e.printStackTrace();}
        
    }
    
    public LocalDate toLocalDate(Date dateToConvert) {
        //convert Date to LocalDate
       return dateToConvert.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate();
}
        
    public void showMsg(String msg,String type){
        // success massage
        if(type.equals("s")){
        Media file = new Media (new File("src\\sound\\success.mp4").toURI().toString());
        MediaPlayer sound = new MediaPlayer(file);
        sound.play();
        successmsg.setText(msg);
        successmsg.setVisible(true);  
        FadeTransition fadeOut= new FadeTransition(javafx.util.Duration.seconds(5),successmsg );
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();}
        else{
        // error massage
        Media file = new Media (new File("src\\sound\\error.mp4").toURI().toString());
        MediaPlayer sound = new MediaPlayer(file);
        sound.play();
        errormsg.setText(msg);
        errormsg.setVisible(true);  
        FadeTransition fadeOut= new FadeTransition(javafx.util.Duration.seconds(5),errormsg );
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();
        }

    }
    
    public void passUser(String n,String e){
       name=n;
       email=e;    
    }

}
