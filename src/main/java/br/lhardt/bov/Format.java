package br.lhardt.bov;

import android.content.Context;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.DateFormat;

/** The format in which the dates and weights are stored in the SQL Database are
 different from the data structures that java presents us. Here we convert from
 one to the other;
 */
public class Format {
    public static final String dateFormatStr = "dd/MM/yyyy";
    public static final DateFormat df = new SimpleDateFormat(dateFormatStr);
    public static final Date NO_DATE = new Date(0);
    public static final String UNKNOWN_DATE_STR = "Data desconhecida";
    public static final int NO_INT = -1;

    public static String dateToString( Date d ){
        if(d == null || d.compareTo(NO_DATE) == 0)
            return UNKNOWN_DATE_STR;
        return df.format(d);
    }
    public static Date stringToDate( String s ) throws ParseException {
        if(s == null || s.equals(UNKNOWN_DATE_STR))
            return NO_DATE;
        return df.parse(s);
    }

    public static boolean isValidDate(String dateString) {
        try {
            Date d = stringToDate(dateString);
        } catch( ParseException pex ){
            return false;
        }
        return true;
    }

    public static String getDateFormat() {
        return dateFormatStr;
    }

    public static int stringToWeight( String s ) {
        if(s.length() == 0)
            return -1;
        return floatToWeight(Double.valueOf(s));
    }
    public static int floatToWeight( double d ){
        return (int) Math.round(1000*d);
    }
    public static String weightToString( int weight ) {
        return String.valueOf((float) weight / 1000.0);
    }
    public static String intToString( int i ) { return NumberFormat.getInstance().format(i);}
    public static int stringToInt( String s ) {
        try {
            return NumberFormat.getInstance().parse(s).intValue();
        } catch( ParseException ex ) {
            return NO_INT;
        }
    }
}