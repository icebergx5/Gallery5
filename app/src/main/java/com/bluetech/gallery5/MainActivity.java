package com.bluetech.gallery5;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bluetech.gallery5.ui.ImageGridFragment;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public File getMupaDirectory(){

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String mupa = "MUPA";
        File directory = new File(root + File.separator + mupa);

        if(!directory.exists()){
            directory.mkdir();
        }

        return directory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */

                int stackCount = getSupportFragmentManager().getBackStackEntryCount();

                if( getSupportFragmentManager().getFragments() != null ){
                    Fragment fr = getSupportFragmentManager().getFragments().get( stackCount > 0 ? stackCount-1 : stackCount );
                    if(null != fr){
                        ((ImageGridFragment) fr).clearCache();
                    }
                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        File directory = getMupaDirectory();
        File[] files = directory.listFiles();
        Arrays.sort(files);

        for (int i = 0; i < files.length; i++) {
            if(files[i].isDirectory()){
                String fileName = files[i].getName();
                if(!fileName.equals("thumbs") && !fileName.equals("http") && !fileName.equals("images") ) {
                    menu.add(0, i, 0, fileName);
                }
            }
        }
        navigationView.setNavigationItemSelectedListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
      //  int id = item.getItemId();
      //  String idStr = id+"";

        Fragment fragment = new ImageGridFragment();
        ((ImageGridFragment)fragment).setPath( getMupaDirectory().toString() + File.separator + item.getTitle().toString());

        // if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, item.getTitle().toString());
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
