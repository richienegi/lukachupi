package com.negi.ritika.setwallpaper.Activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.negi.ritika.setwallpaper.R;

public class SignUpActivity extends AppCompatActivity {

    private AdView mAdView;
    private InterstitialAd inter1;

    EditText name, email, pass;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        name = (EditText)findViewById(R.id.input_name);
        email = (EditText)findViewById(R.id.input_email);
        pass = (EditText)findViewById(R.id.input_password);

        inter1 = new InterstitialAd(this);
        inter1.setAdUnitId(getString(R.string.inter_signup));
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

        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        findViewById(R.id.link_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {
        final String nm = name.getText().toString().trim();
        String em = email.getText().toString().trim();
        String ps = pass.getText().toString().trim();

        if(nm.isEmpty())
        {
            name.setError("Please enter your name");
            name.requestFocus();
            return;
        }

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

        if(ps.length()<8)
        {
            pass.setError("Please enter strong password");
            pass.requestFocus();
            return;
        }
        final ProgressDialog pd = new ProgressDialog(SignUpActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Signing Up... Wait...");
        pd.show();

        auth.createUserWithEmailAndPassword(em, ps).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pd.dismiss();
                if(task.isSuccessful())
                {
                    FirebaseUser user = auth.getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nm).build();
                    user.updateProfile(profileUpdates);

                    user.sendEmailVerification();

                    auth.signOut();

                    Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_LONG).show();

                    finish();
                }
                else if(task.getException() instanceof FirebaseAuthUserCollisionException)
                {
                    Toast.makeText(SignUpActivity.this, "This Email is already registered\n\nTry Login", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Oops... Registration Failed\n\nPlease Try Again", Toast.LENGTH_LONG).show();
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
    }
}
