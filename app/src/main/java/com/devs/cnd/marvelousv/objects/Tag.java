package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 7/15/17.
 */

public class Tag {
    private String tag;
    public Tag(){

    }
    public Tag(String tag){
        this.tag = tag;
    }

    public String getTag(){return tag;}
    public void setTag(String tag){this.tag=tag;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tag", tag);
        return result;
    }
}

