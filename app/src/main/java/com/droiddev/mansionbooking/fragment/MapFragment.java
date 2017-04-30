package com.droiddev.mansionbooking.fragment;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;


import com.droiddev.mansionbooking.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private static final LatLng b1 = new LatLng(13.6594121, 100.599031);
    private static final LatLng b2 = new LatLng(13.6594121, 100.599031);
    private static final LatLng b3 = new LatLng(13.6728759, 100.6051552);
    private static final LatLng b4 = new LatLng(13.6821736, 100.6067373);
    private static final LatLng b5 = new LatLng(13.691347, 100.6025014);
    private static final LatLng b6 = new LatLng(13.7068666, 100.593213);
    private static final LatLng b7 = new LatLng(13.7117256, 100.5888495);
    private static final LatLng b8 = new LatLng(13.7154937, 100.5820363);

    private static LatLng b9 = new LatLng(13.7154937, 100.5820363);

    private Marker mPerth;
    private Marker mSydney;
    private Marker mBrisbane;
    SupportMapFragment fragment;
    LatLng loc;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
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

        // Add some markers to the map, and add a data object to each marker.
        mPerth = mMap.addMarker(new MarkerOptions()
                .position(b1)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("สถานี BTS"));
        mPerth.setTag(0);

        mPerth = mMap.addMarker(new MarkerOptions()
                .position(b2)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("สถานี BTS"));
        mPerth.setTag(1);

        mPerth = mMap.addMarker(new MarkerOptions()
                .position(b3)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("สถานี BTS"));
        mPerth.setTag(2);

        mPerth = mMap.addMarker(new MarkerOptions()
                .position(b4)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("สถานี BTS"));
        mPerth.setTag(3);

        mPerth = mMap.addMarker(new MarkerOptions()
                .position(b5)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("สถานี BTS"));
        mPerth.setTag(4);

        mPerth = mMap.addMarker(new MarkerOptions()
                .position(b6)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("สถานี BTS"));
        mPerth.setTag(5);

        mSydney = mMap.addMarker(new MarkerOptions()
                .position(b7)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("สถานี BTS"));
        mSydney.setTag(6);

        mBrisbane = mMap.addMarker(new MarkerOptions()
                .position(b8)
//                .ic_launcher(BitmapDescriptorFactory.fromResource(R.drawable.cpu2))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("สถานี BTS"));
        mBrisbane.setTag(7);


        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        googleMap.setBuildingsEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(13.6594121, 100.599031)).zoom(11).build(); // เอาไว้ Fix Location
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));//แบบมี Animation
//        googleMap.getUiSettings().setScrollGesturesEnabled(false);
//        ปิดไม่ให้เลื่อน map
//        googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE);// แบบจริง
//        googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN);//แบบภาพวาด
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    /**
     * Called when the user clicks a marker.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(getActivity(),
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

}