package com.ricardogarfe.rsu.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class RSUInformationActivityFragment extends Fragment {

    public RSUInformationActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        View viewRoot = inflater.inflate(R.layout.fragment_rsuinformation, container, false);

        if (null != intent && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String rsuData = intent.getStringExtra(Intent.EXTRA_TEXT);
            TextView rsuInfoTextView = (TextView) viewRoot.findViewById(R.id.rsu_dummy_info);
            rsuInfoTextView.setText(rsuData);
        }
        return viewRoot;
    }
}
