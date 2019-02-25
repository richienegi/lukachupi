package com.negi.wallpapers.setwallpaper.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.negi.wallpapers.setwallpaper.Activity.ShowPostActivity;
import com.negi.wallpapers.setwallpaper.Adapters.PostListAdapter;
import com.negi.wallpapers.setwallpaper.ClickListener;
import com.negi.wallpapers.setwallpaper.Constants;
import com.negi.wallpapers.setwallpaper.Models.All_Images;
import com.negi.wallpapers.setwallpaper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditorsFragment extends Fragment implements ClickListener {

    private AdView mAdView;
    private InterstitialAd inter1;

    RecyclerView rv;
    ProgressBar pb;
    View v;

    DatabaseReference ref;

    List<All_Images> data;

    public EditorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_editors, container, false);
        rv = (RecyclerView)v.findViewById(R.id.rview);

        mAdView = (AdView) v.findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        AdRequest banner = new AdRequest.Builder().build();
        mAdView.loadAd(banner);

        inter1 = new InterstitialAd(getContext());
        inter1.setAdUnitId(getString(R.string.inter_editor_1));
        inter1.loadAd(new AdRequest.Builder().build());

        inter1.setAdListener(new AdListener()
        {
            public void onAdLoaded()
            {
                if (inter1.isLoaded()) {
                    inter1.show();
                }
            }
        });

        mAdView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });

        pb = (ProgressBar)v.findViewById(R.id.progress);
        pb.setVisibility(View.VISIBLE);
        ref = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS).child("Admin");
        data = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv.setLayoutManager(gridLayoutManager);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    All_Images im = ds.getValue(All_Images.class);
                    data.add(im);
                }
                if(pb.getVisibility()==View.VISIBLE)
                {
                    pb.setVisibility(View.GONE);
                }
                if(data.size()==0)
                {
                    LinearLayout li = v.findViewById(R.id.error);
                    li.setVisibility(View.VISIBLE);
                }
                else {
                    LinearLayout li = v.findViewById(R.id.error);
                    if(li.getVisibility()==View.VISIBLE)
                    {
                        li.setVisibility(View.GONE);
                    }
                }
                Collections.reverse(data);
                PostListAdapter ad = new PostListAdapter(getContext(), data);
                ad.setOnClick(EditorsFragment.this);
                rv.setAdapter(ad);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }

    @Override
    public void onItemClick(View view, int position) {

        All_Images images = data.get(position);
        Intent i = new Intent(getContext(), ShowPostActivity.class);

        i.putExtra("id", images.getId());
        i.putExtra("uid", images.getUid());
        i.putExtra("downloads", images.getDownloads());
        i.putExtra("image", images.getImageUrl());
        i.putExtra("date", images.getDate());
        i.putExtra("owner", images.getOwner());
        i.putExtra("thumb", images.getThumb());

        startActivity(i);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
