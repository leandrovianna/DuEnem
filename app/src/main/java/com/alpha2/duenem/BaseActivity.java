package com.alpha2.duenem;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.SharedPreferencesCompat;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alpha2.duenem.signin.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String SELECTED_ITEM_ID = "com.alpha2.duenem.selected_item_id";

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    protected FirebaseAuth mAuth;
    private ProfilePhotoTarget mProfilePhotoHandler;

    protected View setContentLayout(int contentLayoutResId) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout layout = (FrameLayout) findViewById(R.id.content_layout);
        return inflater.inflate(contentLayoutResId, layout, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        int selectedId = getIntent().getIntExtra(SELECTED_ITEM_ID, 0);
        if (selectedId != 0)
            mNavigationView.setCheckedItem(selectedId);

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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        int selectedId = getIntent().getIntExtra(SELECTED_ITEM_ID, 0);

        Intent intent = null;

        if (id == R.id.perfil && selectedId != R.id.perfil) {
            intent = new Intent(this, SignInActivity.class);
            intent.putExtra(SELECTED_ITEM_ID, R.id.perfil);
        }
        else  if (id == R.id.materialestudo && selectedId != R.id.materialestudo) {

        }
        else  if (id == R.id.treinar && selectedId != R.id.treinar) {

        }

        if (intent != null) {
            startActivity(intent);
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void updateDrawerHeaderUI(FirebaseUser user) {
        View headerView = mNavigationView.getHeaderView(0);
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

