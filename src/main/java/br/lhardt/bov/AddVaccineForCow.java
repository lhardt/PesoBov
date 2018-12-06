package br.lhardt.bov;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

public class AddVaccineForCow extends AppCompatActivity {

    private int cowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccine_for_cow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cowId = getIntent().getIntExtra("cow_id", -1);
        if(cowId == -1) {
            Toast.makeText(this, "Erro! Identificação impossível: -1.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void submitVaccine(View view) {
        String name = ((EditText)findViewById(R.id.add_vacc_name)).getText().toString();
        String d = getDateString();
        if(name.length() == 0 ){
            Toast.makeText(this,"Insira o nome da vacina!", Toast.LENGTH_LONG).show();
        } else if( ! Format.isValidDate(d) ) {
            Toast.makeText(this, "Insira a data corretamente! Formato: " + Format.getDateFormat(), Toast.LENGTH_LONG).show();
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("date", d);
            returnIntent.putExtra("name", name);
            returnIntent.putExtra("cowId", cowId);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    private String getDateString() {
        String dateStr;
        if(((Switch)findViewById(R.id.add_vacc_switch)).isChecked()){
            /* If was vaccinated today, use today's date. */
            dateStr = Format.dateToString(new Date());
        } else {
            dateStr = ((EditText) findViewById(R.id.add_vacc_date)).getText().toString();
        }
        return dateStr;
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

    public void onSwitch( View v ){
        View editText = findViewById(R.id.add_vacc_date);
        if(editText.getVisibility() == View.GONE) {
            editText.setVisibility(View.VISIBLE);
        } else {
            editText.setVisibility(View.GONE);
        }
    }
}
