package com.example.gym

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import java.lang.IllegalArgumentException
import java.util.HashMap

class MemberProvider() : ContentProvider() {
    companion object {
        val PROVIDER_NAME = "com.example.Gym.MemberProvider"
        val URL = "content://" + PROVIDER_NAME + "/member"
        val CONTENT_URI = Uri.parse(URL)
        val _ID = "_id"
        val F_NAME = "firstname"
        val L_NAME = "lastname"
        val AGE = "age"
        val NUMBER = "number"
        val MEMBERSHIP = "membership"



        private val MEMBER_PROJECTION_MAP: HashMap<String, String>? = null
        val MEMBER = 1
        val MEMBER_ID = 2
        val uriMatcher: UriMatcher? = null
        val DATABASE_NAME = "Gym11"
        val MEMBER_TABLE_NAME = "members11"
        val DATABASE_VERSION = 1
        val CREATE_DB_TABLE = " CREATE TABLE " + MEMBER_TABLE_NAME +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + " firstname TEXT NOT NULL, " +
                " lastname TEXT NOT NULL, "+" age TEXT NOT NULL, "+" number TEXT NOT NULL, "+" membership TEXT NOT NULL);"
        private var sUriMatcher = UriMatcher(UriMatcher.NO_MATCH);
        init
        {

            sUriMatcher.addURI(PROVIDER_NAME, "member", MEMBER);
            sUriMatcher.addURI(PROVIDER_NAME, "member/#", MEMBER_ID);
        }

    }
    private var db: SQLiteDatabase? = null

    private class DatabaseHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_DB_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + MEMBER_TABLE_NAME)
            onCreate(db)
        }
    }

    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.  */
        db = dbHelper.writableDatabase
        return if (db == null) false else true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val rowID = db!!.insert(MEMBER_TABLE_NAME, "", values)

        if (rowID > 0) {
            val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }
        throw SQLException("Failed to add a record into $uri")
    }

    override fun query(
        uri: Uri, projection: Array<String>?,
        selection: String?, selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var sortOrder = sortOrder
        val qb = SQLiteQueryBuilder()
        qb.tables = MEMBER_TABLE_NAME
        if (uriMatcher != null) {
            when (uriMatcher.match(uri)) {
                /* STUDENTS -> qb.projectionMap =
                    STUDENTS_PROJECTION_MAP */
                MEMBER_ID -> qb.appendWhere(_ID + "=" + uri.pathSegments[1])
                else -> {null
                }
            }
        }


        if (sortOrder == null || sortOrder === "") {

            sortOrder = F_NAME
        }
        val c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var count = 0
        when (uriMatcher!!.match(uri)) {
            MEMBER -> count = db!!.delete(
                MEMBER_TABLE_NAME, selection,
                selectionArgs
            )
            MEMBER_ID -> {
                val id = uri.pathSegments[1]
                count = db!!.delete(
                    MEMBER_TABLE_NAME,
                    _ID + " = " + id +
                            if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var count = 0
        when (uriMatcher!!.match(uri)) {
            MEMBER -> count = db!!.update(
                MEMBER_TABLE_NAME, values, selection,
                selectionArgs
            )
            MEMBER_ID -> count = db!!.update(
                MEMBER_TABLE_NAME,
                values,
                _ID + " = " + uri.pathSegments[1] + (if (!TextUtils.isEmpty(selection)) " AND ($selection)" else ""),
                selectionArgs
            )
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher!!.match(uri)) {
            MEMBER -> return "vnd.android.cursor.dir/vnd.example.member"
            MEMBER_ID -> return "vnd.android.cursor.item/vnd.example.member"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }
}