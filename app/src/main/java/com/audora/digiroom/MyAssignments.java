package com.audora.digiroom;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyAssignments extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_assignments, container, false);
        FloatingActionButton fab=view.findViewById(R.id.fab);
        if(getActivity() instanceof StudentDashBoard){
            fab.setVisibility(View.GONE);
        }
        return view;

    }
}
