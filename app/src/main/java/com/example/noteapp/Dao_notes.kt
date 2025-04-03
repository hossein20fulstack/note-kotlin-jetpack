package com.example.noteapp

import android.content.ContentValues
import android.database.Cursor
import android.util.Log

class Dao_notes(
    private val db: DBHelper
) {
    private val contentValue = ContentValues()
    lateinit var cursor: Cursor
    val data = ArrayList<DBNotesModel>()


    fun saveNotes(notes: DBNotesModel): Boolean {
        val database = db.writableDatabase
        setContenetValues(notes)
        val result = database.insert(DBHelper.NOTE_TABLE, null, contentValue)
        database.close()
        return result > 0
    }

    fun findNotes(deletestate: String): List<DBNotesModel> {
        data.clear()
        val database = db.readableDatabase
        val query = "SELECT * FROM ${DBHelper.NOTE_TABLE} WHERE ${DBHelper.NOTE_DELETE_STATE}=?"
        cursor = database.rawQuery(query, arrayOf(deletestate))
        setData()
        cursor.close()
        database.close()
        return data
    }

    fun findByid(id: Int): DBNotesModel {
        val database = db.readableDatabase
        val query = "SELECT * FROM ${DBHelper.NOTE_TABLE} WHERE ${DBHelper.NOTE_ID}=?"
        cursor = database.rawQuery(query, arrayOf(id.toString()))
        val data = setDataByid()
        cursor.close()
        database.close()
        return data
    }


    fun Delete(id: String) {
        val db = db.writableDatabase
        db.delete(
            DBHelper.NOTE_TABLE,
            "${DBHelper.NOTE_ID} = ?",
            arrayOf(id)
        )

        db.close()
    }

    fun SetDeleteState(data: DBNotesModel, id: String) {
        val database = db.writableDatabase
        contentValue.clear()
        if (data.delete_state == "0")
            contentValue.put(DBHelper.NOTE_DELETE_STATE, "${DBHelper.NOTE_1_STATE}")
        else
            contentValue.put(DBHelper.NOTE_DELETE_STATE, "${DBHelper.NOTE_0_STATE}")

        database.update(
            DBHelper.NOTE_TABLE,
            contentValue,
            "${DBHelper.NOTE_ID} = ?",
            arrayOf(id)

        )
        database.close()

    }

    fun updateNotes(dataa:DBNotesModel,idd:Int) {
        val database = db.writableDatabase
        setContenetValues(dataa)
        database.update(
            DBHelper.NOTE_TABLE,
            contentValue,
            "${DBHelper.NOTE_ID} = ?",
            arrayOf(idd.toString())
        )
        database.close()


    }

    private fun setContenetValues(notes: DBNotesModel) {
        contentValue.clear()
        contentValue.put(DBHelper.NOTE_TITLE, notes.title)
        contentValue.put(DBHelper.NOTE_DETAIL, notes.detail)
        contentValue.put(DBHelper.NOTE_DELETE_STATE, notes.delete_state)
        contentValue.put(DBHelper.NOTE_DATE, notes.date)

    }

    private fun setDataByid(): DBNotesModel {
        val data = DBNotesModel(0, "", "", "", "")
        if (cursor.moveToFirst()) {
            data.id = cursor.getInt(getIndex(DBHelper.NOTE_ID))
            data.title = cursor.getString(getIndex(DBHelper.NOTE_TITLE))
            data.detail = cursor.getString(getIndex(DBHelper.NOTE_DETAIL))
            data.delete_state = cursor.getString(getIndex(DBHelper.NOTE_DELETE_STATE))
            data.date = cursor.getString(getIndex(DBHelper.NOTE_DATE))
        }
        return data
    }

    private fun setData() {
        try {
            if (cursor.moveToFirst())
                do {
                    val id = cursor.getInt(getIndex(DBHelper.NOTE_ID))
                    val title = cursor.getString(getIndex(DBHelper.NOTE_TITLE))
                    val detail = cursor.getString(getIndex(DBHelper.NOTE_DETAIL))
                    val deleteState = cursor.getString(getIndex(DBHelper.NOTE_DELETE_STATE))
                    val date = cursor.getString(getIndex(DBHelper.NOTE_DATE))
                    data.add(DBNotesModel(id, title, detail, deleteState, date))
                } while (cursor.moveToNext())

        } catch (e: Exception) {
            Log.e("Not found Index", e.message.toString())
        }
    }

    private fun getIndex(name: String) = cursor.getColumnIndex(name)
}

