package com.alpha2.duenem.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alpha2.duenem.BaseActivity;
import com.alpha2.duenem.R;
import com.alpha2.duenem.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 10001;
    private static final String TAG = SignInActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_sign_in, null, false);
        mDrawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_play_console_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.googleSignInBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInButtonClicked();
            }
        });

        findViewById(R.id.signoutBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutButtonClicked();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        hideProgressBar();
        if (currentUser == null) {
            findViewById(R.id.subtext).setVisibility(View.VISIBLE);
            findViewById(R.id.googleSignInBt).setVisibility(View.VISIBLE);
            findViewById(R.id.signoutBt).setVisibility(View.GONE);
        } else {
            findViewById(R.id.subtext).setVisibility(View.GONE);
            findViewById(R.id.googleSignInBt).setVisibility(View.GONE);
            findViewById(R.id.signoutBt).setVisibility(View.VISIBLE);

            Toast.makeText(this, "User: "+currentUser.getDisplayName(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void googleSignInButtonClicked() {
        showProgressBar();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOutButtonClicked() {
        showProgressBar();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    updateUI(null);
                }
            });
            mAuth.signOut();
        }
    }

    private void showProgressBar() {
        findViewById(R.id.googleSignInBt).setVisibility(View.GONE);
        findViewById(R.id.signoutBt).setVisibility(View.GONE);
        ProgressBar progressBar = (ProgressBar)
                findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        ProgressBar progressBar = (ProgressBar)
                findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            createUserDb(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, getString(R.string.message_sign_in_error),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void createUserDb(FirebaseUser firebaseUser) {
        User user = new User(firebaseUser);

        mDatabase.child("user").child(firebaseUser.getUid()).setValue(user);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, getString(R.string.message_play_services_error), Toast.LENGTH_SHORT).show();
    }
}
