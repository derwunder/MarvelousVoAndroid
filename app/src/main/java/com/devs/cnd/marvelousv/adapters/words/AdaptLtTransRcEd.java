package com.devs.cnd.marvelousv.adapters.words;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.objects.Translation;

import java.util.ArrayList;

/**
 * Created by wunder on 9/3/17.
 */

public class AdaptLtTransRcEd extends RecyclerView.Adapter<AdaptLtTransRcEd.ALTTViewHolder>{
    private ArrayList<Translation> listTrans = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public AdaptLtTransRcEd(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    public void setListTrans(ArrayList<Translation> listTrans){
        this.listTrans=listTrans;
        notifyDataSetChanged();
    }

    public void addTrans(){
        listTrans.add(new Translation("",""));
        notifyDataSetChanged();
    }

    public void removeTrans(int inx){
        listTrans.remove(inx);
        notifyDataSetChanged();
    }

    public ArrayList<Translation> getListTrans(){
        return listTrans;
    }

    @Override
    public ALTTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_lt_def,parent,false);
        return new AdaptLtTransRcEd.ALTTViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ALTTViewHolder holder, int position) {
        holder.txDef.setVisibility(View.GONE);
        holder.linearBody.setVisibility(View.VISIBLE);

        holder.edTxTp.setText(listTrans.get(position).getLan());
        holder.edTxDef.setText(listTrans.get(position).getTr());
    }

    @Override
    public int getItemCount() {
        return listTrans.size();
    }

    public class ALTTViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearBody;
        TextView txDef;
        EditText edTxTp, edTxDef;
        ImageView icRemove;
        public ALTTViewHolder(View v) {
            super(v);
            linearBody=(LinearLayout)v.findViewById(R.id.LayoutEditor);
            txDef=(TextView)v.findViewById(R.id.txDef);
            edTxTp=(EditText)v.findViewById(R.id.edTxTp);
            edTxDef=(EditText)v.findViewById(R.id.edTxDef);
            icRemove=(ImageButton) v.findViewById(R.id.icRemove);


            icRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeTrans(getAdapterPosition());
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
                    listTrans.get(getAdapterPosition()).setLan(s.toString());
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
                    listTrans.get(getAdapterPosition()).setTr(s.toString());
                }
            });
        }

    }
}
