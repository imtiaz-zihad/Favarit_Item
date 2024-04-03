package com.example.favarit_item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHalper extends SQLiteOpenHelper {

    public static final String DB_NAME = "users";
    public static final int DB_VERSION = 1;
    public static final String DB_TABLE_NAME = "users_table";
    Context context;


    public DataBaseHalper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context =context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + DB_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,isFavorite INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DB_TABLE_NAME );
    }


    public boolean InsertData (String name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues conVal = new ContentValues();
        conVal.put("name",name);
        conVal.put("isFavorite",0);
        long result = database.insert(DB_TABLE_NAME,null,conVal);

        if (result <= 0){
           return false;
        }else {
            return true;
        }
    } // insert data end tag


    public Cursor getUserData () {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + DB_TABLE_NAME, null);
        if (cursor.getCount() != 0) {
            return cursor;
        }
            return null;
    }//getUserData end tag


    public void updateAddFavorite (int id){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            int value = 1;
            database.execSQL("UPDATE "+DB_TABLE_NAME+ " SET isFavorite= '" +value+ "' WHERE id = "+id);
            Toast.makeText(context, "Favourite", Toast.LENGTH_SHORT).show();

        }catch (SQLException e){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }


}// updateAddFavorite end tag

    public void updateRemoveFavorite (int id){

        SQLiteDatabase database = this.getWritableDatabase();

        try {
            int value=0;
            database.execSQL("UPDATE "+DB_TABLE_NAME+" SET isFavorite'" +value+ "' WHERE id ="+id);
            Toast.makeText(context, "UnFavourite", Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

        }


    }



} //end tag
