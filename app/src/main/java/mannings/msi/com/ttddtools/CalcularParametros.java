package mannings.msi.com.ttddtools;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static mannings.msi.com.ttddtools.Utils.Utilidades.arredondar;

public class CalcularParametros extends AppCompatActivity {

    private String selecionado,parametro;
    private String metro,peso,volume,caixa,estante,area;
    private TextView txtViewMetroLinear,txtViewPeso,txtViewVolume,txtViewCaixa,txtViewEstante,txtViewEstante1,txtViewArea;
    private Button btnVoltar;
    private float fparametro,fmetro,fpeso,fvolume,fcaixa,festante,farea;
    private int pesoMetroLinear;
    private float caixasMetroLinear;
    private int caixasEstante;
    private float areaEstante,volumeMetroLinear;
    private Toolbar toolbar;
    SharedPreferences preferences;
    private String texto_completo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_parametros);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtViewMetroLinear = (TextView)findViewById(R.id.textView0202);
        txtViewPeso = (TextView)findViewById(R.id.textView0302);
        txtViewVolume = (TextView)findViewById(R.id.textView0402);
        txtViewCaixa = (TextView)findViewById(R.id.textView0502);
        txtViewEstante1 = (TextView)findViewById(R.id.textView0601);
        txtViewEstante = (TextView)findViewById(R.id.textView0602);
        txtViewArea = (TextView)findViewById(R.id.textView0702);
        btnVoltar = (Button)findViewById(R.id.buttonVoltar);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            parametro= null;
            selecionado= null;
            caixasEstante = 36;
            areaEstante = 1.00f;
            volumeMetroLinear = 0.08f;
            pesoMetroLinear = 50;
            caixasMetroLinear = 0.14f;
        } else {
            parametro = extras.getString("parametro");
            selecionado = extras.getString("selecionado");
            pesoMetroLinear = extras.getInt("pesoMetroLinear");
            volumeMetroLinear = extras.getFloat("volumeMetroLinear");
            caixasMetroLinear = extras.getFloat("caixasMetroLinear");
            caixasEstante = extras.getInt("caixasEstante");
            areaEstante = extras.getFloat("areaEstante");
            String texto = "Estante ("+caixasEstante+" cx):";
            txtViewEstante1.setText(texto);

            if (selecionado.equals("Caixas Arquivo")){
                fcaixa = Float.valueOf(parametro);
                fmetro = fcaixa*caixasMetroLinear;
                fpeso = fmetro*pesoMetroLinear;
                fvolume = fmetro*volumeMetroLinear;
                festante = fcaixa/caixasEstante;
                farea = festante*areaEstante;

                //fmetro *= (Math.pow(10, 2));
                //fmetro = (float) Math.ceil(fmetro);
                //fmetro /= (Math.pow(10, 2));
                fmetro = arredondar(fmetro,2,0);
                fpeso = arredondar(fpeso,2,0);
                fvolume = arredondar(fvolume,3,0);
                fcaixa = arredondar(fcaixa,0,0);
                festante = arredondar(festante,0,1);
                farea = arredondar(farea,2,0);

                metro = String.valueOf(fmetro);
                txtViewMetroLinear.setText(metro);
                peso = String.valueOf(fpeso);
                txtViewPeso.setText(peso);
                volume =  String.valueOf(fvolume);
                txtViewVolume.setText(volume);
                txtViewCaixa.setText(parametro);
                caixa = parametro;
                estante = String.valueOf(festante);
                txtViewEstante.setText(estante);
                area = String.valueOf(farea);
                txtViewArea.setText(area);
            }
            else if (selecionado.equals("Metro Linear Total")){
                fmetro = Float.valueOf(parametro);
                fpeso = fmetro*pesoMetroLinear;
                fvolume = fmetro*volumeMetroLinear;
                fcaixa = fmetro/caixasMetroLinear;
                festante = fcaixa/caixasEstante;
                farea = festante*areaEstante;

                fmetro = arredondar(fmetro,2,0);
                fpeso = arredondar(fpeso,2,0);
                fvolume = arredondar(fvolume,3,0);
                fcaixa = arredondar(fcaixa,0,0);
                festante = arredondar(festante,0,1);
                farea = arredondar(farea,2,0);

                txtViewMetroLinear.setText(parametro);
                metro = parametro;
                peso = String.valueOf(fpeso);
                txtViewPeso.setText(peso);
                volume =  String.valueOf(fvolume);
                txtViewVolume.setText(volume);
                caixa = String.valueOf(fcaixa);
                txtViewCaixa.setText(caixa);
                estante = String.valueOf(festante);
                txtViewEstante.setText(estante);
                area = String.valueOf(farea);
                txtViewArea.setText(area);
            }
            else if (selecionado.equals("Área Física das Estantes")){
                farea = Float.valueOf(parametro);
                festante = farea / areaEstante;
                fcaixa = caixasEstante * festante;
                fmetro = fcaixa * caixasMetroLinear;
                fpeso = fmetro*pesoMetroLinear;
                fvolume = fmetro*volumeMetroLinear;

                fmetro = arredondar(fmetro,2,0);
                fpeso = arredondar(fpeso,2,0);
                fvolume = arredondar(fvolume,3,0);
                fcaixa = arredondar(fcaixa,0,0);
                festante = arredondar(festante,0,1);
                farea = arredondar(farea,2,0);

                metro = String.valueOf(fmetro);
                txtViewMetroLinear.setText(metro);
                peso = String.valueOf(fpeso);
                txtViewPeso.setText(peso);
                volume =  String.valueOf(fvolume);
                txtViewVolume.setText(volume);
                caixa = String.valueOf(fcaixa);
                txtViewCaixa.setText(caixa);
                estante = String.valueOf(festante);
                txtViewEstante.setText(estante);
                txtViewArea.setText(parametro);
                area = parametro;
            }
            else if (selecionado.equals("Volume (m3) Documental")){
                fvolume = Float.valueOf(parametro);
                fmetro = fvolume / volumeMetroLinear;
                fcaixa = fmetro / caixasMetroLinear;
                festante = fcaixa /caixasEstante;
                farea = areaEstante * festante;
                fpeso = fmetro*pesoMetroLinear;

                fmetro = arredondar(fmetro,2,0);
                fpeso = arredondar(fpeso,2,0);
                fvolume = arredondar(fvolume,3,0);
                fcaixa = arredondar(fcaixa,0,0);
                festante = arredondar(festante,0,1);
                farea = arredondar(farea,2,0);

                metro = String.valueOf(fmetro);
                txtViewMetroLinear.setText(metro);
                peso = String.valueOf(fpeso);
                txtViewPeso.setText(peso);
                txtViewVolume.setText(parametro);
                volume = parametro;
                caixa = String.valueOf(fcaixa);
                txtViewCaixa.setText(caixa);
                estante = String.valueOf(festante);
                txtViewEstante.setText(estante);
                area = String.valueOf(farea);
                txtViewArea.setText(area);
            }
            else if (selecionado.equals("Número de Estantes")){
                festante = Float.valueOf(parametro);
                fcaixa = festante*caixasEstante;
                fmetro = fcaixa*caixasMetroLinear;
                fvolume = volumeMetroLinear*fmetro;
                farea = areaEstante * festante;
                fpeso = fmetro*pesoMetroLinear;

                fmetro = arredondar(fmetro,2,0);
                fpeso = arredondar(fpeso,2,0);
                fvolume = arredondar(fvolume,3,0);
                fcaixa = arredondar(fcaixa,0,0);
                festante = arredondar(festante,0,1);
                farea = arredondar(farea,2,0);

                metro = String.valueOf(fmetro);
                txtViewMetroLinear.setText(metro);
                peso = String.valueOf(fpeso);
                txtViewPeso.setText(peso);
                volume = String.valueOf(fvolume);
                txtViewVolume.setText(volume);
                caixa = String.valueOf(fcaixa);
                txtViewCaixa.setText(caixa);
                txtViewEstante.setText(parametro);
                estante = parametro;
                area = String.valueOf(farea);
                txtViewArea.setText(area);
            }

            texto_completo = texto_completo.concat("Metro Linear (m): ").concat(metro).concat("\n").
                    concat("Peso (kg): ").concat(peso).concat("\n").
                    concat("Volume (m3): ").concat(volume).concat("\n").
                    concat("Caixas Arquivos: ").concat(caixa).concat("\n").
                    concat("Estantes: ").concat(estante).concat("\n").
                    concat("Área (m2): ").concat(area).concat("\n");
            //txtViewCaixa.setText(selecionado);

            //Toast.makeText(CalcularParametros.this,"parametro: "+parametro, Toast.LENGTH_SHORT).show();
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        inflater.inflate(R.menu.menu_copy,menu);

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
            case R.id.item_copy:
                TelaCopy();
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

    public void TelaCopy(){
        String st = "Parâmetros";
        st = st.concat(":\n").concat(texto_completo);
        ((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", st));
        Toast.makeText(CalcularParametros.this,"Texto copiado ",Toast.LENGTH_LONG).show();
        return;
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
