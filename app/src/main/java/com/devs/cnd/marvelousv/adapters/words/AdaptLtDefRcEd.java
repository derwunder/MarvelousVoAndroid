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
import com.devs.cnd.marvelousv.objects.Definition;
import com.devs.cnd.marvelousv.objects.Translation;

import java.util.ArrayList;

/**
 * Created by wunder on 9/3/17.
 */

public class AdaptLtDefRcEd extends RecyclerView.Adapter<AdaptLtDefRcEd.ALTDViewHolder>{

    private ArrayList<Definition> listDefs = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public AdaptLtDefRcEd(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    public void setListDefs(ArrayList<Definition> listDefs){
        this.listDefs = listDefs;
        notifyDataSetChanged();
    }

    public void addDef(){
        listDefs.add(new Definition("",""));
        notifyDataSetChanged();
    }

    public void removeTrans(int inx){
        listDefs.remove(inx);
        notifyDataSetChanged();
    }

    public ArrayList<Definition> getListDefs(){
        return listDefs;
    }
    @Override
    public ALTDViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_lt_def,parent,false);
        return new AdaptLtDefRcEd.ALTDViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ALTDViewHolder holder, int position) {
        holder.txDef.setVisibility(View.GONE);
        holder.linearBody.setVisibility(View.VISIBLE);

        holder.edTxTp.setText(listDefs.get(position).getTp());
        holder.edTxDef.setText(listDefs.get(position).getDef());
    }

    @Override
    public int getItemCount() {
        return listDefs.size();
    }

    public class ALTDViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearBody;
        TextView txDef;
        EditText edTxTp, edTxDef;
        ImageView icRemove;
        public ALTDViewHolder(View v) {
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
                    listDefs.get(getAdapterPosition()).setTp(s.toString());
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
                    listDefs.get(getAdapterPosition()).setDef(s.toString());
                }
            });
        }

    }

}
