package com.devs.cnd.marvelousv.adapters;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.customview.RoundImage;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.objects.WordBoxG;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wunder on 9/16/17.
 */

public class AdapterRcWBG extends RecyclerView.Adapter<AdapterRcWBG.RcWBGViewHolder> {

    private int FR_WB_GLOBAL=3006;
    private MyApp myApp;

    private ArrayList<WordBoxG> listWBG = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    private ClickCallBackMain clickCallBack;

    private String currentSort="";
    private String SORT_AZ="wordbox/boxName",SORT_DL="downloadsCount",
            SORT_LK="likeCount",SORT_UP="wordbox/updatedAt",SORT_PS="wordbox/createBy";

    public AdapterRcWBG(Context context, ClickCallBackMain clickCallBack){
        this.context=context;
        this.clickCallBack=clickCallBack;
        inflater=LayoutInflater.from(context);


    }
    public void setListWBG(ArrayList<WordBoxG> listWBG){
        this.listWBG=listWBG;
        notifyDataSetChanged();
    }
    public void setCurrentSort(String currentSort){
        this.currentSort=currentSort;
    }

    private void setColorSort(RcWBGViewHolder holder){
        int colorActive= ResourcesCompat.getColor(context.getResources(),R.color.teal500,null);
        int colorDeactive= ResourcesCompat.getColor(context.getResources(),R.color.grey600,null);

        if(currentSort.equals(SORT_AZ)|| currentSort.equals(SORT_PS)){
            holder.txDownloads.setTextColor(colorDeactive);
            holder.icDownloads.setColorFilter(colorDeactive);
            holder.txLikes.setTextColor(colorDeactive);
            holder.icLikes.setColorFilter(colorDeactive);
            holder.txTime.setTextColor(colorDeactive);
            holder.icTime.setColorFilter(colorDeactive);
        }
        else if(currentSort.equals(SORT_DL)){
            holder.txDownloads.setTextColor(colorActive);
            holder.icDownloads.setColorFilter(colorActive);
            holder.txLikes.setTextColor(colorDeactive);
            holder.icLikes.setColorFilter(colorDeactive);
            holder.txTime.setTextColor(colorDeactive);
            holder.icTime.setColorFilter(colorDeactive);
        }
        else if(currentSort.equals(SORT_LK)){
            holder.txDownloads.setTextColor(colorDeactive);
            holder.icDownloads.setColorFilter(colorDeactive);
            holder.txLikes.setTextColor(colorActive);
            holder.icLikes.setColorFilter(colorActive);
            holder.txTime.setTextColor(colorDeactive);
            holder.icTime.setColorFilter(colorDeactive);
        }
        else if(currentSort.equals(SORT_UP)){
            holder.txDownloads.setTextColor(colorDeactive);
            holder.icDownloads.setColorFilter(colorDeactive);
            holder.txLikes.setTextColor(colorDeactive);
            holder.icLikes.setColorFilter(colorDeactive);
            holder.txTime.setTextColor(colorActive);
            holder.icTime.setColorFilter(colorActive);
        }
    }

    @Override
    public RcWBGViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_wb_global,parent,false);
        // RcMenuViewHolder holder = new RcMenuViewHolder(root);
        return new RcWBGViewHolder(root);    }

    @Override
    public void onBindViewHolder(RcWBGViewHolder holder, int position) {
        WordBoxG wbG = listWBG.get(position);

        holder.itemTitulo.setText(wbG.getBoxName());

        String crNm= wbG.getCreatorName();
        if(crNm.length()>10){
            crNm=crNm.substring(0,10)+"...";
        }
        holder.createBy.setText(crNm);

        String txW=wbG.getWords().size()+" Words";
        holder.txWords.setText(txW);

        String txDL=wbG.getDownloads().size()+" Downloads";
        holder.txDownloads.setText(txDL);

        String txLk=wbG.getLikes().size()+" Likes";
        holder.txLikes.setText(txLk);

        PrettyTime p = new PrettyTime();
        holder.txTime.setText(p.format(new Date(wbG.getUpdatedAt())));

        Picasso.with(context).load(wbG.getCreatorAvatar())
                .transform(new RoundImage()).into(holder.creatorAvatar);

        setColorSort(holder);
    }

    @Override
    public int getItemCount() {
        return listWBG.size();
    }

    public class RcWBGViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout linearLayout;
        ImageView creatorAvatar, icDownloads, icLikes, icTime;
        TextView txWords, txDownloads, txLikes, txTime, createBy, itemTitulo;
        public RcWBGViewHolder(View v) {
            super(v);
            linearLayout =(LinearLayout)v.findViewById(R.id.bodyLinear);
            creatorAvatar =(ImageView)v.findViewById(R.id.creatorAvatar);
            createBy =(TextView)v.findViewById(R.id.createBy);
            itemTitulo =(TextView)v.findViewById(R.id.itemTitulo);
            txWords =(TextView)v.findViewById(R.id.txWords);
            txDownloads =(TextView)v.findViewById(R.id.txDownloads);
            txLikes =(TextView)v.findViewById(R.id.txLikes);
            txTime =(TextView)v.findViewById(R.id.txTime);

            icDownloads=(ImageView)v.findViewById(R.id.icDownloads);
            icLikes=(ImageView)v.findViewById(R.id.icLikes);
            icTime=(ImageView)v.findViewById(R.id.icTime);

            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyLinear)){
                String id=listWBG.get(getAdapterPosition()).getId();
                String bn=listWBG.get(getAdapterPosition()).getBoxName();
                clickCallBack.onWordBoxGOpen(id,bn,FR_WB_GLOBAL);
            }
        }
    }
}
