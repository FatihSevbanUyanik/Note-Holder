package com.example.fatihpc.noteholder.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fatihpc.noteholder.Database.DatabaseContract.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    companion object {

        // Database Attributes
        private val DATABASE_NAME = "MyNoteDatabase.db"
        private val DATABASE_VERSION = 1

        // Database Tables
        private val TABLE_NOTES_CREATE = "CREATE TABLE IF NOT EXISTS ${NotesEntry.TABLE_NAME} (" +
                "${NotesEntry.COLUMN_NOTE_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${NotesEntry.COLUMN_NOTE_CONTENT} TEXT NOT NULL, " +
                "${NotesEntry.COLUMN_NOTE_DATE} INTEGER, " +
                "${NotesEntry.COLUMN_NOTE_IS_EXECUTED} TEXT)"
    }

    /**
     * creates the database and creates the required tables.
     * @param sqLiteDatabase is the created database object.
     */
    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase?.execSQL(TABLE_NOTES_CREATE)
    }

    /**
     * updates the database and removes the required tables.
     * @param sqLiteDatabase is the updated database object.
     */
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, p1: Int, p2: Int) {
        sqLiteDatabase?.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.NotesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}