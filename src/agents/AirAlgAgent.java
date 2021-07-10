package agents;

import engine.*;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import views.HomeClient;

import java.util.ArrayList;
import java.util.Vector;

public class AirAlgAgent extends Agent {
    RuleInferenceEngine rie = new KieRuleInferenceEngine();
    @Override
    protected void setup(){
        setEnabledO2ACommunication(true,0);

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                String info = (String) myAgent.getO2AObject();
                if(info != null){
                    System.out.println(" Air Algérie a reçu le formulaire.");
                    try{
                        getInferenceEngine();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    String t[] = info.split(" ");
                    String from = t[0],to = t[1],dateDep = t[2],dateRet=t[3],chain=t[4];
                    System.out.println("Agent Air Algerie : ");
                    System.out.println(from+"\n"+to+"\n"+dateDep+"\n"+dateRet+"\n"+chain);

                    if(!from.equals("")){
                        rie.addFact(new EqualsClause("From",from));
                    }
                    if(!to.equals("")){
                        rie.addFact(new EqualsClause("To",to));
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
                        }else{
                            HomeClient.ruleBase = rie.getRules().toString()+"\n"+rie.getFacts().toString();
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
        rule.addAntecedent(new EqualsClause("Company","Rahal"));
        rule.setConsequent(new EqualsClause("Available","true"));
        rie.addRule(rule);


        rule = new Rule("Rahal");
        rule.addAntecedent(new EqualsClause("From","Algeria"));
        rule.addAntecedent(new EqualsClause("To","England"));
        rule.setConsequent(new EqualsClause("Company","Rahal"));
        rie.addRule(rule);



        rule = new Rule("disp2");
        rule.addAntecedent(new EqualsClause("Flight","R400"));
        rule.setConsequent(new EqualsClause("Available","true"));
        rie.addRule(rule);



        rule = new Rule("Houssame");
        rule.addAntecedent(new EqualsClause("From","Algeria"));
        rule.addAntecedent(new EqualsClause("To","England"));
        rule.setConsequent(new EqualsClause("Company","Houssame"));
        rie.addRule(rule);


    }
}
