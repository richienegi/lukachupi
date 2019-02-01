package com.negi.ritika.setwallpaper.Activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.negi.ritika.setwallpaper.R;

public class ProfileSettingsActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        auth = FirebaseAuth.getInstance();

    }

    public void changeName(View view) {
        LayoutInflater li = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.alert_changename, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);

        final AlertDialog ad = builder.create();
        ad.show();

        final EditText ed_name = (EditText)v.findViewById(R.id.ch_name);
        Button sub = (Button)v.findViewById(R.id.submit);

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed_name.getText().toString().trim();
                if(name.isEmpty())
                {
                    ed_name.setError("Field must be Filled");
                    ed_name.requestFocus();
                    return;
                }

                final ProgressDialog pd = new ProgressDialog(ProfileSettingsActivity.this);
                pd.setCancelable(false);
                pd.setMessage("Changing Name... Wait...");
                pd.show();

                FirebaseUser user = auth.getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name).build();

                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProfileSettingsActivity.this, "Name Changed Successfully", Toast.LENGTH_SHORT).show();
                            ad.dismiss();
                        }
                        pd.dismiss();
                    }
                });
            }
        });
    }

    public void changePass(View view) {
        LayoutInflater li = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.alert_changepassword, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);

        AlertDialog ad = builder.create();
        ad.show();

        final EditText ed_pass = (EditText)v.findViewById(R.id.ch_pass);
        Button sub = (Button)v.findViewById(R.id.submit);

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = ed_pass.getText().toString();
            }
        });
    }
}
