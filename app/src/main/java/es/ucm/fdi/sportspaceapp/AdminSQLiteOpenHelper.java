package es.ucm.fdi.sportspaceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "sportspaceapp.sql";
    public static final int DATABASE_VERSION = 1;


    public AdminSQLiteOpenHelper(@Nullable Context context,  @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table usuarios(codigo INTEGER primary key autoincrement, nombre text, apellido1 text, apellido2 text, mail text, fechNac text, password text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
