package com.devs.cnd.marvelousv.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.acts.ActMain;
import com.devs.cnd.marvelousv.adapters.words.AdapLtDef;
import com.devs.cnd.marvelousv.adapters.words.AdapLtTag;
import com.devs.cnd.marvelousv.adapters.words.AdapLtTrans;
import com.devs.cnd.marvelousv.customview.NonScrollListView;
import com.devs.cnd.marvelousv.dialogs.DialogWBEdit;
import com.devs.cnd.marvelousv.dialogs.DialogWBWDelete;
import com.devs.cnd.marvelousv.dialogs.DialogWBWEdit;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.objects.Definition;
import com.devs.cnd.marvelousv.objects.Tag;
import com.devs.cnd.marvelousv.objects.Translation;
import com.devs.cnd.marvelousv.objects.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by wunder on 7/16/17.
 */

public class AdapterRcWBW extends RecyclerView.Adapter<AdapterRcWBW.RcWBWViewHolder>{


    private ArrayList<Word> listWord = new ArrayList<>();
    private ArrayList<Boolean> listViewActive = new ArrayList<>();

    private LayoutInflater inflater;

    private ClickCallBackMain clickCallBack;
    private Context context;

    private String WBID="";


    public AdapterRcWBW(Context context,ClickCallBackMain clickCallBack){
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.clickCallBack=clickCallBack;


    }
    public void setWBID(String WBID){
        this.WBID=WBID;
    }
    @Override
    public RcWBWViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_wb_words,parent,false);
        // RcMenuViewHolder holder = new RcMenuViewHolder(root);
        return new RcWBWViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RcWBWViewHolder holder, int position) {
        Word w = listWord.get(position);
        boolean viewActive = listViewActive.get(position);


        holder.itemTitulo.setText(w.getWordTerm());
        holder.txFirstDef.setVisibility(View.GONE);

        if(!w.getDefinitions().isEmpty()){
            Definition def= w.getDefinitions().get(0);
            String source = "<b>"+def.getTp()+": </b>"+def.getDef();
            holder.txFirstDef.setText(Html.fromHtml(source));

            if(w.getDefinitions().get(0).getDef().equals("")){
                holder.txFirstDef.setText("No Definition");
            }
        }

        if(w.getBookmark()){
            holder.icBookmar.setColorFilter(
                    ResourcesCompat.getColor(context.getResources(),R.color.teal600,null));
        }else{
            holder.icBookmar.setColorFilter(
                    ResourcesCompat.getColor(context.getResources(),R.color.colorBgGris,null));
        }

        seeViewActive(viewActive,holder,w);




    }

    @Override
    public int getItemCount() {
        return listWord.size();
    }




    //view Holder Loads Functions
    public void seeViewActive(boolean viewActive, RcWBWViewHolder holder, Word w){
        if(viewActive){
            holder.txFirstDef.setVisibility(View.GONE);
            holder.LayoutDef.setVisibility(View.VISIBLE);
            holder.LayoutTrans.setVisibility(View.VISIBLE);
            holder.LayoutTag.setVisibility(View.VISIBLE);
            holder.icEdit.setVisibility(View.VISIBLE);
            holder.icDelete.setVisibility(View.VISIBLE);
            holder.icShow.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_arrow_drop_up_black_24dp));

            setDefinitions(holder,w);
            setTranslations(holder,w);
            setTags(holder,w);

        }
        else{
            holder.txFirstDef.setVisibility(View.VISIBLE);
            holder.LayoutDef.setVisibility(View.GONE);
            holder.LayoutTrans.setVisibility(View.GONE);
            holder.LayoutTag.setVisibility(View.GONE);
            holder.icEdit.setVisibility(View.GONE);
            holder.icDelete.setVisibility(View.GONE);
            holder.icShow.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_arrow_drop_down_black_24dp));


        }
    }
    public void setDefinitions(RcWBWViewHolder holder, Word w){
        if(w.getDefinitions().size()>1){
            holder.listDefs.setVisibility(View.VISIBLE);
            AdapLtDef adapLtDef= new AdapLtDef(w.getDefinitions(),context);
            holder.listDefs.setAdapter(adapLtDef);
        }
        else if(w.getDefinitions().size()==1){
            if(w.getDefinitions().get(0).getDef().equals("")){
                holder.LayoutDef.setVisibility(View.GONE);
            }else{
                holder.listDefs.setVisibility(View.VISIBLE);
                AdapLtDef adapLtDef= new AdapLtDef(w.getDefinitions(),context);
                holder.listDefs.setAdapter(adapLtDef);
            }
        }
    }
    public void setTranslations(RcWBWViewHolder holder, Word w){
        if(w.getTranslations().size()>1){
            holder.listTrans.setVisibility(View.VISIBLE);
            AdapLtTrans adapLtTrans = new AdapLtTrans(w.getTranslations(),context);
            holder.listTrans.setAdapter(adapLtTrans);
        }
        else if(w.getTranslations().size()==1){
            if(w.getTranslations().get(0).getTr().equals("")){
                holder.LayoutTrans.setVisibility(View.GONE);
            }else{
                holder.listTrans.setVisibility(View.VISIBLE);
                AdapLtTrans adapLtTrans = new AdapLtTrans(w.getTranslations(),context);
                holder.listTrans.setAdapter(adapLtTrans);
            }
        }
    }
    public void setTags(RcWBWViewHolder holder, Word w){
        if(w.getTags().size()>1){
            holder.listTags.setVisibility(View.VISIBLE);
            holder.adapLtTag.setListTag(w.getTags());
        }
        else if(w.getTags().size()==1){
            if(w.getTags().get(0).getTag().equals("")){
                holder.LayoutTag.setVisibility(View.GONE);
            }else{
                holder.listTags.setVisibility(View.VISIBLE);
                holder.adapLtTag.setListTag(w.getTags());
            }
        }
    }

    public void setWordList(ArrayList<Word> listWord){

        ArrayList<Boolean> listViewActive = new ArrayList<>();
        for (int i=0;i<listWord.size();i++){
            listViewActive.add(false);
        }
        this.listWord =listWord;
        this.listViewActive=listViewActive;
        //notifyItemChanged(0,listWord.size());
        notifyDataSetChanged();
    }

    public class RcWBWViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout linearLayout, LayoutDef, LayoutTrans,LayoutTag;
        LinearLayout LayoutTagsPar, LayoutTagsImpar;
        ImageView itemImage, icBookmar, icEdit, icDelete,icShow;
        TextView itemTitulo,  txFirstDef,txWords;

        RecyclerView   listTags;



        NonScrollListView listDefs,listTrans;
        AdapLtTag adapLtTag;



        public RcWBWViewHolder(View v) {
            super(v);

            linearLayout =(LinearLayout) itemView.findViewById(R.id.bodyLinear);
            LayoutDef=(LinearLayout)itemView.findViewById(R.id.LayoutDef);
            LayoutTrans=(LinearLayout)itemView.findViewById(R.id.LayoutTrans);
            LayoutTag=(LinearLayout)itemView.findViewById(R.id.LayoutTag);

            LayoutTagsPar=(LinearLayout)itemView.findViewById(R.id.LayoutTagsPar);
            LayoutTagsImpar=(LinearLayout)itemView.findViewById(R.id.LayoutTagsImpar);

            itemImage=(ImageView)itemView.findViewById(R.id.itemImagen);
            itemTitulo=(TextView)itemView.findViewById(R.id.itemTitulo);
            txFirstDef=(TextView)itemView.findViewById(R.id.txFirstDef);

            // List Load
            listTags=(RecyclerView) itemView.findViewById(R.id.listTags);

            listDefs=(NonScrollListView)itemView.findViewById(R.id.listDefs);
            listTrans=(NonScrollListView) itemView.findViewById(R.id.listTrans);

            //Menu vars
            icBookmar=(ImageView)itemView.findViewById(R.id.icBookmark);
            icEdit=(ImageView)itemView.findViewById(R.id.icEdit);
            icDelete=(ImageView)itemView.findViewById(R.id.icDelete);
            icShow=(ImageView)itemView.findViewById(R.id.icShow);


            adapLtTag = new AdapLtTag(context);
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
            listTags.setLayoutManager(staggeredGridLayoutManager);
            listTags.setAdapter(adapLtTag);

            linearLayout.setOnClickListener(this);
            icDelete.setOnClickListener(this);
            icEdit.setOnClickListener(this);


            //definition = new TextView(context);
        }


        @Override
        public void onClick(View v) {
            if(v == v.findViewById(R.id.bodyLinear)){
                boolean viewActive;
                viewActive=listViewActive.get(getAdapterPosition());
                if(viewActive){
                    listViewActive.set(getAdapterPosition(),false);
                }
                else if(!viewActive){
                    listViewActive.set(getAdapterPosition(),true);
                }
                notifyItemChanged(getAdapterPosition());
            }
            if(v == v.findViewById(R.id.icDelete)){
                FragmentManager fragmentManager = ((ActMain)context).getSupportFragmentManager();
                DialogWBWDelete dialogWBWDelete =new DialogWBWDelete();
                dialogWBWDelete.setWBWord(listWord.get(getAdapterPosition()),WBID);

                dialogWBWDelete.show(fragmentManager, "dialog");
            }
            if(v==v.findViewById(R.id.icEdit)){
                FragmentManager fragmentManager = ((ActMain)context).getSupportFragmentManager();
                DialogWBWEdit dialogWBWEdit =new DialogWBWEdit();
                dialogWBWEdit.setWBId(WBID);
                dialogWBWEdit.setWord(listWord.get(getAdapterPosition()));

                //dialogWBEdit.show(fragmentManager, "dialog");
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(R.id.drawer_layout, dialogWBWEdit)
                      .addToBackStack(null).commit();
            }
        }
    }
}
