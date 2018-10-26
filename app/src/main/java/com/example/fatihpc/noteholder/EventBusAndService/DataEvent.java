package com.example.fatihpc.noteholder.EventBusAndService;

import com.example.fatihpc.noteholder.ModelObjects.Note;

import java.util.ArrayList;

public class DataEvent {

    /**
     * provides the transportation of the
     * updated notes from service to main activity.
     */
    public static class DataNotes {

        // properties
        private ArrayList<Note> notes;

        // constructor
        public DataNotes(ArrayList<Note> notes) {
            this.notes = notes;
        }

        public DataNotes() {
        }

        // methods

        public ArrayList<Note> getNotes() {
            return notes;
        }

        public void setNotes(ArrayList<Note> notes) {
            this.notes = notes;
        }
    }

}
