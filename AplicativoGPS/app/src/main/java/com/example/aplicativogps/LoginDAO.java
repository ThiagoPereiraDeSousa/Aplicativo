package com.example.aplicativogps;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
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
    private boolean retorno = false;
    private int retornoId;

    public LoginDAO() {
        this.mDataBase = FirebaseDatabase.getInstance().getReference();
    }

    public boolean findUsers(final String usuario) {

        mDataBase.child("login").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnp : objSnapShot.getChildren()){
                        if (usuario == childSnp.getValue()) {
                            retorno = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return retorno;
    }

    public boolean insereUsuario(final String nomeUsuario) {
        try {
            Integer retorninho = getLastId();

            Thread.sleep(3000);

            mDataBase.child("login").child(String.valueOf(retorninho + 1)).child("nome").setValue(nomeUsuario);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private int getLastId() {

        mDataBase.child("login").orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnp: dataSnapshot.getChildren() ) {
                    for (DataSnapshot chilçSnp: dataSnapshot.getChildren()) {
                        retornoId = Integer.parseInt(chilçSnp.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return retornoId;
    }

}
