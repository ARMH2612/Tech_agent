package agents;

import Index.Main;
import engine.KieRuleInferenceEngine;
import engine.Rule;
import engine.RuleInferenceEngine;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.wrapper.AgentController;

import java.util.ArrayList;

public class CentralAgent extends Agent {
   String s;
   @Override
   protected  void setup(){
       setEnabledO2ACommunication(true,0);
       addBehaviour(new CyclicBehaviour(this) {
           @Override
           public void action() {
               String info = (String) myAgent.getO2AObject();
//               System.out.println(info);
               if(info != null ){
                   System.out.println("Envois de formulaire au agents...");


                   try{
                       Main.AIRAL.putO2AObject(info, AgentController.ASYNC);
                       Main.AIRFR.putO2AObject(info, AgentController.ASYNC);
                       Main.AIRTU.putO2AObject(info,AgentController.ASYNC);
                       Main.LUFTDU.putO2AObject(info,AgentController.ASYNC);
                   }catch(Exception e){
                       e.printStackTrace();
                       System.out.println("L'envois a echoué!");
                   }

               }
           }
       });
   }
}
