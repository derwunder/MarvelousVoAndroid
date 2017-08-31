package com.devs.cnd.marvelousv.adapters.words;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.objects.Definition;
import com.devs.cnd.marvelousv.objects.Translation;

import java.util.ArrayList;

/**
 * Created by wunder on 7/19/17.
 */

public class AdapLtTrans extends ArrayAdapter<Translation> {

    private ArrayList<Translation> listTrans;
    Context mContext;
    private int lastPosition = -1;


    public AdapLtTrans(ArrayList<Translation> listTrans, Context context) {
        super(context, R.layout.row_lt_def, listTrans);
        this.listTrans = listTrans;
        this.mContext=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Translation Trans = getItem(position);

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.row_lt_def, null);
        }
        if (Trans != null) {
            TextView txDef = (TextView) v.findViewById(R.id.txDef);

            if (txDef != null) {
                String source = "<b>"+Trans.getLan()+": </b>"+Trans.getTr();
                if(Trans.getTr().equals(""))
                    txDef.setText("No Translation");
                txDef.setText(Html.fromHtml(source));
            }
        }
        return v;//super.getView(position, convertView, parent);
    }
}
