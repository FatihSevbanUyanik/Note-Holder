package com.example.fatihpc.noteholder.Database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.widget.Toast
import com.example.fatihpc.noteholder.Database.DatabaseContract.*

class DatabaseProvider: ContentProvider() {

    // properties
    lateinit var sqLiteDatabase: SQLiteDatabase

    companion object {

        // static properties
        val URI_MATCHER: UriMatcher
        val URI_CODE_NOTES = 1

        // static constructor
        init {
            URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)
            URI_MATCHER.addURI(CONTENT_AUTHORITY, PATH_NOTES, URI_CODE_NOTES)
        }
    }

    /**
     * creates the database and gets the writable database.
     */
    override fun onCreate(): Boolean {
        val databaseHelper = DatabaseHelper(context)
        sqLiteDatabase = databaseHelper.writableDatabase
        return true
    }

    /**
     * inserts the passed items to the database.
     * @param uri is the passed uri that determines the table of the inserted item.
     * @param contentValues are the values that will be inserted to the database.
     * @return the resulting uri of the recorded object.
     */
    override fun insert(uri: Uri?, contentValues: ContentValues?): Uri? {

        when(URI_MATCHER.match(uri)) {

            // inserting an object to Notes Table.
            URI_CODE_NOTES -> {
                val rowId = sqLiteDatabase.insert(NotesEntry.TABLE_NAME, null, contentValues)
                val recordUri = ContentUris.withAppendedId(uri, rowId)
                Toast.makeText(context, recordUri.toString(), Toast.LENGTH_SHORT).show()
                return recordUri
            }

        }

        return null
    }

    /**
     * queries the demanded data from database.
     * @param uri is the passed uri that determines the table to be queried.
     * @param selection contains the constraints of the demanded data.
     * @param selectionArgs contains the arguments of the selection.
     * @param sortOrder is the data that indicates the ordering of the data.
     * @return the cursor of the required data.
     */
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        when(URI_MATCHER.match(uri)) {

            // querying data from Notes Table.
            URI_CODE_NOTES -> {
                return sqLiteDatabase.query(NotesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }

        }

        return null
    }

    /**
     * updates data in database.
     * @param uri is the passed uri that determines the table to be updated.
     * @param contentValues are the values that will be substituted.
     * @param selection contains the constraints of the demanded data.
     * @param selectionArgs contains the arguments of the selection.
     * @return the affected rows of the database.
     */
    override fun update(uri: Uri?, contentValues: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {

        when(URI_MATCHER.match(uri)) {

            // updates data in Notes Table.
            URI_CODE_NOTES -> {
                val rowsAffected = sqLiteDatabase.update(NotesEntry.TABLE_NAME, contentValues, selection, selectionArgs)

                if (rowsAffected == 0) {
                    Toast.makeText(context, "Error while updating note.", Toast.LENGTH_SHORT).show()
                } else {
                    return rowsAffected
                }
            }
        }

        return 0
    }

    /**
     * deletes data in database.
     * @param uri determines the table of the object to be deleted.
     * @param selection contains the constraints of the data to be deleted.
     * @param selectionArgs contains the arguments of the selection.
     * @return the affected rows of the database.
     */
    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {

        when(URI_MATCHER.match(uri)) {

            // deletes data from notes table
            URI_CODE_NOTES -> {
                val rowsAffected = sqLiteDatabase.delete(NotesEntry.TABLE_NAME, selection, selectionArgs)

                if (rowsAffected == 0) {
                    Toast.makeText(context, "Error while deleting note.", Toast.LENGTH_SHORT).show()
                } else {
                    return rowsAffected
                }
            }
        }

        return 0
    }

    override fun getType(p0: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}