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

public class NoteDialogTripleNumberPicker extends NoteDialog {

    private String[] values1, values2, values3;
    private int field;

    public NoteDialogTripleNumberPicker(String[] values1, String[] values2, String[] values3, int field){
        this.values1 = values1;
        this.values2 = values2;
        this.values3 = values3;
        this.field = field;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.note_fragment_dialog_triplenumberpicker, (ViewGroup) getView(), false);
        NumberPicker picker1 = v.findViewById(R.id.numberpicker1);
        picker1.setMinValue(0);
        picker1.setMaxValue(values1.length-1);
        picker1.setDisplayedValues(values1);

        NumberPicker picker2 = v.findViewById(R.id.numberpicker2);
        picker2.setMinValue(0);
        picker2.setMaxValue(values2.length-1);
        picker2.setDisplayedValues(values2);

        NumberPicker picker3 = v.findViewById(R.id.numberpicker3);
        picker3.setMinValue(0);
        picker3.setMaxValue(values3.length-1);
        picker3.setDisplayedValues(values3);

        builder.setPositiveButton("Godkend", (dialog, which) -> {
            getCallback().onTripleNumberPickerSelected(
                    values1[picker1.getValue()],
                    values2[picker2.getValue()],
                    values3[picker3.getValue()],
                    field);
        });

        builder.setNegativeButton("Afbryd", (dialog, which) -> {
            dialog.cancel();
        });

        builder.setView(v);

        return builder.create();
    }
}
