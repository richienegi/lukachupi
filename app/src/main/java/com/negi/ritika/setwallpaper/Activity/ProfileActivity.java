package com.negi.ritika.setwallpaper.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.negi.ritika.setwallpaper.R;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView id_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();

        id_name = (TextView)findViewById(R.id.username);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null)
        {
            id_name.setText(auth.getCurrentUser().getDisplayName());
        }
        else
        {
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
        switch (id)
        {
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
