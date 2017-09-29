package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 9/11/17.
 */

public class Friend {

    private String id;
    private String frName;
    private String frEmail;
    private String frPhoto;

    public Friend(){}
    public Friend(String id, String frName, String frEmail, String frPhoto){
        this.id=id;
        this.frName=frName;
        this.frEmail=frEmail;
        this.frPhoto=frPhoto;
    }

    public String getId(){return id;}
    public String getFrName(){return  frName;}
    public String getFrEmail(){return  frEmail;}
    public String getFrPhoto(){return  frPhoto;}

    public void setId(String id){this.id=id;}
    public void setFrName(String frName){this.frName=frName;}
    public void setFrEmail(String frEmail){this.frEmail=frEmail;}
    public void setFrPhoto(String frPhoto){this.frPhoto=frPhoto;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("frName", frName);
        result.put("frEmail", frEmail);
        result.put("frPhoto", frPhoto);


        //result.put("stars", stars);

        return result;
    }
}
