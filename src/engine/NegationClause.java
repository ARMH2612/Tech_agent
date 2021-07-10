package engine;

import enums.IntersectionType;

public class NegationClause extends Clause {
	private Clause origin;
    
    public NegationClause(Clause c){
        super(c.getVariable(), c.getValue());
        condition = "!=";
        origin = c;
    }

    @Override
    protected IntersectionType intersect(Clause rhs) {
        IntersectionType r = origin.intersect(rhs); // evaluer par rapport à l'egalité et inversé le resultat. 
        if(r == IntersectionType.Inclusive){
            return IntersectionType.MutuallyExclusive;
        } else if(r == IntersectionType.MutuallyExclusive) {
            return IntersectionType.Inclusive;
        }
        return IntersectionType.Unknown;
    }

    @Override
    public String toString(){
        return origin.getVariable() + " !" + origin.getCondition() + " " + origin.getValue();
    }
}
