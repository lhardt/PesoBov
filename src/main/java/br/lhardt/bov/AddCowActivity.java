package br.lhardt.bov;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class AddCowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        View editText = findViewById(R.id.cow_birth_edittext);
        if(editText.getVisibility() == View.GONE) {
            editText.setVisibility(View.VISIBLE);
        } else {
            editText.setVisibility(View.GONE);
        }
    }

    public void submitCow( View v ) {
        String cowid =  ((EditText)findViewById(R.id.cow_id_edittext)).getText().toString();
        String dateString = getDateString();
        if( cowid.length() == 0 ){
            Toast.makeText(this, "Preencha o campo da id. do gado!", Toast.LENGTH_LONG).show();
        } else if(dateString == null) {
            Toast.makeText(this, "Preencha o campo da data!", Toast.LENGTH_LONG).show();
        } else {
            Intent returnCow = new Intent();
            returnCow.putExtra("id", Integer.parseInt(cowid));
            returnCow.putExtra("date", dateString);
            setResult(Activity.RESULT_OK, returnCow);
            finish();
        }
    }


    private String getDateString(){
        boolean unknownBirth = ((Switch)findViewById(R.id.birth_unknown_switch)).isChecked();
        String birth = ((EditText)findViewById(R.id.cow_birth_edittext)).getText().toString();
        if( unknownBirth ) {
            return Format.UNKNOWN_DATE_STR;
        } else if(Format.isValidDate(birth)) {
            return birth;
        } else {
            return null;
        }
    }
}