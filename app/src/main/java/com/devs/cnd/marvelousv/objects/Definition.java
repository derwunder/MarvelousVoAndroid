package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 7/15/17.
 */

public class Definition {
    private String def;
    private String tp;
    public Definition(){

    }
    public Definition(String def, String tp){
       this.def=def;this.tp=tp;
    }

    public String getDef(){return def;}
    public String getTp(){return tp;}

    public void setDef(String def){this.def=def;}
    public void setTp(String tp){this.tp=tp;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("def", def);
        result.put("tp", tp);
        return result;
    }
}
