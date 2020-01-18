package com.example.vikingesejllog.note.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vikingesejllog.R;

public class NoteDialogComment extends NoteDialog {
    private String comment;

    public NoteDialogComment(String comment){
        this.comment = comment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.note_fragment_commentdialog, null);
        EditText commentText = v.findViewById(R.id.commentdialogtext);
        commentText.setText(comment);

        b.setPositiveButton("Godkend", (dialog, which) -> {
            getCallback().onCommentSelected(commentText.getText().toString());
        });

        b.setNegativeButton("Afbryd", (dialog, which) -> {
            dialog.cancel();
        });


        b.setView(v);

        return b.create();
    }
}
