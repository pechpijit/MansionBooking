package com.droiddev.mansionbooking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddev.mansionbooking.fragment.AccountFragment;
import com.droiddev.mansionbooking.fragment.BlankFragment;
import com.droiddev.mansionbooking.fragment.PostHomeFragment;
import com.google.android.gms.maps.MapFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import mehdi.sakout.fancybuttons.FancyButton;

public class HomeActivity extends AppCompatActivity {
    Fragment selectFragment;
    Toolbar toolbar;
    FloatingActionButton fab;
    String ID = "";
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sp = getSharedPreferences("Preferences_MansionBooking", Context.MODE_PRIVATE);
        ID = String.valueOf(sp.getInt("id", 0));
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
                        new ConnectAPI().apartmentUsrapp(HomeActivity.this, ID, "5");
                        break;
                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
//                Toast.makeText(getApplicationContext(), TabMessage.get(tabId, true), Toast.LENGTH_LONG).show();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertActive();
            }
        });
    }

    public void onActiveFailed() {
        progressDialog.cancel();
    }
    public void onActiveSuccess() {
        progressDialog.cancel();
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Success")
                .setMessage("ดำเนินการเสร็จสิ้น กรุณารอการอนุมัติ")
                .setNegativeButton("OK", null)
                .show();
    }

    private void insertActive() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setTitle("Devahoy");
        dialog.setContentView(R.layout.dialog_insert_active);

        final EditText username = (EditText) dialog.findViewById(R.id.cod_active);
        FancyButton button = (FancyButton) dialog.findViewById(R.id.btn_active);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogActive(username.getText().toString());
            }
        });

        dialog.show();
    }

    private void dialogActive(String s) {
        progressDialog = new ProgressDialog(HomeActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Connected...");
        progressDialog.show();

        new ConnectAPI().ActiveCustomer(HomeActivity.this,ID,s);

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
                fab.hide();
                Log.d("HomeActivity", "setFram 1");
                selectFragment = new PostHomeFragment().newInstance(s,url);
                break;
            case "3":
                fab.hide();
                Log.d("HomeActivity", "setFram 3");
                selectFragment = new com.droiddev.mansionbooking.fragment.MapFragment();
                break;
            case "5":
                fab.show();
                selectFragment = new AccountFragment().newInstance(s,url);
                break;
        }

        ft.replace(R.id.content, selectFragment);
        ft.commit();
    }
}