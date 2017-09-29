package com.devs.cnd.marvelousv.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.acts.ActMain;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.dialogs.DialogWBDelete;
import com.devs.cnd.marvelousv.dialogs.DialogWBEdit;
import com.devs.cnd.marvelousv.events.ClickCallBack;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.objects.Definition;
import com.devs.cnd.marvelousv.objects.Tag;
import com.devs.cnd.marvelousv.objects.Translation;
import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wunder on 7/14/17.
 */

public class AdapterRcWB extends RecyclerView.Adapter<AdapterRcWB.RcWBViewHolder>{

    private MyApp myApp;

    private ArrayList<String> listTitulo = new ArrayList<>();
    private ArrayList<WordBox> listWordBox = new ArrayList<>();
    private ArrayList<Boolean> listMoreOpSelected= new ArrayList<>();
    ArrayList<String> listmoreOp=new ArrayList<>(Arrays.asList("helper","Favorite","Global Post","Edit WordBox","Delete WordBox"));


    private LayoutInflater inflater;

    private ClickCallBackMain clickCallBack;
    private Context context;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public AdapterRcWB(Context context,ClickCallBackMain clickCallBack){
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.clickCallBack=clickCallBack;

        myApp=(MyApp)context.getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //wordboxesLoad();
        listTitulo.add("Titulo 1\n( ͡° ͜ʖ ͡°)");
        listTitulo.add("Titulo 2");
        listTitulo.add("Titulo 3");
        listTitulo.add("Titulo 4");

    }
    @Override
    public RcWBViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_wordboxes,parent,false);
        // RcMenuViewHolder holder = new RcMenuViewHolder(root);
        return new RcWBViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RcWBViewHolder holder, int position) {
        WordBox wB = listWordBox.get(position);
        int lastCheckedAtDay= dateGetDays(wB.getLastCheckedAt());
        PrettyTime p = new PrettyTime();
        //System.out.println(p.format(new Date()));

        holder.itemTitulo.setText(wB.getBoxName());
        holder.txTime.setText(p.format(new Date(wB.getLastCheckedAt())));
        holder.txWords.setText(wB.getWords().size()+" Words: ");

        if(wB.getFavorite())
            holder.icFavorite.setColorFilter
                        (ResourcesCompat.getColor(context.getResources(),R.color.colorWhite,null));
        else
            holder.icFavorite.setColorFilter
                        (ResourcesCompat.getColor(context.getResources(),R.color.Grey90_900,null));

        if(wB.getGBoard())
            holder.icGlobal.setColorFilter
                    (ResourcesCompat.getColor(context.getResources(),R.color.colorWhite,null));
        else
            holder.icGlobal.setColorFilter
                    (ResourcesCompat.getColor(context.getResources(),R.color.Grey90_900,null));


        if(lastCheckedAtDay<=7){
            holder.myLearning.setColorFilter
                    (ResourcesCompat.getColor(context.getResources(),R.color.teal600,null));
        }else if(lastCheckedAtDay>=8 && lastCheckedAtDay<=31){
            holder.myLearning.setColorFilter
                    (ResourcesCompat.getColor(context.getResources(),R.color.teal300,null));
        }




       // Toast.makeText(context,"Last checked At: "+lastCheckedAtDay,Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return listWordBox.size();
    }


    public int dateGetDays(long lastCheckedAt){
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        Calendar lastCheckedAtCal = Calendar.getInstance();
        lastCheckedAtCal.setTime(new Date(lastCheckedAt));
        int lastCheckedAtDay= lastCheckedAtCal.get((Calendar.DAY_OF_YEAR));


        return dayOfYear-lastCheckedAtDay;
    }

    public class RcWBViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout linearLayout;
        ImageView itemImage, myLearning, moreOptions, icFavorite,icGlobal;
        TextView itemTitulo,  txTime,txWords;

        Spinner moreOp;

        public RcWBViewHolder(View v) {
            super(v);

            linearLayout =(LinearLayout) itemView.findViewById(R.id.bodyLinear);
            itemImage=(ImageView)itemView.findViewById(R.id.itemImagen);
            itemTitulo=(TextView)itemView.findViewById(R.id.itemTitulo);
            myLearning=(ImageView)itemView.findViewById(R.id.myLearning);
            icFavorite=(ImageView)itemView.findViewById(R.id.icFavorite);
            icGlobal=(ImageView)itemView.findViewById(R.id.icGlobal);
            txTime=(TextView) itemView.findViewById(R.id.txTime);
            txWords=(TextView) itemView.findViewById(R.id.txWords);


            moreOptions=(ImageView)itemView.findViewById(R.id.moreOptions);
            moreOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreOp.performClick();
                }
            });
            moreOp=(Spinner)v.findViewById(R.id.moreOp);
            moreOp.setAdapter(new AdapSpinMO(context,listmoreOp));
            moreOp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(moreOp.getSelectedItemPosition()!=0) {
                        String wbn = listWordBox.get(getAdapterPosition()).getBoxName();
                        makeToas(position, wbn);

                        if(position==1){
                            myApp.wordboxes.updateWBFavFB(listWordBox.get(getAdapterPosition()));
                        }
                        if(position==2){
                            myApp.wordboxes.updateWBGBoardFB(listWordBox.get(getAdapterPosition()));
                        }
                        if(position==3){
                            FragmentManager fragmentManager = ((ActMain)context).getSupportFragmentManager();
                            DialogWBEdit dialogWBEdit =new DialogWBEdit();
                            dialogWBEdit.setWordBox(listWordBox.get(getAdapterPosition()));

                            dialogWBEdit.show(fragmentManager, "dialog");
                            /*FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.add(R.id.drawer_layout, dialogWBEdit)
                                    .addToBackStack(null).commit();*/
                        }
                        if(position==4){
                            DialogWBDelete dialogWBDelete= new DialogWBDelete();
                            dialogWBDelete.setWordBox(listWordBox.get(getAdapterPosition()));
                            dialogWBDelete.show(((ActMain)context).getSupportFragmentManager(),"Delete WB");
                        }
                        moreOp.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            linearLayout.setOnClickListener(this);
            myLearning.setOnClickListener(this);
        }

        public void makeToas(int position, String wbn){
            Toast.makeText(context,"WB: "+wbn+"\nPass: "+position,Toast.LENGTH_SHORT).show();

        }
        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyLinear)){
                clickCallBack.onWordBoxOpen(
                        listWordBox.get(getAdapterPosition()).getId()
                );
            }
            else if(v==v.findViewById(R.id.myLearning)){
                myApp.wordboxes.updateWBLastCheckedAtFB(listWordBox.get(getAdapterPosition()));
            }
        }


    }

    public void setWordBoxList(ArrayList<WordBox> listWordBox){
        this.listWordBox=listWordBox;
        ArrayList<Boolean> listMoreOpSelected= new ArrayList<>();
        for (int i=0;i<listWordBox.size();i++){
            listMoreOpSelected.add(false);
        }
        this.listMoreOpSelected=listMoreOpSelected;
        //notifyItemChanged(0,listWordBox.size());
        notifyDataSetChanged();
    }

}
