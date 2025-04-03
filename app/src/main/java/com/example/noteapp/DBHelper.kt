package com.example.noteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_Name, null, DB_Version) {

    companion object {
        private const val DB_Name = "note.db"
        private const val DB_Version = 1

        const val NOTE_TABLE = "Notes"
        val NOTE_ID = "id"
        var NOTE_TITLE = "title"
        var NOTE_DETAIL = "detail"
        const val NOTE_DELETE_STATE = "deleteState"
        const val NOTE_DATE = "date"
        const val NOTE_0_STATE = "0"
        const val NOTE_1_STATE = "1"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS $NOTE_TABLE(" +
                    "$NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$NOTE_TITLE VARCHAR(255)," +
                    "$NOTE_DETAIL TEXT," +
                    "$NOTE_DELETE_STATE VARCHAR(1)," +
                    "$NOTE_DATE VARCHAR(255) )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}