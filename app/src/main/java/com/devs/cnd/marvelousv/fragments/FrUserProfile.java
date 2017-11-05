package com.devs.cnd.marvelousv.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.customview.RoundImage;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.firebase.UserDataRF;
import com.devs.cnd.marvelousv.objects.FSearch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by wunder on 9/29/17.
 */

public class FrUserProfile extends Fragment implements UserDataRF.FSrhRefCallback{

    private ClickCallBackMain clickCallBack;
    private InputMethodManager imm;

    private MyApp myApp;
    private FirebaseAuth mAuth;

    private boolean UDsearchable=false;
    private Switch fRqSwt;
    Button btEditUD, btResetPS;
    Button btEditUDCancel, btEditUDSave;
    LinearLayout linearEditor;

    EditText userNameToUpload;
    ImageView userPhotoToUpload;

    private ProgressDialog progressDialog ;


    ImageView userPhoto;
    TextView userName;


    public FrUserProfile(){}

    public  static FrUserProfile newInstance(){
        FrUserProfile frUserProfile=new FrUserProfile();
        /*Bundle args = new Bundle();
        args.putString(ARG_WBGID, id);
        args.putInt(ARG_PREV_FR,mainFr);
        frWbGWords.setArguments(args);*/
        return frUserProfile;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApp =(MyApp)getContext().getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        //setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FirebaseUser user = mAuth.getCurrentUser();

        View v = inflater.inflate(R.layout.fr_user_profile,container,false);

        userPhoto= (ImageView)v.findViewById(R.id.userPhoto);
        ImageView icTypeProf= (ImageView)v.findViewById(R.id.icTypeProf);
        userName=(TextView)v.findViewById(R.id.userName);
        TextView userEmail=(TextView)v.findViewById(R.id.userEmail);
        fRqSwt=(Switch)v.findViewById(R.id.fRqSwt);

        btEditUD=(Button) v.findViewById(R.id.btEditUD);
        btResetPS=(Button) v.findViewById(R.id.btResetPS);

        linearEditor=(LinearLayout)v.findViewById(R.id.linearEditor);
        btEditUDCancel=(Button) v.findViewById(R.id.btEditUDCancel);
        btEditUDSave=(Button)v.findViewById(R.id.btEditUDSave);
        userNameToUpload=(EditText)v.findViewById(R.id.userNameToUpload);
        userPhotoToUpload=(ImageView)v.findViewById(R.id.userPhotoToUpload);
        userPhotoToUpload.setDrawingCacheEnabled(true);

        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());
        String photoU="";
        if(user.getPhotoUrl()!=null){
            photoU= user.getPhotoUrl().toString();
        }
        Log.d("SizePrv","Sz: "+user.getProviderData().size());
        for(UserInfo profile : user.getProviderData()) {
            Log.d("ProviderID", profile.getProviderId());
            if (profile.getProviderId().equals("facebook.com")) {
                if(!photoU.contains("firebase"))
                    photoU="https://graph.facebook.com/" + profile.getUid() + "/picture?height=500";

                icTypeProf.setImageResource(R.drawable.ic_facebook_box_black_24dp);
            }
            else if(profile.getProviderId().equals("google.com")){
                icTypeProf.setImageResource(R.drawable.ic_google_black_24dp);
            }
            else if(profile.getProviderId().equals("password")){
                btResetPS.setVisibility(View.VISIBLE);
                icTypeProf.setImageResource(R.drawable.ic_email_black_24dp);
            }

        }

        Picasso.with(getContext()).load(photoU)
                .transform(new RoundImage()).into(userPhotoToUpload);
        Picasso.with(getContext()).load(photoU)
                .transform(new RoundImage()).into(userPhoto);


        userNameToUpload.setText(user.getDisplayName());



        FrUserProfile frUserProfile =(FrUserProfile)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor_base);
        myApp.userDataRF.onFrUserProfileActive(frUserProfile);
        myApp.userDataRF.UserDataLoad();


        fRqSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myApp.userDataRF.userDataSearhAbleUpdate(isChecked); UDsearchable=true;
                    //Toast.makeText(getContext(),"Fr Rq Able",Toast.LENGTH_SHORT).show();
                }else{
                    myApp.userDataRF.userDataSearhAbleUpdate(isChecked);  UDsearchable=false;
                    //Toast.makeText(getContext(),"Fr Rq Disable",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btEditUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearEditor.setVisibility(View.VISIBLE);
                btEditUD.setVisibility(View.GONE);
            }
        });

        btEditUDCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btEditUD.setVisibility(View.VISIBLE);
                linearEditor.setVisibility(View.GONE);
            }
        });


        userPhotoToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(RESULT_LOAD_IMG);

                /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pick a Choice")
                        .setItems(R.array.catg_intent, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    loadImagefromGallery(RESULT_LOAD_IMG);
                                }else if(which==1){
                                    loadImagefromCamera(RESULT_LOAD_IMG);
                                }
                            }
                        });
                builder.create();
                builder.show();*/
            }
        });

        btEditUDSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btEditUD.setVisibility(View.VISIBLE);
                linearEditor.setVisibility(View.GONE);
                encodeImg();
            }
        });

        return v;
    }


    @Override
    public void onFSearchRefSet(FSearch fSearch, boolean searchAble) {
        fRqSwt.setChecked(searchAble);
    }
    @Override
    public void onFSearchRefFail() {

    }
    @Override
    public void onUserDataUpdate() {
        final FirebaseUser user = mAuth.getCurrentUser();
        String photoU="";
        if(user.getPhotoUrl()!=null){
            photoU= user.getPhotoUrl().toString();
        }

        userName.setText(user.getDisplayName());
        Picasso.with(getContext()).load(photoU)
                .transform(new RoundImage()).into(userPhoto);

        Picasso.with(getContext()).load(photoU)
                .transform(new RoundImage()).into(userPhotoToUpload);
    }


    public void loadImagefromCamera(int result) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // MediaStore.ACTION_IMAGE_CAPTURE);
                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(cameraIntent, result);
    }
    public void loadImagefromGallery(int result) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                // MediaStore.ACTION_IMAGE_CAPTURE);
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, result);
    }
    //VARS ENCODE IMG
    String imgPath;
    Bitmap bitmap, bitmapResize;
    byte[] dataImg;
    private static int RESULT_LOAD_IMG = 1;
    Uri selectedImage;
    private boolean imageLoad=false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if(requestCode == RESULT_LOAD_IMG && resultCode== getActivity().RESULT_OK
                    && null != data){

                //Bitmap for FBStorage
                // Get the Image from data
                selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor =getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                Picasso.with(getContext()).load(selectedImage)
                        .transform(new RoundImage()).into(userPhotoToUpload);

                //userPhotoToUpload.buildDrawingCache();
                //bitmap = userPhotoToUpload.getDrawingCache();
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                imageLoad=true;


                /*Uri pu= Picasso.with(getContext()).load(selectedImage).resize(500,500)
                        .onlyScaleDown().centerCrop().*/

                //Setear imagen en el app
                /*imgFormacion.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                saveImgFormacion.setVisibility(View.VISIBLE);
                imgFormacion.setClickable(true);*/

            }else
            {
                //imgFormacion.setClickable(true);
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong: "+e, Toast.LENGTH_LONG)
                    .show();
        }
    }
    public void encodeImg(){
        progressDialog.setMessage("Compressing image ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                //super.onPreExecute();

            }
            @Override
            protected String doInBackground(Void... params) {

                if(imageLoad) {
                    if (bitmap.getWidth() > 700) {

                        final int maxSize = 700;
                        int outWidth;
                        int outHeight;
                        int inWidth = bitmap.getWidth();
                        int inHeight = bitmap.getHeight();
                        if (inWidth > inHeight) {
                            outWidth = maxSize;
                            outHeight = (inHeight * maxSize) / inWidth;
                        } else {
                            outHeight = maxSize;
                            outWidth = (inWidth * maxSize) / inHeight;
                        }
                        bitmapResize = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);

                    } else {
                        bitmapResize = bitmap;
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();


                    try {
                        bitmapResize = rotateImageIfRequired(bitmapResize, getContext(), selectedImage);
                    } catch (IOException e) {

                    }
                    bitmapResize.compress(Bitmap.CompressFormat.JPEG, 100, baos);


                    dataImg = baos.toByteArray();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                //super.onPostExecute(s);
                checkImgEncode();


            }
        }.execute(null,null,null);
    }
    public void checkImgEncode(){
        //L.t(getContext(),"IMG En: "+encodeImageString.substring(0,50));
       // Toast.makeText(getContext(),"Resizing img: "+fl,Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        String uname="";
        uname= userNameToUpload.getText().toString();
        if(imageLoad){
            myApp.userDataRF.userPhotoUpdate(dataImg,uname,UDsearchable);
        }
        else{
            final FirebaseUser user = mAuth.getCurrentUser();
            String photoU="";
            if(user.getPhotoUrl()!=null){
                photoU= user.getPhotoUrl().toString();
            }
            myApp.userDataRF.userDataUpdate(uname,photoU,UDsearchable);
        }
        //sendImageToBackend(getContext() ,encodeImageString);
    }




    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            //Timber.d("orientation: %s", orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            clickCallBack=(ClickCallBackMain) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Fragment Interaction Listener");
        }
    }
}
