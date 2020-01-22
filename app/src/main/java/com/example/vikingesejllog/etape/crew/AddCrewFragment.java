package com.example.vikingesejllog.etape.crew;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;

import io.sentry.Sentry;

public class AddCrewFragment extends Fragment implements View.OnClickListener{

    private Button acceptNameButton, cancelButton;
    private EditText crewMemberName;
    private View crewView;
    CrewListener callback;
    SharedPreferences toastFirstTime;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState){
        crewView = i.inflate(R.layout.note_fragment_addcrew, container, false);

        crewMemberName = crewView.findViewById(R.id.crewMemberNameEditText);

        acceptNameButton = crewView.findViewById(R.id.acceptNameButton);
        cancelButton = crewView.findViewById(R.id.cancelNameButton);

        acceptNameButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        crewMemberName.requestFocus();
        toggleKeyboard();

        return crewView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.acceptNameButton:

                if(!crewMemberName.getText().toString().isEmpty()) {
                    callback.onMemberSelected(crewMemberName.getText().toString());
                    toastFirstTime = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    if (!toastFirstTime.getBoolean("firstTime", false)){ // Oplyser brugeren om at han kan swipe besætningsmedlemmer væk for at slette dem - KUN FØRSTE GANG!
                        Toast.makeText(getActivity(), "Swipe for at slette besætningsmedlemmer", Toast.LENGTH_LONG).show();
                        toastFirstTime.edit().putBoolean("firstTime", true).apply();
                    }
                }else{
                    Toast toast = Toast.makeText(getActivity(), "Indtast et navn",Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;
            case R.id.cancelNameButton:

               break;
        }
        toggleKeyboard();

        try {
            getActivity().getSupportFragmentManager().popBackStack();
        }catch (NullPointerException npe){
            Sentry.capture(npe);
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

    private void toggleKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
}