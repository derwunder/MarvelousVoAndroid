package com.devs.cnd.marvelousv.adapters.words;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.objects.Definition;

import java.util.ArrayList;

/**
 * Created by wunder on 7/19/17.
 */

public class AdapLtDef extends ArrayAdapter<Definition> {

    private ArrayList<Definition> listDef;
    Context mContext;
    private int lastPosition = -1;


    public AdapLtDef(ArrayList<Definition> listDef, Context context) {
        super(context, R.layout.row_lt_def, listDef);
        this.listDef = listDef;
        this.mContext=context;

    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        Definition Def = getItem(position);

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.row_lt_def, null);
        }
        if (Def != null) {
            TextView txDef = (TextView) v.findViewById(R.id.txDef);

            if (txDef != null) {
                String source = "<b>"+Def.getTp()+": </b>"+Def.getDef();
                if(Def.getDef().equals(""))
                    txDef.setText("No Definition");
                txDef.setText(Html.fromHtml(source));
            }
        }
        return v;//super.getView(position, convertView, parent);
    }
}
