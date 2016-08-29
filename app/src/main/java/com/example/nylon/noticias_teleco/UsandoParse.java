package com.example.nylon.noticias_teleco;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by nylon on 08/12/2015.
 */
//El parse solo se tiene que inicializar una vez, hay que nombrarlo desde el manifest
public class UsandoParse extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "fES7MEXG9WsqwFwKvK2LKnqw77g4YfwlSu16motQ", "rTL5fScPEztxLvzyNTDr4xPCkgfC70AzbE3F3eR7");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
