package com.droiddev.mansionbooking.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.droiddev.mansionbooking.ConnectAPI;
import com.droiddev.mansionbooking.HomeActivity;
import com.droiddev.mansionbooking.R;
import com.droiddev.mansionbooking.model.ModelDetailMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private Marker mPerth;
    Context mContext;
    int ID;
    ArrayList<ModelDetailMap> post;
    String TAG = "MapFragment";

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mContext = getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final GoogleMap mMap = googleMap;
        getMapAll(getActivity(), mMap, ID);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        Integer id = (Integer) marker.getTag();

        Log.i(TAG, "id : " + id);
        for (int i = 0; i < post.size(); i++) {
            if (id == post.get(i).getId()) {
                dialogDetailMarker(getActivity(), "รายระเอียด", "ชื่อหอพัก :" + post.get(i).getName()+
                "\nที่อยู่ :"+post.get(i).getAddress()+
                "\nอีเมล์ :"+post.get(i).getEmail()+
                "\nเบอร์โทรศัพท์ :"+post.get(i).getPhone());
                break;
            }
        }


        return false;
    }

    public void addMarker(GoogleMap mMap, String string, String url) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<ModelDetailMap>>() {
        }.getType();
        Collection<ModelDetailMap> enums = gson.fromJson(string, collectionType);
        post = new ArrayList<ModelDetailMap>(enums);

        LatLng Position = null;
        int lastIndex = 0;
        for (ModelDetailMap contentModel : post) {
            mPerth = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(contentModel.getLatitude()), Double.parseDouble(contentModel.getLongitude())))
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .icon((BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(url + "/images_resize/build/" + contentModel.getImage())))));
//                    .title(contentModel.getName()));

            mPerth.setTag(contentModel.getId());
            if (lastIndex == 0) {
                Position = new LatLng(Double.parseDouble(contentModel.getLatitude()), Double.parseDouble(contentModel.getLongitude()));
                lastIndex++;
            }
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.setBuildingsEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(Position).zoom(11).build(); // เอาไว้ Fix Location
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));//แบบมี Animation
//        googleMap.getUiSettings().setScrollGesturesEnabled(false);
//        ปิดไม่ให้เลื่อน map
//        googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE);// แบบจริง
//        googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN);//แบบภาพวาด

//        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
//        mMap.setInfoWindowAdapter(new MapInfoWindowAdapter(this, markerSet));
    }

    private Bitmap getMarkerBitmapFromView(final String url) {

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.avatar_marker_image_view);

        Picasso.with(mContext).load(url).error(R.drawable.nopic).into(markerImageView);
        Picasso.with(mContext).load(url).error(R.drawable.nopic).into(markerImageView);
//        Glide.with(mContext).load(url).error(R.drawable.nopic).into(markerImageView);
//        Glide.with(mContext).load(url).error(R.drawable.nopic).into(markerImageView);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public void getMapAll(final FragmentActivity activity, final GoogleMap mMap, int ID) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(ConnectAPI.URL + "/api/apartment/getmap")
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
                    dialogErrorNoIntent(activity, string);
                } else if (string.equals("NotFound")) {
                    dialogError(activity, string);
                } else {
                    addMarker(mMap, string, ConnectAPI.URL);
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

    private static void dialogErrorNoIntent(final Activity context, String string) {
        new AlertDialog.Builder(context)
                .setTitle("The system temporarily")
                .setMessage("ไม่สามารถเข้าใช้งานได้ กรุณาลองใหม่ภายหลัง error code = " + string)
                .setNegativeButton("OK", null)
                .show();
    }

    private void dialogDetailMarker(final Activity context, String title, String detail) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(detail)
                .setNegativeButton("ดูข้อมูล", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("ปิด", null)
                .show();
    }

}