package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 7/15/17.
 */

public class Translation {
    private String lan;
    private String tr;
    public Translation(){

    }
    public Translation(String lan, String tr){
        this.lan = lan;this.tr = tr;
    }

    public String getLan(){return lan;}
    public String getTr(){return tr;}

    public void setLan(String lan){this.lan=lan;}
    public void setTr(String tr){this.tr=tr;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lan", lan);
        result.put("tr", tr);
        return result;
    }
}
