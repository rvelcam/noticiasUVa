package com.example.nylon.noticias_teleco;

/**
 * Created by nylon on 08/12/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class ImagenUva extends Activity {
    String primera_noticia;
    Asincrono asincrono;
    ArrayList<HashMap<String, String>> listaNoticias = new ArrayList<>();

    private final int DURACION = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_imagen_uva);

        empezarAsincrono();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    listaNoticias = asincrono.getListaNoticias();
                    primera_noticia = asincrono.getPrimeraNoticia();

                    Bundle bolsa = new Bundle();
                    bolsa.putSerializable("lista", listaNoticias);
                    bolsa.putSerializable("primera",primera_noticia);
                    Intent intent = new Intent(ImagenUva.this, MainActivity.class);
                    intent.putExtras(bolsa);
                    startActivity(intent);
                    finish();
                }catch(Exception e){
                    startActivity(new Intent(ImagenUva.this, ImagenUva.class));
                }
            }
        }, DURACION);
    }

    public void empezarAsincrono(){
        if(asincrono!=null){
            asincrono.cancel(true);
        }else{
            asincrono = new Asincrono();
            asincrono.execute();
        }
    }
}
