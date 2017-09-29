package com.devs.cnd.marvelousv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.customview.RoundImage;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.objects.Comment;
import com.devs.cnd.marvelousv.objects.Reply;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wunder on 9/25/17.
 */

public class AdapterRcReply  extends RecyclerView.Adapter<AdapterRcReply.RcRpViewHolder>{


    private MyApp myApp;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private LayoutInflater inflater;
    private Context context;

    private String idWBG="", idCm="";

    private ClickCallBackMain clickCallBack;

    private ArrayList<Reply> listReply = new ArrayList<>();


    public AdapterRcReply(Context context,ClickCallBackMain clickCallBack, String idWBG){
        myApp=(MyApp)context.getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        inflater=LayoutInflater.from(context);
        this.context=context;
        this.clickCallBack=clickCallBack;
        this.idWBG=idWBG;
    }

    public void setReplylist(ArrayList<Reply> listReply){
        this.listReply =listReply;
        notifyDataSetChanged();
    }

    public void addReply(Reply reply){
        listReply.add(0,reply);
        notifyDataSetChanged();
    }

    public void setIdCm(String idCm){
        this.idCm=idCm;
    }

    @Override
    public AdapterRcReply.RcRpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_reply,parent,false);
        return new AdapterRcReply.RcRpViewHolder(root);
    }

    @Override
    public void onBindViewHolder(AdapterRcReply.RcRpViewHolder holder, int position) {
        Reply rp =  listReply.get(position);
        PrettyTime p = new PrettyTime();

        holder.replyUName.setText(rp.getReplyUName());
        holder.reply.setText(rp.getReply());
        holder.replyTime.setText(p.format(new Date(rp.getReplyTime())));

        Picasso.with(context)
                .load(rp.getReplyUAvatar())
                .transform(new RoundImage()).into(holder.replyUAvatar);

        if(!user.getUid().equals(rp.getReplyBy()))
            holder.moreOptions.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return listReply.size();
    }

    public class RcRpViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout linearLayout;
        ImageView replyUAvatar,moreOptions;
        TextView replyUName, replyTime, reply;

        Spinner moreOp;
        public RcRpViewHolder(View v) {
            super(v);
            linearLayout =(LinearLayout)v.findViewById(R.id.bodyLinear);
            replyUAvatar =(ImageView)v.findViewById(R.id.replyUAvatar);
            replyUName =(TextView)v.findViewById(R.id.replyUName);
            replyTime =(TextView)v.findViewById(R.id.replyTime);
            reply =(TextView)v.findViewById(R.id.reply);

            linearLayout.setOnClickListener(this);

            //More Option
            moreOptions=(ImageView)itemView.findViewById(R.id.moreOptions);
            moreOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreOp.performClick();
                }
            });

            ArrayList<String> categ= new ArrayList<>();
            categ.add("helper");
            categ.add("Delete Reply");

            moreOp=(Spinner)v.findViewById(R.id.moreOp);
            moreOp.setAdapter(new AdapSpinMOCm(context,categ));
            moreOp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(moreOp.getSelectedItemPosition()!=0) {

                        if(position==1){
                            myApp.wordboxesGF.deleteReply(idWBG,idCm,listReply.get(getAdapterPosition()).getId());
                            //myApp.wordboxes.updateWBFavFB(listWordBox.get(getAdapterPosition()));
                            //Toast.makeText(context,"Delete Reply: "+getAdapterPosition(),Toast.LENGTH_SHORT).show();
                        }
                        moreOp.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyLinear)){
               /* clickCallBack.onFIDOpen(
                        listReply.get(getAdapterPosition()).getId(),
                        listReply.get(getAdapterPosition()).getFrName()
                );*/
            }
        }
    }
}
