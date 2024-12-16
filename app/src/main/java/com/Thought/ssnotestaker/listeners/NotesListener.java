package com.Thought.ssnotestaker.listeners;

import com.Thought.ssnotestaker.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
