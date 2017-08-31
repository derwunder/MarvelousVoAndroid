package com.devs.cnd.marvelousv.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;

import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;

/**
 * Created by wunder on 7/26/17.
 */

public class DialogWBWDelete extends DialogFragment {
    private MyApp myApp;
    private Word w;
    private String WBID="";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        myApp=(MyApp)getContext().getApplicationContext();
        String source =
                "<p>Are you sure that you want to delete the word term <strong>"+ w.getWordTerm()+"?</strong></p>"+
                        "<p>This will erase all Global share , Friend share, and the content of the box.</p>"+
                        "<p>Once done, <strong>it cannot be undo.</strong></p>";


        builder.setTitle("Delete Word Term")
                .setMessage(Html.fromHtml(source))
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //clickCallBack.onLogInDialogSet(editTextCodigo.getText().toString());
                        myApp.wordboxes.deleteWBWFB(w,WBID);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create(); //super.onCreateDialog(savedInstanceState);
    }
    public void setWBWord(Word w, String WBID){
        this.w =w; this.WBID=WBID;
    }
}
