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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mannings.msi.com.ttddtools.Utils.Utilidades;

public class EditarEstante extends AppCompatActivity {

    private int caixasEstante,pesoMetroLinear;
    private float caixasMetroLinear;
    private float areaEstante,volumeMetroLinear;
    private EditText edtVolume,edtPeso,edtCaixas,edtArea,edtCml;
    private String texto;
    private Button btnGravar;
    private Toolbar toolbar;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_estante);

        edtVolume = (EditText)findViewById(R.id.editTextVolumeMetroLinear);
        edtPeso = (EditText)findViewById(R.id.editTextPesoMetroLinear);
        edtCaixas = (EditText)findViewById(R.id.editTextCaixasEstante);
        edtArea = (EditText)findViewById(R.id.editTextAreaEstante);
        edtCml = (EditText)findViewById(R.id.editTextCaixasMetroLinear);
        btnGravar = (Button)findViewById(R.id.buttonGravar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            volumeMetroLinear = 0.08f;
            pesoMetroLinear = 50;
            caixasEstante = 36;
            areaEstante = 1.00f;
            caixasMetroLinear = 0.14f;
        } else {
            volumeMetroLinear = extras.getFloat("volumeMetroLinear");
            pesoMetroLinear = extras.getInt("pesoMetroLinear");
            caixasEstante = extras.getInt("caixasEstante");
            areaEstante = extras.getFloat("areaEstante");
            caixasMetroLinear = extras.getFloat("caixasMetroLinear");
        }

        edtVolume.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        edtPeso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        edtCaixas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        edtArea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });

        edtCml.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EscondeTeclado(v);
                }
            }
        });


        texto = String.valueOf(volumeMetroLinear);
        edtVolume.setText(texto);
        texto = String.valueOf(pesoMetroLinear);
        edtPeso.setText(texto);
        texto = String.valueOf(caixasEstante);
        edtCaixas.setText(texto);
        texto = String.valueOf(areaEstante);
        edtArea.setText(texto);
        texto = String.valueOf(caixasMetroLinear);
        edtCml.setText(texto);

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarEstante.this, Parametros.class);
                pesoMetroLinear = Integer.valueOf(edtPeso.getText().toString());
                caixasEstante = Integer.valueOf(edtCaixas.getText().toString());
                intent.putExtra("pesoMetroLinear", pesoMetroLinear);
                intent.putExtra("caixasEstante", caixasEstante); // não precisa testar pq android:inputType="number"

                boolean digitsOnly = Utilidades.testarCampo(edtCml);
                if (!digitsOnly) {
                    Toast.makeText(EditarEstante.this, "Valor inválido para " + "Metro Linear por caixa", Toast.LENGTH_LONG).show();
                } else {

                    caixasMetroLinear = Float.valueOf(edtCml.getText().toString());
                    intent.putExtra("caixasMetroLinear", caixasMetroLinear);
                    digitsOnly = Utilidades.testarCampo(edtArea);
                    if (!digitsOnly) {
                        Toast.makeText(EditarEstante.this, "Valor inválido para " + "Área", Toast.LENGTH_LONG).show();
                    } else {

                        areaEstante = Float.valueOf(edtArea.getText().toString());
                        intent.putExtra("areaEstante", areaEstante);

                        digitsOnly = Utilidades.testarCampo(edtVolume);
                        if (!digitsOnly) {
                            Toast.makeText(EditarEstante.this, "Valor inválido para " + "Volume", Toast.LENGTH_LONG).show();
                        } else {

                            volumeMetroLinear = Float.valueOf(edtVolume.getText().toString());
                            intent.putExtra("volumeMetroLinear", volumeMetroLinear);
                            try {
                                SQLiteDatabase db = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
                                db.execSQL("CREATE TABLE IF NOT EXISTS estante (id INTEGER, volume FLOAT, peso INTEGER, caixas INTEGER, area FLOAT, cml FLOAT);");
                                ContentValues valores = new ContentValues();
                                valores.put("id", 1); // esta tabela terá somente 1 registro
                                valores.put("volume", volumeMetroLinear);
                                valores.put("peso", pesoMetroLinear);
                                valores.put("caixas", caixasEstante);
                                valores.put("area", areaEstante);
                                valores.put("cml", caixasMetroLinear);
                                Cursor c = db.rawQuery("SELECT * FROM estante", null);
                                int count = c.getCount();
                                if (count > 0) {
                                    db.update("estante", valores, "id=1", null);
                                } else {
                                    db.insert("estante", null, valores);
                                }
                                db.close();
                            } catch (Exception e) {
                                Toast.makeText(EditarEstante.this, "Erro na base de dados (2)", Toast.LENGTH_LONG).show();
                            }

                            startActivity(intent);
                            finish();
                        }
                    }
                }
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
