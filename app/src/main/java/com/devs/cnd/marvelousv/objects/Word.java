package com.devs.cnd.marvelousv.objects;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 7/15/17.
 */

public class Word {

    private String id;
    private String wordTerm;
    private boolean bookmark;

    private ArrayList<Definition> definitions = new ArrayList<>();
    private ArrayList<Translation> translations = new ArrayList<>();
    private ArrayList<Tag> tags = new ArrayList<>();
    public Word(){

    }
    public Word(String id, String wordTerm, boolean bookmark,
                ArrayList<Definition> definitions,
                ArrayList<Translation> translations,
                ArrayList<Tag> tags){
        this.id=id; this.wordTerm=wordTerm;
        this.bookmark=bookmark;

        this.definitions=definitions;
        this.translations=translations;
        this.tags=tags;
    }

    public String getId(){ return id;    }
    public String getWordTerm(){return wordTerm;}
    public boolean getBookmark(){return bookmark;}
    public ArrayList<Definition> getDefinitions(){return definitions;}
    public ArrayList<Translation> getTranslations(){return translations;}
    public ArrayList<Tag> getTags(){return tags;}

    public void setId(String id){this.id=id;}
    public void setWordTerm(String wordTerm){this.wordTerm=wordTerm;}
    public void setBookmark(boolean bookmark){this.bookmark=bookmark;}
    public void setDefinitions(ArrayList<Definition> definitions){this.definitions=definitions;}
    public void setTranslations(ArrayList<Translation> translations){this.translations=translations;}
    public void setTags(ArrayList<Tag> tags){this.tags=tags;}


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("wordTerm", wordTerm);
        result.put("bookmark", bookmark);

        HashMap<String, Object> resultDefs=new HashMap<>(); int inxDefs=0;
        for(Definition def: definitions){
            resultDefs.put(Integer.toString(inxDefs),def.toMap()); inxDefs++;
        }
        HashMap<String, Object> resultTrans=new HashMap<>(); int inxTrans=0;
        for(Translation trans: translations){
            resultTrans.put(Integer.toString(inxTrans),trans.toMap()); inxTrans++;
        }
        HashMap<String,Object>resultTags=new HashMap<>(); int inxTags=0;
        for(Tag tag: tags){
            resultTags.put(Integer.toString(inxTags),tag.toMap());
        }

        result.put("definitions",resultDefs);
        result.put("translations",resultTrans);
        result.put("tags",resultTags);



        return result;
    }
}
