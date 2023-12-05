package com.example.pocketml.Utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HelperDb extends SQLiteOpenHelper {

    public static String DB_FILE = "tempDataset.db";
    public static String TABLE_NAME = "tempDataset";
    public static String[] COLUMNS;


    public HelperDb(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_FILE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String st = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME;
        st+=" ( ";
        for (int i =0; i<COLUMNS.length; i++){
            if(i!=COLUMNS.length-1)
                st += COLUMNS[i] + " TEXT, ";
            else{
                st += COLUMNS[i] + " TEXT ";
            }
        }
        st+=" )";
        sqLiteDatabase.execSQL(st);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void updateTableStructure(String[] newColumns) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        String st = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( ";
        for (int i =0; i<newColumns.length; i++){
            if(i!=newColumns.length-1)
                st += newColumns[i] + " TEXT, ";
            else{
            st += newColumns[i] + " TEXT ";
            }
        }
        st += " )";
        db.execSQL(st);
        COLUMNS = newColumns;
        db.close();
    }
}

