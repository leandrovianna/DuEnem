package com.alpha2.duenem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.Discipline;
import com.alpha2.duenem.signin.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected static final String SELECTED_ITEM_ID_EXTRA = "com.alpha2.duenem.selected_item_id";

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    protected FirebaseAuth mAuth;
    private ProfilePhotoTarget mProfilePhotoHandler;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

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

        int selectedId = getIntent().getIntExtra(SELECTED_ITEM_ID_EXTRA, 0);
        if (selectedId != 0)
            mNavigationView.setCheckedItem(selectedId);

        mAuth = FirebaseAuth.getInstance();

        Query disciplinesQuery = DBHelper.getDisciplines();

        disciplinesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SubMenu disciplinesMenu =  mNavigationView.getMenu().findItem(R.id.materialestudo)
                        .getSubMenu();

                for (DataSnapshot disciplineSnap : dataSnapshot.getChildren()) {
                    Discipline d = disciplineSnap.getValue(Discipline.class);
                    if (d != null) {
                        d.setUid(disciplineSnap.getKey());
                        final Intent intent = IntentAbstractFactory
                                .createHomeActivityIntent(BaseActivity.this, d);

                        disciplinesMenu.add(Menu.NONE, Menu.NONE, Menu.NONE, d.getName())
                                .setIcon(R.drawable.ic_description_black_24dp)
                                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        startActivity(intent);
                                        return false;
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateDrawerHeaderUI(firebaseAuth.getCurrentUser());
            }
        };

        mAuth.addAuthStateListener(mAuthStateListener);

        updateDrawerHeaderUI(mAuth.getCurrentUser());
    }

    @Override
    protected void onPause() {
        super.onPause();

        mAuth.removeAuthStateListener(mAuthStateListener);
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

        int selectedId = getIntent().getIntExtra(SELECTED_ITEM_ID_EXTRA, 0);

        Intent intent = null;

        if (id == R.id.perfil && selectedId != R.id.perfil) {
            intent = IntentAbstractFactory.createSignInActivityIntent(this);
        }
        else  if (id == R.id.treinar && selectedId != R.id.treinar) {

        }
        else  if (id == R.id.ranking && selectedId != R.id.ranking) {
            intent = IntentAbstractFactory.createRankingActivityIntent(this);
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

