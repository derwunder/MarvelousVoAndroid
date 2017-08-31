package com.devs.cnd.marvelousv.adapters.words;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.objects.Definition;

import java.util.ArrayList;

/**
 * Created by wunder on 7/23/17.
 */

public class AdaptLtDefEd extends ArrayAdapter<Definition> {

    private ArrayList<Definition> listDef;
    Context mContext;
    private int lastPosition = -1;

    private LinearLayout LayoutEditor;
    private TextView txDef;
    private EditText edTxTp, edTxDef;
    private ImageButton icRemove;


    public AdaptLtDefEd(ArrayList<Definition> listDef, Context context) {
        super(context, R.layout.row_lt_def, listDef);
        this.listDef = listDef;
        this.mContext=context;

    }

    public void setListDef(ArrayList<Definition> listDef){
        this.listDef=listDef;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Definition Def = listDef.get(position);

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.row_lt_def, null);
        }


        LayoutEditor=(LinearLayout)v.findViewById(R.id.LayoutEditor);
        txDef=(TextView)v.findViewById(R.id.txDef);
        edTxTp=(EditText)v.findViewById(R.id.edTxTp);
        edTxDef=(EditText)v.findViewById(R.id.edTxDef);
        icRemove=(ImageButton) v.findViewById(R.id.icRemove);

        edTxTp.setText(listDef.get(position).getTp());
        edTxDef.setText(listDef.get(position).getDef());

        icRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDef(position);
            }
        });
        edTxDef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                listDef.get(position).setDef(s.toString());
            }
        });
        edTxTp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                listDef.get(position).setTp(s.toString());
            }
        });


        LayoutEditor.setVisibility(View.VISIBLE);
        txDef.setVisibility(View.GONE);


        edTxTp.setHint("Tp");
        edTxDef.setHint("Definition");
        return v;//super.getView(position, convertView, parent);
    }

    public void removeDef(int position){
        listDef.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public void add(@Nullable Definition object) {
        listDef.add(object);
        notifyDataSetChanged();
    }
    @Override
    public void remove(Definition object) {
        listDef.remove(object);
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public Definition getItem(int position) {
        return super.getItem(position);
    }

    public ArrayList<Definition> getListDef() {
        return listDef;
    }
}
