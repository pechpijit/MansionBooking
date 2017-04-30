package com.droiddev.mansionbooking;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

import mehdi.sakout.fancybuttons.FancyButton;

public class SignupSocialActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText txtFirstname;
    EditText txtLastname;
    EditText txtUsername;
    EditText txtPhone;
    EditText _emailText;
    EditText _passwordText;
    FancyButton btn_register;
    ProgressDialog progressDialog;
    String social = "";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        Bundle i = getIntent().getExtras();
        social = i.getString("social", "");

        txtFirstname = (EditText) findViewById(R.id.input_firstname);
        txtLastname = (EditText) findViewById(R.id.input_lastname);
        txtUsername = (EditText) findViewById(R.id.input_username);
        txtPhone = (EditText) findViewById(R.id.input_phone);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        btn_register = (FancyButton) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btn_register.setEnabled(false);

        progressDialog = new ProgressDialog(SignupSocialActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstname = txtFirstname.getText().toString().trim();
        String lastname = txtLastname.getText().toString().trim();
        String username = txtUsername.getText().toString().toLowerCase().trim();
        String phone = txtPhone.getText().toString().trim();
        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        new ConnectAPI().SocialRegister(
                SignupSocialActivity.this,
                firstname,
                lastname,
                email,
                phone,
                username,
                password,
                refreshedToken,
                social
        );

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        onSignupSuccess();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    public void onSignupSuccess(String json,String url) {
        btn_register.setEnabled(true);
        setResult(RESULT_OK, null);
        new InsertUser().ConverUserDetail(SignupSocialActivity.this,json,url);
    }

    public void onSignupFailed() {
        try {
            progressDialog.cancel();
        } catch (Exception e) {

        }
        btn_register.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstname = txtFirstname.getText().toString();
        String lastname = txtLastname.getText().toString();
        String username = txtUsername.getText().toString();
        String phone = txtPhone.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (phone.isEmpty()) {
            txtPhone.setError("not empty");
            valid = false;
        } else {
            txtPhone.setError(null);
        }

        if (lastname.isEmpty()) {
            txtLastname.setError("not empty");
            valid = false;
        } else {
            txtLastname.setError(null);
        }

        if (firstname.isEmpty()) {
            txtFirstname.setError("not empty");
            valid = false;
        } else {
            txtFirstname.setError(null);
        }

        if (username.isEmpty() || username.length() < 6) {
            txtUsername.setError("at least 6 characters");
            valid = false;
        } else {
            txtUsername.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}