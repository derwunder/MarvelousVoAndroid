package com.devs.cnd.marvelousv.adapters.words;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.objects.Tag;

import java.util.ArrayList;

/**
 * Created by wunder on 7/19/17.
 */

public class AdapLtTag extends RecyclerView.Adapter<AdapLtTag.ALTViewHolder>{

    private ArrayList<Tag> listTag= new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public AdapLtTag(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    public void setListTag(ArrayList<Tag> listTag){
        this.listTag=listTag;
        notifyDataSetChanged();
    }

    @Override
    public ALTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_lt_tag,parent,false);
        return new ALTViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ALTViewHolder holder, int position) {
        Tag t = listTag.get(position);

        holder.txTag.setText(t.getTag());
    }

    @Override
    public int getItemCount() {
        return listTag.size();
    }

    public class ALTViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearBody;
        TextView txTag;
        public ALTViewHolder(View v) {
            super(v);
            linearBody=(LinearLayout)v.findViewById(R.id.linearBody);
            txTag=(TextView)v.findViewById(R.id.txTag);
        }

    }
}
