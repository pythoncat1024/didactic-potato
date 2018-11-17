package com.python.cat.potato.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.python.cat.potato.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TODOFragment extends Fragment {


    public TODOFragment() {
        // Required empty public constructor
    }

    public static TODOFragment newInstance() {
        Bundle args = new Bundle();
        TODOFragment fragment = new TODOFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

}
