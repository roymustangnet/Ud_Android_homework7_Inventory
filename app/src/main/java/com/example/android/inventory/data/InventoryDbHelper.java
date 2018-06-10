package com.example.android.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventory.data.InventoryContract.ProductEntry;

public class InventoryDbHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "inventroy.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODCUTS_TABLE =  "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_PROD_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PROD_PRICE + " INTEGER NOT NULL, "
                + ProductEntry.COLUMN_PROD_NUM + " INTEGER NOT NULL, "
                + ProductEntry.COLUMN_PROD_IMG + " TEXT , "
                + ProductEntry.COLUMN_PROD_SUPPLIER + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PROD_SUPPLIER_EMAIL + " TEXT, "
                + ProductEntry.COLUMN_PROD_SUPPLIER_TEL + " TEXT ); ";
        db.execSQL(SQL_CREATE_PRODCUTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
