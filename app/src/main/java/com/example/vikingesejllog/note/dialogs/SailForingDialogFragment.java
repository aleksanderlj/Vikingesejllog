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

public class SailForingDialogFragment extends NoteDialog {
    private String[] sailforing;

    public SailForingDialogFragment(){
        this.sailforing = new String[]{"F", "Ø", "N1", "N2", "N3"};
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.note_fragment_dialog_singlenumberpicker, (ViewGroup) getView(), false);
        NumberPicker picker = v.findViewById(R.id.numberpicker1);
        picker.setMinValue(0);
        picker.setMaxValue(sailforing.length-1);
        picker.setDisplayedValues(sailforing);

        builder.setPositiveButton("Godkend", (dialog, which) -> {
            getCallback().onSailForingSelected(sailforing[picker.getValue()]);
        });

        builder.setNegativeButton("Afbryd", (dialog, which) -> {
            dialog.cancel();
        });

        builder.setView(v);

        return builder.create();
    }

    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.note_fragment_dialog_singlenumberpicker, container, false);

        NumberPicker picker = v.findViewById(R.id.sail_foring_picker);
        picker.setMinValue(0);
        picker.setMaxValue(sailforing.length-1);
        picker.setDisplayedValues(sailforing);
        return v;
    }

     */
}
