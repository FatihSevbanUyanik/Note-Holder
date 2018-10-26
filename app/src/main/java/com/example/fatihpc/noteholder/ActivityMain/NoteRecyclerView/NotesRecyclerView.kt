package com.example.fatihpc.noteholder.ActivityMain.NoteRecyclerView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

class NotesRecyclerView(context: Context, attributeSet: AttributeSet): RecyclerView(context, attributeSet) {

    // properties
    lateinit var viewsToBeHiddenWhenDataIsPresent : ArrayList<View>
    lateinit var viewsToBePresentWhenDataIsPresent : ArrayList<View>

    val adapterDataObserver = object: AdapterDataObserver(){

        override fun onChanged() {
            arrangeViewsVisibility()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            arrangeViewsVisibility()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            arrangeViewsVisibility()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            arrangeViewsVisibility()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            arrangeViewsVisibility()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            arrangeViewsVisibility()
        }
    }

    /**
     * arranges the views visibility in Activity
     * Main depending on the recycler views state.
     */
    fun arrangeViewsVisibility() {

        if (adapter != null) {

            // list is empty
            if (adapter.itemCount == 0){
               for (view in viewsToBeHiddenWhenDataIsPresent)
                    view.visibility = View.VISIBLE

                for (view in viewsToBePresentWhenDataIsPresent)
                    view.visibility = View.INVISIBLE

                visibility = View.INVISIBLE
            }

            // list is not empty
            else if (adapter.itemCount > 1) {
                for (view in viewsToBeHiddenWhenDataIsPresent)
                    view.visibility = View.INVISIBLE

                for (view in viewsToBePresentWhenDataIsPresent)
                    view.visibility = View.VISIBLE

                visibility = View.VISIBLE
            }
        }
    }

    /**
     * sets the adapter and registers AdapterDataObserver.
     * @param adapter is the adapter that will be set.
     */
    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (adapter != null) {
            adapter.registerAdapterDataObserver(adapterDataObserver)
        }

        adapterDataObserver.onChanged()
    }


}