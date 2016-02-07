package com.ricardogarfe.rsu.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class RSUInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsuinformation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RSUInformationActivityFragment rsuInformationActivityFragment = new RSUInformationActivityFragment();
        Bundle intentParameters = new Bundle();
        rsuInformationActivityFragment.setArguments(intentParameters);

        // Insert the fragment by replacing any existing fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_rsu_info, rsuInformationActivityFragment).commit();
    }

}
