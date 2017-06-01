package com.droiddev.mansionbooking;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.droiddev.mansionbooking.model.ModelPostHome;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectAPI {
    String URL = "http://192.168.1.50/MansionBooking/public";
//    String URL = "http://aof.commsk.com";

    public void Register(
            final Activity context,
            final String firstname,
            final String lastname,
            final String email,
            final String phone,
            final String username,
            final String password,
            final String token
    ) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "firstname=" + firstname +
                                "&lastname=" + lastname +
                                "&email=" + email +
                                "&phone=" + phone +
                                "&username=" + username +
                                "&password=" + password+
                                "&token=" + token
                );
                Request request = new Request.Builder()
                        .url(URL+"/api/register")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Log.d("ConnectAPI : ", "Register " + string);
                String[] temp = string.split(" ");
                if (temp[0].equals("Error") || temp[0].equals("Not")) {
                    dialogErrorNoIntent(context, string);
                    ((SignupActivity) context).onSignupFailed();
                } else if (string.equals("[]")) {
                    dialogNotfound(context);
                    ((SignupActivity) context).onSignupFailed();
                } else if (string.equals("NotFound")) {
                    dialogError(context, string);
                    ((SignupActivity) context).onSignupFailed();
                } else{
                    ((SignupActivity) context).onSignupSuccess(string,URL);
                }
            }
        }.execute();
    }

    public void Login(final Activity context,final String username, final String password,final String token) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "username=" + username + "&password=" + password+ "&token=" + token);
                Request request = new Request.Builder()
                        .url(URL+"/api/login")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Log.d("ConnectAPI : ", "Login " + string);
                String[] temp = string.split(" ");
                if (temp[0].equals("Error") || temp[0].equals("Not")) {
                    dialogErrorNoIntent(context, string);
                    ((LoginActivity) context).onSignupFailed();
                } else if (string.equals("[]")) {
                    dialogNotfound(context);
                    ((LoginActivity) context).onSignupFailed();
                } else if (string.equals("NotFound")) {
                    dialogError(context, string);
                    ((LoginActivity) context).onSignupFailed();
                } else {
                    ((LoginActivity) context).onLoginSuccess(string,URL);
                }
            }
        }.execute();
    }

    public void SocialLogin(final Activity context,final String social,final String token) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "social=" + social + "&token=" + token);
                Request request = new Request.Builder()
                        .url(URL+"/api/social/login")
                        .post(body)
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Log.d("ConnectAPI : ", "SocialLogin " + string);
                String[] temp = string.split(" ");
                if (temp[0].equals("Error") || temp[0].equals("Not")) {
                    dialogErrorNoIntent(context, string);
                    ((SplashActivity) context).onSignupFailed();
                } else if (string.equals("[]")) {
                    dialogNotfound(context);
                    ((SplashActivity) context).onSignupFailed();
                } else if (string.equals("NotFound")) {
                    context.startActivity(new Intent(context,SignupSocialActivity.class).putExtra("social",social));
                    context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    ((SplashActivity) context).onSignupSuccess(string,URL);
                }
            }
        }.execute();
    }

    public void SocialRegister(
            final Activity context,
            final String firstname,
            final String lastname,
            final String email,
            final String phone,
            final String username,
            final String password,
            final String token,
            final String social
    ) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "firstname=" + firstname +
                                "&lastname=" + lastname +
                                "&email=" + email +
                                "&phone=" + phone +
                                "&username=" + username +
                                "&password=" + password+
                                "&token=" + token+
                                "&social=" + social
                );
                Request request = new Request.Builder()
                        .url(URL+"/api/social/register")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Log.d("ConnectAPI : ", "SocialRegister " + string);
                String[] temp = string.split(" ");
                if (temp[0].equals("Error") || temp[0].equals("Not")) {
                    dialogErrorNoIntent(context, string);
                    ((SignupSocialActivity) context).onSignupFailed();
                } else if (string.equals("[]")) {
                    dialogNotfound(context);
                    ((SignupSocialActivity) context).onSignupFailed();
                } else if (string.equals("NotFound")) {
                    dialogError(context, string);
                    ((SignupSocialActivity) context).onSignupFailed();
                } else{
                    ((SignupSocialActivity) context).onSignupSuccess(string,URL);
                }
            }
        }.execute();
    }

    public void apartmentRandom(final Activity context, final String page) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(URL+"/api/apartment/random")
                        .get()
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "a8bf0dca-f75d-ba16-329e-54be1d474ee0")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Log.d("ConnectAPI : ", "apartmentRandom " + string);
                String[] temp = string.split(" ");
                if (temp[0].equals("Error") || temp[0].equals("Not")) {
                    dialogErrorNoIntent(context, string);
                } else {
                    ((HomeActivity) context).setFram(string,page);
                }
            }
        }.execute();
    }

    public void apartmentUsrapp(final Activity context, final String ID , final String page) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(URL+"/api/usr/apartment/"+ID)
                        .get()
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Log.d("ConnectAPI : ", "apartmentRandom " + string);
                String[] temp = string.split(" ");
                if (temp[0].equals("Error") || temp[0].equals("Not")) {
                    dialogErrorNoIntent(context, string);
                } else if (string.equals("NotFound")) {
                    dialogError(context, string);
                    NoApart(context);
                }  else {
                    ((HomeActivity) context).setFram(string,page);
                }
            }
        }.execute();
    }

    public void ActiveCustomer(final Activity context,final String ID,final String Code) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "usrapp_id="+ID+"&CodeContext="+Code);
                Request request = new Request.Builder()
                        .url(URL+"/api/activecustomer")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Log.d("ConnectAPI : ", "SocialLogin " + string);
                String[] temp = string.split(" ");
                if (temp[0].equals("Error") || temp[0].equals("Not")) {
                    dialogErrorNoIntent(context, string);
                    ((HomeActivity) context).onActiveFailed();
                }  else if (string.equals("Activealready")) {
                    ActiveError3(context);
                    ((HomeActivity) context).onActiveFailed();
                }  else if (string.equals("NotFound")) {
                    ActiveError2(context);
                    ((HomeActivity) context).onActiveFailed();
                }  else if (string.equals("already")) {
                    ActiveError1(context);
                    ((HomeActivity) context).onActiveFailed();
                } else {
                    ((HomeActivity) context).onActiveSuccess();
                }
            }
        }.execute();
    }

    private static void dialogError(final Activity context, String string) {
        new AlertDialog.Builder(context)
                .setTitle("Failed")
                .setMessage("ไม่พบข้อมูล กรุณาลองใหม่ภายหลัง error code = " + string)
                .setNegativeButton("OK", null)
                .show();
    }

    private static void NoApart(final Activity context) {
        new AlertDialog.Builder(context)
                .setTitle("Failed")
                .setMessage("ขออภัย ท่านยังไม่มีหอพัก หรือกรุณาลองใหม่อีกครั้ง")
                .setNegativeButton("OK", null)
                .show();
    }

    private static void ActiveError1(final Activity context) {
        new AlertDialog.Builder(context)
                .setTitle("Failed")
                .setMessage("ขออภัย ท่านไม่สามารถส่งซ้ำได้")
                .setNegativeButton("OK", null)
                .show();
    }

    private static void ActiveError2(final Activity context) {
        new AlertDialog.Builder(context)
                .setTitle("Failed")
                .setMessage("ขออภัย รหัสยืนยันไม่ถูกต้องกรุณาลองใหม่อีกครั้ง")
                .setNegativeButton("OK", null)
                .show();
    }

    private static void ActiveError3(final Activity context) {
        new AlertDialog.Builder(context)
                .setTitle("Failed")
                .setMessage("ขออภัย รหัสยืนยันถูกใช้งานแล้ว")
                .setNegativeButton("OK", null)
                .show();
    }


    private static void dialogErrorNoIntent(final Activity context, String string) {
        new AlertDialog.Builder(context)
                .setTitle("The system temporarily")
                .setMessage("ไม่สามารถเข้าใช้งานได้ กรุณาลองใหม่ภายหลัง error code = " + string)
                .setNegativeButton("OK", null)
                .show();
    }

    private static void dialogNotfound(final Activity context) {
        new AlertDialog.Builder(context)
                .setTitle("Not Found")
                .setMessage("ไม่พบข้อมูล")
                .setNegativeButton("OK", null)
                .show();
    }
}
