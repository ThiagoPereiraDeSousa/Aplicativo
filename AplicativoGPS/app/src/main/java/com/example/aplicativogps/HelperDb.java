package com.example.aplicativogps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDb extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String TABELA_LOGIN = "login";    //nome da tabela
    public static final String TABELA_CONTATO = "contato";
    private static final String DATABASE_NAME = "db_login";   //nome do BD

    private static final String TABLE_CREATE = "create table " + TABELA_LOGIN +
            " (nome varchar(50) PRIMARY KEY, endereco varchar(100));";

    private static final String TABLE_CREATE_EMERGENCY = "create table " + TABELA_CONTATO +
            " (nomeUsuario varchar(50) PRIMARY KEY, telefone varchar(9), nomeEmergencia varchar(50));";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABELA_LOGIN;

    HelperDb(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE_EMERGENCY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}