package com.example.nylon.noticias_teleco;


import android.app.Activity;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Handler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends ListActivity {
    Asincrono asincrono;
    ArrayList<HashMap<String, String>> listaNoticias = new ArrayList<>();

    private Context context;
    private MyAdapter mAdapter;

    private ImageView imageView;
    private Bitmap loadedImage;

    //Variables para las preferencias
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor esharedPref;
    public String muestra;
    public String primera_noticia;

    //Variables para la designación de las categorías
    int categoria = 0;
    Boolean [] array = new Boolean[16]; //Array de boolens para activar o no la categoria
    String preferenciasCategoria = "0"; //Cogemos el valor de la categoria que queremos mostrar (preferencias)

    Intent tintent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            listaNoticias = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("lista");
            primera_noticia = (String) getIntent().getSerializableExtra("primera");

            //Funciones relacionadas con las PREFERENCIAS
            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            esharedPref = sharedPref.edit();
            recogePreferencias();

            //Funciones con respecto a NOTIFICACIONES
            if (primera_noticia.equals(muestra)) {
                //No nos muestra la notificacion FALTA METER EN LA PREFERENCIA la variable primera_noticia

            } else {
                //Nos muestra una notificacion y nos cambia en la preferencia para que guarde la nueva primera noticia
                lanzaNotif();
                esharedPref.putString("notificaciones", primera_noticia);
                esharedPref.commit();
                muestra = primera_noticia;
            }

            //Funciones con respecto a CATEGORIAS*************************************************
            categoria = Integer.parseInt(sharedPref.getString("categorias", "0"));
            actualiza();
            setData();
            mAdapter = new MyAdapter(this);
            setListAdapter(mAdapter);
        }catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(),"Error en la conversion a entero", Toast.LENGTH_LONG);
        }catch(Exception e){
            startActivity(new Intent(getApplicationContext(),ImagenUva.class));
        }
    }

    /* @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data){
     if(requestCode ==1234 && resultCode == RESULT_OK){
         new Handler().post(new Runnable(){
             @Override
             public void run (){
                 Intent i = new Intent(getBaseContext(),MainActivity.class);
                 startActivity(i);
             }
         });
         finish();
     }
 }*/

    public void recogePreferencias(){
        muestra = sharedPref.getString("notificaciones","Valor por defecto");
    }

    //El siguiente método nos lanza una notificacion de que ha cambiado el xml al abrir la aplicacion
    //y si pulsamos en ella nos lleva a la noticia
    public void lanzaNotif(){
        //Información de la interfaz de usuario y las acciones para una notificación
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Nueva noticia")
                .setContentText("Ya puedes acceder a la nueva noticia en tu dispositivo")
                .setSmallIcon(R.mipmap.ic_launcher);
        //Creamos un nuevo Intent para que nos lleve a la nueva actividad.
        Intent mIntent = new Intent(this, MostrarNoticia.class);
        //Usamos un PendingIntent, contiene un Intent encargado de arrancar una nueva actividad
        PendingIntent pend = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Añadimos el PendingIntent
        mBuilder.setContentIntent(pend);
        //Para mostrar la notificación se envía al sistema el objeto
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public void CambiaTeleco(View view)
    {
        String link= "http://www.tel.uva.es";
        tintent = new Intent(tintent.ACTION_VIEW, Uri.parse(link));
        startActivity(tintent);
    }

    public void CambiaAIT(View view)
    {
        String link= "http://www.aitcyl.es";
        tintent = new Intent(tintent.ACTION_VIEW, Uri.parse(link));
        startActivity(tintent);
    }

    public void pref (View view)
    {
        tintent = new Intent(this, MainPreferencias.class);
        startActivity(tintent);
    }

    public void act (View view)
    {
        tintent = new Intent(this, MainActivity.class);
        startActivity(tintent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                Bundle bolsa = new Bundle();
                bolsa.putSerializable("primera", primera_noticia);
                Intent intent = new Intent(this,MainPreferencias.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualiza(){
        categoria = Integer.parseInt(sharedPref.getString("categorias", "0"));
        Arrays.fill(array, Boolean.FALSE);
        if(categoria==1){
            array[11]=true;
        }
        if(categoria==2){
            array[0]=true;
            array[1]=true;
        }
        if(categoria==3){
            array[10]=true;
        }
        if(categoria==4){
            array[2]=true;
            array[3]=true;
        }
        if(categoria==5){
            array[4]=true;
        }
        if(categoria==6){
            array[14]=true;
        }
        if(categoria==7){
            array[15]=true;
        }
        if(categoria==8){
            array[5]=true;
            array[6]=true;
            array[7]=true;
            array[8]=true;
            array[9]=true;
            array[12]=true;
            array[13]=true;
        }
        if(categoria==0){
            Arrays.fill(array,Boolean.TRUE);
        }
    }

    public class Node {
        public String mTitulo;
        public String mDescripcion;
        public Integer mImagen;

        //TODO he cambiado esto
        public Node(String mTitulo, String mDescripcion, String categoria) {
            this.mTitulo = mTitulo;
            this.mDescripcion = mDescripcion;
            Log.d("", "categoria : "+categoria);

            switch(categoria){
                case "1": this.mImagen = R.drawable.ag; break;//Se cargan las imagenes correspondientes a cada categoria.
                case "2": this.mImagen = R.drawable.ag; break;
                case "3": this.mImagen = R.drawable.odb; break;
                case "4": this.mImagen = R.drawable.odb; break;
                case "5": this.mImagen = R.drawable.t; break;
                case "6": this.mImagen = R.drawable.o; break;
                case "7": this.mImagen = R.drawable.o; break;
                case "8": this.mImagen = R.drawable.o; break;
                case "9": this.mImagen = R.drawable.o; break;
                case "10": this.mImagen = R.drawable.o; break;
                case "11": this.mImagen = R.drawable.c; break;
                case "12": this.mImagen = R.drawable.ad; break;
                case "13": this.mImagen = R.drawable.o; break;
                case "14": this.mImagen = R.drawable.o; break;
                case "15": this.mImagen = R.drawable.j; break;
                case "16": this.mImagen = R.drawable.i; break;
                default:break;

            }
        }
    }
    private static ArrayList<Node> mArray =new ArrayList<Node>();


    private void  setData(){
        mArray.clear();

        for(int i = 0; i<listaNoticias.size(); i++){
            //si preferenciasCategoria vale 0 es que se tienen que mostrar todas las noticias.
            //if(listaNoticias.get(i).getCategory()== preferenciasCategoria || preferenciasCategoria == "0")
            String categ =listaNoticias.get(i).get("category").toString();
            int num = Integer.parseInt(categ) - 1;
            //int num = Integer.parseInt(listaNoticias.get(i).get("category").toString()) - 1;
            Boolean pos = array[num];
            if(pos){
                Log.d("", listaNoticias.get(i).get("descripcion"));
                mArray.add(new Node(listaNoticias.get(i).get("title"), listaNoticias.get(i).get("descripcion"), listaNoticias.get(i).get("category")));
            }
        }
    }
    public static class MyAdapter extends BaseAdapter {
        private Context mContexto;
        public MyAdapter(Context c){
            this.mContexto = c;
        }
        @Override
        public int getCount(){
            return mArray.size();
        }

        @Override
        public Object getItem(int position) {
            return mArray.get(position);
        }

        @Override
        public long getItemId (int arg0){
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)mContexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate (R.layout.adapter_layout,null);
            }
            else{ view = convertView; }

            //TODO he descomentado estas lineas
            ImageView img= (ImageView) view.findViewById(R.id.imagen);
            img.setImageDrawable(mContexto.getResources().getDrawable(mArray.get(position).mImagen));

            TextView tFrase =(TextView)view.findViewById(R.id.titulo);
            tFrase.setText(mArray.get(position).mTitulo);
            Log.d("","Hole" + mArray.get(position).mTitulo);

            TextView tAutor =(TextView)view.findViewById(R.id.descripcion);
            tAutor.setText(mArray.get(position).mDescripcion);
            return view;
        }
    }

    @Override
    protected void onListItemClick(ListView lv,View v, int position, long id){
        super.onListItemClick(lv, v, position, id);
        Log.d("","HolePosicion" +position);
        Intent i = new Intent(getApplicationContext(), MostrarNoticia.class);
        Bundle bolsa = new Bundle();
        bolsa.putSerializable("noticia",listaNoticias.get(position));
        i.putExtras(bolsa);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}