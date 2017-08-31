package com.devs.cnd.marvelousv.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.events.ClickCallBackFr;
import com.devs.cnd.marvelousv.fragments.FrWordboxes;
import com.devs.cnd.marvelousv.objects.WordBox;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 7/20/17.
 */

public class DialogWBAdd extends DialogFragment implements View.OnClickListener{
    private MyApp myApp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    private static final String TAG = "AKDialogFragment";
    private EditText editTextWBName;
    private ImageView imgFav, imgGlo;
    private Switch favoriteSwt,gBoardSwt;
    private boolean favorite=false,  gBoard=false;

    InputMethodManager imm;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myApp =(MyApp)getContext().getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        rootView = inflater.inflate(R.layout.dialog_wb, container, false);
        editTextWBName=(EditText)rootView.findViewById(R.id.editTextWBName);
        imgFav=(ImageView)rootView.findViewById(R.id.imgFav);
        imgGlo=(ImageView)rootView.findViewById(R.id.imgGlo);
        favoriteSwt=(Switch)rootView.findViewById(R.id.favoriteSwt);
        gBoardSwt=(Switch)rootView.findViewById(R.id.gBoardSwt);


        favoriteSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imgFav.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.teal500,null));
                    favorite=isChecked;
                }else{
                    imgFav.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.colorGris700,null));
                    favorite=isChecked;
                }
            }
        });
        gBoardSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imgGlo.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.teal500,null));
                    gBoard=isChecked;
                }else{
                    imgGlo.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.colorGris700,null));
                    gBoard=isChecked;
                }
            }
        });

        ImageView icCancel= (ImageView)rootView.findViewById(R.id.icCancel);
        icCancel.setOnClickListener(this);

        TextView txTitle=(TextView)rootView.findViewById(R.id.txTitle);
        txTitle.setText("New Word Box");

        TextView txSave=(TextView)rootView.findViewById(R.id.txSave);
        txSave.setOnClickListener(this);

        return rootView;
    }



    private void upWB(){
        final FirebaseUser user = mAuth.getCurrentUser();

        final String wbKey=mDatabase.child("users").child(user.getUid()).child("wordboxes").push().getKey();

        Calendar now =Calendar.getInstance();

        final WordBox wordBox = new WordBox();
        wordBox.setId(wbKey);
        wordBox.setBoxName(editTextWBName.getText().toString());
        wordBox.setFavorite(favorite);
        wordBox.setgBoard(gBoard);
        wordBox.setCreatedAt(now.getTimeInMillis());
        wordBox.setLastCheckedAt(now.getTimeInMillis());

        Map<String, Object> wbItems = wordBox.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
       // final Map<String, Object> childGUpdates = new HashMap<>();
        Map<String, Object> wbGItems = new HashMap<>();
        childUpdates.put("users/"+user.getUid()+"/wordboxes/"+wbKey, wbItems);

        String photoU="";
        if(user.getPhotoUrl()!=null)
             photoU= user.getPhotoUrl().toString();

        if(gBoard){

            wbGItems.put("boxName",editTextWBName.getText().toString());
            wbGItems.put("createdAt",now.getTimeInMillis());
            wbGItems.put("updatedAt",(-1*now.getTimeInMillis()));
            wbGItems.put("createBy",user.getUid());
            wbGItems.put("creatorName",user.getDisplayName());
            wbGItems.put("creatorAvatar",photoU); //FUCK LINEA DA ERROR MMGVA SEAA!
            wbGItems.put("words",false);
            childUpdates.put("global/wordboxes/"+wbKey+"/wordbox", wbGItems);

        }

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myApp.wordboxes.addWordBox(wordBox);
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                closeDialog();
                Toast.makeText(getActivity(),"Succes postG FB",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                closeDialog();
                Toast.makeText(getActivity(),"Fail post FB",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void closeDialog(){
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        if(v==v.findViewById(R.id.txSave)){
            upWB();
        }
        else if(v==v.findViewById(R.id.icCancel)){
           imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
           dismiss();
        }
    }
}
