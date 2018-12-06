package br.lhardt.bov;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Cow {
	/** All the vaccines the cow has taken, together
	  * with the date they had been given */
    private List<Vaccine> vaccines;
	/** All the weights the cow has measured, together
	  * with the date they had been measured */
	private List<Weighting> weightings;
	/** The birth date of a cow. Might be null. */
    private final Date birthDate;
	/** The identification of a cow. */
    private final int id;

    public Cow( int cowId, Date date ) {
        this.vaccines = new ArrayList<>();
        this.weightings = new ArrayList<>();
        this.birthDate = date;
        this.id = cowId;
    }

    public void addVaccine( Vaccine v ){
        vaccines.add(v);
    }
    public void addWeighting( Weighting w ){
        weightings.add(w);
    }


    public List<Vaccine> getAllVaccines(){
        return new ArrayList(vaccines);
    }
    public List<Weighting> getAllWeightings(){
        return new ArrayList(weightings);
    }
    public Date getBirthDate(){
        return new Date(birthDate.getTime());
    }
    public int getId(){
        return id;
    }

    /* This method is use to print both to the program and to the pdf file. */
	@Override
	public String toString() {
		return  id + " (" + Format.dateToString(birthDate) + ", " + vaccines.size()
				   + " vaccines, " + weightings.size() + " weightings" + ")";
	}
}
