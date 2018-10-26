package com.example.fatihpc.noteholder.ActivityMain

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.fatihpc.noteholder.*
import com.example.fatihpc.noteholder.EventBusAndService.DataEvent
import com.example.fatihpc.noteholder.EventBusAndService.MyIntentService
import com.example.fatihpc.noteholder.ActivityMain.Fragments.FragmentNewNote
import com.example.fatihpc.noteholder.ActivityMain.NoteRecyclerView.NoteTouchCallback
import com.example.fatihpc.noteholder.ActivityMain.NoteRecyclerView.NotesRecViewAdapter
import com.example.fatihpc.noteholder.Database.DatabaseContract
import com.example.fatihpc.noteholder.Database.MyAsyncQueryHandler
import com.example.fatihpc.noteholder.ModelObjects.Filters
import com.example.fatihpc.noteholder.ModelObjects.Note
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class ActivityMain : AppCompatActivity(){//, LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var notesRecViewAdapter: NotesRecViewAdapter

    /**
     * creates the initial layout.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRecyclerView()
        setToolbar()
    }

    /**
     * sets the toolbar.
     */
    private fun setToolbar() {
        setSupportActionBar(toolbar)
    }

    /**
     * adds new note to the database and updates the recycler view.
     */
    private fun addNewNote() {

        // getting the date of the new note.
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 0)
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.YEAR, 0)

        // setting content values and adding.
        val contentValues = ContentValues()
        contentValues.put(DatabaseContract.NotesEntry.COLUMN_NOTE_CONTENT, "content")
        contentValues.put(DatabaseContract.NotesEntry.COLUMN_NOTE_IS_EXECUTED, "0")
        contentValues.put(DatabaseContract.NotesEntry.COLUMN_NOTE_DATE, calendar.timeInMillis)
        contentResolver?.insert(DatabaseContract.NotesEntry.CONTENT_URI, contentValues)
        updateLayout()
    }

    /**
     * updates the layout and recycler view
     * depending depending on the arranged filters.
     */
    fun updateLayout() {

        val filter = getSharedFilter()

        when(filter) {

            Filters.COMPLETED -> {
                val orderBy = null
                val isExecuted = "1"
                sendUpdateRequest(orderBy, isExecuted)
            }

            Filters.NON_COMPLETED -> {
                val orderBy = null
                val isExecuted = "0"
                sendUpdateRequest(orderBy, isExecuted)
            }

            Filters.DESCENDING_ORDER -> {
                val orderBy = "${DatabaseContract.NotesEntry.COLUMN_NOTE_DATE} DESC"
                val isExecuted = null
                sendUpdateRequest(orderBy, isExecuted)
            }

            Filters.ASCENDING_ORDER -> {
                val orderBy = "${DatabaseContract.NotesEntry.COLUMN_NOTE_DATE} ASC"
                val isExecuted = null
                sendUpdateRequest(orderBy, isExecuted)
            }

            Filters.NO_FILTER -> {
                val orderBy = null
                val isExecuted = null
                sendUpdateRequest(orderBy, isExecuted)
            }

        }

    }

    /**
     * opens new note fragment.
     */
    fun createNewNote(view: View) {
        val fragmentNewNote = FragmentNewNote()
        fragmentNewNote.show(supportFragmentManager, "FRAG")
    }

    /**
     * sets the filter.
     */
    fun setSharedFilter(filter: Int){
        val sharedPreferences = getSharedPreferences(packageName+"sharedPreferences", Context.MODE_PRIVATE)
        val editor =sharedPreferences.edit()
        editor.putInt("filter", filter)
        editor.apply()
    }

    /**
     * gets the prearranged filter.
     */
    fun getSharedFilter(): Int {
        val sharedPreferences = getSharedPreferences(packageName+"sharedPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("filter", 4)
    }

    /**
     * sets the recycler view and layout manager.
     */
    private fun setRecyclerView() {

        // setting the views that will be visibe
        // and invisible depending on the list size.
        val viewsToBePresent = ArrayList<View>()
        val viewsToBeHidden = ArrayList<View>()
        viewsToBeHidden.add(tvNoNoteInfo)
        viewsToBeHidden.add(btnCreateNewNote)

        rcvNotes.viewsToBeHiddenWhenDataIsPresent = viewsToBeHidden
        rcvNotes.viewsToBePresentWhenDataIsPresent= viewsToBePresent

        // seting toch helper.
        val callback = NoteTouchCallback(this)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rcvNotes)

        // setting recycler view adapter.
        notesRecViewAdapter = NotesRecViewAdapter(this, ArrayList<Note>())
        notesRecViewAdapter.setHasStableIds(true)
        rcvNotes.adapter = notesRecViewAdapter
        updateLayout()

        // setting layout manager.
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rcvNotes.layoutManager = linearLayoutManager
    }

    /**
     * registers EventBus.
     */
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    /**
     * unregisters EventBus.
     */
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * sets the menu items functionality.
     * @param item is the selected menu item.
     * @return is the boolean type.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when {

            // new note
            item?.itemId == R.id.menu_item_menu_new_note -> {
                createNewNote(View(this))
            }

            // completed items
            item?.itemId == R.id.menu_item_completed -> {
                val orderBy = null
                val isExecuted = "1"
                sendUpdateRequest(orderBy, isExecuted)
                setSharedFilter(Filters.COMPLETED)
            }

            // non completed items
            item?.itemId == R.id.menu_item_non_completed -> {
                val orderBy = null
                val isExecuted = "0"
                sendUpdateRequest(orderBy, isExecuted)
                setSharedFilter(Filters.NON_COMPLETED)
            }

            // Descending order.
            item?.itemId == R.id.menu_item_descending_order -> {
                val orderBy = "${DatabaseContract.NotesEntry.COLUMN_NOTE_DATE} DESC"
                val isExecuted = null
                sendUpdateRequest(orderBy, isExecuted)
                setSharedFilter(Filters.DESCENDING_ORDER)
            }

            // Ascending order.
            item?.itemId == R.id.menu_item_ascending_order -> {
                val orderBy = "${DatabaseContract.NotesEntry.COLUMN_NOTE_DATE} ASC"
                val isExecuted = null
                sendUpdateRequest(orderBy, isExecuted)
                setSharedFilter(Filters.ASCENDING_ORDER)
            }

            // no filter.
            item?.itemId == R.id.menu_item_no_filter -> {
                val orderBy = null
                val isExecuted = null
                sendUpdateRequest(orderBy, isExecuted)
                setSharedFilter(Filters.NO_FILTER)
            }

            // removing all note items
            item?.itemId == R.id.menu_item_remove_all_items -> {
                val contentUri = DatabaseContract.NotesEntry.CONTENT_URI

                val myAsyncQueryHandler = MyAsyncQueryHandler(contentResolver)
                myAsyncQueryHandler.startDelete(0, null,
                        contentUri, null, null)

                notesRecViewAdapter.notes.clear()
                notesRecViewAdapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * sends update request for the note data in the recycler view.
     * @param orderBy is the sorting type of the demanded data.
     * @param isExecuted is the state type of the demanded data.
     */
    private fun sendUpdateRequest(orderBy: String?, isExecuted: String?) {
        val intent = Intent(this, MyIntentService::class.java)
        intent.putExtra("orderBy", orderBy)
        intent.putExtra("isExecuted", isExecuted)
        startService(intent)
    }

    /**
     * updates the coming data.
     */
    @Subscribe(sticky = true , threadMode = ThreadMode.MAIN)
    fun updateData( event: DataEvent.DataNotes) {
        val notes = event.notes
        notesRecViewAdapter.notes = notes
        notesRecViewAdapter.notifyDataSetChanged()
    }

    //loaderManager.initLoader(100, null, this)
    /*override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Cursor>? {

        if (id == 100)
            return CursorLoader(this, DatabaseContract.NotesEntry.CONTENT_URI, null, null, null, null)

        return null
    }

    override fun onLoadFinished(p0: Loader<Cursor>?, cursor: Cursor?) {

        val notes = ArrayList<Note>()

        if (cursor != null) {

            while (cursor.moveToNext()) {

                val columnNoteIdIndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_ID)
                val columnNoteContentIndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_CONTENT)
                val columnNoteDateIndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_DATE)
                val columnNoteIsExecutedndex = cursor.getColumnIndex(DatabaseContract.NotesEntry.COLUMN_NOTE_IS_EXECUTED)

                val COLUMN_NOTE_ID = cursor.getString(columnNoteIdIndex)
                val COLUMN_NOTE_CONTENT = cursor.getString(columnNoteContentIndex)
                val COLUMN_NOTE_DATE = cursor.getLong(columnNoteDateIndex)
                val COLUMN_NOTE_IS_EXECUTED = cursor.getString(columnNoteIsExecutedndex)

                notes.add( Note(COLUMN_NOTE_ID, COLUMN_NOTE_CONTENT, COLUMN_NOTE_DATE, COLUMN_NOTE_IS_EXECUTED) )
            }

        }

        notesRecViewAdapter.notes = notes
        notesRecViewAdapter.notifyDataSetChanged()
    }

    override fun onLoaderReset(p0: Loader<Cursor>?) {}*/
}
