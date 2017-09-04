package com.devs.cnd.marvelousv.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.adapters.words.AdaptLtDefEd;
import com.devs.cnd.marvelousv.adapters.words.AdaptLtDefRcEd;
import com.devs.cnd.marvelousv.adapters.words.AdaptLtTagEd;
import com.devs.cnd.marvelousv.adapters.words.AdaptLtTransEd;
import com.devs.cnd.marvelousv.adapters.words.AdaptLtTransRcEd;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.customview.NonScrollListView;
import com.devs.cnd.marvelousv.objects.Definition;
import com.devs.cnd.marvelousv.objects.Tag;
import com.devs.cnd.marvelousv.objects.Translation;
import com.devs.cnd.marvelousv.objects.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by wunder on 7/26/17.
 */

public class DialogWBWEdit extends DialogFragment implements View.OnClickListener {

    private MyApp myApp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String WBId="";

    private static final String TAG = "AKDialogFragment";
    private EditText editTextWName;
    private ImageView imgBookmark;
    private Switch bookmarkSwt;
    private boolean bookmark =false;

    private NonScrollListView listDefs, listTrans;
    private RecyclerView listTags, listTransRc, listDefsRc;
    private AdaptLtDefEd adaptLtDefEd;
    private AdaptLtDefRcEd adaptLtDefRcEd;
    private AdaptLtTransEd adaptLtTransEd;
    private AdaptLtTransRcEd adaptLtTransRcEd;
    private AdaptLtTagEd adaptLtTagEd;
    private Button btDef, btTrans, btTag;
    private EditText editTag;
    private String currentTag="";

    private ArrayList<Definition> listDef= new ArrayList<>();
    private ArrayList<Translation> listTran= new ArrayList<>();
    private ArrayList<Tag> listTag = new ArrayList<>();

    InputMethodManager imm;
    View rootView;

    private Word w=new Word();

    public void setWBId(String id){
        WBId=id;
    }
    public  void setWord(Word w){
        this.w=w;
        for(Definition d: w.getDefinitions()){
            listDef.add(d);
        }
        for(Translation tr: w.getTranslations()){
            listTran.add(tr);
        }
        for(Tag t: w.getTags()){
            listTag.add(t);
        }/*
        listDef=w.getDefinitions();
        listTran=w.getTranslations();
        listTag=w.getTags();*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myApp =(MyApp)getContext().getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        rootView = inflater.inflate(R.layout.dialog_wb_words, container, false);
        editTextWName =(EditText)rootView.findViewById(R.id.editTextWName);
        imgBookmark =(ImageView)rootView.findViewById(R.id.imgBookmark);
        bookmarkSwt =(Switch)rootView.findViewById(R.id.bookmarkSwt);

        editTextWName.setText(w.getWordTerm());

        /*listDefs=(NonScrollListView)rootView.findViewById(R.id.listDefs);
        listDefs.setVisibility(View.VISIBLE);
        adaptLtDefEd= new AdaptLtDefEd(listDef,getContext());
        listDefs.setAdapter(adaptLtDefEd);*/

        listDefsRc=(RecyclerView)rootView.findViewById(R.id.listDefsRc);
        listDefsRc.setVisibility(View.VISIBLE);
        StaggeredGridLayoutManager sglmListDf =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        listDefsRc.setLayoutManager(sglmListDf);
        adaptLtDefRcEd= new AdaptLtDefRcEd(getContext());
        adaptLtDefRcEd.setListDefs(listDef);
        listDefsRc.setAdapter(adaptLtDefRcEd);
        btDef=(Button)rootView.findViewById(R.id.btDef);
        btDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptLtDefRcEd.addDef();
            }
        });

        /*listTrans=(NonScrollListView)rootView.findViewById(R.id.listTrans);
        listTrans.setVisibility(View.VISIBLE);
        adaptLtTransEd= new AdaptLtTransEd(listTran,getContext());
        listTrans.setAdapter(adaptLtTransEd);*/

        listTransRc=(RecyclerView)rootView.findViewById(R.id.listTransRc);
        listTransRc.setVisibility(View.VISIBLE);
        StaggeredGridLayoutManager sglmListTr =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        listTransRc.setLayoutManager(sglmListTr);
        adaptLtTransRcEd= new AdaptLtTransRcEd(getContext());
        adaptLtTransRcEd.setListTrans(listTran);
        listTransRc.setAdapter(adaptLtTransRcEd);
        btTrans=(Button)rootView.findViewById(R.id.btTrans);
        btTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptLtTransRcEd.addTrans();
            }
        });

        listTags=(RecyclerView)rootView.findViewById(R.id.listTags);
        listTags.setVisibility(View.VISIBLE);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        listTags.setLayoutManager(staggeredGridLayoutManager);
        adaptLtTagEd= new AdaptLtTagEd(getContext());
        adaptLtTagEd.setListTag(listTag);
        listTags.setAdapter(adaptLtTagEd);
        editTag=(EditText)rootView.findViewById(R.id.editTag);
        editTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                currentTag = s.toString();
            }
        });
        btTag=(Button)rootView.findViewById(R.id.btTag);
        btTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptLtTagEd.addTag(currentTag);
                editTag.setText("");
            }
        });

        bookmarkSwt.setChecked(w.getBookmark());
        bookmarkSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imgBookmark.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.teal500,null));
                    bookmark =isChecked;
                }else{
                    imgBookmark.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.colorGris700,null));
                    bookmark =isChecked;
                }
            }
        });



        ImageView icCancel= (ImageView)rootView.findViewById(R.id.icCancel);
        icCancel.setOnClickListener(this);

        TextView txTitle=(TextView)rootView.findViewById(R.id.txTitle);
        txTitle.setText("Edit Word");

        TextView txSave=(TextView)rootView.findViewById(R.id.txSave);
        txSave.setOnClickListener(this);
        return rootView;
    }

    public Word saveWord(){
        w.setWordTerm(editTextWName.getText().toString());
        w.setDefinitions(adaptLtDefRcEd.getListDefs());
        w.setTranslations(adaptLtTransRcEd.getListTrans());
        w.setTags(adaptLtTagEd.getListTag());
        w.setBookmark(bookmark);

        return  w;
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
            // upWB();
            myApp.wordboxes.updateWBWordFB(saveWord(),WBId);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
            dismiss();
        }
        else if(v==v.findViewById(R.id.icCancel)){
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
            dismiss();
        }
    }
}
