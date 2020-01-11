package com.example.vikingesejllog.note.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vikingesejllog.R;

public class NoteDialogSingleNumberPicker extends NoteDialog {

    private String[] values;
    private int field;

    public NoteDialogSingleNumberPicker(String[] values, int field){
        this.values = values;
        this.field = field;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.note_fragment_dialog_singlenumberpicker, (ViewGroup) getView(), false);
        NumberPicker picker = v.findViewById(R.id.numberpicker1);
        picker.setMinValue(0);
        picker.setMaxValue(values.length-1);
        picker.setDisplayedValues(values);

        builder.setPositiveButton("Godkend", (dialog, which) -> {
            getCallback().onSingleNumberPickerSelected(values[picker.getValue()], field);
        });

        builder.setNegativeButton("Afbryd", (dialog, which) -> {
            dialog.cancel();
        });

        builder.setView(v);

        return builder.create();
    }
}
