package com.example.nylon.noticias_teleco;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Héctor on 02/12/2015.
 */
public class MostrarNoticia extends Activity {
    HashMap<String, String> noticia;
    String link;
    Intent intent = null;
    String titulo;
    String descripcion;
    String fecha;
    TextView tvTitulo, tvDescripcion, tvFecha;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mostrar);
        tvTitulo=(TextView)findViewById(R.id.tvTitulo);
        tvDescripcion=(TextView)findViewById(R.id.tvDescripcion);
        tvFecha=(TextView)findViewById(R.id.tvFecha);
        noticia = (HashMap<String, String>) getIntent().getSerializableExtra("noticia");
        link=noticia.get("link");
        titulo=noticia.get("title");
        descripcion =noticia.get("descripcion");
        fecha =noticia.get("pubDate");
        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);
        tvFecha.setText(fecha);
    }
    public void irNoticia(View view)
    {
        intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}

