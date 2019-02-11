package com.negi.ritika.setwallpaper.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.negi.ritika.setwallpaper.Adapters.PendingPostListAdapter;
import com.negi.ritika.setwallpaper.Adapters.PostListAdapter;
import com.negi.ritika.setwallpaper.ClickListener;
import com.negi.ritika.setwallpaper.Constants;
import com.negi.ritika.setwallpaper.Fragment.Image_Upload;
import com.negi.ritika.setwallpaper.Models.All_Images;
import com.negi.ritika.setwallpaper.Models.User_Images;
import com.negi.ritika.setwallpaper.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements ClickListener {

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

        posts = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        pending = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_REQUEST);

        id_name = (TextView) findViewById(R.id.username);
        no_pend_posts = (TextView) findViewById(R.id.pending_img);
        no_posts = (TextView) findViewById(R.id.user_img);
        pending_btn = (ImageButton) findViewById(R.id.pending_btn);
        posts_btn = (ImageButton) findViewById(R.id.post_btn);
        pending_rv = (RecyclerView) findViewById(R.id.pending_rview);
        uploads_rv = (RecyclerView) findViewById(R.id.uploads_rview);

        post_images = new ArrayList<>();
        requested_images = new ArrayList<>();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds2 : ds1.getChildren()) {
//                        Log.d("CHECKING", ds2.getKey());
                        if (ds2.getKey().equals(auth.getCurrentUser().getUid())) {
                            for (DataSnapshot ds3 : ds2.getChildren()) {
//                                Log.d("CHECKING", ds3.getKey());
                                User_Images um = ds3.getValue(User_Images.class);
                                requested_images.add(um);
                            }
                        }
                    }
                }
                PendingPostListAdapter ad = new PendingPostListAdapter(ProfileActivity.this, requested_images);
                ad.setOnClick(ProfileActivity.this);
                pending_rv.setAdapter(ad);
                no_pend_posts.setText(String.valueOf(requested_images.size()));
//                Toast.makeText(ProfileActivity.this, ""+requested_images.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post_images.clear();
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds2 : ds1.getChildren()) {
//                        Log.d("CHECKING", ds2.getKey());
                        if (ds2.getKey().equals(auth.getCurrentUser().getUid())) {
                            for (DataSnapshot ds3 : ds2.getChildren()) {
//                                Log.d("CHECKING", ds3.getKey());
                                All_Images um = ds3.getValue(All_Images.class);
                                post_images.add(um);
                            }
                        }
                    }
                }
                PostListAdapter ad = new PostListAdapter(ProfileActivity.this, post_images);
                ad.setOnClick(ProfileActivity.this);
                uploads_rv.setAdapter(ad);
                no_posts.setText(String.valueOf(post_images.size()));
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
        Toast.makeText(this, "hello222", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
