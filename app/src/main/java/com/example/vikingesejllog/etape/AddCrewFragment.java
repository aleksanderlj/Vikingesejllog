package com.example.vikingesejllog.etape;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;

public class AddCrewFragment extends Fragment implements View.OnClickListener{

    private Button acceptNameButton, cancelButton;
    private EditText crewMemberName;
    private View crewView;
    CrewListener callback;
    
    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState){
        crewView = i.inflate(R.layout.addcrew_fragment, container, false);

        crewMemberName = crewView.findViewById(R.id.crewMemberNameEditText);

        acceptNameButton = crewView.findViewById(R.id.acceptNameButton);
        cancelButton = crewView.findViewById(R.id.cancelNameButton);

        acceptNameButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        return crewView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.acceptNameButton:

                if(!crewMemberName.getText().toString().isEmpty()) {
                    callback.onMemberSelected(crewMemberName.getText().toString());
                }else{
                    Toast toast = Toast.makeText(getActivity(), "Indtast et navn",Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;
            case R.id.cancelNameButton:

               break;
        }

        try {
            getActivity().getSupportFragmentManager().popBackStack();
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void setCrewListener(CrewListener callback){
        this.callback = callback;
    }

    @Override
    public void onDestroy() {
        callback.enableButtons();
        super.onDestroy();
    }
}