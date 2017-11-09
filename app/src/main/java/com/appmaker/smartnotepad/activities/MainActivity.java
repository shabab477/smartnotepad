package com.appmaker.smartnotepad.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.appmaker.smartnotepad.R;
import com.appmaker.smartnotepad.fragments.MainFragment;
import com.appmaker.smartnotepad.fragments.TextFragment;
import com.appmaker.smartnotepad.model.FullText;
import com.appmaker.smartnotepad.model.MessageEvent;
import com.appmaker.smartnotepad.model.RemoveEvent;
import com.appmaker.smartnotepad.model.UpdateText;
import com.appmaker.smartnotepad.utils.DBHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


//This activity is responsible for the naviagation drawer menu
//and showing different types of screens like List Note Screen
//and add note screen

//PLEASE LOOK INTO JAVA DOCS FOR OVERRIDEN METHODS
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(findViewById(R.id.fragment_container).getId(), new MainFragment());
        transaction.commit();
    }

    //Helper method to change fragments with animation
    private void addFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(findViewById(R.id.fragment_container).getId(), fragment);
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Registering to event bus
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //unregistering to event bus
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBHelper.getInstance(this).closeDB();
    }


    //Event bus subscribed method
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        addFragment(new MainFragment());
        navigationView.getMenu().getItem(0).setChecked(true);
        Snackbar.make(findViewById(R.id.co_ordinator), event.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    //Event bus subscribed method
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRemoveEvent(RemoveEvent event) {
        Snackbar.make(findViewById(R.id.fragment_container), "Note has been deleted.", Snackbar.LENGTH_LONG).show();
    }

    //Event bus subscribed method
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTextEvent(FullText event) {
        Intent intent = new Intent(this, FullTextActivity.class);
        intent.putExtra("note_text", event.getText());
        startActivity(intent);
    }

    //Event bus subscribed method
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateText event) {
        addFragment(new TextFragment());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.home)
        {
            addFragment(new MainFragment());
        }
        else if(id == R.id.add)
        {
            addFragment(new TextFragment());
        }
        else if(id == R.id.nav_share)
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this

            intent.putExtra(Intent.EXTRA_SUBJECT, "Recommended app to use");
            intent.putExtra(Intent.EXTRA_TEXT, "Hi check this cool app out!");
            Log.e("here_share", "sharing");
            startActivity(intent);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
