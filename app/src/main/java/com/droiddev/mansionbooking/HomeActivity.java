package com.droiddev.mansionbooking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddev.mansionbooking.fragment.BlankFragment;
import com.droiddev.mansionbooking.fragment.PostHomeFragment;
import com.google.android.gms.maps.MapFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class HomeActivity extends AppCompatActivity {
    Fragment selectFragment;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.menu1));
                        new ConnectAPI().apartmentRandom(HomeActivity.this,"1");
                        break;
                    case R.id.tab_search:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.menu2));
                        break;
                    case R.id.tab_map:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.menu3));
                       setFram("","3");
                        break;
                    case R.id.tab_top:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.menu4));
                        break;
                    case R.id.tab_user:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.menu5));
                        break;
                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                Toast.makeText(getApplicationContext(), TabMessage.get(tabId, true), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setFram(String s,String page) {
        Log.d("HomeActivity", "setFram");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        selectFragment = new BlankFragment();

        SharedPreferences sp = getSharedPreferences("Preferences_MansionBooking", Context.MODE_PRIVATE);
        String url = sp.getString("url", "");


        switch (page) {
            case "1":
                Log.d("HomeActivity", "setFram 1");
                selectFragment = new PostHomeFragment().newInstance(s,url);
                break;
            case "3":
                Log.d("HomeActivity", "setFram 3");
                selectFragment = new com.droiddev.mansionbooking.fragment.MapFragment();
                break;
        }

        ft.replace(R.id.content, selectFragment);
        ft.commit();
    }
}