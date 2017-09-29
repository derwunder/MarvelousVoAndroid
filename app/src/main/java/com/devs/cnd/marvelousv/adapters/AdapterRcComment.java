package com.devs.cnd.marvelousv.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.acts.ActMain;
import com.devs.cnd.marvelousv.api.FilterAndSort;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.customview.RoundImage;
import com.devs.cnd.marvelousv.dialogs.DialogWBDelete;
import com.devs.cnd.marvelousv.dialogs.DialogWBEdit;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.objects.Comment;
import com.devs.cnd.marvelousv.objects.Reply;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wunder on 9/24/17.
 */

public class AdapterRcComment extends RecyclerView.Adapter<AdapterRcComment.RcCmViewHolder>{


    private MyApp myApp;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private LayoutInflater inflater;
    private Context context;
    InputMethodManager imm;


    private ClickCallBackMain clickCallBack;

    private ArrayList<Comment> listComment = new ArrayList<>();
    private ArrayList<Boolean> listReplyActive = new ArrayList<>();
    private String currentReply = "";

    private String idWBG="";

    private FilterAndSort filterAndSort;



    public AdapterRcComment(Context context,ClickCallBackMain clickCallBack){
        myApp=(MyApp)context.getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        filterAndSort= new FilterAndSort();

        inflater=LayoutInflater.from(context);
        this.context=context;
        this.clickCallBack=clickCallBack;
    }

    public void setCommentlist(ArrayList<Comment> listComment){
        ArrayList<Boolean> listReplyActive = new ArrayList<>();
        for (int i=0;i<listComment.size();i++){
            listReplyActive.add(false);
        }
        this.listReplyActive=listReplyActive;
        this.listComment =listComment;
        notifyDataSetChanged();
    }

    public void addComment(Comment comment){
        listComment.add(0,comment);
        listReplyActive.add(0,false);
        notifyDataSetChanged();
    }
    public void deleteComment(String idCm){
        for(int i=0; i<listComment.size();i++){
            if(listComment.get(i).getId().equals(idCm)){
                notifyItemRemoved(i);
                listComment.remove(i);
                listReplyActive.remove(i);
                notifyItemRangeChanged(i, getItemCount());

            }
        }
    }
    public void addReply(String idCm, Reply reply){
        for(int i=0;i<listComment.size();i++){
            if(listComment.get(i).getId().equals(idCm)){
                listComment.get(i).getReplys().add(0,reply);

            }
        }
        notifyDataSetChanged();
    }
    public void deleteReply(String idCm, String idRp){
        for(int i=0; i<listComment.size();i++){
            if(listComment.get(i).getId().equals(idCm)){
                for(int j=0;j<listComment.get(i).getReplys().size();j++){
                    if(listComment.get(i).getReplys().get(j).getId().equals(idRp)){
                        listComment.get(i).getReplys().remove(j);
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public void setIdWBG(String idWBG){
        this.idWBG=idWBG;
    }

    @Override
    public RcCmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_comments,parent,false);
        return new RcCmViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RcCmViewHolder holder, int position) {
        Comment cm =  listComment.get(position);
        boolean replyActive = listReplyActive.get(position);
        PrettyTime p = new PrettyTime();

        holder.commentUName.setText(cm.getCommentUName());
        holder.comment.setText(cm.getComment());
        holder.commentTime.setText(p.format(new Date(cm.getCommentTime())));

        Picasso.with(context)
                .load(cm.getCommentUAvatar())
                .transform(new RoundImage()).into(holder.commentUAvatar);

        holder.adapterRcReply.setReplylist(filterAndSort.Replys(cm.getReplys()));
        holder.adapterRcReply.setIdCm(cm.getId());


        if(replyActive)
            holder.replyBox.setVisibility(View.VISIBLE);
        else
            holder.replyBox.setVisibility(View.GONE);

        Picasso.with(context)
                .load(user.getPhotoUrl())
                .transform(new RoundImage()).into(holder.replyUAvatarBX);

        holder.UreplyET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                currentReply=s.toString();
            }
        });

        if(!user.getUid().equals(cm.getCommentBy()))
            holder.moreOptions.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    public class RcCmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout linearLayout;
        ImageView commentUAvatar,moreOptions;
        TextView commentUName, commentTime, comment;
        RecyclerView recycleReply;
        AdapterRcReply adapterRcReply;
        Spinner moreOp;

        LinearLayout replyBox;
        Button btReplyOn,btReply,btCancelRp;
        ImageView replyUAvatarBX;
        EditText UreplyET;


        public RcCmViewHolder(View v) {
            super(v);
            linearLayout =(LinearLayout)v.findViewById(R.id.bodyLinear);
            commentUAvatar =(ImageView)v.findViewById(R.id.commentUAvatar);
            commentUName =(TextView)v.findViewById(R.id.commentUName);
            commentTime=(TextView)v.findViewById(R.id.commentTime);
            comment=(TextView)v.findViewById(R.id.comment);

            recycleReply=(RecyclerView)v.findViewById(R.id.recycleReply);

            adapterRcReply=new AdapterRcReply(context,clickCallBack,idWBG);
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

            recycleReply.setLayoutManager(staggeredGridLayoutManager);
            recycleReply.setAdapter(adapterRcReply);

            linearLayout.setOnClickListener(this);

            //Reply Box INI
            replyBox=(LinearLayout)v.findViewById(R.id.replyBox);
            replyUAvatarBX=(ImageView)v.findViewById(R.id.replyUAvatarBX);
            UreplyET=(EditText)v.findViewById(R.id.UreplyET);
            btReplyOn=(Button)v.findViewById(R.id.btReplyOn);
            btReply=(Button)v.findViewById(R.id.btReply);
            btCancelRp=(Button)v.findViewById(R.id.btCancelRp);

            btReplyOn.setOnClickListener(this);
            btReply.setOnClickListener(this);
            btCancelRp.setOnClickListener(this);


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
            categ.add("Delete Comment");

            moreOp=(Spinner)v.findViewById(R.id.moreOp);
            moreOp.setAdapter(new AdapSpinMOCm(context,categ));
            moreOp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(moreOp.getSelectedItemPosition()!=0) {

                        if(position==1){
                            myApp.wordboxesGF.deleteComment(idWBG,listComment.get(getAdapterPosition()).getId());
                            //myApp.wordboxes.updateWBFavFB(listWordBox.get(getAdapterPosition()));
                            //Toast.makeText(context,"Delete comment: "+getAdapterPosition(),Toast.LENGTH_SHORT).show();
                        }
                        moreOp.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        private void rpActivation(){
            boolean replyActive;
            replyActive=listReplyActive.get(getAdapterPosition());
            if(replyActive){
                listReplyActive.set(getAdapterPosition(),false);
            }
            else if(!replyActive){
                listReplyActive.set(getAdapterPosition(),true);
            }
            notifyItemChanged(getAdapterPosition());
        }
        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyLinear)){
               /* clickCallBack.onFIDOpen(
                        listComment.get(getAdapterPosition()).getId(),
                        listComment.get(getAdapterPosition()).getFrName()
                );*/
            }
            else if(v==v.findViewById(R.id.btReplyOn)){
                rpActivation();
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            else if(v==v.findViewById(R.id.btCancelRp)){
                rpActivation();
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            else if(v==v.findViewById(R.id.btReply)){
                String idCm= listComment.get(getAdapterPosition()).getId();
                myApp.wordboxesGF.postReply(idWBG,idCm,currentReply);
                currentReply="";
                rpActivation();
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }

}
