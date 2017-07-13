package com.devs.cnd.marvelousv.acts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;

public class ActMain extends AppCompatActivity
                        implements NavigationView.OnNavigationItemSelectedListener{

    private int SPLASH_TIME = 3000;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";

    //Var Layouts
    private CoordinatorLayout  mCoordinator;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        mAuth = FirebaseAuth.getInstance();

        iniGoogle();
        iniCoordinator();
        iniToolBar();
        iniNavDrawer();
        iniFab();

        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        mAppBarLayout.setExpanded(false,false);
    }

    /******* AUTH CHECKING     ***********/
    private void iniGoogle(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
                        // be available.
                        Log.d(TAG, "onConnectionFailed:" + connectionResult);
                        // Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();

        if (user == null) {
            Log.d(TAG,"Sign IN Fail");
            goToLaunch();

        } else {
            Log.d(TAG,"Sign IN Good");
            TextView textHello = (TextView)findViewById(R.id.textHello);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String LoginProvider =prefs.getString("LoginProvider",null);

            textHello.setText(LoginProvider+"\n"+ user.getDisplayName()+"\n"+user.getEmail()+"\n"+user.getProviderId()+"\n"+
            user.getProviderData());

        }
    }
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }
    /******* LAYOUT INICIALIZATION  ******/
    public  void iniCoordinator(){
        //Grupo de Coordinacion para la actividad Base
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.app_bar_layout);
    }
    public void iniToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
    }
    public void iniNavDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorWhite));
        View view = navigationView.getHeaderView(0);
        ImageView imgNav=(ImageView)view.findViewById(R.id.imgNavHeader);
        TextView text1=(TextView)view.findViewById(R.id.textNavHeader1);
        TextView text2=(TextView)view.findViewById(R.id.textNavHeader2);

        imgNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* UserRegistro userRegistro=MyApp.getWritableDatabase().getUserRegistro();

                if(!userRegistro.getNick_name().equals("noAsig")){
                    DialogPassUserProfile dialogPassUserProfile= new DialogPassUserProfile();
                    dialogPassUserProfile.show(getSupportFragmentManager(),"Log In");
                }else {
                    DialogRegiToClan dialogRegiToClan = new DialogRegiToClan();
                    dialogRegiToClan.show(getSupportFragmentManager(), "Regi");
                }*/

            }
        });

    }
    public void iniFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setVisibility(View.VISIBLE);
        mFab.setImageResource(R.drawable.ic_email_white_36dp);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mAppBarLayout.setExpanded(true, true);
                mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
                //Notice how the Coordinator Layout object is used here
                stateBackPress = 0;
                Snackbar.make(mCoordinator, getResources().getString(R.string.fab_home),
                        Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
                fragmentChanger("menu");*/

                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        // mAuth.addAuthStateListener(mAuthListener);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_item_1) {
            mAppBarLayout.setExpanded(true, true);
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
           // fragmentChanger("menu");
        } else if (id == R.id.navigation_item_2) {
           // stateBackPress=1000;
           // mAppBarLayout.setExpanded(false, true);
           // mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
           // mCollapsingToolbarLayout.setTitle("Fan Page Fiends");
           // fragmentChanger("web");
        }else if (id== R.id.nav_logout){
            signOut();
        }
        //stateBackPress=0;
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_main, menu);
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

    public void goToLaunch(){
        SPLASH_TIME=500;
        /*YoYo.with(Techniques.Shake)
                .duration(1000)
                .playOn(findViewById(R.id.imageView));*/
        new ActMain.CierreDeAplicacion().execute();
    }
    public class CierreDeAplicacion extends AsyncTask {

        private Intent myIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myIntent = new Intent(ActMain.this, ActLaunch.class);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            startActivity(myIntent);
            finish();
        }

    }
}
