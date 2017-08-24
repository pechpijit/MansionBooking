package com.droiddev.mansionbooking.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddev.mansionbooking.ConnectAPI;
import com.droiddev.mansionbooking.R;
import com.droiddev.mansionbooking.adapter.AdapterRandomApartment;
import com.droiddev.mansionbooking.model.ModelPostHome;
import com.droiddev.mansionbooking.model.ModelPostImg;
import com.droiddev.mansionbooking.model.ModelPostRatePrice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class PostHomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "data";
    private static final String ARG_PARAM2 = "url";
    private String data;
    private String url;

    TextView headerView;
    // TODO: Rename and change types of parameters

    private AdapterRandomApartment adapter;

    public PostHomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PostHomeFragment newInstance(String data,String url) {
        PostHomeFragment fragment = new PostHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, data);
        args.putString(ARG_PARAM2, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = getArguments().getString(ARG_PARAM1);
            url = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<ModelPostHome>>() {}.getType();
        Collection<ModelPostHome> enums = gson.fromJson(data, collectionType);
         final ArrayList<ModelPostHome> posts = new ArrayList<ModelPostHome>(enums);

        SharedPreferences sp = getActivity().getSharedPreferences("Preferences_MansionBooking", Context.MODE_PRIVATE);

        adapter = new AdapterRandomApartment(getActivity(), posts,url,sp.getInt("id",0));
        recyclerView.setAdapter(adapter);

        adapter.SetOnItemClickListener(new AdapterRandomApartment.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int ID =posts.get(position).getId();
                Toast.makeText(getActivity(), ""+ID, Toast.LENGTH_SHORT).show();
            }
        });

        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ConnectAPI().apartmentRandom(getActivity(),"1");
            }
        });

        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }

}
