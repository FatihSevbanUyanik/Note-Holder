package com.example.fatihpc.noteholder.ActivityMain.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import com.example.fatihpc.noteholder.ActivityMain.ActivityMain
import com.example.fatihpc.noteholder.Database.DatabaseContract
import com.example.fatihpc.noteholder.Database.DatabaseContract.*
import com.example.fatihpc.noteholder.ModelObjects.Note
import com.example.fatihpc.noteholder.R
import kotlinx.android.synthetic.main.fragment_new_note.view.*
import java.util.*

class FragmentNewNote: DialogFragment() {

    // properties
    lateinit var btnAddNewNote: Button
    lateinit var etNewNote: EditText
    lateinit var datePicker: DatePicker
    lateinit var btnCancelNewNote: Button

    /**
     * creates the required fragment.
     * @param inflater is the layout inflater that inflates the corresponding layout.
     * @param container is the parent layout of the fragment that will be added.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflating the required layout
        val view = inflater.inflate(R.layout.fragment_new_note, container, false)

        // initialising properties
        btnAddNewNote = view.btnAddNewNote
        btnCancelNewNote = view.btnCancelNewNote
        datePicker = view.datePicker
        etNewNote = view.etNewNote

        // setting setOnClickListener
        btnAddNewNote.setOnClickListener{addNewNote()}
        btnCancelNewNote.setOnClickListener {dismiss()}

        return view
    }

    /**
     * adds new note to the database and updates the recycler view.
     */
    private fun addNewNote() {

        // getting the date of the new note.
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
        calendar.set(Calendar.MONTH, datePicker.month)
        calendar.set(Calendar.YEAR, datePicker.year)

        // setting content values and adding.
        val contentValues = ContentValues()
        contentValues.put(DatabaseContract.NotesEntry.COLUMN_NOTE_CONTENT, etNewNote.text.toString())
        contentValues.put(DatabaseContract.NotesEntry.COLUMN_NOTE_IS_EXECUTED, "0")
        contentValues.put(DatabaseContract.NotesEntry.COLUMN_NOTE_DATE, calendar.timeInMillis)
        context?.contentResolver?.insert(DatabaseContract.NotesEntry.CONTENT_URI, contentValues)
        (context as ActivityMain).updateLayout()

        dismiss()
    }

    /**
     * gets the last inserted item as fast as possible.
     * İs not used in my program. Only for example purposes.
     */
    private fun getLastInsertedItem(): Note? {

        val selection = "${NotesEntry.COLUMN_NOTE_ID} = (SELECT MAX(${NotesEntry.COLUMN_NOTE_ID}) FROM ${NotesEntry.TABLE_NAME})"

        val cursor = context?.contentResolver?.query(NotesEntry.CONTENT_URI, null,
                selection, null,null,null)

        while (cursor?.moveToNext() as Boolean) {

            val columnNoteIdIndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_ID)
            val columnNoteContentIndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_CONTENT)
            val columnNoteDateIndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_DATE)
            val columnNoteIsExecutedndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_IS_EXECUTED)

            val COLUMN_NOTE_ID = cursor.getString(columnNoteIdIndex)
            val COLUMN_NOTE_CONTENT = cursor.getString(columnNoteContentIndex)
            val COLUMN_NOTE_DATE = cursor.getLong(columnNoteDateIndex)
            val COLUMN_NOTE_IS_EXECUTED = cursor.getString(columnNoteIsExecutedndex)

            return Note(COLUMN_NOTE_ID, COLUMN_NOTE_CONTENT, COLUMN_NOTE_DATE, COLUMN_NOTE_IS_EXECUTED)
        }

        cursor.close()
        return null
    }

}