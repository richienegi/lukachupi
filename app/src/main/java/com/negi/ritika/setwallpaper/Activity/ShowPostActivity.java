package com.negi.ritika.setwallpaper.Activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.negi.ritika.setwallpaper.Constants;
import com.negi.ritika.setwallpaper.Fragment.ImagePreview;
import com.negi.ritika.setwallpaper.GlideImageLoader;
import com.negi.ritika.setwallpaper.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShowPostActivity extends AppCompatActivity implements ImagePreview.OnFragmentInteractionListener {

    private AdView mAdView, mAdView2;
    private InterstitialAd inter1;

    public static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    ImageView image;
    TextView txt_owner, txt_downloads, txt_date;
    DatabaseReference ref;
    String uid, id, url, downloads, owner, date, thumb;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        url = getIntent().getStringExtra("image");
        id = getIntent().getStringExtra("id");
        downloads = getIntent().getStringExtra("downloads");
        uid = getIntent().getStringExtra("uid");
        owner = getIntent().getStringExtra("owner");
        date = getIntent().getStringExtra("date");
        thumb = getIntent().getStringExtra("thumb");

        mAdView = (AdView) findViewById(R.id.adView);
        mAdView2 = (AdView) findViewById(R.id.adView2);
        mAdView.setVisibility(View.GONE);
        mAdView2.setVisibility(View.GONE);

        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView2.loadAd(new AdRequest.Builder().build());

        mAdView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });

        mAdView2.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                mAdView2.setVisibility(View.VISIBLE);
            }
        });

        inter1 = new InterstitialAd(this);
        inter1.setAdUnitId(getString(R.string.inter_show_post));
        inter1.loadAd(new AdRequest.Builder().build());

        image = (ImageView) findViewById(R.id.mdetail);
        pb = (ProgressBar) findViewById(R.id.progress);
        txt_downloads = (TextView) findViewById(R.id.txt_downloads);
        txt_owner = (TextView) findViewById(R.id.txt_owner);
        txt_date = (TextView) findViewById(R.id.txt_date);

        txt_downloads.setText(downloads);
        txt_owner.setText(owner);
        txt_date.setText(showDate(date));

        ref = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS).child(uid).child(id);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .priority(Priority.HIGH);

        new GlideImageLoader(image, pb).load(thumb,options);

        findViewById(R.id.SaveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);
                    } else {
                        downloadImage();
                    }
                } else {
                    downloadImage();
                }
            }
        });

        findViewById(R.id.ShareBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.WallBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview(url);
            }
        });
    }

    public void downloadImage() {
        Toast.makeText(this, "Downloading Progress is in Notificaions", Toast.LENGTH_LONG).show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String imageName = timeStamp + ".PNG";

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(imageName);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES+"/SetWallPaper",imageName);
        request.setMimeType("*/*");
        downloadManager.enqueue(request);

        int d = Integer.parseInt(downloads);
        d++;

        downloads = String.valueOf(d);

        Map<String, Object> map = new HashMap<>();
        map.put("downloads", downloads);

        ref.updateChildren(map);

        finish();

    }

    private String showDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        Date resultdate = new Date(Long.valueOf(date));
        return String.valueOf(sdf.format(resultdate));
    }

    public void preview(String url) {
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        ImagePreview p = new ImagePreview();

        Bundle b = new Bundle();
        b.putString("passimage", url);

        p.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, p).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        inter1.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            downloadImage();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
