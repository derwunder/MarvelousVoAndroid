package com.devs.cnd.marvelousv.acts;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.customview.RoundImage;
import com.devs.cnd.marvelousv.dialogs.DialogWBAdd;
import com.devs.cnd.marvelousv.dialogs.DialogWBWAdd;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.fragments.FrAllWbWords;
import com.devs.cnd.marvelousv.fragments.FrFriendList;
import com.devs.cnd.marvelousv.fragments.FrTabsFL;
import com.devs.cnd.marvelousv.fragments.FrWbGFriend;
import com.devs.cnd.marvelousv.fragments.FrWbGUFriend;
import com.devs.cnd.marvelousv.fragments.FrWbGWords;
import com.devs.cnd.marvelousv.fragments.FrWbGlobal;
import com.devs.cnd.marvelousv.fragments.FrWbWords;
import com.devs.cnd.marvelousv.fragments.FrWordboxes;
import com.devs.cnd.marvelousv.objects.Friend;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ActMain extends AppCompatActivity
                        implements NavigationView.OnNavigationItemSelectedListener,
        ClickCallBackMain{

    private MyApp myApp;
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
    private ImageView imgViewChanger;

    // Var FRAGMENT TYPE
    private int FR_WORDBOXES= 3001, FR_WB_WORDS=3002, FR_ALL_WB_WORDS=3003;
    private int FR_FRIENDLIST= 3005, FR_WBG_FRIEND=3004, FR_WB_GLOBAL=3006;
    private int FR_WBGU_FRIEND= 3044, FR_WBG_WORDS=3066;

    //VAR Current Friend Data
    private Friend currentFriend;

    // VAR ON BACK PRESS
    private int stateBackPress=0, prevStateBackPress=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        myApp = (MyApp) this.getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        currentFriend= new Friend();

        iniGoogle();

        iniCoordinator();
        iniToolBar();
        iniNavDrawer();
        iniFab();

        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        mAppBarLayout.setExpanded(false,false);
        mAppBarLayout.setActivated(false);
        mCollapsingToolbarLayout.setActivated(false);

        //fragmentChanger(FR_WORDBOXES);
        //fabChanger(FR_WORDBOXES);
        checkFragmentInstace();
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
            //TextView textHello = (TextView)findViewById(R.id.textHello);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String LoginProvider =prefs.getString("LoginProvider",null);

            /*textHello.setText(LoginProvider+"\n"+
                    user.getDisplayName()+"\n"+
                    user.getEmail()+"\n"+
                    user.getProviderId()+"\n"+
            user.getProviderData());*/

        }
    }
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String LoginProvider =prefs.getString("LoginProvider","DEFAULT");

        if(LoginProvider.equals("Facebook"))
            LoginManager.getInstance().logOut();

        // Google sign out
        if(LoginProvider.equals("Google"))
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });

        updateUI(null);
    }

    /******* LAYOUT INICIALIZATION  ******/
    public  void iniCoordinator(){
        //Grupo de Coordinacion para la actividad Base
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.app_bar_layout);
        imgViewChanger=(ImageView)findViewById(R.id.imgViewChanger);
    }
    public void iniToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
    }
    public void iniNavDrawer(){
        final FirebaseUser user = mAuth.getCurrentUser();
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

        String photoU="";
        if(user.getPhotoUrl()!=null)
            photoU= user.getPhotoUrl().toString();

        Picasso.with(this)
                .load(photoU)
                .transform(new RoundImage()).into(imgNav);

        text1.setText(user.getDisplayName());
        text2.setText(user.getEmail());

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


        /* mFab.setOnTouchListener(new View.OnTouchListener() {

            float startX;
            float startRawX;
            float distanceX;
            //int lastAction;

            float dX;
            float dY;
            int lastAction;


            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            Toast.makeText(ActMain.this, "Clicked!", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return false;
                }
                return true;
            }


        });*/
    }


    /******* FAB BT CHANGER   *****/
    public void fabChanger(int where){
        if(where==FR_WORDBOXES){
            mFab.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_add_white_24dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    DialogWBAdd dialogWBAdd = new DialogWBAdd();


                    //OP1:
                    //FragmentTransaction transaction = fragmentManager.beginTransaction();
                    //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    //transaction.add(android.R.id.content, newFragment).commit();

                    //OP2:
                    dialogWBAdd.show(fragmentManager, "dialog");

                    //OP3
                    // The device is smaller, so show the fragment fullscreen
                   /* FragmentTransaction transaction = fragmentManager.beginTransaction();
                    // For a little polish, specify a transition animation
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    // To make it fullscreen, use the 'content' root view as the container
                    // for the fragment, which is always the root view for the activity
                    transaction.add(R.id.drawer_layout, dialogWBAdd)
                            .addToBackStack(null)
                            .commit();*/

                    Snackbar.make(v, "New Wordbox", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        else if(where==FR_FRIENDLIST ){
            mFab.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_add_white_24dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Snackbar.make(v, "New Friend", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        else if(where==FR_WB_GLOBAL || where==FR_WBG_FRIEND || where==FR_WBGU_FRIEND){
            mFab.setVisibility(View.GONE);
            mFab.setImageResource(R.drawable.ic_add_white_24dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Snackbar.make(v, "FR GLOBAL", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }
    public void fabChanger(int where, final String id){
        if(where==FR_WB_WORDS){
            mFab.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_add_white_24dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    DialogWBWAdd dialogWBWAdd = new DialogWBWAdd();
                    dialogWBWAdd.setWBId(id);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(R.id.drawer_layout, dialogWBWAdd)
                            .addToBackStack(null).commit();

                    Snackbar.make(v, "New Word", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        } else if(where==FR_WBGU_FRIEND) {
            mFab.setVisibility(View.GONE);
            mFab.setImageResource(R.drawable.ic_add_white_24dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Snackbar.make(v, "Friend Boxes", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        } else if(where==FR_WBG_WORDS) {
            mFab.setVisibility(View.GONE);
            mFab.setImageResource(R.drawable.ic_add_white_24dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Snackbar.make(v, "WB GLOBAL", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }
    /******* FRAGMENT CHANGER ****/

    public void fragmentChanger(int where) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (where==FR_WORDBOXES) {
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrWordboxes.newInstance())
                    .commit();
        }
        else if (where==FR_ALL_WB_WORDS) {
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrAllWbWords.newInstance())
                    .commit();
        }
        else if (where==FR_WBG_FRIEND){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrWbGFriend.newInstance())
                    .commit();
        }
        else if (where==FR_FRIENDLIST){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrTabsFL.newInstance())
                    .commit();
        }
        else if (where==FR_WB_GLOBAL){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrWbGlobal.newInstance())
                    .commit();
        }
    }
    public void fragmentChanger(int where, String id){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (where==FR_WB_WORDS) {
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrWbWords.newInstance(id))
                    .commit();
        }
        if(where==FR_WBGU_FRIEND){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrWbGUFriend.newInstance(id))
                    .commit();
        }
        if(where==FR_WBG_WORDS){
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FrWbGWords.newInstance(id,prevStateBackPress))
                    .commit();
        }
    }

    /******** BACK PRESS MEMORY **********/
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(stateBackPress==FR_WB_WORDS){
            stateBackPress=0;
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
            //mAppBarLayout.setExpanded(true,true);
            fragmentChanger(FR_WORDBOXES);
            fabChanger(FR_WORDBOXES);
        }
        else if(stateBackPress==FR_WBGU_FRIEND){
            stateBackPress=0;
            mCollapsingToolbarLayout.setTitle("Friend List");
            fragmentChanger(FR_FRIENDLIST);
            fabChanger(FR_FRIENDLIST);
        }
        else if(stateBackPress==FR_WBG_WORDS){
            if(prevStateBackPress==FR_WB_GLOBAL){
                fragmentChanger(FR_WB_GLOBAL);
                fabChanger(FR_WB_GLOBAL);
                mCollapsingToolbarLayout.setTitle("Global Board");
                stateBackPress=0;
            }
            else if(prevStateBackPress==FR_WBG_FRIEND){
                fragmentChanger(FR_WBG_FRIEND);
                fabChanger(FR_WBG_FRIEND);
                mCollapsingToolbarLayout.setTitle("Friend Board");
                stateBackPress=FR_WBG_FRIEND;
            }
            else if(prevStateBackPress==FR_WBGU_FRIEND){
                fragmentChanger(FR_WBGU_FRIEND,currentFriend.getId());
                fabChanger(FR_WBGU_FRIEND,currentFriend.getId());
                mCollapsingToolbarLayout.setTitle(currentFriend.getFrName()+"WB");
                stateBackPress=FR_WBGU_FRIEND;
            }

        }

        else{
            super.onBackPressed();}
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
           // mAppBarLayout.setExpanded(true, true);
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
            fragmentChanger(FR_WORDBOXES);
            fabChanger(FR_WORDBOXES);
            stateBackPress=0;
        } else if (id == R.id.navigation_item_2) {
           // stateBackPress=1000;
           // mAppBarLayout.setExpanded(false, true);
           // mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
            mCollapsingToolbarLayout.setTitle("All Words");
            fragmentChanger(FR_ALL_WB_WORDS);
            stateBackPress=0;
        }else if(id == R.id.navigation_item_3){



        }
        else if(id ==  R.id.navigation_item_4){
            mCollapsingToolbarLayout.setTitle("Friend Board");
            fragmentChanger(FR_WBG_FRIEND);
            fabChanger(FR_WBG_FRIEND);
            stateBackPress=0;
        }
        else if(id ==  R.id.navigation_item_5){
            mCollapsingToolbarLayout.setTitle("Friend List");
            fragmentChanger(FR_FRIENDLIST);
            fabChanger(FR_FRIENDLIST);
            stateBackPress=0;
        }
        else if(id ==  R.id.navigation_item_6){
            mCollapsingToolbarLayout.setTitle("Global Board");
            fragmentChanger(FR_WB_GLOBAL);
            fabChanger(FR_WB_GLOBAL);
            stateBackPress=0;
        }
        else if (id== R.id.nav_logout){
            signOut();
        }
        //stateBackPress=0;
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_act_main, menu);
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


    /******************* CLICKCALL BACK INTERFACE ***************/
    @Override
    public void onWordBoxOpen(String id) {

        fragmentChanger(FR_WB_WORDS,id);
        fabChanger(FR_WB_WORDS,id);
        mCollapsingToolbarLayout.setTitle(myApp.wordboxes.getBoxName(id));
        stateBackPress=FR_WB_WORDS;
    }

    @Override
    public void onWordBoxGOpen(String id, String boxName, int mainFr) {
        prevStateBackPress=mainFr;
        fragmentChanger(FR_WBG_WORDS, id);
        fabChanger(FR_WBG_WORDS,id);
        mCollapsingToolbarLayout.setTitle(boxName);
        stateBackPress=FR_WBG_WORDS;
    }

    @Override
    public void onFIDOpen(String id, String FName) {
        currentFriend.setId(id);
        currentFriend.setFrName(FName);
        fragmentChanger(FR_WBGU_FRIEND,id);
        fabChanger(FR_WBGU_FRIEND,id);
        mCollapsingToolbarLayout.setTitle(FName+" WB");
        stateBackPress=FR_WBGU_FRIEND;

    }

    /******************** GO TO LAUNCH ACT ***********/
    public void goToLaunch(){
        SPLASH_TIME=500;
        /*YoYo.with(Techniques.Shake)
                .duration(1000)
                .playOn(findViewById(R.id.imageView));*/
        new closeActMain().execute();
    }


    public void checkFragmentInstace(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.contenedor_base);
        if (currentFragment instanceof FrWordboxes) {
        }
        else if (currentFragment instanceof FrWbWords) {
        }
        else if (currentFragment instanceof FrWbGWords) {
        }
        else if (currentFragment instanceof FrWbGUFriend) {
        }
        else if (currentFragment instanceof FrWbGlobal) {
        }
        else if (currentFragment instanceof FrWbGFriend) {
        }
        else if (currentFragment instanceof FrTabsFL) {
        }
        else if (currentFragment instanceof FrAllWbWords) {
        }
        else{
            fragmentChanger(FR_WORDBOXES);
            fabChanger(FR_WORDBOXES);
        }
    }

    public class closeActMain extends AsyncTask {

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
