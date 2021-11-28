package com.lopatinm.intercity.ui.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lopatinm.intercity.MainActivity;
import com.lopatinm.intercity.R;

public class RegistrationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);


        Button dispatcher = (Button) root.findViewById(R.id.button6);
        Button user = (Button) root.findViewById(R.id.button5);
        Button driver = (Button) root.findViewById(R.id.button4);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentReplacer(R.id.nav_registration_user);
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentReplacer(R.id.nav_registration_driver);
            }
        });

        dispatcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentReplacer(R.id.nav_registration_dispatcher);
            }
        });

        return root;
    }
}