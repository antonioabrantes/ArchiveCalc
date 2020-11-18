package mannings.msi.com.ttddtools;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mannings.msi.com.ttddtools.Utils.Utilidades;

import static mannings.msi.com.ttddtools.Utils.Utilidades.arredondar;

public class DeletarDados extends AppCompatActivity {

    private int position;
    private float total;
    private String texto;
    private Button btnDeletar,btnCancelDeletar,btnDeletarTodos;
    private TextView txtDeletar;
    private Toolbar toolbar;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletar_dados);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btnDeletar = (Button)findViewById(R.id.buttonDeletar);
        btnDeletarTodos = (Button)findViewById(R.id.buttonDeletarTodos);
        btnCancelDeletar = (Button)findViewById(R.id.buttonCancelDeletar);
        txtDeletar = (TextView) findViewById(R.id.textViewDeletar);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            position = 0;
        } else {
            position = extras.getInt("position");
            //Toast.makeText(this, "position= " + position, Toast.LENGTH_LONG).show();
            try {
                SQLiteDatabase db = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
                String sql = "SELECT * FROM parametros WHERE id="+position;
                //sql = "SELECT * FROM parametros";
                Cursor c = db.rawQuery(sql, null);
                c.moveToFirst();

                int i = 0;
                int idd[] = new int[1];
                int tipo[] = new int[1];
                float x[] = new float[1];
                float y[] = new float[1];
                float z[] = new float[1];
                float w[] = new float[1];

                idd[i] = c.getInt(c.getColumnIndex("id"));
                tipo[i] = c.getInt(c.getColumnIndex("tipo"));
                x[i] = c.getFloat(c.getColumnIndex("x"));
                y[i] = c.getFloat(c.getColumnIndex("y"));
                z[i] = c.getFloat(c.getColumnIndex("z"));
                w[i] = c.getFloat(c.getColumnIndex("w"));

                texto = "Id: "+idd[i]+"\n";
                texto = "";
                if (tipo[i]==1) {
                    texto = texto + "Tipo: Documentos arquivados em caixas \n";
                    texto = texto + "Extensão A: " + x[i] + "\n";
                    texto = texto + "Prateleiras A: " + y[i] + "\n";
                    texto = texto + "Extensão B: " + z[i] + "\n";
                    texto = texto + "Prateleiras B: " + w[i] + "\n";
                    total = y[i]*x[i] + w[i]*z[i]; // total = prateleiras1*extensao1 + prateleiras2*extensao2;
                    total = arredondar(total,2,0);
                    texto = texto + "Metro Linear: " + total + "\n";
                }
                else if (tipo[i]==2) {
                    texto = texto + "Tipo: Documentos empilhados \n";

                    x[i] = c.getFloat(c.getColumnIndex("x"));
                    texto = texto + "Altura da 1° pilha de docs: " + x[i] + "\n";

                    y[i] = c.getFloat(c.getColumnIndex("y"));
                    texto = texto + "Altura da 2° pilha de docs: " + y[i] + "\n";

                    total = x[i] +  y[i]; // total = altura1 + altura2;
                    total = arredondar(total,2,0);
                    texto = texto + "Metro Linear: " + total + "\n";
                }
                else if (tipo[i]==3) {
                    texto = texto + "Tipo: Documentos encardenados \n";

                    x[i] = c.getFloat(c.getColumnIndex("x"));
                    texto = texto + "Altura da pilha de docs: " + x[i] + "\n";

                    y[i] = c.getFloat(c.getColumnIndex("y"));
                    texto = texto + "Extensão da pilha de docs: " + y[i] + "\n";

                    total = x[i] +  y[i]; // total = altura + extensao;
                    total = arredondar(total,2,0);
                    texto = texto + "Metro Linear: " + total + "\n";
                }
                else if (tipo[i]==4) {
                    texto = texto + "Tipo: Documentos fichários ou arquivos \n";

                    x[i] = c.getFloat(c.getColumnIndex("x"));
                    texto = texto + "Profundidade da 1° gaveta: " + x[i] + "\n";

                    y[i] = c.getFloat(c.getColumnIndex("y"));
                    texto = texto + "Profundidade da 2° gaveta: " + y[i] + "\n";

                    total = x[i] +  y[i]; // total = profundidade1 + profundidade2;
                    total = arredondar(total,2,0);
                    texto = texto + "Metro Linear: " + total + "\n";
                }
                else if (tipo[i]==5) {
                    texto = texto + "Tipo: Documentos em pacotes \n";

                    x[i] = c.getFloat(c.getColumnIndex("x"));
                    texto = texto + "Altura do pacote: " + x[i] + "\n";

                    y[i] = c.getFloat(c.getColumnIndex("y"));
                    texto = texto + "Comprimento do pacote: " + y[i] + "\n";

                    z[i] = c.getFloat(c.getColumnIndex("z"));
                    texto = texto + "Largura do pacote: " + z[i] + "\n";

                    w[i] = c.getFloat(c.getColumnIndex("w"));
                    texto = texto + "Número de pacotes: " + w[i] + " \n";

                    total = x[i]*y[i]*z[i]*12*w[i]; // total = altura*comprimento*largura*12*pacotes;
                    total = arredondar(total,2,0);
                    texto = texto + "Metro Linear: " + total + " (m) \n";

                }
                else if (tipo[i]==6) {
                    texto = texto + "Tipo: Documentos em montes \n";

                    x[i] = c.getFloat(c.getColumnIndex("x"));
                    texto = texto + "Altura do monte: " + x[i] + "\n";

                    y[i] = c.getFloat(c.getColumnIndex("y"));
                    texto = texto + "Comprimento do monte: " + y[i] + "\n";

                    z[i] = c.getFloat(c.getColumnIndex("z"));
                    texto = texto + "Largura do monte: " + z[i] + "\n";

                    total = x[i]*y[i]*z[i]*12; // total = altura*comprimento*largura*12;
                    total = arredondar(total,2,0);
                    texto = texto + "Metro Linear: " + total + "\n";
                }

            } catch (Exception e) {
                Toast.makeText(DeletarDados.this, "Erro (1) na base de dados" + e.toString(), Toast.LENGTH_LONG).show();
            }

            //Toast.makeText(this, texto, Toast.LENGTH_LONG).show();

        }
        txtDeletar.setText(texto);

        btnCancelDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeletarDados.this);
                builder.setTitle("Message")
                        .setTitle("Confirmação")
                        .setMessage("Confirma apagar este registro ?")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_delete)
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(DeletarDados.this, "Registro mantido inalterado", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    SQLiteDatabase db = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
                                    String _id = String.valueOf(position);
                                    String sql = "DELETE FROM parametros WHERE id=" + position;
                                    //Cursor c = db.rawQuery(sql, null);
                                    db.delete("parametros","id="+position,null);
                                    db.close();
                                    texto = "Registro " + position + " apagado !";
                                    Toast.makeText(DeletarDados.this, texto, Toast.LENGTH_LONG).show();
                                }
                                catch (Exception e){
                                    Toast.makeText(DeletarDados.this, "Erro na base de dados" + e.toString(), Toast.LENGTH_LONG).show();
                                }
                                Intent intent = new Intent();
                                intent = new Intent(DeletarDados.this, CalcularDados.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnDeletarTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeletarDados.this);
                builder.setTitle("Message")
                        .setTitle("Confirmação")
                        .setMessage("Confirma apagar todos os registros ?")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_delete)
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(DeletarDados.this, "Registros mantidos inalterados", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    SQLiteDatabase db = openOrCreateDatabase("tabela", MODE_PRIVATE, null);
                                    //String sql = "DELETE FROM parametros";
                                    //Cursor c = db.rawQuery(sql, null);
                                    db.execSQL("DROP TABLE IF EXISTS '" + "parametros" + "'");
                                    db.close();
                                    //db.delete("parametros","1",null);
                                    texto = "Todos os Registros apagados !";
                                    Toast.makeText(DeletarDados.this, texto, Toast.LENGTH_LONG).show();
                                }
                                catch (Exception e){
                                    Toast.makeText(DeletarDados.this, "Erro na base de dados" + e.toString(), Toast.LENGTH_LONG).show();
                                }
                                Intent intent = new Intent();
                                intent = new Intent(DeletarDados.this, CalcularDados.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
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
