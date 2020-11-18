package mannings.msi.com.ttddtools;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import mannings.msi.com.ttddtools.Utils.Utilidades;

public class Parametros extends AppCompatActivity {

    private Spinner spinner;
    private Button btnCalcular,btnEstante,btnVoltar;
    private EditText edtParametro;
    private TextView txvtEstante;
    private String parametro,selecionado,texto;
    private int ultima_posicao;
    private int pesoMetroLinear,caixasEstante;
    private float caixasMetroLinear;
    private float areaEstante,volumeMetroLinear;
    private Toolbar toolbar;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros);
        spinner = (Spinner)findViewById(R.id.spinnerX);
        btnCalcular = (Button)findViewById(R.id.buttonCalcular);
        btnEstante = (Button)findViewById(R.id.buttonEstante);
        btnVoltar = (Button)findViewById(R.id.btnVoltar1);
        edtParametro = (EditText)findViewById(R.id.editTextParametro);
        txvtEstante = (TextView)findViewById(R.id.textViewEstante);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            bandejasEstante = 7;
            caixasEstante = 36;
            areaEstante = 1.01f;
        } else {
            bandejasEstante = extras.getInt("bandejasEstante");
            caixasEstante = extras.getInt("caixasEstante");
            areaEstante = extras.getFloat("areaEstante");
        }
        */

        edtParametro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        try {
            SQLiteDatabase db = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
            //db.execSQL("DROP TABLE IF EXISTS '" + "estante" + "'");
            //db.close();
            //db = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS estante (id INTEGER, volume FLOAT, peso INTEGER, caixas INTEGER, area FLOAT, cml FLOAT);");
            Cursor c = db.rawQuery("SELECT * FROM estante", null);
            int count = c.getCount();
            if (count>0) {
                c.moveToFirst();
                volumeMetroLinear = c.getFloat(c.getColumnIndex("volume"));
                pesoMetroLinear = c.getInt(c.getColumnIndex("peso"));
                caixasEstante = c.getInt(c.getColumnIndex("caixas"));
                areaEstante = c.getFloat(c.getColumnIndex("area"));
                caixasMetroLinear = c.getFloat(c.getColumnIndex("cml"));
            }
            else{
                volumeMetroLinear = 0.08f;
                pesoMetroLinear = 50;
                caixasEstante = 36;
                areaEstante = 1.00f;
                caixasMetroLinear = 0.14f;
                ContentValues valores = new ContentValues();
                valores.put("id",1);
                valores.put("volume",volumeMetroLinear);
                valores.put("peso",pesoMetroLinear);
                valores.put("caixas",caixasEstante);
                valores.put("area",areaEstante);
                valores.put("cml",caixasMetroLinear);
                db.insert("estante",null,valores);
            }
            db.close();
        }
        catch (Exception e){
            Toast.makeText(Parametros.this,"Erro na base de dados (4)",Toast.LENGTH_LONG).show();
        }


        texto = "Estante com "+caixasEstante+" caixas arquivo e área de "+areaEstante+" m2\n";
        texto = texto + "Cada caixa arquivo com "+caixasMetroLinear+" metros lineares\n";
        texto = texto + "Cada metro linear com "+pesoMetroLinear+" kg de peso\n";
        texto = texto + "Cada metro linear com "+volumeMetroLinear+" m3 de volume";
        txvtEstante.setText(texto);

        String[] strParametros = getResources().getStringArray(R.array.lista_parametros);

        //final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.lista_parametros, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ultima_posicao = position;
                long id_da_posicao = id;
                selecionado = parent.getItemAtPosition(position).toString();
                ultima_posicao=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Parametros.this,"Nenhum selecionado",Toast.LENGTH_LONG).show();
            }
        });

        btnEstante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Parametros.this,EditarEstante.class);
                intent.putExtra("volumeMetroLinear",volumeMetroLinear);
                intent.putExtra("pesoMetroLinear",pesoMetroLinear);
                intent.putExtra("caixasEstante",caixasEstante);
                intent.putExtra("areaEstante",areaEstante);
                intent.putExtra("caixasMetroLinear",caixasMetroLinear);
                startActivity(intent);
                finish();
            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MetroLinear.this,"Selecionado botão", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Parametros.this,CalcularParametros.class);
                boolean digitsOnly = Utilidades.testarCampo(edtParametro);
                if (!digitsOnly){
                    Toast.makeText(Parametros.this, "Valor inválido para o parâmetro", Toast.LENGTH_LONG).show();
                }
                else {
                    parametro = edtParametro.getText().toString();
                    intent.putExtra("parametro", parametro);
                    intent.putExtra("selecionado", selecionado);
                    intent.putExtra("volumeMetroLinear",volumeMetroLinear);
                    intent.putExtra("pesoMetroLinear",pesoMetroLinear);
                    intent.putExtra("caixasEstante",caixasEstante);
                    intent.putExtra("areaEstante",areaEstante);
                    intent.putExtra("caixasMetroLinear",caixasMetroLinear);
                    startActivity(intent);
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MetroLinear.this,"Selecionado "+position, Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public void EscondeTeclado (View v){
        if (v!=null){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
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
