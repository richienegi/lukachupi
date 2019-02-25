package com.negi.wallpapers.setwallpaper.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.negi.wallpapers.setwallpaper.R;

public class LoginActivity extends AppCompatActivity {

    private AdView mAdView;
    private InterstitialAd inter1;

    EditText email, pass;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null)
        {
            if(auth.getCurrentUser().isEmailVerified())
            {
                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                finish();
            }
            else
            {
                auth.signOut();
            }
        }

        setContentView(R.layout.activity_login);

        inter1 = new InterstitialAd(this);
        inter1.setAdUnitId(getString(R.string.inter_login));
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

        mAdView = (AdView) findViewById(R.id.adView);
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

        email = (EditText)findViewById(R.id.input_email);
        pass = (EditText)findViewById(R.id.input_password);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        findViewById(R.id.link_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        findViewById(R.id.link_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPasswordDialog();
            }
        });
    }

    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.passwordreset, null);

        builder.setTitle("Password Reset");
        builder.setMessage("Send Password Reset Link to Email");
        builder.setView(v);

        final AlertDialog ad = builder.create();
        ad.show();

        final EditText em = v.findViewById(R.id.email);
        v.findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(em.getText().toString().trim());
                Toast.makeText(LoginActivity.this, "Password Reset Link Send Successfull", Toast.LENGTH_SHORT).show();
                ad.dismiss();
            }
        });
    }

    public void loginUser() {
        String em = email.getText().toString().trim();
        String ps = pass.getText().toString().trim();

        if(em.isEmpty())
        {
            email.setError("Please enter your email id");
            email.requestFocus();
            return;
        }

        if(ps.isEmpty())
        {
            pass.setError("Please enter password");
            pass.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
        {
            email.setError("Please enter your valid email id");
            email.requestFocus();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Signing Up...");
        pd.setCancelable(false);
        pd.show();

        auth.signInWithEmailAndPassword(em, ps).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pd.cancel();
                if(task.isSuccessful())
                {
                    if(auth.getCurrentUser().isEmailVerified())
                    {
                        //send to profile activity
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        finish();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Send Mail for Verification");
                        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                auth.getCurrentUser().sendEmailVerification();
                                dialog.dismiss();
                                auth.signOut();
                            }
                        });
                        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                auth.signOut();
                            }
                        });
//                        Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                        builder.setTitle("Email is not Verified");

                        AlertDialog ad = builder.create();
                        ad.show();
                        ad.setCancelable(false);
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Login Failed\n\nPlease Try Again", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.cancel();
                Toast.makeText(LoginActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
