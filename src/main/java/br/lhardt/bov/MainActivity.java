package br.lhardt.bov;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    protected List<Cow> allCows;
    protected CowAdapter mAdapter;



    public static int COW_ITEM_INTENT_CODE = 11;


    private static final int COW_INTENT_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showSplashScreen();

        setContentView(R.layout.activity_main);

        DatabaseHelper dbh = new DatabaseHelper(this);
        allCows = dbh.getAll();
        dbh.close();
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new CowAdapter(allCows,R.layout.cow_item, this);

        mRecyclerView.setAdapter(mAdapter);

        EditText searchEdit = findViewById(R.id.search_text_id);
        searchEdit.addTextChangedListener(
            new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence csq, int i, int i1, int i2){}

                @Override
                public void onTextChanged(CharSequence csq, int i, int i1, int i2){}

                @Override
                public void afterTextChanged(Editable ed){
                    filter(ed.toString());
                }
            }
        );
    }


    private void showSplashScreen() {
        Intent it = new Intent(this, SplashScreen.class);
        startActivity(it);

    }

    private void filter( String text ){
        ArrayList<Cow> filteredCows = new ArrayList<>();
        for( Cow c : allCows ) {
            if(Format.intToString(c.getId()).contains(text))
                filteredCows.add(c);
        }
        mAdapter.filterList(filteredCows);
//        document.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onExport( MenuItem it ) {
        boolean success = InformationExporter.exportToPdf(allCows, "relatorio.pdf", this );
        if( success ) {
            Toast.makeText(this, "PDF exportado!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Erro na exportação do PDF!", Toast.LENGTH_LONG).show();
        }
    }

    /// TODO: Fazer ele montar um pdf

    public void onCleanData( MenuItem it ){
        DatabaseHelper dbh = new DatabaseHelper( this );
        dbh.cleanAll();
        dbh.close();
        Toast.makeText(this, "Limpando todos os dados!", Toast.LENGTH_SHORT).show();
        allCows.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void openInsertCowDialog( View v ){
        Intent intent = new Intent(MainActivity.this, AddCowActivity.class);
        startActivityForResult(intent, COW_INTENT_CODE);
    }

    public void viewCow( int cowId ) {
        Toast.makeText(this, "Crash or works-ash", Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult( int reqCode, int resCode, Intent data ) {
        if( resCode == RESULT_OK && reqCode == COW_INTENT_CODE ) {
            try {
                int cowId = data.getIntExtra("id", -1);
                String bornDate = data.getStringExtra("date");
                Cow cow;
                Log.wtf("bornDate ", bornDate );
                if(bornDate.length() != 0)
                    cow = new Cow( cowId, Format.stringToDate(bornDate));
                else cow = new Cow(cowId, Format.NO_DATE);

                DatabaseHelper dbh = new DatabaseHelper(this);
                dbh.insertCow(cow);
                allCows.add(cow);
                dbh.close();
                mAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Inserido com sucesso!", Toast.LENGTH_LONG).show();
            } catch ( ParseException ex ) {
                Toast.makeText(this, "Erro inesperado... Não foi inserido", Toast.LENGTH_LONG).show();
            }

        }
        else if( resCode == RESULT_OK && reqCode == COW_ITEM_INTENT_CODE) {
            int idForDeletion = data.getIntExtra("cowIdForDeletion", -1);
            if(idForDeletion != -1){
                for( Cow c : allCows ){
                    if(c.getId() == idForDeletion){
                        allCows.remove(c);
                        break;
                    }
                }
                mAdapter.notifyDataSetChanged();
                DatabaseHelper dbh = new DatabaseHelper(this);
                dbh.removeCow(idForDeletion);
                dbh.close();
            }
        }
    }
}
