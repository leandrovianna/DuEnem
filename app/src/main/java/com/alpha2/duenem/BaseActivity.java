package com.alpha2.duenem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.alpha2.duenem.signin.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = HomeActivity.class.getSimpleName();
    protected DrawerLayout mDrawer;
    protected ActionBarDrawerToggle mToggle;

    protected FirebaseAuth mAuth;
    protected ProfilePhotoTarget mProfilePhotoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateDrawerHeaderUI(mAuth.getCurrentUser());
    }

    @Override
    protected void onDestroy() {
        mDrawer.removeDrawerListener(mToggle);
        Picasso.with(this).cancelRequest(mProfilePhotoHandler);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.perfil:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.materialestudo:
                break;
            case R.id.treinar:
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateDrawerHeaderUI(FirebaseUser user) {
        View headerView = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        ImageView avatar = (ImageView) headerView.findViewById(R.id.avatar);
        mProfilePhotoHandler = new ProfilePhotoTarget(avatar);
        TextView username = (TextView) headerView.findViewById(R.id.username);
        TextView email = (TextView) headerView.findViewById(R.id.email);

        if (user != null) {
            Log.d(TAG, "User photo url: "+user.getPhotoUrl());

            Picasso.with(this).load(user.getPhotoUrl()).into(mProfilePhotoHandler);

            username.setText(user.getDisplayName());
            email.setText(user.getEmail());
        } else {
            avatar.setImageResource(R.mipmap.ic_launcher);
            username.setText(getString(R.string.app_name));
            email.setText("");
        }
    }

    private class ProfilePhotoTarget implements Target {
        private ImageView mView;

        ProfilePhotoTarget(ImageView avatarView) {
            this.mView = avatarView;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(BaseActivity.this.getResources(), bitmap);
            drawable.setCircular(true);
            mView.setImageDrawable(drawable);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            mView.setImageResource(R.mipmap.ic_launcher);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}
    }
}

