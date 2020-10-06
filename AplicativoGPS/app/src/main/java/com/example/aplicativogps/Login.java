package com.example.aplicativogps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Locale;

public class Login extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech TTS;
    private final int ID_TEXTO_PARA_VOZ = 100;
    private DatabaseReference mDataBase;
    //private LoginDaoHelper loginDAO = new LoginDaoHelper(getApplicationContext());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        TTS = new TextToSpeech(this, this);
        SpeechOut("Olá! Qual o seu nome?");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListenSound();
            }
        }, 6000);

    }

    @Override
    protected void onActivityResult(int id, int resultCodeId, Intent dados) {
        super.onActivityResult(id, resultCodeId, dados);
        switch (id) {
            case ID_TEXTO_PARA_VOZ:
                if (resultCodeId == RESULT_OK && null != dados) {
                    ArrayList<String> result = dados.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String ditado = result.get(0);

                    ValidaLogin(ditado);
                }
                break;
        }
    }

    public void onPause() {
        if (TTS != null) {
            TTS.stop();
            TTS.stop();
        }
        super.onPause();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = TTS.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                Log.e("TTS", "Idioma não suportado");
            } else {
                SpeechOut("Olá! Qual o seu nome?");
            }
        } else {
            Log.e("TTS", "Inicialização falhou...");
        }
    }

    private void SpeechOut(String texto) {
        TTS.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void ListenSound() {
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
        if (id == R.id.home) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        if (id == R.id.maps) {
            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(i);
        }

        if (id == R.id.sair) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ValidaLogin(String nomeUsuario) {
        LoginDaoHelper loginDAO = new LoginDaoHelper(getApplicationContext());
        if (loginDAO.findUser(nomeUsuario)) {
            Intent it = new Intent(Login.this, Mapa.class);
            startActivity(it);
        } else {
            SpeechOut("Notamos que ainda não possui o login em nosso sistema! Aguarde um momento que iremos fazer o seu cadastro.");
            try {
                loginDAO.insereUsuario(nomeUsuario);
            }catch (Exception ex){
                SpeechOut("Ocorreu um erro ao tentar realizar o cadastro! Tente mais tarde por favor!");
                return;
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent it = new Intent(Login.this, Mapa.class);
                    startActivity(it);
                }
            }, 6100);
        }
    }

}