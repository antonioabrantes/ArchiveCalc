package mannings.msi.com.ttddtools.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mannings.msi.com.ttddtools.R;
import mannings.msi.com.ttddtools.TiposRegistros;

public class RegistroAdapter extends ArrayAdapter<TiposRegistros> {

    private ArrayList<TiposRegistros> registros;
    private Context context;

    public RegistroAdapter(Context c, ArrayList<TiposRegistros> objects) {
        super(c, 0, objects);
        this.registros = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if( registros != null ){

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_registro, parent, false);

            // recupera elemento para exibição
            TextView nome = (TextView) view.findViewById(R.id.textViewNome);
            ImageView fotografia = (ImageView) view.findViewById(R.id.imageViewFoto);

            nome.setTypeface(null, Typeface.BOLD);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/lucidatypewriterregular.ttf");
            nome.setTypeface( font );

            TiposRegistros registro = registros.get( position );
            nome.setText( registro.getNome() );
            fotografia.setImageResource(registro.getImagem());
            fotografia.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        return view;

    }
}

