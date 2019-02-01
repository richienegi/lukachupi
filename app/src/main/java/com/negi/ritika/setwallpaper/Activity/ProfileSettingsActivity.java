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
import com.google.firebase.auth.AuthResult;
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

        final AlertDialog ad = builder.create();
        ad.show();

        final EditText ed_n_pass = (EditText)v.findViewById(R.id.ch_pass);
        final EditText ed_email = (EditText)v.findViewById(R.id.email);
        final EditText ed_o_pass = (EditText)v.findViewById(R.id.old_pass);
        Button sub = (Button)v.findViewById(R.id.submit);

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newpass = ed_n_pass.getText().toString();
                String email = ed_email.getText().toString();
                String oldpass = ed_o_pass.getText().toString();
//scf 28/5 18 d pushpan traders
                if(email.isEmpty())
                {
                    ed_email.setError("Enter Your Email");
                    ed_email.requestFocus();
                    return;
                }
                if(oldpass.isEmpty())
                {
                    ed_o_pass.setError("Enter Your Current Password");
                    ed_o_pass.requestFocus();
                    return;
                }
                if(newpass.isEmpty())
                {
                    ed_n_pass.setError("Enter New Password");
                    ed_n_pass.requestFocus();
                    return;
                }
                if(!email.equals(auth.getCurrentUser().getEmail()))
                {
                    ed_email.setError("Email id is wrong");
                    ed_email.requestFocus();
                    return;
                }

                final ProgressDialog pd = new ProgressDialog(ProfileSettingsActivity.this);
                pd.setCancelable(false);
                pd.setMessage("Changing Password... Wait...");
                pd.show();

                auth.signInWithEmailAndPassword(email, oldpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = auth.getCurrentUser();

                            user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        auth.signOut();
                                        finish();
                                        Toast.makeText(ProfileSettingsActivity.this, "Password Changed Successfully\n\nLogin Again", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(ProfileSettingsActivity.this, "Failed"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                    pd.dismiss();
                                    ad.dismiss();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(ProfileSettingsActivity.this, "You Entered Wrong Password", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }
}
