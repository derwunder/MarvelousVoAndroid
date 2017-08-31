package com.devs.cnd.marvelousv.adapters.words;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.objects.Translation;

import java.util.ArrayList;

/**
 * Created by wunder on 7/23/17.
 */

public class AdaptLtTransEd extends ArrayAdapter<Translation> {

    private ArrayList<Translation> listTrans;
    Context mContext;
    private int lastPosition = -1;


    View v;
    private LinearLayout LayoutEditor;
    private TextView txDef;
    private EditText edTxTp, edTxDef;
    private ImageButton icRemove;

    public int currentLineCount=1;



    public AdaptLtTransEd(ArrayList<Translation> listTrans, Context context) {
        super(context, R.layout.row_lt_def, listTrans);
        this.listTrans = listTrans;
        this.mContext=context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Translation Trans = listTrans.get(position);

        v = convertView;
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

        edTxTp.setText(listTrans.get(position).getLan());
        edTxDef.setText(listTrans.get(position).getTr());

        icRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTrans(position);
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
                listTrans.get(position).setTr(s.toString());
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
                listTrans.get(position).setLan(s.toString());
            }
        });


        LayoutEditor.setVisibility(View.VISIBLE);
        txDef.setVisibility(View.GONE);


        edTxTp.setHint("Lan");
        edTxDef.setHint("Translation");
        return v;//super.getView(position, convertView, parent);
    }

    private int lineStringCalc(int strLength){
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);

        /*    SCREEN DENSITY
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int screenDensity = metrics.densityDpi;*/

        Display display =wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        //int height = size.y;



        Resources r = getContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18*(4+strLength), r.getDisplayMetrics());

        return Math.round(px/width);// Math.round(width/px);
    }

    public void removeTrans(int position){
        listTrans.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public void add(@Nullable Translation object) {
        listTrans.add(object);
        notifyDataSetChanged();
    }


    @Nullable
    @Override
    public Translation getItem(int position) {
        return super.getItem(position);
    }

    public ArrayList<Translation> getListTrans() {
        return listTrans;
    }
}
