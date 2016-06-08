package com.android.cows.fahrgemeinschaft;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentGruppenNutzerActivity extends Fragment {

    View contentViewGruppenNutzer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contentViewGruppenNutzer = inflater.inflate(R.layout.activity_fragment_gruppen_nutzer, null);


        return contentViewGruppenNutzer;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//todo button kann raus, da er nur als demonstartion des fragments dient. dort muss noch ein Listview rein bzw. die Group übersicht.

        Button btn = (Button) contentViewGruppenNutzer.findViewById(R.id.buttonFragmentGruppenNutzer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button", Toast.LENGTH_SHORT).show();
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }
}
