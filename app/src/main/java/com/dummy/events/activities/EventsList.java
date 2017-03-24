package com.dummy.events.activities;

import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dummy.events.providers.Adapter;
import com.dummy.events.R;
import com.dummy.events.database.Dao;
import com.pixplicity.easyprefs.library.Prefs;

public class EventsList extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    Adapter adapter;
    Toolbar toolbar;
    Dao dao;

    String query;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        dao = new Dao(this);

        initNavigationDrawer();
        fab.setOnClickListener(this);

        Toast.makeText(this, "Welcome "+Prefs.getString("Name",""), Toast.LENGTH_LONG).show();
    }

    public void initNavigationDrawer() {

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.add : drawerLayout.closeDrawers();
                        Intent intent = new Intent(EventsList.this,AddEvent.class);
                        startActivity(intent);
                    break;

                    case R.id.login: drawerLayout.closeDrawers();
                        Intent intent1 = new Intent(EventsList.this,Login.class);
                        startActivity(intent1);
                        break;

                }
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }
            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        query = "Select AID as id,title as title,signState as signState,image as image from events";
        cursor = dao.fetch(query);
        cursor.moveToFirst();
        adapter = new Adapter(this,cursor);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(EventsList.this,AddEvent.class);
        startActivity(intent);
    }
}
