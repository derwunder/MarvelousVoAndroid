package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 7/15/17.
 */

public class WordBox {

    String id;
    String boxName;
    long createdAt;
    boolean favorite;
    boolean gBoard;
    long lastCheckedAt;
    private ArrayList<Word> words = new ArrayList<>();

    public WordBox(){

    }
    public WordBox(String id, String boxName,
                   long createdAt, long lastCheckedAt,
                   boolean favorite, boolean gBoard,
                   ArrayList<Word> words){
        this.id=id; this.boxName=boxName;
        this.createdAt=createdAt; this.lastCheckedAt=lastCheckedAt;
        this.favorite=favorite; this.gBoard=gBoard;
        this.words=words;
    }

    public String getId(){
        return id;
    }
    public String getBoxName(){
        return boxName;
    }
    public long getCreatedAt(){
        return createdAt;
    }
    public boolean getFavorite(){
        return favorite;
    }
    public boolean getGBoard(){
        return gBoard;
    }
    public long getLastCheckedAt(){
        return lastCheckedAt;
    }
    public ArrayList<Word> getWords(){return words;}


    public void setId(String id){this.id=id;}
    public void setBoxName(String boxName){this.boxName=boxName;}
    public void setCreatedAt(long createdAt){this.createdAt=createdAt;}
    public void setFavorite(boolean favorite){this.favorite=favorite;}
    public void setgBoard(boolean gBoard){this.gBoard=gBoard;}
    public void setLastCheckedAt(long lastCheckedAt){this.lastCheckedAt=lastCheckedAt;}
    public void setWords(ArrayList<Word> words){this.words=words;}

    public void addWord(Word word){
        words.add(word);
    }
    public void removeWord(int inx){
        words.remove(inx);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("boxName", boxName);
        result.put("createdAt", createdAt);
        result.put("favorite", favorite);
        result.put("gBoard", gBoard);
        result.put("lastCheckedAt", lastCheckedAt);


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


}
