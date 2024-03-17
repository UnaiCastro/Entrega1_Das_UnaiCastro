package com.tfg.inicioactivity.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MiBaseDeDatos.db"

        // Estructura de la tabla Usuario
        private const val SQL_CREATE_USUARIO_TABLE =
            "CREATE TABLE Usuario (" +
                    "email TEXT PRIMARY KEY," +
                    "contraseña TEXT," +
                    "nombre TEXT)"

        // Estructura de la tabla Partidos
        private const val SQL_CREATE_PARTIDOS_TABLE =
            "CREATE TABLE Partidos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "resultado TEXT," +
                    "email_jugador TEXT," +
                    "nombre_compañero TEXT," +
                    "lugar TEXT)"

        //Eliminar tabla Usuario
        private const val SQL_DELETE_TABLE_Usuario =
            "DROP TABLE IF EXISTS Usuario; "

        //Eliminar tabla Partidos
        private const val SQL_DELETE_TABLE_Partidos =
            "DROP TABLE IF EXISTS Partidos; "

    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla Usuario
        db.execSQL(SQL_CREATE_USUARIO_TABLE)
        // Crear la tabla Partidos
        db.execSQL(SQL_CREATE_PARTIDOS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // En caso de actualizaciones futuras de la base de datos
    }

    fun borrarTodasLasTablas() {
        val db = writableDatabase
        db.execSQL(SQL_DELETE_TABLE_Usuario)
        db.execSQL(SQL_DELETE_TABLE_Partidos)
        onCreate(db)
    }
}

