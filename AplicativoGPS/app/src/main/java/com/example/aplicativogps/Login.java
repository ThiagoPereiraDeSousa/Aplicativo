package com.example.aplicativogps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private TextToSpeech TTS;

    private final int ID_TEXTO_PARA_VOZ = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TTS = new TextToSpeech(this,this);
        SpeechOut("Olá! Informe seu nome para fazer nossas configurações internas.");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ListenSound();

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
        if(TTS != null){
            TTS.stop();
            TTS.stop();
        }
        super.onPause();
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS)
        {
            int result = TTS.setLanguage(Locale.getDefault());
            if(result == TextToSpeech.LANG_NOT_SUPPORTED || result ==TextToSpeech.LANG_MISSING_DATA)
            {
                Log.e("TTS", "Idioma não suportado");
            }else{
                SpeechOut("Olá! Informe seu nome para fazer nossas configurações internas.");
            }
        }else {
            Log.e("TTS","Inicialização falhou...");
        }
    }
    private void SpeechOut(String texto)
    {
        Toast.makeText(getApplicationContext(), "OLÁ", Toast.LENGTH_SHORT);
        TTS.speak(texto,TextToSpeech.QUEUE_FLUSH,null);
    }

    private void ListenSound(){
        Intent iVoz = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        iVoz.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga seu nome: ");

        try {
            startActivityForResult(iVoz, ID_TEXTO_PARA_VOZ);
        } catch (ActivityNotFoundException e) {
            String erro = "ERRO " + e;
            SpeechOut(erro);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
        if (id == R.id.maps){
            Intent i = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(i);
        }

        if (id == R.id.sair){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}