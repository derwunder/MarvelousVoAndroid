package com.devs.cnd.marvelousv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.customview.RoundImage;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.objects.Friend;
import com.devs.cnd.marvelousv.objects.FriendRq;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wunder on 9/12/17.
 */

public class AdapterRcFLRq extends RecyclerView.Adapter<AdapterRcFLRq.RcFLRqViewHolder> {

    private LayoutInflater inflater;
    private Context context;

    private ClickCallBackMain clickCallBack;
    private ArrayList<FriendRq> listFriendRq= new ArrayList<>();

    public AdapterRcFLRq(Context context,ClickCallBackMain clickCallBack){
        //myApp=(MyApp)context.getApplicationContext();

        inflater=LayoutInflater.from(context);
        this.context=context;
        this.clickCallBack=clickCallBack;
    }
    public void setFriendRqlist(ArrayList<FriendRq> listFriendRq){
        this.listFriendRq=listFriendRq;
        notifyDataSetChanged();
    }

    @Override
    public RcFLRqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_friendrqlist,parent,false);
        // RcMenuViewHolder holder = new RcMenuViewHolder(root);
        return new RcFLRqViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RcFLRqViewHolder holder, int position) {
        FriendRq rq = listFriendRq.get(position);
        PrettyTime p = new PrettyTime();

        holder.reqUName.setText(rq.getReqUName());
        holder.reqUEmail.setText(rq.getReqUEmail());
        holder.reqUTime.setText(p.format(new Date(rq.getReqUTime())));

        Picasso.with(context).load(rq.getReqUPhoto()).transform(new RoundImage()).into(holder.reqUPhoto);

    }

    @Override
    public int getItemCount() {
        return listFriendRq.size();
    }

    public class RcFLRqViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        ImageView reqUPhoto;
        TextView reqUName, reqUEmail, reqUTime;
        Button btConfirm, btDelete;
        public RcFLRqViewHolder(View v) {
            super(v);
            linearLayout =(LinearLayout)v.findViewById(R.id.bodyLinear);
            reqUPhoto =(ImageView)v.findViewById(R.id.reqUPhoto);
            reqUName =(TextView)v.findViewById(R.id.reqUName);
            reqUEmail =(TextView)v.findViewById(R.id.reqUEmail);
            reqUTime =(TextView)v.findViewById(R.id.reqUTime);
            btConfirm=(Button)v.findViewById(R.id.btConfirm);
            btDelete=(Button)v.findViewById(R.id.btDelete);

        }
    }
}
