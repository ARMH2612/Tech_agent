package engine;

import enums.ConditionType;
import enums.IntersectionType;

import java.util.ArrayList;
import java.util.List;

public class KieRuleInferenceEngine implements RuleInferenceEngine {
    protected List<Rule> rules = new ArrayList<>(); // ens of rules.
    protected WorkingMemory memory = new WorkingMemory(); // current facts.

    public KieRuleInferenceEngine(){
    	
    }

    @Override public void addRule(Rule rule){
        rules.add(rule);
    }

    @Override public void clearRules(){
        rules.clear();
    }

    //forward chaining
    @Override public void infer(){
        List<Rule> cs = null;
        do{
            cs = match(); // finding the applicable rules. 
            if(!cs.isEmpty()){
                if(!fireRule(cs)){ // firing all the rules that haven't been fired.
                    break;
                }
            }
        } while(!cs.isEmpty());
    }

    // backward chaining using only the info we have right now no question asking.
    @Override public Clause infer(String goalVariable, List<Clause> unproved_conditions){
        Clause conclusion = null;
        // the rules give the type of the vehicle.
        // so given the facts we have in the memory we test each type.
        // i.e. test each rule the one that has all the antecedents true 
        // we take its consequent.
        // unproved_conditions : we use it for asking questions.
        
        for(Rule rule : rules){
            rule.firstAntecedent();
            boolean goalReached = true;
            while(rule.hasNextAntecedent()){
                Clause antecedent = rule.nextAntecedent();

                if(!memory.isFact(antecedent)){
                    if(memory.isNotFact(antecedent)){ //conflict with what is already a fact.
                        goalReached = false;
                        break;
                    }else if(isFact(antecedent, unproved_conditions)){ //deduce to be a fact : DEPTH in the tree.
                        memory.addFact(antecedent);
                    } else { //deduce to not be a fact
                        goalReached = false;
                        break;
                    }
                }
            }
            if(goalReached){
                conclusion = rule.getConsequent();
                break;
            }

        }

        return conclusion;
    }

    @Override public RuleBuilder newRule(){
        return new RuleBuilder(this);
    }

    @Override public RuleBuilder newRule(String name){
        return new RuleBuilder(this, name);
    }

    @Override public void clearFacts(){
        memory.clearFacts();
    }

    @Override public boolean isFact(Clause goal, List<Clause> unproved_conditions){
        List<Rule> goalStack = new ArrayList<>();

        for(Rule rule : rules){
            Clause consequent = rule.getConsequent();
            IntersectionType it = consequent.matchClause(goal);

            if(it == IntersectionType.Inclusive){
                goalStack.add(rule);
            }
        }

        if(goalStack.isEmpty()){
            unproved_conditions.add(goal);
        } else {
            for(Rule rule : goalStack){
                rule.firstAntecedent();
                boolean goalReached = true;
                while(rule.hasNextAntecedent()){
                    Clause antecedent = rule.nextAntecedent();

                    if(!memory.isFact(antecedent)){// si il est dans les facts c'est bon sinon on essaye de le prouver.
                        if(memory.isNotFact(antecedent)){ // contradiction with the facts that we have.
                            goalReached = false;
                            break;
                        } else if(isFact(antecedent, unproved_conditions)){ //trying to prove the antecedent recursively. 
                            memory.addFact(antecedent); // if we do we add it to the facts we have and continue the process.
                        } else {
                            goalReached = false; // can't be proved => checking an other rule.
                            break;
                        }
                    }
                }

                if(goalReached){
                    return true;
                }
            }
        }

        return false;

    }


    protected boolean fireRule(List<Rule> conflictingRules){
        boolean hasRule2Fire = false;
        for(Rule rule : conflictingRules){
            if(!rule.isFired()){
                hasRule2Fire = true;
                rule.fire(memory);
            }
        }

        return hasRule2Fire;
    }

    @Override public WorkingMemory getKnowledgeBase(){
        return memory;
    }

    @Override public List<Clause> getFacts() { return memory.getFacts();}

    @Override public void addFact(Clause c){
        memory.addFact(c);
    }

    @Override public List<Clause> getFactsAboutVariable(String variable){
        List<Clause> facts = new ArrayList<>();
        for(Clause c : memory.getFacts()){
            if(c.getVariable().equalsIgnoreCase(variable)){
                facts.add(c);
            }
        }
        return facts;
    }

    @Override public List<Rule> match(){
        List<Rule> cs = new ArrayList<>();
        for(Rule rule : rules){
            if(rule.isTriggered(memory)){
                cs.add(rule);
            }
        }

        return cs;
    }

    @Override public List<Rule> getRules() {
        return rules;
    }

    @Override public void addFact(String name, String value) {
        addFact(new EqualsClause(name, value));
    }

    @Override public void addFact(String name, ConditionType conditionType, String value){
        Clause clause = null;
        switch(conditionType){
            case Equals:
                clause = new EqualsClause(name, value);
                break;
            case Less:
                clause = new LessClause(name, value);
                break;
            case LessEquals:
                clause = new LEClause(name, value);
                break;
            case Greater:
                clause = new GreaterClause(name, value);
                break;
            case GreaterEquals:
                clause = new GEClause(name, value);
                break;
            /*case Match:
                clause = new RegexMatchClause(name, value);
                break;*/
            case NotEquals:
                clause = new NegationClause(new EqualsClause(name, value));
                break;
            /*case NotMatch:
                clause = new NegationClause(new RegexMatchClause(name, value));
                break;*/
        }

        if(clause == null){
            return;
        }
        addFact(clause);
    }


    @Override public Rule getRule(int index) {
        return rules.get(index);
    }
}
