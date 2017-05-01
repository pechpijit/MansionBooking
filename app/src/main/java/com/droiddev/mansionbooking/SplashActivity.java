package com.droiddev.mansionbooking;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import mehdi.sakout.fancybuttons.FancyButton;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    LinearLayout layout;
    FancyButton btn_login, btn_register, signInButton;
    ProgressBar progressBar;
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 9001;
    String TAG = "SplashActivity";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        broadcast_reciever();

        layout = (LinearLayout) findViewById(R.id.btnView);
        btn_login = (FancyButton) findViewById(R.id.btn_login);
        btn_register = (FancyButton) findViewById(R.id.btn_register);
        progressBar = (ProgressBar) findViewById(R.id.proView);
        signInButton = (FancyButton) findViewById(R.id.sign_in_button);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        signInButton.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("Preferences_MansionBooking", Context.MODE_PRIVATE);
        boolean user = sp.getBoolean("login", false);
        String permission = sp.getString("permission", "user");

        if (user) {
            if (permission.equals("user")) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        } else {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            layout.setVisibility(View.VISIBLE);
                        }
                    }, 3000);
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(SplashActivity.this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void signIn() {
        signInButton.setEnabled(false);

        progressDialog = new ProgressDialog(SplashActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onSignupFailed() {
        try {
            progressDialog.cancel();
        } catch (Exception e) {

        }
        signInButton.setEnabled(true);
    }

    public void onSignupSuccess(String json,String url) {
        signInButton.setEnabled(true);
        new InsertUser().ConverUserDetail(SplashActivity.this,json,url);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            new ConnectAPI().SocialLogin(SplashActivity.this, acct.getId(), refreshedToken);
        } else {
            signInButton.setEnabled(true);
            Toast.makeText(SplashActivity.this, "handleSignInResult : "+result.isSuccess(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void broadcast_reciever() {
        BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.btn_login:
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, SignupActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
