package com.devs.cnd.marvelousv.adapters.words;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.objects.Tag;

import java.util.ArrayList;

/**
 * Created by wunder on 7/25/17.
 */

public class AdaptLtTagEd extends RecyclerView.Adapter<AdaptLtTagEd.ALTEViewHolder>{
    private ArrayList<Tag> listTag= new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public AdaptLtTagEd(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    public void setListTag(ArrayList<Tag> listTag){
        this.listTag=listTag;
        notifyDataSetChanged();
    }

    public void addTag(String strTag){
        listTag.add(new Tag(strTag));
        notifyDataSetChanged();
    }

    public void removeTag(int inx){
        listTag.remove(inx);
        notifyDataSetChanged();
    }

    public ArrayList<Tag> getListTag(){
        return listTag;
    }

    @Override
    public AdaptLtTagEd.ALTEViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_lt_tag,parent,false);
        return new AdaptLtTagEd.ALTEViewHolder(root);
    }

    @Override
    public void onBindViewHolder(AdaptLtTagEd.ALTEViewHolder holder, int position) {
        Tag t = listTag.get(position);

        holder.txTag.setText(t.getTag());
        holder.icRemove.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return listTag.size();
    }

    public class ALTEViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearBody;
        TextView txTag;
        ImageView icRemove;
        public ALTEViewHolder(View v) {
            super(v);
            linearBody=(LinearLayout)v.findViewById(R.id.linearBody);
            txTag=(TextView)v.findViewById(R.id.txTag);
            icRemove=(ImageView)v.findViewById(R.id.icRemove);

            icRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeTag(getAdapterPosition());
                }
            });
        }

    }
}
