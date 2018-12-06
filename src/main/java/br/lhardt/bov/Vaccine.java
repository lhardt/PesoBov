package br.lhardt.bov;

import java.util.Date;

/**
 * The description of a vaccine given to a cow.
 *
 *
 **/
public class Vaccine {
    /** The name of the vaccine given */
    private final String name;
    /** The date in which the vaccination occurred */
    private final Date date;
    /** The id of the cow which was vaccinated */
    private final int cowId;

    public Vaccine( String name, Date date, int cowId ){
        this.name = name;
        this.date = date;
        this.cowId = cowId;
    }

    public String getName(){
        return name;
    }
    public Date getDate(){
        return date;
    }
    public int getCowId(){
        return cowId;
    }

    @Override
    public String toString(){
        return "(" + name + ", " + Format.dateToString(date)  + ");";
    }
}
