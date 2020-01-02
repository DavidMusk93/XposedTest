package com.example.xposedtest.extension

import android.database.Cursor
import net.sqlcipher.database.SQLiteDatabase

fun String.rawQuery(handler: SQLiteDatabase, vararg args: Any): Cursor =
    handler.rawQuery(this, args)