package com.example.aplicativogps;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Map;

public class LoginDAO {
    private DatabaseReference mDataBase;
    private boolean retorno;
    private int retornoId;

    public LoginDAO() {
        this.mDataBase = FirebaseDatabase.getInstance().getReference();
    }

    public boolean findUsers(final String usuario) {

        mDataBase.child("login").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    retorno = getAllUsers((Map<String, String>) dataSnapshot.getValue(), usuario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return retorno;
    }

    public boolean insereUsuario(String nomeUsuario) {
        try {
            Integer resposta = getLastId();
            mDataBase.child("login").child(String.valueOf(1)).child("nome").setValue(nomeUsuario);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean getAllUsers(Map<String, String> users, String usuarioParam) {
        for (Map.Entry<String, String> entry : users.entrySet()) {
            String usuario = entry.getValue();
            if (usuario == usuarioParam) {
                return true;
            }
        }
        return false;
    }
    private int getLastId(){
        mDataBase.child("login").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    getId((Map<String, String>) dataSnapshot.getValue());
                    retornoId = 0;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return  retornoId;
    }
    private boolean getId(Map<String, String> users) {
        for (Map.Entry<String, String> entry : users.entrySet()) {
            String usuario = entry.getValue();
        }
        return false;
    }

}
