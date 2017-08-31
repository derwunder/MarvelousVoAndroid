package com.devs.cnd.marvelousv.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
 * Created by wunder on 7/23/17.
 */

public class DialogWBEdit extends DialogFragment implements View.OnClickListener{
    private MyApp myApp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private WordBox wb;

    private static final String TAG = "AKDialogFragment";
    private EditText editTextWBName;
    private ImageView imgFav, imgGlo;
    private Switch favoriteSwt,gBoardSwt;
    private boolean favorite=false,  gBoard=false;
    private String currentWBName="";

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

        editTextWBName.setText(currentWBName);
        favoriteSwt.setChecked(favorite);
        gBoardSwt.setChecked(gBoard);
        //Toast.makeText((ActMain)getActivity(),"ID: "+wb.getId(),Toast.LENGTH_LONG).show();

        ImageView icCancel= (ImageView)rootView.findViewById(R.id.icCancel);
        icCancel.setOnClickListener(this);

        TextView txTitle=(TextView)rootView.findViewById(R.id.txTitle);
        txTitle.setText("Word Box Editor");

        TextView txSave=(TextView)rootView.findViewById(R.id.txSave);
        txSave.setOnClickListener(this);


        return rootView;
    }



    public void setWordBox(WordBox wb){
        this.wb=wb;
        favorite=wb.getFavorite();
        gBoard=wb.getGBoard();
        currentWBName=wb.getBoxName();

    }

    private void upWB(){
        final FirebaseUser user = mAuth.getCurrentUser();

        final String wbKey=wb.getId();

        Calendar now =Calendar.getInstance();

        //wb.setId(wbKey);
        wb.setBoxName(editTextWBName.getText().toString());
        wb.setFavorite(favorite);
        wb.setgBoard(gBoard);
        //wb.setCreatedAt(now.getTimeInMillis());
        wb.setLastCheckedAt(now.getTimeInMillis());

        Map<String, Object> wbItems = new HashMap<>();
        wbItems.put("boxName",wb.getBoxName());
        wbItems.put("favorite",wb.getFavorite());
        wbItems.put("gBoard",wb.getGBoard());
        wbItems.put("createdAt",wb.getCreatedAt());
        wbItems.put("lastCheckedAt",wb.getLastCheckedAt());
        wbItems.put("words",wb.toMapWords());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/"+user.getUid()+"/wordboxes/"+wbKey, wbItems);

        String photoU="";
        if(user.getPhotoUrl()!=null)
            photoU= user.getPhotoUrl().toString();

        Map<String, Object> wbGItems = new HashMap<>();
        if(gBoard){

            wbGItems.put("boxName",editTextWBName.getText().toString());
            wbGItems.put("createdAt",wb.getCreatedAt());
            wbGItems.put("updatedAt",(-1*now.getTimeInMillis()));
            wbGItems.put("createBy",user.getUid());
            wbGItems.put("creatorName",user.getDisplayName());
            wbGItems.put("creatorAvatar",photoU);
            if(wb.getWords().size()==0){
                wbGItems.put("words",false);
            }else{
                wbGItems.put("words",wb.toMapWords());
            }
            childUpdates.put("global/wordboxes/"+wbKey+"/wordbox", wbGItems);

        }else{
            childUpdates.put("global/wordboxes/"+wbKey+"/wordbox", null);
        }

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myApp.wordboxes.updateWordBox(wb);
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                closeDialog();
                Toast.makeText(getActivity(),"Succes UpdateG FB",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                closeDialog();
                Toast.makeText(getActivity(),"Fail update FB",Toast.LENGTH_SHORT).show();
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
