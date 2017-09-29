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
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.objects.Friend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by wunder on 9/11/17.
 */

public class AdapterRcFL extends RecyclerView.Adapter<AdapterRcFL.RcFLViewHolder>{

    private MyApp myApp;
    private LayoutInflater inflater;
    private Context context;

    private ClickCallBackMain clickCallBack;

    private ArrayList<Friend> listFriend= new ArrayList<>();

    public AdapterRcFL(Context context,ClickCallBackMain clickCallBack){
        myApp=(MyApp)context.getApplicationContext();

        inflater=LayoutInflater.from(context);
        this.context=context;
        this.clickCallBack=clickCallBack;
    }

    public void setFriendlist(ArrayList<Friend> listFriend){
        this.listFriend=listFriend;
        notifyDataSetChanged();
    }

    @Override
    public RcFLViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_friendlist,parent,false);
        // RcMenuViewHolder holder = new RcMenuViewHolder(root);
        return new RcFLViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RcFLViewHolder holder, int position) {
        Friend f = listFriend.get(position);

        holder.frName.setText(f.getFrName());
        holder.frEmail.setText(f.getFrEmail());

        Picasso.with(context).load(f.getFrPhoto()).transform(new RoundImage()).into(holder.frPhoto);


    }

    @Override
    public int getItemCount() {
        return listFriend.size();
    }


    public class RcFLViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout linearLayout;
        ImageView frPhoto;
        TextView frName, frEmail;
        public RcFLViewHolder(View v) {
            super(v);
            linearLayout =(LinearLayout)v.findViewById(R.id.bodyLinear);
            frPhoto=(ImageView)v.findViewById(R.id.frPhoto);
            frName=(TextView)v.findViewById(R.id.frName);
            frEmail=(TextView)v.findViewById(R.id.frEmail);

            linearLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyLinear)){
                clickCallBack.onFIDOpen(
                        listFriend.get(getAdapterPosition()).getId(),
                        listFriend.get(getAdapterPosition()).getFrName()
                );
            }
        }
    }
}
