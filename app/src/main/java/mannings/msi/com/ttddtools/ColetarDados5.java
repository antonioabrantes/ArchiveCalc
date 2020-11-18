package mannings.msi.com.ttddtools;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mannings.msi.com.ttddtools.Utils.Utilidades;

public class ColetarDados5 extends AppCompatActivity {

    private EditText edtAltura,edtComprimento,edtLargura,edtPacotes;
    private Button btnCalcular,btnCancelar;
    private int tipo;
    private float altura,comprimento,largura,total,pacotes;
    private Toolbar toolbar;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coletar_dados5);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtAltura = (EditText)findViewById(R.id.editText5Altura);
        edtComprimento = (EditText)findViewById(R.id.editText5Comprimento);
        edtLargura = (EditText)findViewById(R.id.editText5Largura);
        edtPacotes = (EditText)findViewById(R.id.editText5Pacotes);
        btnCalcular = (Button)findViewById(R.id.btn5Calcular);
        btnCancelar = (Button)findViewById(R.id.btn5Cancelar);

        Bundle extras = getIntent().getExtras();
        tipo = extras.getInt("tipo");

        edtAltura.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        edtComprimento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        edtLargura.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        edtPacotes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });


        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColetarDados5.this, CalcularDados.class);
                pacotes = Float.valueOf(edtPacotes.getText().toString());
                boolean digitsOnly = Utilidades.testarCampo(edtAltura);
                if (!digitsOnly){
                    Toast.makeText(ColetarDados5.this, "Valor inválido para "+"Altura do pacote", Toast.LENGTH_LONG).show();
                }
                else {
                    altura = Float.valueOf(edtAltura.getText().toString());
                    digitsOnly = Utilidades.testarCampo(edtComprimento);
                    if (!digitsOnly){
                        Toast.makeText(ColetarDados5.this, "Valor inválido para "+"Comprimento do pacote", Toast.LENGTH_LONG).show();
                    }
                    else {
                        comprimento = Float.valueOf(edtComprimento.getText().toString());
                        digitsOnly = Utilidades.testarCampo(edtLargura);
                        if (!digitsOnly){
                            Toast.makeText(ColetarDados5.this, "Valor inválido para "+"Largura do pacote", Toast.LENGTH_LONG).show();
                        }
                        else {
                            largura = Float.valueOf(edtLargura.getText().toString());
                            total = altura * comprimento * largura * 12;
                            try {
                                SQLiteDatabase banco = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
                                banco.execSQL("create table if not exists parametros (id INTEGER primary key autoincrement,tipo INTEGER,x FLOAT,y FLOAT,z FLOAT,w FLOAT);");
                                String sql = "insert into parametros (id,tipo,x,y,z,w) values (null," + tipo + "," + altura + "," + comprimento + "," + largura + ","+pacotes+");";
                                banco.execSQL(sql);
                                banco.close();
                            } catch (Exception e) {
                                Toast.makeText(ColetarDados5.this, "Erro na base de dados" + e.toString(), Toast.LENGTH_LONG).show();
                            }

                            //intent.putExtra("tipo", tipo);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
