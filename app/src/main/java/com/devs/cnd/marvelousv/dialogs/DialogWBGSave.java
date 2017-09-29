package com.devs.cnd.marvelousv.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.objects.WordBox;
import com.devs.cnd.marvelousv.objects.WordBoxG;
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
 * Created by wunder on 9/28/17.
 */

public class DialogWBGSave extends DialogFragment implements View.OnClickListener {

    private MyApp myApp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    private static final String TAG = "AKDialogFragment";
    private EditText editTextWBName;
    private ImageView imgFav, imgGlo;
    private Switch favoriteSwt, gBoardSwt;
    private LinearLayout boxShapeGB, boxShapeFa;

    private WordBoxG wordBoxG;

    InputMethodManager imm;
    View rootView;

    public void setWordBoxGToSave(WordBoxG wordBoxG){
        this.wordBoxG=wordBoxG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        myApp = (MyApp) getContext().getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        rootView = inflater.inflate(R.layout.dialog_wb, container, false);
        editTextWBName = (EditText) rootView.findViewById(R.id.editTextWBName);
        imgFav = (ImageView) rootView.findViewById(R.id.imgFav);
        imgGlo = (ImageView) rootView.findViewById(R.id.imgGlo);
        favoriteSwt = (Switch) rootView.findViewById(R.id.favoriteSwt);
        gBoardSwt = (Switch) rootView.findViewById(R.id.gBoardSwt);

        boxShapeGB=(LinearLayout)rootView.findViewById(R.id.linearGbo);
        boxShapeFa=(LinearLayout)rootView.findViewById(R.id.linearFav);


       boxShapeFa.setVisibility(View.GONE);
        boxShapeGB.setVisibility(View.GONE);

        ImageView icCancel = (ImageView) rootView.findViewById(R.id.icCancel);
        icCancel.setOnClickListener(this);

        TextView txTitle = (TextView) rootView.findViewById(R.id.txTitle);
        txTitle.setText("Save Box As");

        TextView txSave = (TextView) rootView.findViewById(R.id.txSave);
        txSave.setOnClickListener(this);

        String helperTx="";
        helperTx=wordBoxG.getBoxName();
        Toast.makeText(getContext(),"NameBox: "+helperTx,Toast.LENGTH_SHORT).show();
        editTextWBName.setText(helperTx);

        return rootView;
    }


    public void closeDialog() {
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
        if (v == v.findViewById(R.id.txSave)) {
            String helperTx="";
            helperTx=editTextWBName.getText().toString();
            wordBoxG.setBoxName(helperTx);
            myApp.wordboxesGF.saveWordBox(wordBoxG);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
            dismiss();
        } else if (v == v.findViewById(R.id.icCancel)) {
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
            dismiss();
        }
    }
}
