package com.example.fatihpc.noteholder.EventBusAndService

import android.app.IntentService
import android.content.Intent
import com.example.fatihpc.noteholder.Database.DatabaseContract
import com.example.fatihpc.noteholder.ModelObjects.Note
import org.greenrobot.eventbus.EventBus

class MyIntentService: IntentService("MyIntentService") {

    override fun onHandleIntent(intent: Intent?) {

        // getting the corresponding data for updating.
        val orderBy = intent?.getStringExtra("orderBy")
        val isExecuted = intent?.getStringExtra("isExecuted")

        // setting selection according to the obtained data.
        var selection: String? = null
        var selectionArgs :Array<String?>? = null

        if (isExecuted != null) {
            selection = "${DatabaseContract.NotesEntry.COLUMN_NOTE_IS_EXECUTED} = ?"
            selectionArgs = arrayOf(isExecuted)
        }

        // updating through a cursor.
        val notes = ArrayList<Note>()

        val cursor = contentResolver.query(DatabaseContract.NotesEntry.CONTENT_URI,
                null, selection, selectionArgs, orderBy)

        if (cursor != null) {

            while (cursor.moveToNext()) {

                // getting the corresponding indexes.
                val columnNoteIdIndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_ID)
                val columnNoteContentIndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_CONTENT)
                val columnNoteDateIndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_DATE)
                val columnNoteIsExecutedndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_IS_EXECUTED)

                // querying through indexes
                val COLUMN_NOTE_ID = cursor.getString(columnNoteIdIndex)
                val COLUMN_NOTE_CONTENT = cursor.getString(columnNoteContentIndex)
                val COLUMN_NOTE_DATE = cursor.getLong(columnNoteDateIndex)
                val COLUMN_NOTE_IS_EXECUTED = cursor.getString(columnNoteIsExecutedndex)

                // adding note
                val tempNote = Note(
                        COLUMN_NOTE_ID,
                        COLUMN_NOTE_CONTENT,
                        COLUMN_NOTE_DATE,
                        COLUMN_NOTE_IS_EXECUTED)

                notes.add(tempNote)
            }
        }

        // sending to main activity through EventBus.
        EventBus.getDefault().post(DataEvent.DataNotes(notes))
    }

}