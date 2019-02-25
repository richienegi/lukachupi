package com.negi.wallpapers.setwallpaper.Fragment;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.negi.wallpapers.setwallpaper.GlideImageLoader;
import com.negi.wallpapers.setwallpaper.R;


public class ImagePreview extends Fragment {
    private AdView mAdView;

    ImageView img;
    Button setimg;
    ProgressBar pb;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ImagePreview() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        img = view.findViewById(R.id.preimg);
        pb = view.findViewById(R.id.progress);

        mAdView = (AdView) view.findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });


        String imgi = getArguments().getString("passimage");

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .priority(Priority.HIGH);

        new GlideImageLoader(img, pb).load(imgi,options);

        setimg = (Button) view.findViewById(R.id.set);
        setimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Drawable mDrawable = img.getDrawable();
                    final Bitmap mbitmap = ((BitmapDrawable) mDrawable).getBitmap();

                    WallpaperManager myWallpaperManager = WallpaperManager
                            .getInstance(getContext());
                    myWallpaperManager.setBitmap(mbitmap);
                    Toast.makeText(getContext(), "Done ! Set as Wallpaper", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Sorry ! Something Went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
