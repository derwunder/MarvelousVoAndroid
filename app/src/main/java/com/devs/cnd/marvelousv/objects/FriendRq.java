package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 9/12/17.
 */

public class FriendRq {

    private String reqUid;
    private String reqUEmail;
    private String reqUName;
    private String reqUPhoto;
    private long reqUTime;

    public FriendRq(){}
    public FriendRq(String reqUid, String reqUName, String reqUEmail, String reqUPhoto, long reqUTime){
        this.reqUid=reqUid;
        this.reqUName = reqUName;
        this.reqUEmail=reqUEmail;
        this.reqUPhoto=reqUPhoto;
        this.reqUTime=reqUTime;
    }

    public String getReqUid(){return reqUid;}
    public String getReqUName(){return  reqUName;}
    public String getReqUEmail(){return reqUEmail;}
    public String getReqUPhoto(){return reqUPhoto;}
    public long getReqUTime(){return  reqUTime;}

    public void setReqUid(String reqUid){this.reqUid=reqUid;}
    public void setReqUName(String reqUName){this.reqUName=reqUName;}
    public void setReqUEmail(String reqUEmail){this.reqUEmail=reqUEmail;}
    public void setReqUPhoto(String reqUPhoto){this.reqUPhoto=reqUPhoto;}
    public void setReqUTime(long reqUTime){this.reqUTime=reqUTime;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("reqUName", reqUName);
        result.put("reqUEmail", reqUEmail);
        result.put("reqUPhoto", reqUPhoto);
        result.put("reqUTime", reqUTime);


        //result.put("stars", stars);

        return result;
    }
}
