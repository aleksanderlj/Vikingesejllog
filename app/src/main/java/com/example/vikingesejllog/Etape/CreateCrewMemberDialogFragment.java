package com.example.vikingesejllog.etape;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.vikingesejllog.R;

public class CreateCrewMemberDialogFragment extends AppCompatDialogFragment {

    private EditText edittextCrewName;
    private Button buttonConfirm;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addcrew_fragment, null);

        builder.setView(view)
            .setTitle("OK")
            .setNegativeButton("Afbryd", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
            }
        })
            .setPositiveButton("Godkend", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
            }
        });

        edittextCrewName = view.findViewById(R.id.crewMemberNameEditText);
        buttonConfirm = view.findViewById(R.id.acceptNameButton);
        return builder.create();
    }
}
