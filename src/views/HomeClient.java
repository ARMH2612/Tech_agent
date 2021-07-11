package views;

import Classes.Offre;
import Index.Main;
import jade.wrapper.AgentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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

    public static String listAirAlg = "Agent AirALG : No Offers yet!";
    public static String listAirFr = "Agent AirFra : No Offers yet!";
    public static String listAirGr = "Agent AirGer : No Offers yet!";
    public static String listAirTr = "Agent AirTr : No Offers yet!";


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

    @FXML
    private TextField maxPrix;

    @FXML
    private TextArea tfAA;
    @FXML
    private TextArea tfAF;

    @FXML
    private TableView<Offre> offersTab;

    @FXML
    private TableColumn<Offre, String> offersCol;

    ObservableList<Offre> offres = FXCollections.observableArrayList();

    public void restButoon(){
        offres.clear();
    }

    public void searchBtnClicked(){

        offres.clear();
        offersCol.setCellValueFactory(new PropertyValueFactory<>("offre"));
        offersTab.setItems(offres);


        String fromS = "", toS = "",mprix = "",dateDe = "",dateRet = "",chain="";

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
        if(!this.maxPrix.getText().isEmpty()){
            mprix = this.maxPrix.getText();
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
            String info = fromS + " " + toS+ " "+mprix+" "+dateDe+ " "+dateRet+" "+chain;
            Main.CENTRAL.putO2AObject(info, AgentController.ASYNC);
            TimeUnit.SECONDS.sleep(2);
            if(HomeClient.available == null ){
                System.out.println("No offers for you sorry");
            }else {
                System.out.println("Available : "+HomeClient.available.toString());

                for (String str: listAirAlg.split("\n")) {
                    offres.add(new Offre(str));
                }
                for (String str: listAirFr.split("\n")) {
                    offres.add(new Offre(str));
                }

                for (String str: listAirTr.split("\n")) {
                    offres.add(new Offre(str));
                }
                for (String str: listAirGr.split("\n")) {
                    offres.add(new Offre(str));
                }

                offersCol.setCellValueFactory(new PropertyValueFactory<>("offre"));
                offersTab.setItems(offres);


            }
            System.out.println("--------------------------");
            System.out.println("Rule Base : \n"+ HomeClient.ruleBase);
            ruleFactArea.setText(HomeClient.ruleBase);
        }catch(Exception e){
            e.printStackTrace();
        }




    }


    public void proceed(){

        TablePosition pos = offersTab.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();

// Item here is the table view type:
        Offre item = offersTab.getItems().get(row);

        TableColumn col = pos.getTableColumn();

// this gives the value in the selected cell:
        String data = (String) col.getCellObservableValue(item).getValue();


        try {

            FXMLLoader root1 =  new FXMLLoader(getClass().getResource("ValidateFlight.fxml"));

            Stage stage = new Stage();

            stage.setTitle("Validation ");
            stage.setScene(new Scene(root1.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            ValidateFlight validateFlight = root1.getController();
            validateFlight.initData(data);

            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ruleFactArea.setText(HomeClient.ruleBase);

    }
}
