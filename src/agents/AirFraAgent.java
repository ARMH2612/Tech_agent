package agents;

import engine.*;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import views.HomeClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class AirFraAgent extends Agent {
    HashMap<String,String> flightPrice = new HashMap<>();
    RuleInferenceEngine rie = new KieRuleInferenceEngine();
    @Override
    protected void setup(){
        flightPrice.put("F-FR001","8509695");
        flightPrice.put("F-FR002","2084526");
        flightPrice.put("F-FR003","6963413");
        flightPrice.put("F-FR004","6279480");
        flightPrice.put("F-FR005","907701");
        flightPrice.put("F-FR006","7878303");
        flightPrice.put("F-FR007","891252");
        flightPrice.put("F-FR008","6680298");
        flightPrice.put("F-FR009","9857996");
        flightPrice.put("F-FR010","5900812");
        setEnabledO2ACommunication(true,0);

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                String info = (String) myAgent.getO2AObject();
                if(info != null){
                    System.out.println(" Air France a reçu le formulaire.");
                    try{
                        getInferenceEngine();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    String t[] = info.split(" ");
                    String from = t[0],to = t[1],mprix = t[2],dateDep = t[3],dateRet=t[4],chain=t[5];
                    System.out.println("Agent Air France : ");
                    System.out.println(from+"\n"+to+"\n"+dateDep+"\n"+dateRet+"\n"+chain);

                    if(!from.equals("")){
                        rie.addFact(new EqualsClause("From",from));
                    }
                    if(!to.equals("")){
                        rie.addFact(new EqualsClause("To",to));
                    }
                    if(!mprix.equals("")){
                        rie.addFact("Price",mprix);
                    }
                    // still have to compare dateDep and dateRte
                    if(chain.equals("forwardChain")){
                        rie.infer();
                        System.out.println("Rule base size : "+rie.getFacts().size());
                        HomeClient.ruleBase = rie.getRules().toString()+"\n"+rie.getFacts().toString();

                        if(rie.getFacts().size() >= 4){
                            Clause resultat = rie.getFacts().get(rie.getFacts().size()-1);
                            HomeClient.ruleBase = rie.getRules().toString()+"\n"+rie.getFacts().toString();
                            HomeClient.available = true;

                            String tab[] = rie.getFacts().toString().split(",");
                            StringBuilder sb = new StringBuilder();
                            int count = 0;
                            for (int i = 0; i< rie.getFacts().size();i++){
                                if (tab[i].contains("Flight")){
                                    sb.append(tab[i].substring(9)+" - "+ "Air FRA"+" - "+flightPrice.get(tab[i].substring(9).trim())+"\n");
                                    count ++;
                                }
                            }
                            if (count>0){
                                HomeClient.listAirFr = new String(sb);
                            }

                            rie.clearFacts();


                        }else{
                            HomeClient.ruleBase = "Agent AirFrance\n"+rie.getRules().toString()+"\n"+rie.getFacts().toString();
                            HomeClient.available = null;
                        }
                    }

                    if ((chain.equals("backwardChain"))){
                        Vector<Clause> notProvCond = new Vector<>();
                        Clause conclusion = rie.infer("Available",notProvCond);

                        if (conclusion == null ){
                            HomeClient.ruleBase = rie.getRules().toString()+"\n"+rie.getFacts().toString();
                            HomeClient.available = null;
                        }else{
                            HomeClient.ruleBase = rie.getRules().toString()+"\n"+rie.getFacts().toString();
                            if(conclusion.getValue().equals("true")){
                                HomeClient.available = true;
                            }else{
                                HomeClient.available = null;
                            }
                        }
                    }

                }
            }
        });
    }

    public void getInferenceEngine(){
        Rule rule;
        ArrayList<Rule> rulesList = new ArrayList<>();
        rie = new KieRuleInferenceEngine();

        rule = new Rule("disp1");
        rule.addAntecedent(new EqualsClause("Flight","F-FR001"));
        rule.setConsequent(new EqualsClause("Available","true"));
        rie.addRule(rule);


        rule = new Rule("F-FR001");
        rule.addAntecedent(new EqualsClause("From","Algeria"));
        rule.addAntecedent(new EqualsClause("To","England"));
        rule.addAntecedent(new GEClause("Price","7000000"));
        rule.setConsequent(new EqualsClause("Flight","F-FR001"));
        rie.addRule(rule);



        rule = new Rule("disp2");
        rule.addAntecedent(new EqualsClause("Flight","F-FR002"));
        rule.setConsequent(new EqualsClause("Available","true"));
        rie.addRule(rule);



        rule = new Rule("F-FR002");
        rule.addAntecedent(new EqualsClause("From","Algeria"));
        rule.addAntecedent(new EqualsClause("To","England"));
        rule.setConsequent(new EqualsClause("Flight","F-FR002"));
        rie.addRule(rule);


    }
}
