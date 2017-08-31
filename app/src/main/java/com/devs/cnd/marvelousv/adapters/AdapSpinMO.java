package com.devs.cnd.marvelousv.adapters;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.acts.ActMain;

import java.util.ArrayList;

/**
 * Created by wunder on 7/22/17.
 */

public class AdapSpinMO extends BaseAdapter {

    Context context;
    ArrayList<String> listOp;

    public AdapSpinMO(Context context, ArrayList<String> listOp) {
        super();
        this.context = context;
        this.listOp = listOp;
    }

    @Override
    public int getCount() {
         return listOp.size();
    }

    @Override
    public Object getItem(int position) {
        return listOp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;//=LayoutInflater.from(context);
        inflater=((ActMain)context).getLayoutInflater();

        View row = inflater.inflate(R.layout.spinner_row_more_opc, parent, false);

        TextView itemName=(TextView)row.findViewById(R.id.tx_row);
        ImageView imgSpinner=(ImageView)row.findViewById(R.id.ic_row);

        itemName.setText(listOp.get(position));

        if(position==0){
         itemName.setVisibility(View.GONE);
            imgSpinner.setVisibility(View.GONE);
        }
        if(position==1){
            imgSpinner.setImageResource(R.drawable.ic_favorite_white_24dp);
            imgSpinner.setColorFilter
                    (ResourcesCompat.getColor(context.getResources(),R.color.colorBgGris,null));
        }else if(position==2){
            imgSpinner.setImageResource(R.drawable.ic_language_white_24dp);
            imgSpinner.setColorFilter
                    (ResourcesCompat.getColor(context.getResources(),R.color.colorBgGris,null));
        }else if(position==3){
            imgSpinner.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            imgSpinner.setColorFilter
                    (ResourcesCompat.getColor(context.getResources(),R.color.colorBgGris,null));
        }else if(position==4){
            imgSpinner.setImageResource(R.drawable.ic_delete_black_24dp);
            imgSpinner.setColorFilter
                    (ResourcesCompat.getColor(context.getResources(),R.color.colorBgGris,null));
        }



        return row;
    }
}
