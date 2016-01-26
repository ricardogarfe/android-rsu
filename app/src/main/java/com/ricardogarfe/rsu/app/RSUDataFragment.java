package com.ricardogarfe.rsu.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RSUDataFragment extends Fragment {

    private static final String LOG_TAG = RSUDataFragment.class.getSimpleName();
    final static String LAT = "39471791";
    final static String LONG  = "-382460";
    final static String TYPE_PARAM = "pilas";

    private ArrayAdapter<String> rsuLocationAdapter;

    public RSUDataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rsu_data, container, false);

        String[] rsuStringItems = {
                "CONTENEDORES DE PILAS",
                "CONTENEDORES DE PILAS",
                "CONTENEDORES DE PILAS"
        };

        String[] rsuStringMessage = {
                "MERCADO ROJAS CLEMENTE\nBotanico\nNúmero de contenedores: 1",
                "MERCADO CENTRAL\\nPlaza Mercado\\nNúmero de contenedores: 1",
                "COLEGIO JESUS-MARIA\\nG.V. Fernando el Catolico, 37\\nNúmero de contenedores: 1"
        };

        /*
        http://mapas.valencia.es/lanzadera/gps/contenedores/pilas/39471791/-382460
         */
        List<String> rsuLocationResults = Arrays.asList(rsuStringItems);

        rsuLocationAdapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.list_item_rsu,
                        R.id.list_item_rsu_textview,
                        rsuLocationResults);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_rsu);

        listView.setAdapter(rsuLocationAdapter);

        return rootView;
    }

    private void updateRSU() {
        FetchRSUTask fetchRSUTask = new FetchRSUTask(getContext(), rsuLocationAdapter);
        fetchRSUTask.execute(TYPE_PARAM, LAT, LONG);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRSU();
    }

}
