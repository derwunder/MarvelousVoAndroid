package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 9/24/17.
 */

public class Comment {
    private String id;
    private String comment, commentBy, commentUName, commentUAvatar;
    private long commentTime;
    private ArrayList<Reply> replys= new ArrayList<>();

    public Comment(){ }
    public Comment(String id, String comment, String commentBy,
                   String commentUName, String commentUAvatar, long commentTime,
                   ArrayList<Reply> replys){
        this.id=id;
        this.comment = comment;
        this.commentBy = commentBy;
        this.commentUName = commentUName;
        this.commentUAvatar = commentUAvatar;
        this.commentTime = commentTime;
        this.replys=replys;
    }

    public String getId(){return id;}
    public String getComment(){return comment;}
    public String getCommentBy(){return commentBy;}
    public String getCommentUName(){return commentUName;}
    public String getCommentUAvatar(){return commentUAvatar;}
    public long getCommentTime(){return commentTime;}
    public ArrayList<Reply> getReplys(){return replys;}

    public void setId(String id){this.id=id;}
    public void setComment(String comment){this.comment = comment;}
    public void setCommentBy(String commentBy){this.commentBy = commentBy;}
    public void setCommentUName(String commentUName){this.commentUName = commentUName;}
    public void setCommentUAvatar(String commentUAvatar){this.commentUAvatar = commentUAvatar;}
    public void setCommentTime(long commentTime){this.commentTime = commentTime;}
    public void setReplys(ArrayList<Reply> replys){this.replys=replys;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("comment", comment);
        result.put("commetBy", commentBy);
        result.put("commentUName", commentUName);
        result.put("commentUAvatar", commentUAvatar);
        result.put("commentTime", commentTime);

        //result.put("stars", stars);

        return result;
    }

    @Exclude
    public  Map<String,Object> toMapReplys(){
        HashMap<String, Object> resultReplys=new HashMap<>();
        for(Reply reply: replys){
            resultReplys.put(reply.getId(),reply.toMap());
        }
        return resultReplys;
    }
}
