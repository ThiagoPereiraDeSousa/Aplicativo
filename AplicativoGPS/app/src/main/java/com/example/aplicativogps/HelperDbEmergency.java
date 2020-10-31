package com.example.aplicativogps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDbEmergency extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String TABELA = "contato";    //nome da tabela
    private static final String DATABASE_NAME = "db_contatoemergency";   //nome do BD

    private static final String TABLE_CREATE = "create table " + TABELA +
            " (nomeUsuario varchar(50) PRIMARY KEY, telefone varchar(9) PRIMARY KEY, nomeEmergencia varchar(50));";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABELA;

    HelperDbEmergency(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}