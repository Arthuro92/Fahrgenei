package com.android.cows.fahrgemeinschaft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        Button btn = (Button) contentViewGruppenNutzer.findViewById(R.id.buttonFragmentGruppenNutzer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo this in popup please not as activity

                Intent intent = new Intent(getActivity(), InviteUser.class);
                Bundle bundle = getActivity().getIntent().getExtras();
                intent.putExtra("gid", bundle.getString("gid"));

                startActivity(intent);
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }
}
