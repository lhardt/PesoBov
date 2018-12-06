package br.lhardt.bov;

import java.util.Date;

public class Weighting {
    /** In grams. Avoids floating point arithmetic */
    private final int weight;
    /** The date in which that weight was measured */
    private final Date date;
    /** The cow of which the weight was measured  */
    private final int cowId;

    public Weighting( int cowId, int cowWeight, Date measureDate) {
        this.weight = cowWeight;
        this.date = measureDate;
        this.cowId = cowId;
    }

    public int getWeight(){
        return weight;
    }
    public Date getMeasureDate(){
        return date;
    }
    public int getCowId(){
        return this.cowId;
    }

    @Override
    public String toString() {
        return "(" + Format.weightToString(weight) + "kg, " + Format.dateToString(date) + ")";
    }
}
