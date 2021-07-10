package views;

import Index.Main;
import jade.wrapper.AgentController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class HomeClient implements Initializable {

    public static Boolean available = null;
    public static String ruleBase = "";

    @FXML
    private TextField fromTF;

    @FXML
    private TextField toTf;

    @FXML
    private DatePicker depDate;

    @FXML
    private DatePicker returnDate;

    @FXML
    private Button searchBtn;

    @FXML
    private RadioButton forChain;

    @FXML
    private ToggleGroup chain;

    @FXML
    private RadioButton backChain;

    @FXML
    private TextArea ruleFactArea;


    public void searchBtnClicked(){
        String fromS = "", toS = "",dateDe = "",dateRet = "",chain="";

        if(!this.fromTF.getText().isEmpty()){
            fromS = this.fromTF.getText();
        }
        if (!this.toTf.getText().isEmpty()){
            toS = this.toTf.getText();
        }
        if(this.depDate.getValue() !=null){
            LocalDate localDate = depDate.getValue();
//        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
//        Date date = Date.from(instant);
            dateDe = !localDate.toString().isEmpty()?  localDate.toString() : "";
        }


        if(this.returnDate.getValue() != null){
            LocalDate localDate2 = depDate.getValue();
//        Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
//        Date date2 = Date.from(instant2);
            dateRet = !localDate2.toString().isEmpty()?  localDate2.toString() : "";
        }


        if(this.forChain.isSelected()){
            chain = "forwardChain";
        }
        if(this.backChain.isSelected()){
            chain = "backwardChain";
        }

        try{
            String info = fromS + " " + toS+ " "+dateDe+ " "+dateRet+" "+chain;
            Main.CENTRAL.putO2AObject(info, AgentController.ASYNC);
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Controller : "+HomeClient.ruleBase);
            if(HomeClient.available == null ){
                System.out.println("No offers for you sorry");
            }else {
                System.out.println("Available : "+HomeClient.available.toString());
            }
            System.out.println("--------------------------");
            System.out.println("Rule Base : \n"+ HomeClient.ruleBase);
            ruleFactArea.setText(HomeClient.ruleBase);
        }catch(Exception e){
            e.printStackTrace();
        }




    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ruleFactArea.setText(HomeClient.ruleBase);
    }
}
