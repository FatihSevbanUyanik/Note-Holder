package com.example.fatihpc.noteholder.ActivityMain.NoteRecyclerView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.example.fatihpc.noteholder.ActivityMain.ActivityMain
import com.example.fatihpc.noteholder.ActivityMain.NoteRecyclerView.NotesRecViewAdapter

class NoteTouchCallback(val context: Context) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {

        // adding only to items touchable property.
        if (viewHolder?.itemViewType == NotesRecViewAdapter.ITEM ) {
            return makeMovementFlags(0, ItemTouchHelper.END)
        }

        return makeMovementFlags(0, 0)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        (context as ActivityMain).notesRecViewAdapter.onSwipe(viewHolder?.adapterPosition)
    }
}