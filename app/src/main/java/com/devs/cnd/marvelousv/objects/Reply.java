package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 9/24/17.
 */

public class Reply {

    private String id;
    private String reply, replyBy, replyUName, replyUAvatar;
    private long replyTime;

    public Reply(){ }
    public Reply(String id,String reply, String replyBy, String replyUName, String replyUAvatar, long replyTime){
        this.id=id;
        this.reply=reply;
        this.replyBy=replyBy;
        this.replyUName=replyUName;
        this.replyUAvatar=replyUAvatar;
        this.replyTime=replyTime;
    }
    public String getId(){return id;}
    public String getReply(){return reply;}
    public String getReplyBy(){return replyBy;}
    public String getReplyUName(){return replyUName;}
    public String getReplyUAvatar(){return replyUAvatar;}
    public long getReplyTime(){return replyTime;}

    public void setId(String id){this.id=id;}
    public void setReply(String reply){this.reply=reply;}
    public void setReplyBy(String replyBy){this.replyBy=replyBy;}
    public void setReplyUName(String replyUName){this.replyUName=replyUName;}
    public void setReplyUAvatar(String replyUAvatar){this.replyUAvatar=replyUAvatar;}
    public void setReplyTime(long replyTime){this.replyTime=replyTime;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("reply", reply);
        result.put("replyBy", replyBy);
        result.put("replyUName", replyUName);
        result.put("replyUAvatar", replyUAvatar);
        result.put("replyTime", replyTime);

        //result.put("stars", stars);

        return result;
    }
}
