package com.example.vikingesejllog.note.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vikingesejllog.R;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.List;

public class NoteDialogNumberPicker extends NoteDialog {

    private int field;
    private String[][] values;

    public NoteDialogNumberPicker(int field, String[] ...values){
        this.field = field;
        this.values = values;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LinearLayout v = new LinearLayout(getContext());
        v.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(params);
        v.setGravity(Gravity.CENTER);

        List<NumberPicker> pickerList = new ArrayList<>();
        for(String[] s : values){
            NumberPicker picker = new NumberPicker(getContext());
            picker.setMinValue(0);
            picker.setMaxValue(s.length-1);
            picker.setDisplayedValues(s);
            NumberPicker.LayoutParams npParams = new NumberPicker.LayoutParams(
                    NumberPicker.LayoutParams.WRAP_CONTENT,
                    NumberPicker.LayoutParams.WRAP_CONTENT);
            picker.setLayoutParams(npParams);

            pickerList.add(picker);
            v.addView(picker);
        }

        builder.setPositiveButton("Godkend", (dialog, which) -> {
            String[] pickedValues = new String[pickerList.size()];
            for(int n=0 ; n < pickerList.size() ; n++){
                pickedValues[n] = values[n][pickerList.get(n).getValue()];
            }

            getCallback().onNumberPickerSelected(
                    pickedValues,
                    field);
        });

        builder.setNegativeButton("Afbryd", (dialog, which) -> {
            dialog.cancel();
        });

        builder.setView(v);

        return builder.create();
    }
}
