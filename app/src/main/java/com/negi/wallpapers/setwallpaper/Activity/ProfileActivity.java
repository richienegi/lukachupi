package com.negi.wallpapers.setwallpaper.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.negi.wallpapers.setwallpaper.Adapters.PendingPostListAdapter;
import com.negi.wallpapers.setwallpaper.Adapters.PostListAdapter;
import com.negi.wallpapers.setwallpaper.ClickListener;
import com.negi.wallpapers.setwallpaper.Constants;
import com.negi.wallpapers.setwallpaper.Fragment.Image_Upload;
import com.negi.wallpapers.setwallpaper.Fragment.ShowImage;
import com.negi.wallpapers.setwallpaper.Models.All_Images;
import com.negi.wallpapers.setwallpaper.Models.User_Images;
import com.negi.wallpapers.setwallpaper.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements ClickListener, PendingPostListAdapter.PendingClickListener {

    private AdView mAdView;
    private InterstitialAd inter2;

    TextView id_name;
    TextView no_posts;
    TextView no_pend_posts;
    ImageButton pending_btn, posts_btn;
    RecyclerView pending_rv, uploads_rv;

    FirebaseAuth auth;
    DatabaseReference posts, pending;

    List<All_Images> post_images;
    List<User_Images> requested_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();

        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });

        inter2 = new InterstitialAd(this);
        inter2.setAdUnitId(getString(R.string.inter_profile_2));
        inter2.loadAd(new AdRequest.Builder().build());

        inter2.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                inter2.show();
            }
        });

        posts = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS).child(auth.getCurrentUser().getUid());
        pending = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_REQUEST).child(auth.getCurrentUser().getUid());

        id_name = (TextView) findViewById(R.id.username);
        no_pend_posts = (TextView) findViewById(R.id.pending_img);
        no_posts = (TextView) findViewById(R.id.user_img);
        pending_btn = (ImageButton) findViewById(R.id.pending_btn);
        posts_btn = (ImageButton) findViewById(R.id.post_btn);
        pending_rv = (RecyclerView) findViewById(R.id.pending_rview);
        uploads_rv = (RecyclerView) findViewById(R.id.uploads_rview);

        post_images = new ArrayList<>();
        requested_images = new ArrayList<>();

        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 3);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        pending_rv.setLayoutManager(layoutManager);
        uploads_rv.setLayoutManager(layoutManager2);

        pending_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pending_rv.getVisibility() == View.VISIBLE) {
                    pending_rv.setVisibility(View.GONE);
                    pending_btn.setImageResource(R.drawable.ic_arrow_down);
                } else if (pending_rv.getVisibility() == View.GONE) {
                    pending_rv.setVisibility(View.VISIBLE);
                    pending_btn.setImageResource(R.drawable.ic_arrow_up);
                }
            }
        });

        posts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploads_rv.getVisibility() == View.VISIBLE) {
                    uploads_rv.setVisibility(View.GONE);
                    posts_btn.setImageResource(R.drawable.ic_arrow_down);
                } else if (uploads_rv.getVisibility() == View.GONE) {
                    uploads_rv.setVisibility(View.VISIBLE);
                    posts_btn.setImageResource(R.drawable.ic_arrow_up);
                }
            }
        });


        pending.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requested_images.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User_Images images = ds.getValue(User_Images.class);
                    requested_images.add(images);
                }
                no_pend_posts.setText(String.valueOf(requested_images.size()));
                PendingPostListAdapter ad = new PendingPostListAdapter(ProfileActivity.this, requested_images, getFragmentManager());
                ad.setListener(ProfileActivity.this);
                pending_rv.setAdapter(ad);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post_images.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    All_Images images = ds.getValue(All_Images.class);
                    post_images.add(images);
                }
                no_posts.setText(String.valueOf(post_images.size()));
                PostListAdapter ad = new PostListAdapter(ProfileActivity.this, post_images);
                ad.setOnClick(ProfileActivity.this);
                uploads_rv.setAdapter(ad);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RelativeLayout rl = findViewById(R.id.container);
        if (rl.getVisibility() == View.VISIBLE) {
            rl.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            id_name.setText(auth.getCurrentUser().getDisplayName());
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.upload_image:
                RelativeLayout rl = findViewById(R.id.container);
                rl.setVisibility(View.VISIBLE);
                Image_Upload up = new Image_Upload();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, up).addToBackStack(null).commit();
                break;
            case R.id.ch_profile:
                startActivity(new Intent(ProfileActivity.this, ProfileSettingsActivity.class));
                break;
            case R.id.signout:
                showLogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Do You Want to Sign Out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.signOut();
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        RelativeLayout rl = findViewById(R.id.container);
        rl.setVisibility(View.VISIBLE);

        ShowImage im = new ShowImage();
        Bundle b = new Bundle();
        b.putString("url", post_images.get(position).getThumb());
        im.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.container, im).addToBackStack(null).commit();
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Image");
        builder.setMessage("Do you want to Delete this image?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImage(position);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }

    @Override
    public void onPendingItemClick(View v, int position) {
        RelativeLayout rl = findViewById(R.id.container);
        rl.setVisibility(View.VISIBLE);

        ShowImage im = new ShowImage();
        Bundle b = new Bundle();
        b.putString("url", requested_images.get(position).getThumb());
        im.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.container, im).addToBackStack(null).commit();
    }

    @Override
    public void onPendingItemLongClick(View v, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Image");
        builder.setMessage("Do you want to Delete this image?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePendingImage(position);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }

    private void deleteImage(int position) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting...");
        pd.setCancelable(false);
        pd.show();
        final All_Images ui = post_images.get(position);
        final StorageReference rf2 = FirebaseStorage.getInstance().getReferenceFromUrl(ui.getThumb());
        StorageReference rf = FirebaseStorage.getInstance().getReferenceFromUrl(ui.getImageUrl());
        rf.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    rf2.delete();
                    posts.child(ui.getId()).removeValue();
                } else {
                    Toast.makeText(ProfileActivity.this, "Deletion Failed", Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }
        });
    }

    private void deletePendingImage(int position) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting...");
        pd.setCancelable(false);
        pd.show();
        final User_Images ui = requested_images.get(position);
        final StorageReference rf2 = FirebaseStorage.getInstance().getReferenceFromUrl(ui.getThumb());
        StorageReference rf = FirebaseStorage.getInstance().getReferenceFromUrl(ui.getImageUrl());
        rf.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    rf2.delete();
                    pending.child(ui.getId()).removeValue();
                } else {
                    Toast.makeText(ProfileActivity.this, "Deletion Failed", Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }
        });
    }
}
