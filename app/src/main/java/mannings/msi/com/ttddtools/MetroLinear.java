package mannings.msi.com.ttddtools;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import mannings.msi.com.ttddtools.adapter.RegistroAdapter;

public class MetroLinear extends AppCompatActivity  {

    private ListView listView;
    private ArrayAdapter<TiposRegistros> adapter;
    private Toolbar toolbar;
    SharedPreferences preferences;
    private Button btnUltimos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_linear);
        toolbar = (Toolbar) findViewById(R.id.toolbarMetroLinear);
        btnUltimos = (Button) findViewById(R.id.buttonUltimos);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.listViewMetroLinear);
        ArrayList<TiposRegistros> list = new ArrayList<TiposRegistros>();
        list.clear();
        list.add(new TiposRegistros(0, "Documentos arquivados em caixas"));
        list.add(new TiposRegistros(1, "Documentos empilhados"));
        list.add(new TiposRegistros(2, "Documentos encadernados"));
        list.add(new TiposRegistros(3, "Documentos fichários ou arquivos"));
        list.add(new TiposRegistros(4, "Documentos em pacotes"));
        list.add(new TiposRegistros(5, "Documentos em montes"));

        adapter = new RegistroAdapter(MetroLinear.this, list );
        listView.setAdapter( adapter );
        adapter.notifyDataSetChanged();

        //SQLiteDatabase banco = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
        //banco.execSQL("DROP TABLE IF EXISTS parametros");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent = new Intent(MetroLinear.this, ColetarDados1.class);
                        break;
                    case 1:
                        intent = new Intent(MetroLinear.this, ColetarDados2.class);
                        break;
                    case 2:
                        intent = new Intent(MetroLinear.this, ColetarDados3.class);
                        break;
                    case 3:
                        intent = new Intent(MetroLinear.this, ColetarDados4.class);
                        break;
                    case 4:
                        intent = new Intent(MetroLinear.this, ColetarDados5.class);
                        break;
                    case 5:
                        intent = new Intent(MetroLinear.this, ColetarDados6.class);
                        break;
                    default:
                        break;
                }
                intent.putExtra("tipo", position+1);
                startActivity(intent);

            }
        });

        btnUltimos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MetroLinear.this, CalcularDados.class);
                intent.putExtra("tipo", 0);
                startActivity(intent);
            }
        });

    }

    protected void onStart(){
        super.onStart();
        preferences = getSharedPreferences("status_app", MODE_PRIVATE);
        if (preferences.getBoolean("app_encerrado",false)) finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_sair:
                preferences = getSharedPreferences("status_app", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("app_encerrado", true);
                editor.apply();
                finish();
                return true;
            case R.id.item_inicio:
                vaParaTelaInicial();
                return true;
            case R.id.item_about:
                TelaAbout();
                return true;
            case R.id.item_ajuda:
                TelaAjuda();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void vaParaTelaInicial(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void TelaAbout(){
        Intent intent = new Intent(getApplicationContext(),About.class);
        startActivity(intent);
        finish();
    }

    public void TelaAjuda(){
        Intent intent = new Intent(getApplicationContext(),Ajuda.class);
        startActivity(intent);
        finish();
    }

}
