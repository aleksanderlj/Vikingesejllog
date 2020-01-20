package com.example.vikingesejllog.note.dialogs;

public interface NoteDialogListener {
    void onNumberPickerSelected(String[] values, int field);
    void onNumberPickerDelete(int field);
    void onCommentSelected(String comment);
}
