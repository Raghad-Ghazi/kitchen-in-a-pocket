
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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

//comments by Shahad
/**
 *
 * @author shahd
 */
public class ListController implements Initializable {

    @FXML
    private TableView<Items> ItemsTable;
    @FXML
    private TableColumn<Items, String> Name;
    @FXML
    private TableColumn<Items, Integer> Quantity;   
    @FXML
    private TableColumn<Items, Date> bDate; 
    @FXML
    private TableColumn<Items, Date> eDate;
    @FXML
    private TextField filterField;
    @FXML
    private Label errormsg;
    @FXML
    private Label successmsg;
    User user=new User();
    ObservableList<Items> listM;
    ObservableList<Items> dataList;
    ObservableList<Items> all=FXCollections.observableArrayList();
    private String name;
    private String email;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        ItemsTable.setEditable(true); 
        Name.setCellValueFactory(new PropertyValueFactory<Items, String>("Name"));
        Name.setCellFactory(TextFieldTableCell.forTableColumn());
        Name.setOnEditCommit(t ->
        {if(t.getNewValue().equals(""))
        deleteItem1();
        else
        {t.getRowValue().setName(t.getNewValue());
        updateRow();}});
        
        Quantity.setCellValueFactory(new PropertyValueFactory<Items, Integer>("Quantity"));
        Quantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        Quantity.setOnEditCommit(t ->
        {
        if(t.getNewValue()==0)
        deleteItem1();
        else if(t.getNewValue()<0)
        {showMsg("Quantity can not be in minus!","e");
        showTable();}
        else
        {t.getRowValue().setQuantity(t.getNewValue());
        updateRow();}});
        
        bDate.setCellValueFactory(new PropertyValueFactory<Items, Date>("bDate"));
        bDate.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
        bDate.setOnEditCommit(t ->
        {
        LocalDateTime now = LocalDateTime.now();
        LocalDate d2 = toLocalDate(t.getNewValue());
        Duration diff = Duration.between(now, d2.atStartOfDay());
        long diffDays = diff.toDays();
        if (diffDays>0) 
        {showMsg("Invalid date !","e");
        showTable();} 
        else
        {t.getRowValue().setBDate(t.getNewValue());
        updateRow();}
        });
        
        eDate.setCellValueFactory(new PropertyValueFactory<Items, Date>("eDate"));
        eDate.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
        eDate.setOnEditCommit(t ->
        {t.getRowValue().setEDate(t.getNewValue());
        updateRow();});
    } 
    

    
    @FXML
    void addItem(ActionEvent event) throws IOException {
            try{//go to add page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("add.fxml"));
                Parent root= (Parent)loader.load();
                AddController AC =loader.getController();
                AC.passUser(name,email);   
                Scene s = new Scene ((Pane)root);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(s);
                window.show();
            }
            catch(IOException e){e.printStackTrace();}
    }

    @FXML
    void deleteItem(ActionEvent event) {
        deleteItem1();
    }
    
    @FXML
    private void back(MouseEvent event) {
        //if clicked on back icon, go to home page
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root= (Parent)loader.load();
            HomeController HC =loader.getController();
            HC.passUser(name,email);   
            Scene s = new Scene ((Pane)root);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();
        }
        catch(IOException e){e.printStackTrace();}
    }

    void deleteItem1() {
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Items> sList = null;
            String queryStr = "from Items";
            Query query = session.createQuery(queryStr);
            sList = query.list();
            session.close();
            // get selected row in TableView
            Items c =  ItemsTable.getSelectionModel().getSelectedItem();
            if(c!=null)
            {Session session2 = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            try{// delete item from database
                tx = session2.beginTransaction();
                session2.delete(c);
                int id=c.getId()+1;
                for(Items c1: sList ){
                    // reset id for rest item
                  if(c1.getId()==id){
                    session2.delete(c1);
                    c1.setId(id-1);
                    session2.save(c1);                 
                    id++;
                  }
               }
               tx.commit();
               System.out.println("deleted Items: "+c.getName());
            } catch(Exception e){
                if(tx!=null)
                   tx.rollback();
                e.printStackTrace();
            } finally{
                session2.close();
                showMsg("Deleted successfully !","s");
            }          
            }
            else{
            showMsg("Please select an item !","e");
            }  
        // reset TableView items and make sure it match items in database   
        showTable();   
    }

    void updateRow() {
        //edite table
        Session session1 = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
        tx = session1.beginTransaction();
        for(Items c: ItemsTable.getItems())
        {
        c = new Items(
        c.getId(),
        c.getName(),
        c.getQuantity(),
        c.getBDate(),
        c.getEDate(),c.getEmail());
        //update cells in database
        session1.update(c);
        } 
        tx.commit();
        } catch(Exception e){
            if(tx!=null)
                tx.rollback();
            e.printStackTrace();
        }finally{
        session1.close();
        showMsg("Updated successfully !","s");
        // update cells in table view
        showTable();
        }
        }

    void search(){
        Name.setCellValueFactory(new PropertyValueFactory<Items,String>("Name"));
        Quantity.setCellValueFactory(new PropertyValueFactory<Items,Integer>("Quantity"));
        bDate.setCellValueFactory(new PropertyValueFactory<Items,Date>("bDate"));
        eDate.setCellValueFactory(new PropertyValueFactory<Items,Date>("eDate"));
        
        dataList = ItemsTable.getItems();
        ItemsTable.setItems(dataList);
        //filter to display items that contine search word.
        FilteredList<Items> filteredData = new FilteredList<>(dataList, b -> true);  
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(person -> {
        if (newValue == null || newValue.isEmpty()) {
         return true;
        }    
        String lowerCaseFilter = newValue.toLowerCase();
        // search by name 
        if (person.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
         return true; // Filter matches username
        }
             else  
              return false; // Does not match.
        });
       });  
       SortedList<Items> sortedData = new SortedList<>(filteredData);  
       sortedData.comparatorProperty().bind(ItemsTable.comparatorProperty());  
       ItemsTable.setItems(sortedData);      
        // youtube video: https://www.youtube.com/watch?v=2KllVaX5cd4
        // code source: https://bit.ly/2YhCMuZ


    }
    
    void showTable(){
        // clear table view
            all.clear();
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Items> sList = null;
            String queryStr = "from Items i where i.Email = :email";
            Query query = session.createQuery(queryStr);
            query.setParameter("email", email);
            //read from database
            sList = query.list();
            session.close();
            for(Items c: sList)
            all.add(c);
            // add all items to table view
            ItemsTable.setItems(all);
            // notification and change single row color
            ItemsTable.setRowFactory(tv -> new TableRow<Items>() {
                public void updateItem(Items item, boolean empty) {
                    
                    super.updateItem(item, empty);
                    if (item == null) {
                        setStyle("");
                        //make sure expired date != null
                    } else if(item.getEDate()!=null){
                        LocalDateTime now = LocalDateTime.now();
                        LocalDate d2 = toLocalDate(item.getEDate());
                        Duration diff = Duration.between(now, d2.atStartOfDay());
                        long diffDays = diff.toDays();
                        // if 3-7 days are lefted to reach expired date, color light yellow
                        if (diffDays<7&&diffDays>=3) {
                        setStyle("-fx-background-color: #F7F379;");
                        // if 0-3 days are lefted to reach expired date, color light red
                        } else if(diffDays<3&&diffDays>(-1)){
                            setStyle("-fx-background-color: #F68770;");
                        // if reached expired date, color light light grey
                        } else if(diffDays<0){
                           setStyle("-fx-background-color: #DBDBD8;");
                        }
                        // if have date and quantity =1, color light orange
                        else if (item.getQuantity()==1) {
                        setStyle("-fx-background-color: orange;");
                        } 
                    } // if quantity =1, color light orange
                        else if (item.getQuantity()==1) {
                        setStyle("-fx-background-color: orange;");
                    } else {
                        setStyle("");
                    }
                    
                }
            });
            
            search();
    }

    public void passUser(String n,String e){
       name=n;
       email=e;    
    }
    
    public LocalDate toLocalDate(Date dateToConvert) {
          //convert Date to LocalDate
    return dateToConvert.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate();
   // code source : https://www.baeldung.com/java-date-to-localdate-and-localdatetime
}
    
    public void showMsg(String msg,String type){
        if(type.equals("s")){
            // success massage
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
            //error massage
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
    }
       
    

