package com.example.fatihpc.noteholder.ActivityMain.NoteRecyclerView

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.fatihpc.noteholder.ActivityMain.ActivityMain
import com.example.fatihpc.noteholder.ActivityMain.Fragments.FragmentUpdate
import com.example.fatihpc.noteholder.Database.DatabaseContract
import com.example.fatihpc.noteholder.ModelObjects.Filters
import com.example.fatihpc.noteholder.ModelObjects.Note
import com.example.fatihpc.noteholder.R
import java.util.*

class NotesRecViewAdapter(val context: Context, var notes : ArrayList<Note>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // static properties
    companion object {
        val ITEM = 0
        val FOOTER = 1
    }

    /**
     * determines which layout will be inflated.
     * @param parent is the parent of the each item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // inflating views depending on being footer or item.
        val inflater : LayoutInflater = LayoutInflater.from(context)

        // item
        if (viewType == ITEM) {
            val view = inflater.inflate(R.layout.notes_list_item, parent ,false )
            return MyNoteViewHolder(view)
        }

        // footer
        else {
            val view = inflater.inflate(R.layout.notes_list_footer, parent ,false )
            return MyFooterViewHolder(view)
        }
    }

    /**
     * gets the item count.
     */
    override fun getItemCount(): Int {

        if (notes.isEmpty()) {
            return 0
        } else {
            return notes.size + 1
        }

    }

    /**
     * positions items and footer in
     * the list by assigning integer values.
     */
    override fun getItemViewType(position: Int): Int {

        if (position < notes.size) {
            return ITEM
        } else {
            return FOOTER
        }

    }

    /**
     * gets each items id in order to set animations.
     */
    override fun getItemId(position: Int): Long {
        if (position < notes.size) {
            return notes[position].COLUMN_NOTE_ID.toLong()
        } else {
            return RecyclerView.NO_ID
        }
    }

    /**
     * adds functionality and sets each list item.
     * @param holder holds the all required views.
     * @param position is the position of the list item.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MyNoteViewHolder) {

            holder.tvNoteContent?.text = notes[position].COLUMN_NOTE_CONTENT

            val noteDate = notes[position].COLUMN_NOTE_DATE

            val convertedDate = DateUtils.getRelativeTimeSpanString( noteDate,
                    System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, 0)

            holder.tvNoteDate?.text = convertedDate
            holder.listItem?.setOnClickListener { openUpdateFragment(position) }
            setListItemBackgroundColor(holder.listItem, position)
        }

    }

    /**
     * sets the list items color background.
     * @param listItem is the item whose color background will be changed.
     * @param position of the item whose color background will be changed.
     */
    private fun setListItemBackgroundColor(listItem: View?, position: Int) {

        if (notes[position].COLUMN_NOTE_IS_EXECUTED.equals("1")) {
            listItem?.background = context.resources.getDrawable(R.color.colorNoteCompleted)
        } else if (notes[position].COLUMN_NOTE_IS_EXECUTED.equals("0")){
            listItem?.background = context.resources.getDrawable(R.color.colorNoteNonCompleted)
        }
    }

    /**
     * directs to deleteNoteItem() method by
     * passing the corresponding position value.
     */
    fun onSwipe(position: Int?) {
        deleteNoteItem(position as Int)
    }

    /**
     * deletes the note item in the specified
     * position from the database and the list.
     * @param position is the specified position.
     */
    fun deleteNoteItem(position: Int) {

        // removing from database.
        val COLUMN_NOTE_ID = notes[position].COLUMN_NOTE_ID

        context.contentResolver.delete(DatabaseContract.NotesEntry.CONTENT_URI,
                "${DatabaseContract.NotesEntry.COLUMN_NOTE_ID} = ${COLUMN_NOTE_ID}", null)

        // removing from the list.
        notes.removeAt(position)

        if (notes.size == 0) {
            context as ActivityMain
            context.setSharedFilter(Filters.NO_FILTER)
            context.updateLayout()
        } else {
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, notes.size + 1)
        }
    }

    /**
     * opens update fragment and passes the corresponding position.
     */
    private fun openUpdateFragment(position: Int) {

        val bundle = Bundle()
        bundle.putInt("position", position)

        val fragmentUpdate = FragmentUpdate()
        fragmentUpdate.arguments = bundle

        val fragmentManager = (context as ActivityMain).supportFragmentManager
        fragmentUpdate.show(fragmentManager, "FRAGMENT UPDATE")
    }

    /**
     * updates a note in database and in the list.
     * @param position is the position value of the object to be updated.
     * @param updatedNote is the new content that will be substituted.
     * @param isExecuted is the new state value that will be substituted.
     */
    fun updateNote(position: Int, updatedNote: String, isExecuted: String) {

        // updating database.
        val contentValues = ContentValues()
        contentValues.put(DatabaseContract.NotesEntry.COLUMN_NOTE_CONTENT, updatedNote)
        contentValues.put(DatabaseContract.NotesEntry.COLUMN_NOTE_IS_EXECUTED, isExecuted)

        val selection = "${DatabaseContract.NotesEntry.COLUMN_NOTE_ID} = ${notes[position].COLUMN_NOTE_ID}"
        context.contentResolver.update(DatabaseContract.NotesEntry.CONTENT_URI, contentValues, selection, null)

        // updating list.
        notes[position].COLUMN_NOTE_CONTENT = updatedNote
        notes[position].COLUMN_NOTE_IS_EXECUTED = isExecuted
        notifyDataSetChanged()
    }

    /**
     * holds all the required views of each list item.
     */
    inner class MyNoteViewHolder( itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var listItem = itemView
        var tvNoteContent = itemView?.findViewById<TextView>(R.id.tvNoteContent)
        var tvNoteDate = itemView?.findViewById<TextView>(R.id.tvNoteDate)
    }

    /**
     * holds all the required views of the footer.
     */
    inner class MyFooterViewHolder( itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var listItem = itemView
        var btnFooterAddNewNote = itemView?.findViewById<Button>(R.id.btnFooterAddNewNote)
    }

    /*override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        EventBus.getDefault().register(this)
        println("ATTACHED")
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        EventBus.getDefault().unregister(this)
        println("DETTACHED")
    }*/
}