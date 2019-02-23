package com.negi.ritika.setwallpaper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.negi.ritika.setwallpaper.Activity.ShowPostActivity;
import com.negi.ritika.setwallpaper.Adapters.PostListAdapter;
import com.negi.ritika.setwallpaper.ClickListener;
import com.negi.ritika.setwallpaper.Constants;
import com.negi.ritika.setwallpaper.Models.All_Images;
import com.negi.ritika.setwallpaper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewImagesFragment extends Fragment implements ClickListener {

    private AdView mAdView;
    private InterstitialAd inter1;

    RecyclerView mRecylerview;
    ProgressBar pb;
    View v;

    DatabaseReference mFirebaseDatabase;

    private GridLayoutManager layoutManager;

    private List<All_Images> data;

    public NewImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user, container, false);
        mRecylerview = (RecyclerView)v.findViewById(R.id.rview);
        mAdView = (AdView) v.findViewById(R.id.adView);

        mAdView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        inter1 = new InterstitialAd(getContext());
        inter1.setAdUnitId(getString(R.string.inter_user_1));
        inter1.loadAd(new AdRequest.Builder().build());


        inter1.setAdListener(new AdListener()
        {
            public void onAdLoaded()
            {
                if (inter1.isLoaded()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            inter1.show();
                        }
                    }, 60000);
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

        data = new ArrayList<>();
        layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecylerview.setLayoutManager(layoutManager);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if(ds.getKey().equals("Admin"))
                    {
                        continue;
                    }
                    for(DataSnapshot ds2 : ds.getChildren())
                    {
                        All_Images im = ds2.getValue(All_Images.class);
                        data.add(im);
                    }
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
                ad.setOnClick(NewImagesFragment.this);
                mRecylerview.setAdapter(ad);

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
