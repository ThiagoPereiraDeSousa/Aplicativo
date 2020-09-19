package com.example.aplicativogps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.proto.ProtoOutputStream;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Login extends AppCompatActivity {
    TextToSpeech textToSpeech;
    private final int ID_TEXTO_PARA_VOZ = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        String textoInicial = "Olá! Informe seu nome para fazer nossas configurações internas.";
        Toast.makeText(getApplicationContext(), textoInicial, Toast.LENGTH_SHORT).show();
        textToSpeech.speak(textoInicial, TextToSpeech.QUEUE_FLUSH, null);

        Intent iVoz = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        iVoz.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga seu nome: ");

        try {
            startActivityForResult(iVoz, ID_TEXTO_PARA_VOZ);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "ERRO!!!!!!!!" + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int id, int resultCodeId, Intent dados) {
        super.onActivityResult(id, resultCodeId, dados);
        switch (id) {
            case ID_TEXTO_PARA_VOZ:
                if (resultCodeId == RESULT_OK && null != dados) {
                    ArrayList<String> result = dados.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String ditado = result.get(0);
                    Toast.makeText(getApplicationContext(), ditado, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onPause() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.stop();
        }
        super.onPause();
    }
}