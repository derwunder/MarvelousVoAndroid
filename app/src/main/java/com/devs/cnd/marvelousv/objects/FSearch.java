package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 9/29/17.
 */

public class FSearch {
    private String id;
    private String userName;
    private String userEmail;
    private String userPhoto;
    public FSearch(){

    }
    public FSearch(String id, String userName, String userEmail, String userPhoto){
        this.userName = userName;this.userEmail = userEmail;
        this.id=id; this.userPhoto=userPhoto;
    }

    public String getId(){return id;}
    public String getUserName(){return userName;}
    public String getUserEmail(){return userEmail;}
    public String getUserPhoto(){return userPhoto;}

    public void setId(String id){this.id=id;}
    public void setUserName(String userName){this.userName = userName;}
    public void setUserEmail(String userEmail){this.userEmail = userEmail;}
    public void setUserPhoto(String userPhoto){this.userPhoto=userPhoto;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("userEmail", userEmail);
        result.put("userPhoto", userPhoto);
        return result;
    }
}
