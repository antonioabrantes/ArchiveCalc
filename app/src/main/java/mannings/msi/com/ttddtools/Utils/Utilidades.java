package mannings.msi.com.ttddtools.Utils;

import android.text.TextUtils;
import android.widget.EditText;

public class Utilidades {

    public static float arredondar(float valor, int casas, int ceilOrFloor) {
        float arredondado = valor;
        arredondado *= (Math.pow(10, casas));
        if (ceilOrFloor == 1) {
            arredondado = (float) Math.ceil(arredondado);
        } else if (ceilOrFloor == 2) {
            arredondado = (float) Math.floor(arredondado);
        } else {
            float ceil = (float)Math.ceil(arredondado);
            float floor = (float)Math.floor(arredondado);
            if ((ceil-arredondado)<(arredondado-floor)){
                arredondado = ceil;
            } else{
                arredondado = floor;
            }
        }

        arredondado /= (Math.pow(10, casas));
        return arredondado;
    }

    public static boolean testarCampo(EditText editText){
        boolean digitsOnly = true;
        try {
            String texto = editText.getText().toString();
            if (texto.matches("")) {
                digitsOnly = false;
            }
            else {
                texto = texto.replace(',', '.');
                texto = texto.replace(';', '.');
                editText.setText(texto);
                String texto1 = texto.replace('.', ' ');
                texto1 = texto1.replaceAll("\\s+", "");
                digitsOnly = TextUtils.isDigitsOnly(texto1);
            }
        }
        catch (Exception e) {
            digitsOnly = false;
        }
        return digitsOnly;
    }


}
