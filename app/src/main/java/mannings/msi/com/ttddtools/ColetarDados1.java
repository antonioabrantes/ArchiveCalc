package mannings.msi.com.ttddtools;

import android.content.ContentValues;
import android.content.DialogInterface;
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

import static android.support.v4.content.ContextCompat.getSystemService;

public class ColetarDados1 extends AppCompatActivity {

    private EditText edtExtensao1,edtPrateleiras1,edtExtensao2,edtPrateleiras2;
    private Button btnCalcular,btnCancelar;
    private int tipo;
    private int prateleiras1,prateleiras2;
    private float extensao1,extensao2,total;
    String sql;
    private Toolbar toolbar;
    SharedPreferences preferences;
    View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coletar_dados1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtExtensao1 = (EditText)findViewById(R.id.editText1Extensao1);
        edtPrateleiras1 = (EditText)findViewById(R.id.editText1Prateleiras1);
        edtExtensao2 = (EditText)findViewById(R.id.editText1Extensao2);
        edtPrateleiras2 = (EditText)findViewById(R.id.editText1Prateleiras2);
        btnCalcular = (Button)findViewById(R.id.btn1Calcular);
        btnCancelar = (Button)findViewById(R.id.btn1Cancelar);

        // para funcionar tem que ajustar activity_coletar_dados1.xml para
        //    android:clickable="true"
        //    android:focusableInTouchMode="true"
        // https://www.youtube.com/watch?v=Cj_uOYhxbgU
        // icone ic_copy https://www.materialui.co/icon/content-copy
        edtExtensao1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                    }
            }
        });

        edtExtensao2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        edtPrateleiras1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        edtPrateleiras2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        tipo = extras.getInt("tipo");

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColetarDados1.this, CalcularDados.class);

                //prateleiras1 = Integer.parseInt(edtPrateleiras1.getText().toString());
                boolean digitsOnly = Utilidades.testarCampo(edtPrateleiras1);
                if (!digitsOnly){
                    Toast.makeText(ColetarDados1.this, "Valor inválido para "+"Número de prateleiras A", Toast.LENGTH_LONG).show();
                }
                else {
                    prateleiras1 = Integer.valueOf(edtPrateleiras1.getText().toString()); // não precisa crítica pois android:inputType="number"
                    digitsOnly = Utilidades.testarCampo(edtPrateleiras2);
                    if (!digitsOnly) {
                        Toast.makeText(ColetarDados1.this, "Valor inválido para " + "Número de prateleiras B", Toast.LENGTH_LONG).show();
                    } else {
                        prateleiras2 = Integer.valueOf(edtPrateleiras2.getText().toString());
                        digitsOnly = Utilidades.testarCampo(edtExtensao1);
                        if (!digitsOnly) {
                            Toast.makeText(ColetarDados1.this, "Valor inválido para " + "Extensão A", Toast.LENGTH_LONG).show();
                        } else {
                            extensao1 = Float.valueOf(edtExtensao1.getText().toString());
                            digitsOnly = Utilidades.testarCampo(edtExtensao2);
                            if (!digitsOnly) {
                                Toast.makeText(ColetarDados1.this, "Valor inválido para " + "Extensão B", Toast.LENGTH_LONG).show();
                            } else {
                                extensao2 = Float.valueOf(edtExtensao2.getText().toString());
                                total = prateleiras1 * extensao1 + prateleiras2 * extensao2;
                                try {
                                    SQLiteDatabase banco = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
                                    //banco.execSQL("DROP TABLE IF EXISTS parametros");
                                    banco.execSQL("CREATE TABLE IF NOT EXISTS parametros (id INTEGER PRIMARY KEY AUTOINCREMENT,tipo INTEGER,x FLOAT,y FLOAT,z FLOAT,w FLOAT);");

                                    ContentValues valores = new ContentValues();
                                    valores.put("tipo", tipo);
                                    valores.put("x", extensao1);
                                    valores.put("y", prateleiras1);
                                    valores.put("z", extensao2);
                                    valores.put("w", prateleiras2);
                                    banco.insert("parametros", null, valores);
                                    banco.close();
                                    sql = "insert into parametros (id,tipo,x,y,z,w) values (null," + tipo + "," + extensao1 + "," + prateleiras1 + "," + extensao2 + "," + prateleiras2 + ");";
                                    //banco.execSQL(sql);
                                } catch (Exception e) {
                                    Toast.makeText(ColetarDados1.this, "Erro na base de dados" + e.toString(), Toast.LENGTH_LONG).show();
                                }

                                //intent.putExtra("tipo", tipo);
                                intent.putExtra("sql", sql);
                                startActivity(intent);
                            }
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
