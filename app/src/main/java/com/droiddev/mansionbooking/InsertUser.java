package com.droiddev.mansionbooking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.droiddev.mansionbooking.model.ModelUser;
import com.google.gson.Gson;

public class InsertUser {

    public void ConverUserDetail(Activity context,String string,String url) {
        Gson gson = new Gson();
        ModelUser m = gson.fromJson(string, ModelUser.class);

        Intent intent = new Intent("finish_activity");
        context.sendBroadcast(intent);

        SharedPreferences sp = context.getSharedPreferences("Preferences_MansionBooking", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("login", true);
        editor.putString("permission", "user");
        editor.putString("url",url);
        editor.putInt("id", m.getId());
        editor.putString("username", m.getUsername());
        editor.putString("firstname", m.getFirstname());
        editor.putString("lastname", m.getLastname());
        editor.putString("fullname", m.getFirstname()+" "+m.getLastname());
        editor.putString("email", m.getEmail());
        editor.putString("phone", m.getPhone());
        editor.putString("token", m.getTokenFirebase());
        editor.commit();

        context.startActivity(new Intent(context, HomeActivity.class));
        context.finish();
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }
}
