package com.devs.cnd.marvelousv.adapters;

import android.content.Context;
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
import com.devs.cnd.marvelousv.events.ClickCallBack;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.objects.WordBoxG;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wunder on 9/13/17.
 */

public class AdapterRcWBF extends RecyclerView.Adapter<AdapterRcWBF.RcWBFViewHolder>{

    private int FR_WBG_FRIEND=3004;
    private MyApp myApp;

    private ArrayList<WordBoxG> listWBG = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    private ClickCallBackMain clickCallBack;

    public AdapterRcWBF(Context context, ClickCallBackMain clickCallBack){
        this.context=context;
        this.clickCallBack=clickCallBack;
        inflater=LayoutInflater.from(context);


    }
    public void setListWBG(ArrayList<WordBoxG> listWBG){
        this.listWBG=listWBG;
        notifyDataSetChanged();
    }

    @Override
    public RcWBFViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_wb_global,parent,false);
        // RcMenuViewHolder holder = new RcMenuViewHolder(root);
        return new RcWBFViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RcWBFViewHolder holder, int position) {
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

    }

    @Override
    public int getItemCount() {
        return listWBG.size();
    }
    //private ArrayList<FriendRq> listFriendRq= new ArrayList<>();

    public class RcWBFViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout linearLayout;
        ImageView creatorAvatar;
        TextView txWords, txDownloads, txLikes, txTime, createBy, itemTitulo;
        public RcWBFViewHolder(View v) {
            super(v);
            linearLayout =(LinearLayout)v.findViewById(R.id.bodyLinear);
            creatorAvatar =(ImageView)v.findViewById(R.id.creatorAvatar);
            createBy =(TextView)v.findViewById(R.id.createBy);
            itemTitulo =(TextView)v.findViewById(R.id.itemTitulo);
            txWords =(TextView)v.findViewById(R.id.txWords);
            txDownloads =(TextView)v.findViewById(R.id.txDownloads);
            txLikes =(TextView)v.findViewById(R.id.txLikes);
            txTime =(TextView)v.findViewById(R.id.txTime);

            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyLinear)){
                String id=listWBG.get(getAdapterPosition()).getId();
                String bn=listWBG.get(getAdapterPosition()).getBoxName();
                clickCallBack.onWordBoxGOpen(id,bn,FR_WBG_FRIEND);
            }
        }
    }
}
