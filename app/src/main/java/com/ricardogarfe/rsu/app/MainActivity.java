package com.ricardogarfe.rsu.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String RSUINFORMATIONFRAGMENT_TAG = "DFTAG";

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Init fragment values.
        updateRSUDataFragment(Utility.BATTERIES);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "OnResume from MainActivy.");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Retrieve selected containers
        String selectedContainer = getSelectedContainerd(id);
        // Update Fragment values.
        updateRSUDataFragment(selectedContainer);

        // Highlight the selected item, update the title, and close the drawer
        getSupportActionBar().setTitle(item.getTitle());
        getSupportActionBar().setIcon(item.getIcon());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateRSUDataFragment(String selectedContainer) {
        // Create a new fragment and specify the option to show based on
        // position
        RSUDataFragment fragment = new RSUDataFragment();
        Bundle intentParameters = new Bundle();
        intentParameters.putString(Utility.CONTAINER, selectedContainer);
        fragment.setArguments(intentParameters);

        // Insert the fragment by replacing any existing fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_rsu_main, fragment).commit();
    }

    @NonNull
    private String getSelectedContainerd(int id) {
        String selectedContainer = "";
        if (id == R.id.nav_batteries) {
            selectedContainer = Utility.BATTERIES;
        } else if (id == R.id.nav_oil) {
            selectedContainer = Utility.OIL;
        } else if (id == R.id.nav_clothes) {
            selectedContainer = Utility.CLOTHES;
        } else if (id == R.id.nav_cardboard) {
            selectedContainer = Utility.CARDBOARD;
        } else if (id == R.id.nav_glass) {
            selectedContainer = Utility.GLASS;
        } else if (id == R.id.nav_plastic) {
            selectedContainer = Utility.BOTTLING;
        } else if (id == R.id.nav_waste) {
            selectedContainer = Utility.WASTE;
        }
        return selectedContainer;
    }
}
