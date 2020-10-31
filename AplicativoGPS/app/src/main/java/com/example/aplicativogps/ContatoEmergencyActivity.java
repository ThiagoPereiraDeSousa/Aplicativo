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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class ContatoEmergencyActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech TTS;
    private final int ID_TEXTO_PARA_VOZ = 100;
    //private ContatoEmergencyDAO contatoDao = new ContatoEmergencyDAO(getApplicationContext());
    private DatabaseReference mDataBase;
    private String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_emergency);

        //mDataBase = FirebaseDatabase.getInstance().getReference();
        TTS = new TextToSpeech(this, this);
        //SpeechOut("Olá! Qual o seu nome?");

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

                    if (Character.isLetter(ditado.charAt(0))) {
                        nome = ditado;
                        SpeechOut("Agora nos informe o telefone de contato emergencial!");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ListenSound();
                            }
                        }, 6000);
                    } else {
                        Intent it = getIntent();
                        gravaContatoEmergencia(it.getStringExtra("Nome"), ditado, nome);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent it = new Intent(ContatoEmergencyActivity.this, MapsActivity.class);
                                it.putExtra("Nome", it.getStringExtra("NomeUsuario"));
                                startActivity(it);
                            }
                        }, 6100);
                    }

                }
                break;
        }
    }

    private void gravaContatoEmergencia(String usuario, String telefone, String usuarioEmergencia) {
        ContatoEmergencyDAO contatoDao = new ContatoEmergencyDAO(getApplicationContext());
        contatoDao.insereUsuarioEmergencia(usuario, telefone, usuarioEmergencia);
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
                SpeechOut("Por favor, nos informe o nome de um contato Emergencial!");
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
}