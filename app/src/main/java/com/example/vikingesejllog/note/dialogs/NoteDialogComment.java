package com.example.vikingesejllog.note.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vikingesejllog.R;

public class NoteDialogComment extends NoteDialog {
    private String comment;
    private EditText commentText;

    public NoteDialogComment(String comment) {
        this.comment = comment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.note_fragment_commentdialog, null);
        commentText = v.findViewById(R.id.commentdialogtext);
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

    @Override
    public void onResume() {
        super.onResume();

        commentText.post(() -> {
            commentText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(commentText, InputMethodManager.SHOW_IMPLICIT);
            commentText.setSelection(commentText.getText().length());
        });
    }
}
