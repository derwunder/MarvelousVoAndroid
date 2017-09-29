package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 9/13/17.
 */

public class WordBoxG {

    String id;
    String boxName;
    long createdAt;
    long updatedAt;

    private long downloadsCount;
    private long likeCount;
    private long dislikeCount;

    private String createBy;
    private String creatorName;
    private String creatorAvatar;

    private ArrayList<String> likes = new ArrayList<>();
    private ArrayList<String> disLikes = new ArrayList<>();
    private ArrayList<String> downloads = new ArrayList<>();
    private ArrayList<Word> words = new ArrayList<>();

    private ArrayList<Maker> makers = new ArrayList<>();

    private ArrayList<Comment> comments= new ArrayList<>();

    public WordBoxG(){

    }
    public WordBoxG(String id, String boxName, long createdAt, long updatedAt,
                    long downloadsCount, long likeCount, long dislikeCount,
                    String createBy, String creatorName, String creatorAvatar,
                    ArrayList<String> likes, ArrayList<String> dislikes,
                    ArrayList<String> downloads,ArrayList<Word> words,
                    ArrayList<Maker> makers,ArrayList<Comment> comments){
        this.id=id;
        this.boxName=boxName;
        this.downloadsCount=downloadsCount;
        this.likeCount=likeCount;
        this.dislikeCount=dislikeCount;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.createBy=createBy;
        this.creatorName=creatorName; this.creatorAvatar=creatorAvatar;
        this.likes=likes;this.disLikes=dislikes;
        this.downloads=downloads;
        this.words=words;
        this.makers=makers;
        this.comments=comments;
    }

    public String getId(){
        return id;
    }
    public String getBoxName(){
        return boxName;
    }
    public long getDownloadsCount(){return downloadsCount;}
    public long getLikeCount(){return  likeCount;}
    public long getDislikeCount(){return dislikeCount;}
    public long getCreatedAt(){
        return createdAt;
    }
    public long getUpdatedAt() { return updatedAt;}
    public String getCreateBy(){return createBy;}
    public String getCreatorName(){return creatorName;}
    public String getCreatorAvatar(){return creatorAvatar;}
    public ArrayList<String> getLikes(){return likes;}
    public ArrayList<String> getDisLikes(){return disLikes;}
    public ArrayList<String> getDownloads(){return downloads;}
    public ArrayList<Word> getWords(){return words;}
    public ArrayList<Maker> getMakers(){return  makers;}
    public ArrayList<Comment> getComments(){return comments;}

    public void setId(String id){this.id=id;}
    public void setBoxName(String boxName){this.boxName=boxName;}
    public void setDownloadsCount(long downloadsCount){this.downloadsCount=downloadsCount;}
    public void setLikeCount(long likeCount){this.likeCount=likeCount;}
    public void setDislikeCount(long dislikeCount){this.dislikeCount=dislikeCount;}
    public void setCreatedAt(long createdAt){this.createdAt=createdAt;}
    public void setUpdatedAt(long updatedAt){this.updatedAt=updatedAt;}
    public void setCreateBy(String createBy){this.createBy=createBy;}
    public void setCreatorName(String creatorName){this.creatorName=creatorName;}
    public void setCreatorAvatar(String creatorAvatar){this.creatorAvatar=creatorAvatar;}
    public void setLikes(ArrayList<String> likes){this.likes=likes;}
    public void setDisLikes(ArrayList<String> disLikes){this.disLikes=disLikes;}
    public void setDownloads(ArrayList<String> downloads){this.downloads=downloads;}
    public void setWords(ArrayList<Word> words){this.words=words;}
    public void setMakers(ArrayList<Maker> makers){this.makers=makers;}
    public void setComments(ArrayList<Comment> comments){this.comments=comments;}

    public void addWord(Word word){
        words.add(word);
    }
    public void removeWord(int inx){
        words.remove(inx);
    }




    @Exclude
    public Map<String, Object> toMapWB() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("boxName", boxName);
        result.put("createdAt", createdAt);
        result.put("createBy", createBy);
        result.put("creatorName", creatorName);
        result.put("creatorAvatar", creatorAvatar);


        //result.put("stars", stars);

        return result;
    }

    @Exclude
    public  Map<String,Object> toMapWords(){
        HashMap<String, Object> resultWords=new HashMap<>(); int inxWords=0;
        for(Word word: words){
            resultWords.put(Integer.toString(inxWords),word.toMap()); inxWords++;
        }
        return resultWords;
    }

    @Exclude
    public  Map<String,Object> toMapMakers(){
        HashMap<String, Object> resultWords=new HashMap<>(); int inxMakers=0;
        for(Maker maker: makers){
            resultWords.put(Integer.toString(inxMakers),maker.toMap()); inxMakers++;
        }
        return resultWords;
    }
    @Exclude
    public  Map<String,Object> toMapComments(){
        HashMap<String, Object> resultComments=new HashMap<>();
        for(Comment comment: comments){
            resultComments.put(comment.getId(),comment.toMap());
            resultComments.put("replys",comment.toMapReplys());
        }
        return resultComments;
    }

    private Map<String,Object> arrayToMap(ArrayList<String> lt){
        HashMap<String, Object> resultLT=new HashMap<>(); int inx=0;
        for(String s: lt){
            resultLT.put(s,true);inx++;
        }
        return resultLT;
    }

    public Map<String,Object> toMapLikes(){
        return arrayToMap(likes);
    }
    public Map<String,Object> toMapDisLikes(){
        return arrayToMap(disLikes);
    }
    public Map<String,Object> toMapDownloads(){
        return arrayToMap(downloads);
    }

}
