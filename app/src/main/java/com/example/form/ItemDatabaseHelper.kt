package com.example.form

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ItemDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "items.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_SURNAME = "surname"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_DEPARTMENT = "department"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_SURNAME TEXT, " +
                "$COLUMN_ADDRESS TEXT, " +
                "$COLUMN_PHONE TEXT, " +
                "$COLUMN_DEPARTMENT TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertItem(name: String, surname: String, address: String, phone: String, department: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_SURNAME, surname)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PHONE, phone)
            put(COLUMN_DEPARTMENT, department)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateItem(id: Long, name: String, surname: String, address: String, phone: String, department: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_SURNAME, surname)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PHONE, phone)
            put(COLUMN_DEPARTMENT, department)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun getAllItems(): List<Item> {
        val items = mutableListOf<Item>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val surname = cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME))
                val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                val phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE))
                val department = cursor.getString(cursor.getColumnIndex(COLUMN_DEPARTMENT))
                items.add(Item(id, name, surname, address, phone, department))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return items
    }

    fun deleteItem(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}