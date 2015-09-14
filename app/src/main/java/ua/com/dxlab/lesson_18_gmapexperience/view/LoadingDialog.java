package ua.com.dxlab.lesson_18_gmapexperience.view;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.com.dxlab.lesson_18_gmapexperience.R;

/**
 * Created by Dima on 14.09.2015.
 */
public class LoadingDialog extends DialogFragment {

    private TextView mTV;

    public LoadingDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_loader, container);
        mTV = (TextView) view.findViewById(R.id.loading);
        getDialog().setTitle("Loading...");
        getDialog().getWindow().setLayout(100, 100);


        return view;
    }

}
