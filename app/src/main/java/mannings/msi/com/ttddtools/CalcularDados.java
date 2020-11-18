package mannings.msi.com.ttddtools;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mannings.msi.com.ttddtools.Utils.Utilidades;

import java.util.ArrayList;

public class CalcularDados extends AppCompatActivity {

    private String sql;
    private float total,somaTotal;
    private TextView txtViewMetroLinear;
    private ListView listView;
    private ArrayList<String> mensagens;
    private ArrayAdapter<String> adapter;
    private String[] descricao;
    int idd[] = new int[200];
    private Toolbar toolbar;
    SharedPreferences preferences;
    private String texto_completo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_dados);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnVoltar = (Button)findViewById(R.id.buttonZVoltar);
        listView = (ListView) findViewById(R.id.listViewZ);
        txtViewMetroLinear = (TextView) findViewById(R.id.textViewMetroLinear);

        Bundle extras = getIntent().getExtras();
        if (extras==null){
            sql = "vazio";
        }
        else {
            sql = extras.getString("sql");
        }
        //Toast.makeText(this, "sql= "+sql, Toast.LENGTH_LONG).show();

        mensagens = new ArrayList<String>();
        adapter = new ArrayAdapter(CalcularDados.this, android.R.layout.simple_list_item_1, mensagens);
        listView.setAdapter(adapter);
        mensagens.clear();
        adapter.notifyDataSetChanged();

        try {
            SQLiteDatabase db = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS parametros (id INTEGER PRIMARY KEY AUTOINCREMENT,tipo INTEGER,x FLOAT,y FLOAT,z FLOAT,w FLOAT);");
            Cursor c = db.rawQuery("SELECT * FROM parametros", null);
            int count = c.getCount();
            //int idd[] = new int[count];
            int tipo[] = new int[count];
            float x[] = new float[count];
            float y[] = new float[count];
            float z[] = new float[count];
            float w[] = new float[count];
            int i = 0;
            somaTotal = 0;
            if (c.getCount() > 0) {
                c.moveToFirst();
                TextView text;
                String texto;
                do {
                    idd[i] = c.getInt(c.getColumnIndex("id"));
                    texto = "Id: "+idd[i]+"\n";
                    texto = "";

                    tipo[i] = c.getInt(c.getColumnIndex("tipo"));
                    if (tipo[i]==1) {
                        texto = texto + "Tipo: Documentos arquivados em caixas \n";

                        x[i] = c.getFloat(c.getColumnIndex("x"));
                        texto = texto + "Extensão A: " + x[i] + " (m) \n";

                        y[i] = c.getFloat(c.getColumnIndex("y"));
                        float p1 = Utilidades.arredondar(y[i],0,0);
                        texto = texto + "Prateleiras A: " + p1 + "\n";

                        z[i] = c.getFloat(c.getColumnIndex("z"));
                        texto = texto + "Extensão B: " + z[i] + " (m) \n";

                        w[i] = c.getFloat(c.getColumnIndex("w"));
                        float p2 = Utilidades.arredondar(w[i],0,0);
                        texto = texto + "Prateleiras B: " + p2 + "\n";

                        total = y[i]*x[i] + w[i]*z[i]; // total = prateleiras1*extensao1 + prateleiras2*extensao2;
                        total = Utilidades.arredondar(total,2,0);
                        texto = texto + "Metro Linear: " + total + " (m) \n";
                    }
                    else if (tipo[i]==2) {
                        texto = texto + "Tipo: Documentos empilhados \n";

                        x[i] = c.getFloat(c.getColumnIndex("x"));
                        texto = texto + "Altura da 1° pilha de docs: " + x[i] + " (m) \n";

                        y[i] = c.getFloat(c.getColumnIndex("y"));
                        texto = texto + "Altura da 2° pilha de docs: " + y[i] + " (m) \n";

                        total = x[i] +  y[i]; // total = altura1 + altura2;
                        total = Utilidades.arredondar(total,2,0);
                        texto = texto + "Metro Linear: " + total + " (m) \n";
                    }
                    else if (tipo[i]==3) {
                        texto = texto + "Tipo: Documentos encardenados \n";

                        x[i] = c.getFloat(c.getColumnIndex("x"));
                        texto = texto + "Altura da pilha de docs: " + x[i] + " (m) \n";

                        y[i] = c.getFloat(c.getColumnIndex("y"));
                        texto = texto + "Extensão da pilha de docs: " + y[i] + " (m) \n";

                        total = x[i] +  y[i]; // total = altura + extensao;
                        total = Utilidades.arredondar(total,2,0);
                        texto = texto + "Metro Linear: " + total + " (m) \n";
                    }
                    else if (tipo[i]==4) {
                        texto = texto + "Tipo: Documentos fichários ou arquivos \n";

                        x[i] = c.getFloat(c.getColumnIndex("x"));
                        texto = texto + "Profundidade da 1° gaveta: " + x[i] + " (m) \n";

                        y[i] = c.getFloat(c.getColumnIndex("y"));
                        texto = texto + "Profundidade da 2° gaveta: " + y[i] + " (m) \n";

                        total = x[i] +  y[i]; // total = profundidade1 + profundidade2;
                        total = Utilidades.arredondar(total,2,0);
                        texto = texto + "Metro Linear: " + total + " (m) \n";
                    }
                    else if (tipo[i]==5) {
                        texto = texto + "Tipo: Documentos em pacotes \n";

                        x[i] = c.getFloat(c.getColumnIndex("x"));
                        texto = texto + "Altura do pacote: " + x[i] + " (m) \n";

                        y[i] = c.getFloat(c.getColumnIndex("y"));
                        texto = texto + "Comprimento do pacote: " + y[i] + " (m) \n";

                        z[i] = c.getFloat(c.getColumnIndex("z"));
                        texto = texto + "Largura do pacote: " + z[i] + " (m) \n";

                        w[i] = c.getFloat(c.getColumnIndex("w"));
                        texto = texto + "Número de pacotes: " + w[i] + " \n";

                        total = x[i]*y[i]*z[i]*12*w[i]; // total = altura*comprimento*largura*12*pacotes;
                        total = Utilidades.arredondar(total,2,0);
                        texto = texto + "Metro Linear: " + total + " (m) \n";
                    }
                    else if (tipo[i]==6) {
                        texto = texto + "Tipo: Documentos em montes \n";

                        x[i] = c.getFloat(c.getColumnIndex("x"));
                        texto = texto + "Altura do monte: " + x[i] + " (m) \n";

                        y[i] = c.getFloat(c.getColumnIndex("y"));
                        texto = texto + "Comprimento do monte: " + y[i] + " (m) \n";

                        z[i] = c.getFloat(c.getColumnIndex("z"));
                        texto = texto + "Largura do monte: " + z[i] + " (m) \n";

                        total = x[i]*y[i]*z[i]*12; // total = altura*comprimento*largura*12;
                        total = Utilidades.arredondar(total,2,0);
                        texto = texto + "Metro Linear: " + total + " (m) \n";
                    }

                    mensagens.add(texto);
                    texto_completo = texto_completo+texto+"\n";
                    adapter.notifyDataSetChanged();
                    somaTotal = somaTotal + total;

                    i++;
                } while (c.moveToNext());
                c.close();
                somaTotal = Utilidades.arredondar(somaTotal,2,0);
                texto = "Metro Linear Total: "+String.valueOf(somaTotal)+ " metros";
                texto_completo = texto_completo+texto;
                txtViewMetroLinear.setText(texto);
            } else {
                String texto = "Lista vazia !";
                txtViewMetroLinear.setText(texto);
            }
            db.close();
        }
        catch (Exception e){
            Toast.makeText(CalcularDados.this, "Erro na base de dados" + e.toString(), Toast.LENGTH_LONG).show();
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(CalcularDados.this, MetroLinear.class);
                startActivity(intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CalcularDados.this, DeletarDados.class);
                intent.putExtra("position", idd[position]);
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
        String st = "Registros";
        st = st.concat(":\n").concat(texto_completo);
        ((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", st));
        Toast.makeText(CalcularDados.this,"Texto copiado ",Toast.LENGTH_LONG).show();
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
