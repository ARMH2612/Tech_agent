package engine;

import enums.IntersectionType;

public class EqualsClause extends Clause {

    public EqualsClause(String variable, String value){
        super(variable, value);
        condition = "=";
    }

    @Override
    protected IntersectionType intersect(Clause rhs) {
        // For string values and classes : EqualsClause, RegexMatchClause, NegationClause.
        if(rhs instanceof EqualsClause){
            if(value.equals(rhs.getValue())){
                return IntersectionType.Inclusive; // equal
            } else {
                return IntersectionType.MutuallyExclusive; // not equal
            }
        } /*else if(rhs instanceof RegexMatchClause){
            RegexMatchClause rhs2 = (RegexMatchClause)rhs; // clause for patterns don't need it.
            return rhs2.intersect(this);
        }*/else if(rhs instanceof NegationClause){
            return rhs.intersect(this);
        }

        // For numeric values and classes.

        String v1 = value;
        String v2 = rhs.getValue();

        double a = 0;
        double b = 0;

        boolean isNumeric = true;
        try{
            a = Double.parseDouble(v1);
            b = Double.parseDouble(v2);
        }catch(NumberFormatException exception){
            isNumeric = false;
        }

        if(isNumeric){
            if(rhs instanceof LessClause) {
                if (a >= b) {
                    return IntersectionType.MutuallyExclusive; // false
                } else {
                    return IntersectionType.Inclusive; // true
                }
            } else if(rhs instanceof LEClause) {
                if(a > b) return IntersectionType.MutuallyExclusive; // false
                else return IntersectionType.Inclusive; // true
            } else if(rhs instanceof  GreaterClause){
                if(a <= b) return IntersectionType.MutuallyExclusive;// false
                else return IntersectionType.Inclusive; // true
            } else if(rhs instanceof  GEClause){
                if(a < b) return IntersectionType.MutuallyExclusive; // false
                else return IntersectionType.Inclusive; // true
            } else {
                return IntersectionType.Unknown;
            }
        } else {
            return IntersectionType.Unknown;
        }
    }
}
