package com.example.aplicativogps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

public class LoginDaoHelper {
    private Context ctx;
    private SQLiteDatabase db;

    public LoginDaoHelper(Context ctx) {
        this.ctx = ctx;
        db = new HelperDb(ctx).getWritableDatabase();
    }

    public boolean findUser(String usuario) {
        Cursor cursor = db.query(HelperDb.TABELA, null, null, null, null, null, "nome");
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(usuario)) {
                return true;
            }
        }
        return false;
    }

    public String findAdress(String usuario){
        Cursor cursor = db.query(HelperDb.TABELA, null, null, null, null, null, "nome");
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(usuario)) {
                return  cursor.getString(1);
            }
        }
        return "";
    }

    public boolean insereUsuario(String usuario) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("nome", usuario);
            cv.put("endereco", "");

            long id = db.insert("login", null, cv);
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

    public boolean insereEndereco(String usuario, String endereco) {
        try {
            ContentValues values = new ContentValues();
            values.put("nome",usuario);
            values.put("endereco", endereco);

            String[] whereArgs = new String[]{usuario};
            return  (db.update("login",values, "nome=?", whereArgs) > 0);
        } catch (Exception ex) {
            return false;
        }
    }

}
