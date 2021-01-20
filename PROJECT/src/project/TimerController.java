
package project;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//comments by Shahad
/**
 *
 * @author merao
 */
//youtube video : https://www.youtube.com/watch?v=tkdE7R04UVI&t=1312s
public class TimerController implements Initializable {

    @FXML
    private Text hoursTimer;
    @FXML
    private Text minutesTimer;
    @FXML
    private Text secondsTimer;
    @FXML
    private ComboBox<Integer> hoursInput;
    @FXML
    private ComboBox<Integer> minutesInput;
    @FXML
    private ComboBox<Integer> secondsInput;
    
    Map<Integer, String> numberMap;
    Integer currSeconds;
    Thread thrd;
    private String name;
    private String email;
    Media file = new Media (new File("src\\sound\\alarm1.mp4").toURI().toString());
    MediaPlayer sound = new MediaPlayer(file);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       ObservableList<Integer> hoursList = FXCollections.observableArrayList();
       ObservableList<Integer> minutesAndSecondsList = FXCollections.observableArrayList();
       for(int i=0 ; i <=60 ; i++){
           if(0<= i && i<=24){
               hoursList.add(new Integer(i));
           }
           minutesAndSecondsList.add(new Integer(i));
       }
       
       hoursInput.setItems(hoursList);
       hoursInput.setValue(0);//set default 
       
       minutesInput.setItems(minutesAndSecondsList);
       minutesInput.setValue(0);//set default 
       
       secondsInput.setItems(minutesAndSecondsList);
       secondsInput.setValue(0);//set default 
       
       numberMap = new TreeMap<Integer,String>();
       for(Integer i=0; i<=60 ; i++){
           if(0<=i && i<=9)
           {
               numberMap.put(i, "0"+i.toString());
           }else {
               numberMap.put(i, i.toString());
           }
       }    
    }   
    
    @FXML
    void start(ActionEvent event) {
        currSeconds = hmsToSeconds(hoursInput.getValue(),minutesInput.getValue(),secondsInput.getValue());
        startCountdown();
    }    
    
    @FXML
    private void reset(ActionEvent event) {
        //reset hours, minutes and second to 0
        sound.stop();
        thrd.stop();
        hoursTimer.setText("00");
        minutesTimer.setText("00");
        secondsTimer.setText("00"); 
        hoursInput.setValue(0);
        minutesInput.setValue(0);
        secondsInput.setValue(0);
    }

    @FXML
    void suspend(ActionEvent event) {
        //suspend timer
        thrd.suspend();
    }
    
    @FXML
    void resume(ActionEvent event) {
        //resume timer
        thrd.resume();
    }
    
    @FXML
    private void back(MouseEvent event) {
        sound.stop();
        try{// go back to home page
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
    
    public void startCountdown(){
        //start timer
        thrd = new  Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (true){
                        setOutput();
                        Thread.sleep(1000);
                        if(currSeconds == 0){
                            sound.play();    
                            System.out.print("finished");
                            thrd.stop();
                        }
                        currSeconds -=1;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        thrd.start();
    }
    
    public void setOutput(){
        sound.stop();
        LinkedList<Integer> currHms = secondsToHms(currSeconds);
        hoursTimer.setText(numberMap.get(currHms.get(0)));
        minutesTimer.setText(numberMap.get(currHms.get(1)));
        secondsTimer.setText(numberMap.get(currHms.get(2)));
    }
    
    public Integer hmsToSeconds(Integer h, Integer m , Integer s){
        Integer hToSeconds = h * 3600;
        Integer mToSeconds= m * 60;
        Integer total = hToSeconds + mToSeconds + s;
        return total;
    }
    
    public LinkedList<Integer> secondsToHms(Integer currSecond){
        Integer hours = currSecond / 3600;
        currSecond = currSecond % 3600;
        Integer minutes = currSecond / 60;
        currSecond = currSecond % 60;
        Integer seconds= currSecond;
        LinkedList<Integer> answer= new LinkedList<>();
        answer.add(hours);
        answer.add(minutes);
        answer.add(seconds);
        return answer;
    }
  
    public void passUser(String n,String e){
       name=n;
       email=e;    
    }

}

