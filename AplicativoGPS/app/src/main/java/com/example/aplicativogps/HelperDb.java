package com.example.aplicativogps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDb extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String TABELA = "login";    //nome da tabela
    private static final String DATABASE_NAME = "db_login";   //nome do BD

    private static final String TABLE_CREATE = "create table " + TABELA +
            " (nome String PRIMARY KEY);";

    HelperDb(Context context) {

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