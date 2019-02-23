package com.negi.ritika.setwallpaper.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.negi.ritika.setwallpaper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowImage extends Fragment {

    private InterstitialAd inter1;

    public ShowImage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.image, container, false);
        ImageView iv = (ImageView)v.findViewById(R.id.imageView);

        inter1 = new InterstitialAd(getActivity());
        inter1.setAdUnitId(getString(R.string.inter_show_image));
        inter1.loadAd(new AdRequest.Builder().build());

        inter1.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                if(inter1.isLoaded())
                {
                    inter1.show();
                }
            }
        });

        String url = getArguments().getString("url");
        Glide.with(this)
                .load(url)
                .into(iv);

        return v;
    }

}