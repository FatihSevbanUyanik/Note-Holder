package com.example.fatihpc.noteholder.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    // Authority
    public static final String CONTENT_AUTHORITY = "com.example.fatihpc.noteholder.Database.DatabaseProvider";

    // Base Content
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Paths
    public static final String PATH_NOTES = NotesEntry.TABLE_NAME;


    /**
     * Contains the structure of the note object for database.
     */
    public static final class NotesEntry implements BaseColumns{
        // Table Name
        public static final String TABLE_NAME = "notes";

        // Content Uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTES);

        // attributes
        public static final String COLUMN_NOTE_ID = BaseColumns._ID;
        public static final String COLUMN_NOTE_CONTENT = "noteContent";
        public static final String COLUMN_NOTE_DATE = "noteDate";
        public static final String COLUMN_NOTE_IS_EXECUTED = "noteIsExecuted";
    }

}