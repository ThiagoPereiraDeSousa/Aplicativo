package com.example.aplicativogps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContatoEmergencyDAO {
    private Context ctx;
    private SQLiteDatabase db;

    public ContatoEmergencyDAO(Context ctx) {
        this.ctx = ctx;
        db = new HelperDb(ctx).getWritableDatabase();
    }

    public String findCell(String usuarioEmergency) {
        Cursor cursor = db.query(HelperDb.TABELA_CONTATO, null, null, null, null, null, "nomeUsuario");
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(usuarioEmergency)) {
                return cursor.getString(1);
            }
        }
        return "";
    }

    public boolean insereUsuarioEmergencia(String usuario, String telefone, String usuarioEmergencia) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("nomeUsuario", usuario);
            cv.put("telefone", telefone);
            cv.put("nomeEmergencia", usuarioEmergencia);

            long id = db.insert("contato", null, cv);
            if (id == -1) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

}
