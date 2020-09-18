package com.example.aplicativogps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean login = true;

        if (login){
            Intent it = new Intent(MainActivity.this, Login.class);
            startActivity(it);
        }else{
            Intent it = new Intent(MainActivity.this, Mapa.class);
            startActivity(it);
        }


    }
}