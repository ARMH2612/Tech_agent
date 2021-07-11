package Index;

import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static AgentController CENTRAL,AIRFR,AIRAL,LUFTDU,AIRTU;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/home_client.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 642, 620));
        primaryStage.show();
    }


    public static void main(String[] args) {
        // Create and start agents.
        try{

            // Agent AIR Algerie
            jade.core.Runtime rtAirAlg = jade.core.Runtime.instance();
            ProfileImpl pAirAlg = new ProfileImpl("localhost",5000,"RSHP");
            ContainerController ccAirAlg = rtAirAlg.createMainContainer(pAirAlg);
            AgentController agAirAlg = ccAirAlg.createNewAgent("Agent Air Algerie","agents.AirAlgAgent",new Object[0]);
            agAirAlg.start();
            AIRAL = agAirAlg;

            jade.core.Runtime rtAirTU = jade.core.Runtime.instance();
            ProfileImpl pAirTU = new ProfileImpl("localhost",5001,"RSHP");
            ContainerController ccAirTU = rtAirTU.createMainContainer(pAirTU);
            AgentController agAirTU = ccAirTU.createNewAgent("Agent Air Turquie","agents.AirTuAgent",new Object[0]);
            agAirTU.start();
            AIRTU = agAirAlg;

            jade.core.Runtime rtAirDU = jade.core.Runtime.instance();
            ProfileImpl pAirDU = new ProfileImpl("localhost",5002,"RSHP");
            ContainerController ccAirDU = rtAirDU.createMainContainer(pAirDU);
            AgentController agAirDU = ccAirDU.createNewAgent("Agent Air Germany","agents.AirDuAgent",new Object[0]);
            agAirDU.start();
            LUFTDU = agAirAlg;

            // Agent AIR Algerie
            jade.core.Runtime rtAirFr = jade.core.Runtime.instance();
            ProfileImpl pAirFr = new ProfileImpl("localhost",5003,"RSHP");
            ContainerController ccAirFr = rtAirFr.createMainContainer(pAirFr);
            AgentController agAirFr = ccAirFr.createNewAgent("Agent Air Algerie","agents.AirFraAgent",new Object[0]);
            agAirFr.start();
            AIRFR = agAirFr;


            // Agent Central :
            jade.core.Runtime rtCentral = jade.core.Runtime.instance();
            ProfileImpl pCentral = new ProfileImpl("localhost",5004,"RSHP");
            ContainerController ccCentral = rtCentral.createMainContainer(pCentral);
            AgentController agCentral = ccCentral.createNewAgent("Agent Central","agents.CentralAgent",new Object[0]);
            agCentral.start();
            CENTRAL = agCentral;


        }catch(Exception e){
            e.printStackTrace();
        }
        launch(args);
    }
}
