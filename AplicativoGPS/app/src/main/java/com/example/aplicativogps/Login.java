package com.example.aplicativogps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
    private String nomeEmergencia;
    private String telefoneEmergencia;
    private boolean controleAjuda = false;
    private boolean askName = false;
    private String nomeUsuarioCadastro;
    final int PERMISSIONS_CALL_PHONE_ID = 1;
    boolean permissions_call_phone_value = false;
    //private LoginDaoHelper loginDAO = new LoginDaoHelper(getApplicationContext());


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_CALL_PHONE_ID);
        } else {
            permissions_call_phone_value = true;
        }

        mDataBase = FirebaseDatabase.getInstance().getReference();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int id, int resultCodeId, Intent dados) {
        super.onActivityResult(id, resultCodeId, dados);
        switch (id) {
            case ID_TEXTO_PARA_VOZ:

                if (resultCodeId == RESULT_OK && null != dados) {
                    ArrayList<String> result = dados.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String ditado = result.get(0);
                    if (ditado.equalsIgnoreCase("Ajuda")) {
                        controleAjuda = true;
                        SpeechOut("Informe seu nome para consulta do contato");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ListenSound();
                            }
                        }, 6100);

                        return;
                    } else if (!controleAjuda && !askName) {
                        SpeechOut("Qual seu nome?");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ListenSound();
                            }
                        }, 4100);
                        controleAjuda = false;
                        askName = true;
                        return;
                    } else if (ditado.equalsIgnoreCase("sim")) {
                        LoginDaoHelper loginDAO = new LoginDaoHelper(getApplicationContext());
                        if (loginDAO.findUser(nomeUsuarioCadastro)) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent it = new Intent(Login.this, ContatoEmergencyActivity.class);
                                    it.putExtra("Nome", nomeUsuarioCadastro);
                                    startActivity(it);
                                }
                            }, 6100);
                        } else {
                            cadastraUsuario(nomeUsuarioCadastro);
                        }
                    }
                    if (controleAjuda) {
                        fazLigacaoContato(ditado);
                    } else {
                        ValidaLogin(ditado);
                    }

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
                SpeechOut("Olá! O que você precisa?");
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

    private void ValidaLogin(final String nomeUsuario) {
        LoginDaoHelper loginDAO = new LoginDaoHelper(getApplicationContext());
        if (loginDAO.findUser(nomeUsuario)) {
            Intent it = new Intent(Login.this, MapsActivity.class);
            it.putExtra("Nome", nomeUsuario);
            startActivity(it);
        } else {
            cadastraUsuario(nomeUsuario);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fazLigacaoContato(String nomeUsuario) {
        ContatoEmergencyDAO contatoDao = new ContatoEmergencyDAO(getApplicationContext());
        String retorno = contatoDao.findCell(nomeUsuario);
        if (retorno.equals("")) {
            nomeUsuarioCadastro = nomeUsuario;
            SpeechOut("Você não possui um contato de emergência cadastrado. Deseja Cadastrar?");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ListenSound();
                }
            }, 6100);
        } else {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Uri uri = Uri.parse("tel:" + retorno);  //ligar para o número de telefone do contato selecionado
                Intent it = new Intent(Intent.ACTION_CALL, uri);
                startActivity(it);
            } else {
                Toast.makeText(this, "\nAs ligações não foram autorizadas neste aparelho.\n", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void cadastraUsuario(final String nomeUsuario) {
        LoginDaoHelper loginDAO = new LoginDaoHelper(getApplicationContext());
        SpeechOut("Notamos que ainda não possui o login em nosso sistema! Aguarde um momento.");
        try {
            loginDAO.insereUsuario(nomeUsuario);

        } catch (Exception ex) {
            SpeechOut("Ocorreu um erro ao tentar realizar o cadastro! Tente mais tarde por favor!");
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(Login.this, ContatoEmergencyActivity.class);
                it.putExtra("Nome", nomeUsuario);
                startActivity(it);
            }
        }, 6100);
    }

}