package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 9/13/17.
 */

public class Maker {

    private String makerAvatar,makerName,makerUID;
    public Maker(){ }
    public Maker(String makerAvatar, String makerName, String makerUID){
        this.makerAvatar=makerAvatar;
        this.makerName=makerName;
        this.makerUID=makerUID;
    }

    public String getMakerAvatar(){return makerAvatar;}
    public String getMakerName(){return makerName;}
    public String getMakerUID(){return makerUID;}

    public void setMakerAvatar(String makerAvatar){this.makerAvatar=makerAvatar;}
    public void setMakerName(String makerName){this.makerName=makerName;}
    public void setMakerUID(String makerUID){this.makerUID=makerUID;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("makerAvatar", makerAvatar);
        result.put("makerName", makerName);
        result.put("makerUID", makerUID);

        //result.put("stars", stars);

        return result;
    }

}
