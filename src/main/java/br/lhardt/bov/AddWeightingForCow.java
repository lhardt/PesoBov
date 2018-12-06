package br.lhardt.bov;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

public class AddWeightingForCow extends AppCompatActivity {

    private int cowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weighting_for_cow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cowId = getIntent().getIntExtra("cow_id", -1);
        if(cowId == -1) {
            Toast.makeText(this, "Erro! Identificação impossível: -1.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void submitWeighting(View view) {
        int weight = Format.stringToWeight(((EditText)findViewById(R.id.add_weight_weight)).getText().toString());
        String date = getDateString();
        if(weight == -1){
            Toast.makeText(this,"Insira o peso!", Toast.LENGTH_LONG).show();
        } else if( ! Format.isValidDate(date)){
            Toast.makeText(this, "Insira a data corretamente!", Toast.LENGTH_LONG).show();
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("weight", weight);
            returnIntent.putExtra("cow_id", cowId );
            returnIntent.putExtra("date", date);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    private String getDateString() {
        String dateStr;
        if(((Switch)findViewById(R.id.add_weight_switch)).isChecked()){
            /* If was weighted today, use today's date. */
            dateStr = Format.dateToString(new Date());
        } else {
            dateStr = ((EditText) findViewById(R.id.add_weight_date)).getText().toString();
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
        View editText = findViewById(R.id.add_weight_date);
        if(editText.getVisibility() == View.GONE) {
            editText.setVisibility(View.VISIBLE);
        } else {
            editText.setVisibility(View.GONE);
        }
    }
}
