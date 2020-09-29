package com.example.aplicativogps;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginDAO {
    private DatabaseReference mDataBase;
    private boolean controleUser;

    public LoginDAO() {
        this.mDataBase = FirebaseDatabase.getInstance().getReference();
    }

    public boolean findUsers(final String nomeUsuario) {
        mDataBase = FirebaseDatabase.getInstance().getReference().child("login");
        Query lastQuery = mDataBase.orderByKey();
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String result = (String) data.getValue();
                    if (result.equals(nomeUsuario)){
                        controleUser = true;
                    } else {
                        controleUser = false;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
        return  controleUser;
    }

    public boolean insereUsuario(String nomeUsuario) {
        try {
            mDataBase.child("login").child("nome").setValue(nomeUsuario);
            return true;
        } catch (Exception e) {
            return  false;
        }
    }

}
