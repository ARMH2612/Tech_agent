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
        primaryStage.setScene(new Scene(root, 600, 620));
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

//            // Agent AIR Algerie
//            jade.core.Runtime rtAirFr = jade.core.Runtime.instance();
//            ProfileImpl pAirAlg = new ProfileImpl("localhost",5001,"RSHP");
//            ContainerController ccAirAlg = rtAirAlg.createMainContainer(pAirAlg);
//            AgentController agAirAlg = ccAirAlg.createNewAgent("Agent Air Algerie","agents.AirAlgAgent",new Object[0]);
//            agAirAlg.start();
//            AIRAL = agAirAlg;


            // Agent Central :
            jade.core.Runtime rtCentral = jade.core.Runtime.instance();
            ProfileImpl pCentral = new ProfileImpl("localhost",5002,"RSHP");
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
