package com.example.vikingesejllog.note.dialogs;

import androidx.fragment.app.DialogFragment;

public class NoteDialog extends DialogFragment {
    private NoteDialogListener callback;

    public void setNoteDialogListener(NoteDialogListener callback){
        this.callback = callback;
    }

    public NoteDialogListener getCallback() {
        return callback;
    }
}
