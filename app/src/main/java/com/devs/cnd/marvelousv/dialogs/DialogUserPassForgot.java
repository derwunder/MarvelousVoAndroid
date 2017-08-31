package com.devs.cnd.marvelousv.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.acts.ActLaunch;
import com.devs.cnd.marvelousv.events.ClickCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

/**
 * Created by wunder on 7/13/17.
 */

public class DialogUserPassForgot extends DialogFragment {

    private ClickCallBack clickCallBack;
    private LayoutInflater inflater;
    private EditText editTxEmail;
    private ImageView imgApp;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_user_pass_forgot,null);

        // View Vars
        imgApp=(ImageView)view.findViewById(R.id.imgApp);
        editTxEmail=(EditText)view.findViewById(R.id.editTxEmail);

        builder.setTitle(R.string.UPassForgotTittle)
                .setView(view)
                .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //clickCallBack.onLogInDialogSet(editTextCodigo.getText().toString());
                        if(validateForm()){

                            clickCallBack.onDialogEmailPassResetSet(editTxEmail.getText().toString());

                        }else{
                            Toast.makeText(getActivity(), "Wrong Email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = editTxEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTxEmail.setError("Required.");
            valid = false;
        } else {
            editTxEmail.setError(null);
        }


        return valid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickCallBack=(ClickCallBack)context;
    }
}
