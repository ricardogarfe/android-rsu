package com.ricardogarfe.rsu.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RSUDataActivityFragment extends Fragment {

    public RSUDataActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rsu_data_fragment, container, false);

        List<String> rsuLocationResults = new ArrayList<>();
        rsuLocationResults.add("CONTENEDORES DE PILAS - MERCADO ROJAS CLEMENTE");
        rsuLocationResults.add("CONTENEDORES DE PILAS - MERCADO CENTRAL");
        rsuLocationResults.add("CONTENEDORES DE PILAS - COLEGIO JESUS-MARIA");

        ArrayAdapter<String> RSULocationAdapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.list_item_rsu,
                        R.id.list_item_rsu_textview,
                        rsuLocationResults);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_rsu);

        listView.setAdapter(RSULocationAdapter);

        return rootView;
    }
}
