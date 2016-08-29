package com.example.nylon.noticias_teleco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nylon on 07/12/2015.
 */
public class MainPreferencias extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    public static final String NOTIFICACIONES = "notificaciones";
    public static final String CATEGORY = "categorias";
    Boolean [] array = new Boolean[15]; //Array de boolens para activar o no la categoria

    //Variables para el manejo de las preferencias
    SharedPreferences sharedPref;
    Preference connectionPref;

    //Variable con la primera noticia leida del xml
    //String primera_noticia;

    //Variable que no queremos que muestre en preferencias
    PreferenceScreen myPreferenceScreen;
    EditTextPreference eNotificaciones;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //primera_noticia = (String)getIntent().getSerializableExtra("primera");
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //Para no mostrar lo que guarda del primer titulo -> para NOTIFICACIONES
        myPreferenceScreen = (PreferenceScreen) findPreference("prefScreen");
        eNotificaciones = (EditTextPreference) findPreference(NOTIFICACIONES);
        myPreferenceScreen.removePreference(eNotificaciones);
        modifSumario(NOTIFICACIONES);

        //Para las preferencias de CATEGORIA
        modifPreferencia(CATEGORY);

        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        if(key.equals(NOTIFICACIONES)){
            //En principio no podemos acceder a esta opción porque la hemos ocultado al usuario
            modifSumario(key);
        }
        if (key.equals(CATEGORY))
        {
            connectionPref = findPreference(key);
            modifPreferencia(key);
            Intent mIntent = new Intent(this, MainActivity.class);
        }
    }

    public void modifSumario(String key){
        try{
            if(key.equals(NOTIFICACIONES)){
                connectionPref = findPreference(key);
                //En muestra recogemos lo que introducimos de preferencias
                String muestra = sharedPref.getString(key,"Valor por defecto");//Aqui cojo lo que escribo
                connectionPref.setSummary(muestra); //AQUI ES DONDE DEBERIA ESCRIBIR EL TITULO EN CASO DE SER DIFERENTE
            }
        } catch (Exception e){
            Log.e("Notificaciones UVa", e.toString());
        }
    }

    public void modifPreferencia(String key) {
        try {
            if (key.equals(CATEGORY)) {
                connectionPref = findPreference(key);
                int categoria = Integer.parseInt(sharedPref.getString("categorias", "0"));
                if (categoria == 0)
                    connectionPref.setSummary("Todos los avisos");
                else if (categoria == 1)
                    connectionPref.setSummary("Avisos destacados");
                else if (categoria == 2)
                    connectionPref.setSummary("Avisos generales");
                else if (categoria == 3)
                    connectionPref.setSummary("Conferencias, Charlas y Talleres");
                else if (categoria == 4)
                    connectionPref.setSummary("Ofertas de becas, prácticas y empleo");
                else if (categoria == 5)
                    connectionPref.setSummary("Avisos y Ofertas de TFG y TFM");
                else if (categoria == 6)
                    connectionPref.setSummary("Junta de Escuela");
                else if (categoria == 7)
                    connectionPref.setSummary("Investigación");
                else if (categoria == 8)
                    connectionPref.setSummary("Otros avisos");
            }
        } catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Error en la conversion a entero", Toast.LENGTH_LONG);
        }
    }
}