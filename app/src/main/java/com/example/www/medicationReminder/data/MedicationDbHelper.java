package com.example.www.medicationReminder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.www.medicationReminder.data.MedicationContract.MedicationEntry;


public class MedicationDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movies.db";

    public MedicationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MedicationEntry.TABLE_NAME + " (" +

                MedicationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MedicationEntry.COLUMN__NAME + " TEXT NOT NULL, " +
                MedicationEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                MedicationEntry.COLUMN_DOSAGE + " TEXT NOT NULL," +
                MedicationEntry.COLUMN_PRODUCTION_DATE +" TEXT NOT NULL, "+
                MedicationEntry.COLUMN_EXPIRY_DATE +" TEXT NOT NULL, "+
                MedicationEntry.COLUMN_EXPIRED +" INTEGER NOT NULL DEFAULT 0 "+
                " " + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicationEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
