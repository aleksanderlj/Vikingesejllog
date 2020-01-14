package com.example.vikingesejllog.etape;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;

public class AddCrewFragment extends Fragment {

    private FragmentAddListener listener;
    private EditText crewMemberNameEditText;
    private Button acceptNameButton;
    private CrewListener crewListener;

    public interface FragmentAddListener{
        void onInputSent(CharSequence input);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.addcrew_fragment,container, false);
        crewMemberNameEditText = v.findViewById(R.id.crewMemberNameEditText);
        acceptNameButton = v.findViewById(R.id.acceptNameButton);

        acceptNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence input = crewMemberNameEditText.getText();
                listener.onInputSent(input);

                crewListener.onMemberSelected(input.toString());

            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof FragmentAddListener){
            listener = (FragmentAddListener)context;
        }else{
            throw new RuntimeException(context.toString()
            +" must implement FragmentAddListner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    public void setCrewListener(CrewListener crewListener) {
        this.crewListener = crewListener;
    }
}

