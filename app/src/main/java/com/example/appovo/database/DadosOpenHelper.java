package com.example.appovo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DadosOpenHelper extends SQLiteOpenHelper {
    public DadosOpenHelper(Context context) {
        super(context, "DADOS", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db ) {
        db.execSQL(scriptDLL.getCreateTableEstoque());
        db.execSQL(scriptDLL.getCreateTableVendas());
        db.execSQL(scriptDLL.getCreateTableTipo());
        db.execSQL(scriptDLL.getCreateTableRota());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
