package com.example.vikingesejllog.etape;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.vikingesejllog.R;

public class AddCrewFragment extends Fragment implements View.OnClickListener{

    private Button acceptNameButton, cancelButton;
    private EditText crewMemberName;
    private View rod;
    FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState){
        rod = i.inflate(R.layout.addcrew_fragment, container, false);

        crewMemberName = rod.findViewById(R.id.crewMemberNameEditText);

        acceptNameButton = rod.findViewById(R.id.acceptNameButton);
        cancelButton = rod.findViewById(R.id.cancelNameButton);

        acceptNameButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        return rod;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.acceptNameButton:
                Bundle bundle = new Bundle();
                bundle.putString("name", crewMemberName.getText().toString());

                break;
            case R.id.cancelNameButton:
                try {
                    getActivity().getSupportFragmentManager().popBackStack();
                }catch (NullPointerException npe){
                    npe.printStackTrace();
                }
               break;


        }

    }
}