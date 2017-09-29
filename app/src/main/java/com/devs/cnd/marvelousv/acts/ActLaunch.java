package com.devs.cnd.marvelousv.acts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.customview.RoundImage;
import com.devs.cnd.marvelousv.dialogs.DialogUserPassForgot;
import com.devs.cnd.marvelousv.events.ClickCallBack;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

public class ActLaunch extends AppCompatActivity
                        implements ClickCallBack{

    //VARS TIMES & Email view on
    private int SPLASH_TIME = 3000;
    boolean emailVs=false;

    // VARS VIEW
    private ImageView google,face,email, profilePic;
    private TextInputLayout txLyEmail, txLyPassword;
    private EditText edTxEmail, edTxPassword;
    private Switch newAccSwt;

    private FrameLayout frameFace;
    private LinearLayout layoutLogin, LayoutTop;
    private Button btEmCreate, btEmLogin, btEmForgot;
    private TextView logTx , lgtx1,lgtx2,txLogin;

    //VARS SESSIONS AUTH
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";

    private CallbackManager faceCallbackManager;
    private LoginButton loginButtonFace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_launch);

        mAuth = FirebaseAuth.getInstance();
        iniGoogleSignIn();
        iniFaceSignIn();
        iniEmailSignIn();

        iniVarLayout();

    }
    private void iniVarLayout(){
        profilePic=(ImageView)findViewById(R.id.ProfilePic);
        lgtx1=(TextView)findViewById(R.id.lgtx1);
        lgtx2=(TextView)findViewById(R.id.lgtx2);
        txLogin=(TextView)findViewById(R.id.txLogin);
        frameFace =(FrameLayout)findViewById(R.id.FrameFace);
        LayoutTop=(LinearLayout)findViewById(R.id.linearLayout);
        logTx=(TextView)findViewById(R.id.logTx);
        logTx.setText("Log");
        logTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();


                //LoginManager.getInstance().logInWithReadPermissions(ActLaunch.this, Arrays.asList("public_profile"));

                Toast.makeText(ActLaunch.this, "LogOUt from face",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void iniGoogleSignIn(){
        google =(ImageView)findViewById(R.id.ic_google);
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



        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
    }
    private void iniFaceSignIn(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);

        faceCallbackManager = CallbackManager.Factory.create();
        loginButtonFace=(LoginButton)findViewById(R.id.login_button_face);
        face =(ImageView)findViewById(R.id.ic_face);
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonFace.performClick();
            }
        });

        loginButtonFace.setReadPermissions("email", "public_profile");
        loginButtonFace.registerCallback(faceCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
    }
    private void iniEmailSignIn(){
        email =(ImageView)findViewById(R.id.ic_email);


        //Special fields for Email Sign Up/in
        newAccSwt = (Switch)findViewById(R.id.newAccSwt);
        txLyEmail= (TextInputLayout)findViewById(R.id.inputLayoutEmail);
        txLyPassword= (TextInputLayout)findViewById(R.id.inputLayoutPassword);
        edTxEmail=(EditText)findViewById(R.id.editTextEmail);
        edTxPassword=(EditText)findViewById(R.id.editTextPassword);

        layoutLogin=(LinearLayout)findViewById(R.id.layoutLogin);
        btEmCreate=(Button)findViewById(R.id.btEmCreate);
        btEmLogin=(Button)findViewById(R.id.btEmLogin);
        btEmForgot=(Button)findViewById(R.id.btEmForgot);



        newAccSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layoutLogin.setVisibility(View.GONE);
                    btEmCreate.setVisibility(View.VISIBLE);
                }else{
                    layoutLogin.setVisibility(View.VISIBLE);
                    btEmCreate.setVisibility(View.GONE);
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailVs==true){
                    newAccSwt.setVisibility(View.GONE);
                    email.setColorFilter(
                            ResourcesCompat.getColor(getResources(),R.color.colorGris700,null));
                    txLyEmail.setVisibility(View.GONE);
                    txLyPassword.setVisibility(View.GONE);

                    newAccSwt.setChecked(false);
                    layoutLogin.setVisibility(View.GONE);
                    btEmCreate.setVisibility(View.GONE);

                    //
                }
                else{
                    newAccSwt.setVisibility(View.VISIBLE);
                    email.setColorFilter(
                            ResourcesCompat.getColor(getResources(),R.color.teal600,null));
                    txLyEmail.setVisibility(View.VISIBLE);
                    txLyPassword.setVisibility(View.VISIBLE);

                    layoutLogin.setVisibility(View.VISIBLE);


                }
                emailVs= !emailVs;
            }
        });

        btEmLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInEmail(edTxEmail.getText().toString(), edTxPassword.getText().toString());
            }
        });

        btEmCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountEmail(edTxEmail.getText().toString(), edTxPassword.getText().toString());
            }
        });

        btEmForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUserPassForgot dialogUserPassForgot= new DialogUserPassForgot();
                dialogUserPassForgot.show(getSupportFragmentManager(),"Restore UPass");

            }
        });
    }

    /************** EMAIL SIGN IN FUCTIONS *************************/
    private void createAccountEmail(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            prefs.edit().putString("LoginProvider","FireEmail").apply();
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Email sent.");
                                                }
                                            }
                                        });
                            }
                            closeEmailForm();
                            updateUI(user);

                            Toast.makeText(ActLaunch.this, "Wellcome",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ActLaunch.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
    private void signInEmail(String email, String password) {
        Log.d(TAG, "signInGoogle:" + email);
        if (!validateForm()) {
            return;
        }
        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            prefs.edit().putString("LoginProvider","FireEmail").apply();
                            FirebaseUser user = mAuth.getCurrentUser();
                            closeEmailForm();
                            updateUI(user);

                            Toast.makeText(ActLaunch.this, "Wellcome",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(ActLaunch.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                           // mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = edTxEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edTxEmail.setError("Required.");
            valid = false;
        } else {
            edTxEmail.setError(null);
        }

        String password = edTxPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edTxPassword.setError("Required.");
            valid = false;
        } else {
            edTxPassword.setError(null);
        }

        return valid;
    }
    private void closeEmailForm(){
        newAccSwt.setVisibility(View.GONE);
        email.setColorFilter(
                ResourcesCompat.getColor(getResources(),R.color.colorGris700,null));
        txLyEmail.setVisibility(View.GONE);
        txLyPassword.setVisibility(View.GONE);

        newAccSwt.setChecked(false);
        layoutLogin.setVisibility(View.GONE);
        btEmCreate.setVisibility(View.GONE);
    }
    /********** END EMAIL SIGN IN FUCTIONS *************************/

    /************** GOOGLE SIGN IN FUNCTIONS ***********************/
    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }
    /********** END GOOGLE SIGN IN FUNCTIONS ***********************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        faceCallbackManager.onActivityResult(requestCode, resultCode, data);


        //logText.setText("you are here Act resul");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                Log.d("account: ","ur: "+account);
                Log.d("account: ","token: "+account.getIdToken());
                firebaseAuthWithGoogle(account);


            } else {

                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
              //  updateUI(null);
                // [END_EXCLUDE]
            }


            logTx.setText("You are on act result");
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
            //showProgressDialog();
        // [END_EXCLUDE]



        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            prefs.edit().putString("LoginProvider","Google").apply();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Toast.makeText(ActLaunch.this, "Wellcome",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(ActLaunch.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                            //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            prefs.edit().putString("LoginProvider","Facebook").apply();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Toast.makeText(ActLaunch.this, "Wellcome",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(ActLaunch.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
            //hideProgressDialog();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String LoginProvider =prefs.getString("LoginProvider","DEFAULT");


        if (user != null ) {

            Log.d(TAG, "Provider ID: " + user.getProviderId());
            /*google.setColorFilter(
                    ResourcesCompat.getColor(getResources(),R.color.teal600,null));*/

            UIsessionActive(LoginProvider,user);
            goToMain();


        } else {
            google.setColorFilter(
                    ResourcesCompat.getColor(getResources(),R.color.colorGris700,null));
        }
    }

    public void UIsessionActive(final String LoginProvider,FirebaseUser user){
        lgtx1.setVisibility(View.GONE);
        lgtx2.setVisibility(View.GONE);
        txLogin.setVisibility(View.VISIBLE);
        final int colorAuthGoog = ResourcesCompat.getColor(getResources(),R.color.red500,null);
        final int colorAuthFace = ResourcesCompat.getColor(getResources(),R.color.indigo500,null);
        final int colorAuthEmail = ResourcesCompat.getColor(getResources(),R.color.teal500,null);

        if(LoginProvider.equals("Google")){
            LayoutTop.setBackgroundResource(R.drawable.waves_google3);
            txLogin.setText("Login with Google");
            frameFace.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            UIloginLogoColor(google,colorAuthGoog);
        }else if(LoginProvider.equals("Facebook")){
            LayoutTop.setBackgroundResource(R.drawable.waves_face);
            txLogin.setText("Login with Facebook");
            google.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            UIloginLogoColor(face,colorAuthFace);
        }else if(LoginProvider.equals("FireEmail")){
            LayoutTop.setBackgroundResource(R.drawable.waves_email);
            txLogin.setText("Login with Email");
            google.setVisibility(View.GONE);
            frameFace.setVisibility(View.GONE);
            UIloginLogoColor(email,colorAuthEmail);
        }

        profilePic.setVisibility(View.VISIBLE);
        //profilePic.setIma
        Picasso.with(this).load(user.getPhotoUrl()).transform(new RoundImage()).into(profilePic);
    }
    public void UIloginLogoColor(final ImageView logo, final int color){
        final ValueAnimator colorAnim = ObjectAnimator.ofFloat(0f, 1f);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float mul = (Float) animation.getAnimatedValue();
                int alphaOrange = UIadjustAlpha(color, mul);
                logo.setColorFilter(alphaOrange, PorterDuff.Mode.SRC_ATOP);
                if (mul == 0.0) {
                    logo.setColorFilter(color);
                }
            }
        });

        colorAnim.setDuration(700);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.setRepeatCount(-1);
        colorAnim.start();
    }
    public int UIadjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
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
    public void onStop() {
        super.onStop();
       /* if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }*/
    }


    /*************** CLICKCALLBACK INTERFACE *************************/
    @Override
    public void onDialogEmailPassResetSet(String emailPassReset) {

        mAuth.sendPasswordResetEmail(emailPassReset)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(ActLaunch.this, "Email password reset sent",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /*********** END CLICKCALLBACK INTERFACE *************************/


    /*************** LAUNCH MAIN *************************/
    public void goToMain(){
        SPLASH_TIME=2200;
        /*YoYo.with(Techniques.Shake)
                .duration(1000)
                .playOn(findViewById(R.id.imageView));*/
        new AperturaDeAplicacion().execute();
    }
    public class AperturaDeAplicacion extends AsyncTask {

        private Intent myIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myIntent = new Intent(ActLaunch.this, ActMain.class);
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
    /*********** END LAUNCH MAIN *************************/
}
