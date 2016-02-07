package com.ricardogarfe.rsu.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class RSUDataFragment extends Fragment {

    private static final String LOG_TAG = RSUDataFragment.class.getSimpleName();
    final static String LAT = "39471791";
    final static String LONG  = "-382460";

    private String containerType;

    private ArrayAdapter<String> rsuLocationAdapter;

    public RSUDataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setContainerTypeFromBundle();

        View rootView = inflater.inflate(R.layout.fragment_rsu_data, container, false);

        rsuLocationAdapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.list_item_rsu,
                        R.id.list_item_rsu_textview,
                        new ArrayList<String>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_rsu);

        listView.setAdapter(rsuLocationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String rsuDataInfo = rsuLocationAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), RSUInformationActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, rsuDataInfo);
                startActivity(intent) ;
            }
        });

        return rootView;
    }

    private void setContainerTypeFromBundle() {
        Bundle argumentsFromMenu = getArguments();
        if ( null == argumentsFromMenu) {
            containerType = Utility.BATTERIES;
        } else {
            containerType = argumentsFromMenu.getString(Utility.CONTAINER);
        }
    }

    private void updateRSU() {
        FetchRSUTask fetchRSUTask = new FetchRSUTask(getContext(), rsuLocationAdapter);
        fetchRSUTask.execute(containerType, LAT, LONG);
    }

    @Override
    public void onResume() {
        super.onResume();
        setContainerTypeFromBundle();
        updateRSU();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRSU();
    }

}
