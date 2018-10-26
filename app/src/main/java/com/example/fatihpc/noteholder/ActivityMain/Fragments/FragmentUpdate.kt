package com.example.fatihpc.noteholder.ActivityMain.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.example.fatihpc.noteholder.ActivityMain.ActivityMain
import com.example.fatihpc.noteholder.ModelObjects.Note
import com.example.fatihpc.noteholder.R
import kotlinx.android.synthetic.main.fragment_update.view.*

class FragmentUpdate : DialogFragment(){

    // properties
    lateinit var etNoteUpdate: EditText
    lateinit var tvDate: TextView
    lateinit var checkBoxIsExecuted: CheckBox
    lateinit var activityMainContext : ActivityMain

    /**
     * creates the required fragment.
     * @param inflater is the layout inflater that inflates the corresponding layout.
     * @param container is the parent layout of the fragment that will be added.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflating the required layout
        activityMainContext = context as ActivityMain
        val view: View = inflater.inflate(R.layout.fragment_update, container, false)

        // initialising properties
        checkBoxIsExecuted = view.checkBoxIsExecuted
        etNoteUpdate = view.etNoteUpdate
        tvDate = view.tvDate

        // getting the corresponding note and position.
        val position = arguments?.getInt("position") as Int
        val relatedNote = activityMainContext.notesRecViewAdapter.notes[position]
        setFragmentData(relatedNote)

        // setting setOnClickListener
        view.btnUpdateNote.setOnClickListener { updateNote(position) }
        view.btnCancelUpdate.setOnClickListener { dismiss() }
        checkBoxIsExecuted.setOnClickListener { setCheckBoxIsExecutedBehaviour() }

        return view
    }

    /**
     * sets the behaviour of the check box.
     */
    @SuppressLint("SetTextI18n")
    private fun setCheckBoxIsExecutedBehaviour() {
        if (checkBoxIsExecuted.isChecked) {
            checkBoxIsExecuted.text = "Note Executed"
        } else {
            checkBoxIsExecuted.text = "Note Not Executed"
        }
    }

    /**
     * sets the fragments edit text and check box condition.
     * @param relatedNote is the note whose properties will be used.
     */
    private fun setFragmentData(relatedNote: Note) {
        etNoteUpdate.setText(relatedNote.COLUMN_NOTE_CONTENT)
        checkBoxIsExecuted.isChecked = relatedNote.COLUMN_NOTE_IS_EXECUTED.equals("1")

        val noteDate = relatedNote.COLUMN_NOTE_DATE

        val convertedDate = DateUtils.getRelativeTimeSpanString( noteDate,
                System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, 0)

        tvDate.text = convertedDate

        setCheckBoxIsExecutedBehaviour()
    }

    /**
     * gets the data that will be used for
     * substitution and sends it to the adapter.
     */
    private fun updateNote(position: Int) {

        val updatedNote = etNoteUpdate.text.toString()
        val isExecuted : String

        if (checkBoxIsExecuted.isChecked) {
            isExecuted = "1"
        } else {
            isExecuted = "0"
        }

        activityMainContext.notesRecViewAdapter.
                updateNote(position, updatedNote, isExecuted)

        dismiss()
    }

}