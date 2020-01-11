package com.example.vikingesejllog.note.dialogs;

public interface NoteDialogListener {
    //void onSailSpeedSelected(String sailSpeed);
    //void onWindSelected(String direction, String speed);
    //void onRowersSelected(String rowers);
    //void onSailForingSelected(String sailForing);
    //void onSailDirectionSelected(String direction, String board);
    //void onCourseSelected(String course);
    void onSingleNumberPickerSelected(String value, int field);
    void onDoubleNumberPickerSelected(String value1, String value2,  int field);

}
