package com.devs.cnd.marvelousv.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.objects.WordBox;

/**
 * Created by wunder on 7/22/17.
 */

public class DialogWBDelete extends DialogFragment {

    private MyApp myApp;
    private WordBox wb;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        myApp=(MyApp)getContext().getApplicationContext();
        String source =
        "<p>Are you sure that you want to delete the word box <strong>"+wb.getBoxName()+"?</strong></p>"+
      "<p>This will erase all Global share , Friend share, and the content of the box.</p>"+
      "<p>Once done, <strong>it cannot be undo.</strong></p>";


        builder.setTitle("Delete WordBox")
                .setMessage(Html.fromHtml(source))
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //clickCallBack.onLogInDialogSet(editTextCodigo.getText().toString());
                        myApp.wordboxes.deleteWBFB(wb);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create(); //super.onCreateDialog(savedInstanceState);
    }
    public void setWordBox(WordBox wb){
        this.wb=wb;
    }
}
