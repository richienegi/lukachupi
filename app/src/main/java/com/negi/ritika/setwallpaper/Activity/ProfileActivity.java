package com.negi.ritika.setwallpaper.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.negi.ritika.setwallpaper.R;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView id_name;
    TextView no_posts;
    TextView no_pend_posts;
    ImageButton pending_btn, posts_btn;
    RecyclerView pending_rv, uploads_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();

        id_name = (TextView) findViewById(R.id.username);
        no_pend_posts = (TextView)findViewById(R.id.pending_img);
        no_posts = (TextView)findViewById(R.id.user_img);

        pending_rv = (RecyclerView)findViewById(R.id.pending_rview);
        uploads_rv = (RecyclerView)findViewById(R.id.uploads_rview);


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
}
