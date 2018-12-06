package br.lhardt.bov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "sample_database";

    public class CowTable implements BaseColumns {
        private CowTable() {}

        public static final String TABLE_NAME = "cows";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date_birth";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DATE + " TEXT" + ");";

        public static final String DELETE_TABLE = "DROP TABLE " + TABLE_NAME + ";";

        public static final String DELETE_COW = "REMOVE * FROM " + TABLE_NAME + " WHERE ID = ?;";


    }
    public class VaccineTable implements BaseColumns {
        private VaccineTable() {}

        public static final String TABLE_NAME = "vaccines";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_COWID = "cow_id";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_COWID + " TEXT" + ");";

        public static final String DELETE_TABLE = "DROP TABLE " + TABLE_NAME + ";";
    }
    public class WeightingTable implements BaseColumns{
        private WeightingTable() {}

        public static final String TABLE_NAME = "weightings";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_COWID = "cow_id";
        public static final String COLUMN_DATE = "date";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_WEIGHT + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_COWID + " TEXT" + ");";

        public static final String DELETE_TABLE = "DROP TABLE " + TABLE_NAME + ";";
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase d = getWritableDatabase();
        d.execSQL(CowTable.CREATE_TABLE);
        d.execSQL(VaccineTable.CREATE_TABLE);
        d.execSQL(WeightingTable.CREATE_TABLE);


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CowTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeightingTable.TABLE_NAME);
        sqLiteDatabase.execSQL(VaccineTable.CREATE_TABLE);
        onCreate(sqLiteDatabase);
    }

    /* Insert a row on the database(s) */
    public void insertCow( Cow c ) {
        ContentValues vals = new ContentValues();
        vals.put(CowTable.COLUMN_DATE, Format.dateToString(c.getBirthDate()));
        vals.put(CowTable.COLUMN_NAME, c.getId());

        this.getWritableDatabase().insert(CowTable.TABLE_NAME, null, vals);
    }

    public void insertVaccine( Vaccine v ) {
        ContentValues vals = new ContentValues();
        vals.put(VaccineTable.COLUMN_DATE, Format.dateToString(v.getDate()));
        vals.put(VaccineTable.COLUMN_NAME, v.getName());
        vals.put(VaccineTable.COLUMN_COWID, v.getCowId());

        this.getWritableDatabase().insert(VaccineTable.TABLE_NAME, null, vals);
    }

    public void insertWeighting( Weighting w ) {
        ContentValues vals = new ContentValues();
        vals.put(WeightingTable.COLUMN_COWID, w.getCowId());
        vals.put(WeightingTable.COLUMN_DATE, Format.dateToString(w.getMeasureDate()));
        vals.put(WeightingTable.COLUMN_WEIGHT, w.getWeight() );

        this.getWritableDatabase().insert(WeightingTable.TABLE_NAME, null, vals);
    }

    public List<Cow> getAll(){
        List<Cow> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cC = db.rawQuery("select * from " + CowTable.TABLE_NAME, null);
        if(cC.moveToFirst()){
            do {
                int id = cC.getInt(cC.getColumnIndex(CowTable.COLUMN_NAME));
                Date date;
                try {
                    date = Format.stringToDate(cC.getString(cC.getColumnIndex(CowTable.COLUMN_DATE)));
                } catch( ParseException pex ){
                    date = Format.NO_DATE;
                }
                list.add(new Cow(id, date));
            } while(cC.moveToNext());
        }
        cC.close();

        Cursor cV = db.rawQuery("select * from " + VaccineTable.TABLE_NAME, null);
        if(cV.moveToFirst()){
            do {
                int id = cV.getInt(cV.getColumnIndex(VaccineTable.COLUMN_COWID));
                for( Cow c : list ) {
                    if(c.getId() == id ) {
                        String vaccName = cV.getString(cV.getColumnIndex(VaccineTable.COLUMN_NAME));
                        String vaccDate = cV.getString(cV.getColumnIndex(VaccineTable.COLUMN_DATE));
                        try {
                            c.addVaccine(new Vaccine(vaccName, Format.stringToDate(vaccDate), id));
                        } catch( ParseException p ){
                            c.addVaccine(new Vaccine(vaccName, Format.NO_DATE, id));
                        }

                    }
                }
            } while( cV.moveToNext());
        }
        cV.close();

        Cursor cw = db.rawQuery("select * from " + WeightingTable.TABLE_NAME, null);
        if(cw.moveToFirst()){
            do {
                int id = cw.getInt(cw.getColumnIndex(WeightingTable.COLUMN_COWID));
                for( Cow c : list ) {
                    if(c.getId() == id ) {
                        int weight = cw.getInt(cw.getColumnIndex(WeightingTable.COLUMN_WEIGHT));
                        String weightingDate = cw.getString(cw.getColumnIndex(WeightingTable.COLUMN_DATE));

                        try {
                            c.addWeighting(new Weighting(id, weight, Format.stringToDate(weightingDate)));
                        } catch( ParseException p ){
                            c.addWeighting(new Weighting(id, weight, Format.NO_DATE));
                        }

                    }
                }
            } while( cw.moveToNext());
        }
        cw.close();

        return list;
    }

    public Cow getCow(int cowId){
        List<Cow> allCows = getAll();
        for( Cow cow : allCows) {
            if(cow.getId() == cowId)
                return cow;
        }
        return null;
    }

    public void cleanAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(CowTable.DELETE_TABLE);
        db.execSQL(VaccineTable.DELETE_TABLE);
        db.execSQL(WeightingTable.DELETE_TABLE);
    }

    public void removeCow( int cowId ) {
        getWritableDatabase().delete(VaccineTable.TABLE_NAME,
                VaccineTable.COLUMN_COWID + "= " + Integer.toString(cowId), null);
        getWritableDatabase().delete(WeightingTable.TABLE_NAME,
                WeightingTable.COLUMN_COWID + "= " + Integer.toString(cowId), null);
        getWritableDatabase().delete(CowTable.TABLE_NAME,
                CowTable.COLUMN_NAME + "= " + Integer.toString(cowId), null);
    }
// TODO:
//    /* Remove one or more rows in the database. */
//   DONE: public void removeCow( int cowId ) throws SQLException;
//   DONE: public void removeAll( ) throws SQLException;
//
//    /* Get one or more rows from the database */
//   DONE: public Cow getCow(int cowId) throws SQLException;
//   DONE: public List<Cow> getAll( );

}
