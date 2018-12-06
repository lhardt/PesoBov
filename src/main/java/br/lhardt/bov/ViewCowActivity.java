package br.lhardt.bov;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

public class ViewCowActivity extends AppCompatActivity {
    /* dummy codes for identifying activity return values */
    private static final int WEIGHTING_INTENT_CODE = 13;
    private static final int VACCINATION_INTENT_CODE = 12;

    private Cow cow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cow);
        DatabaseHelper dbh = new DatabaseHelper(this);
        cow = dbh.getCow(Format.stringToInt(getIntent().getStringExtra("cowId")));
        dbh.close();
        if(cow == null) {
            finish(); return;
        }
        String vaccText = "";
        String weightText = "";

        for(Vaccine v : cow.getAllVaccines()){
            vaccText += v.toString() + "\n";
        }
        if(vaccText.length() > 0) {
            vaccText = vaccText.substring(0, vaccText.length()-1);
        }
        for(Weighting w : cow.getAllWeightings()){

            weightText += w.toString() + "\n";
        }
        if(weightText.length() > 0 ){
            weightText = weightText.substring(0, weightText.length()-1);
        }
        ((TextView)findViewById(R.id.cow_desc_vaccs)).setText(vaccText);
        ((TextView)findViewById(R.id.cow_desc_weightings)).setText(weightText);
        ((TextView)findViewById(R.id.cow_desc_id)).setText(Format.intToString(cow.getId()));
        ((TextView)findViewById(R.id.cow_desc_birthdate)).setText(Format.dateToString(cow.getBirthDate()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /* TODO: ao invés de usar nomes 'hardcoded', usar os nomes que a tabela dá.
        EX: DatabaseHelper.xTable.___COLUMN */
    public void onActivityResult( int reqCode, int resCode, Intent data ) {
        if( resCode == RESULT_OK && reqCode == VACCINATION_INTENT_CODE ) {
            try {
                Vaccine resultVaccine = new Vaccine(
                        data.getStringExtra("name"),
                        Format.stringToDate(data.getStringExtra("date")),
                        data.getIntExtra("cowId", -1)
                );
                addVaccine(resultVaccine);
            } catch( ParseException pex ) {
                /* A badly formed date should be catched in the AddVaccine activity. */
                Toast.makeText(this, "Erro Inesperado! Vacina não inserida", Toast.LENGTH_LONG ).show();
            }
        } else if( resCode == RESULT_OK && reqCode == WEIGHTING_INTENT_CODE ) {
            try {
                Weighting resultWeighting = new Weighting(
                        data.getIntExtra("cow_id", -1),
                        data.getIntExtra("weight", -1),
                        Format.stringToDate(data.getStringExtra("date"))
                );
                addWeighting(resultWeighting);

            } catch( ParseException pex ){
                /* A badly formed date should be catched in the AddVaccine activity. */
                Toast.makeText(this, "Erro Inesperado! Pesagem não inserida", Toast.LENGTH_LONG ).show();
            }
        }
    }

    private void addWeighting( Weighting weighting ) {
        DatabaseHelper dbh = new DatabaseHelper(this);
        dbh.insertWeighting(weighting);
        cow.addWeighting(weighting);
        dbh.close();

        ((TextView)findViewById(R.id.cow_desc_weightings)).append( "\n" + weighting.toString());

        Toast.makeText(this, "Pesagem inserida com sucesso!\n" + weighting.toString(), Toast.LENGTH_LONG ).show();
    }

    private void addVaccine( Vaccine vaccine ) {
        DatabaseHelper dbh = new DatabaseHelper(this);
        dbh.insertVaccine(vaccine);
        cow.addVaccine(vaccine);
        dbh.close();

        ((TextView)findViewById(R.id.cow_desc_vaccs)).append( "\n" + vaccine.toString());


        Toast.makeText(this, "Vacina inserida com sucesso!\n" + vaccine.toString(), Toast.LENGTH_LONG ).show();
    }

    /* After the user ends */
    public void requestAddWeighting( View v ) {
        Intent it = new Intent(ViewCowActivity.this, AddWeightingForCow.class);
        it.putExtra("cow_id", cow.getId());
        startActivityForResult(it, WEIGHTING_INTENT_CODE);
    }

    public void requestAddVaccine( View v ) {
        Intent it = new Intent(ViewCowActivity.this, AddVaccineForCow.class);
        it.putExtra("cow_id", cow.getId());
        startActivityForResult(it, VACCINATION_INTENT_CODE);
    }

    public void requestRemove( View v ){
        Intent resultIt = new Intent();
        resultIt.putExtra("cowIdForDeletion", this.cow.getId());
        setResult(RESULT_OK, resultIt);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch( item.getItemId() ) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
