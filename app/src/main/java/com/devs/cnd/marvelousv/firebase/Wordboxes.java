package com.devs.cnd.marvelousv.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.devs.cnd.marvelousv.fragments.FrAllWbWords;
import com.devs.cnd.marvelousv.fragments.FrWbWords;
import com.devs.cnd.marvelousv.fragments.FrWordboxes;
import com.devs.cnd.marvelousv.objects.Definition;
import com.devs.cnd.marvelousv.objects.Tag;
import com.devs.cnd.marvelousv.objects.Translation;
import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 7/16/17.
 */

public class Wordboxes {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Context context;

    private WBRefCallback wbRefCallback;

    public ArrayList<WordBox> listWordBox = new ArrayList<>();

    public Wordboxes(Context context){
        this.context=context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void setWordBoxList(ArrayList<WordBox> listWordBox){
        this.listWordBox=listWordBox;
    }

    public void addWordBox(WordBox w){
        listWordBox.add(w);
        wbRefCallback.onWordboxesRefSet(listWordBox);
    }
    public void updateWordBox(WordBox w){
        int index=0;
        for(WordBox wb :listWordBox){
            if(wb.getId().equals(w.getId())){
                listWordBox.get(index).setBoxName(w.getBoxName());
                listWordBox.get(index).setFavorite(w.getFavorite());
                listWordBox.get(index).setgBoard(w.getGBoard());
                listWordBox.get(index).setLastCheckedAt(w.getLastCheckedAt());
            }index++;
        }
        wbRefCallback.onWordboxesRefSet(listWordBox);
    }

    public String getBoxName(String id){
        int index=0;
        for(WordBox wb: listWordBox){
            if(wb.getId().equals(id)){
                return listWordBox.get(index).getBoxName();
            }
            index++;
        }
        return "";

    }
    public WordBox getWordBox(String id){
        int index=0;
        for(WordBox wb: listWordBox){
            if(wb.getId().equals(id)){
                return listWordBox.get(index);
            }
            index++;
        }
        return new WordBox();
    }
    public ArrayList<Word> getWords(String id){
        int index=0;
        for(WordBox wb: listWordBox){
            if(wb.getId().equals(id)){
                return listWordBox.get(index).getWords();
            }
             index++;
        }
        return new ArrayList<>();
    }
    public ArrayList<Word> getAllWords(){
        ArrayList<Word> ltWHelper= new ArrayList<>();
        for(WordBox wb: listWordBox){
            ltWHelper.addAll(wb.getWords());
        }
        return ltWHelper;
    }


    //DELETE WB WORD
    public void deleteWBW(final Word w , final String WBID){
        int index=0;
        for(WordBox wb :listWordBox){
            if(wb.getId().equals(WBID)){
                int inxWord=0;
                for(Word word:listWordBox.get(index).getWords()){
                    if(word.getId().equals(w.getId())){
                        listWordBox.get(index).removeWord(inxWord);
                        Toast.makeText(context,"WBW LOCAL DELETE ",Toast.LENGTH_SHORT).show();
                        wbRefCallback.onWBWordsRefSet(listWordBox.get(index).getWords());
                    }inxWord++;
                }
            }index++;
        }

    }
    public void deleteWBWFB(final Word w, final String WBID){
        final FirebaseUser user = mAuth.getCurrentUser();

        String photoU="";
        if(user.getPhotoUrl()!=null)
            photoU= user.getPhotoUrl().toString();

        Calendar now =Calendar.getInstance();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/"+user.getUid()+"/wordboxes/"+WBID+"/words/"+w.getId(), null);

        final WordBox WB=getWordBox(WBID);
       /* int inxWord=0;
        for(Word word:WB.getWords()){
            if(word.getId().equals(w.getId())){
                WB.removeWord(inxWord);
            }inxWord++;
        }*/
        deleteWBW(w,WBID);
        Map<String, Object> wbGItems = new HashMap<>();
        if(WB.getGBoard()){
            wbGItems.put("boxName", WB.getBoxName());
            wbGItems.put("createdAt",WB.getCreatedAt());
            wbGItems.put("updatedAt",(-1*now.getTimeInMillis()));
            wbGItems.put("createBy",user.getUid());
            wbGItems.put("creatorName",user.getDisplayName());
            wbGItems.put("creatorAvatar",photoU);
            if(WB.getWords().size()==0){
                wbGItems.put("words",false);
            }else{
                wbGItems.put("words",WB.toMapWords());
            }
            childUpdates.put("global/wordboxes/"+WBID+"/wordbox", wbGItems);
        }

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"WBW DELETE ",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"WBW DELETE FAIL",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //UPDATE WB WORD
    private void updateWBWord(Word w,String WBID){
        int index=0;
        for(WordBox wb :listWordBox){
            if(WBID.equals(wb.getId())){
                int inxWord=0;
                for(Word word: listWordBox.get(index).getWords()){
                    if(w.getId().equals(word.getId())){
                      //  listWordBox.get(index).getWords().get(inxWord).setBookmark(w.getBookmark());
                       // listWordBox.get(index).getWords().get(inxWord).setWordTerm(w.getWordTerm());
                       // listWordBox.get(index).getWords().get(inxWord).setDefinitions(w.getDefinitions());
                       // listWordBox.get(index).getWords().get(inxWord).setTranslations(w.getTranslations());
                       // listWordBox.get(index).getWords().get(inxWord).setTags(w.getTags());
                        wbRefCallback.onWBWordsRefSet(listWordBox.get(index).getWords());
                    }
                }inxWord++;
            }index++;
        }

    }
    public void updateWBWordFB(final Word word, final String WBID){
        final FirebaseUser user = mAuth.getCurrentUser();

        String photoU="";
        if(user.getPhotoUrl()!=null)
            photoU= user.getPhotoUrl().toString();


        word.setId(word.getId());
        if(word.getDefinitions().size()==0){
            word.getDefinitions().add(new Definition("",""));
        }
        if(word.getTranslations().size()==0){
            word.getTranslations().add(new Translation("",""));
        }
        if(word.getTags().size()==0){
            word.getTags().add(new Tag(""));
        }
        final WordBox WBhelper=getWordBox(WBID);
        final WordBox WB = new WordBox();
        WB.setId(WB.getId());
        WB.setBoxName(WBhelper.getBoxName());
        WB.setFavorite(WBhelper.getFavorite());
        WB.setgBoard(WBhelper.getGBoard());
        WB.setLastCheckedAt(WBhelper.getLastCheckedAt());
        WB.setCreatedAt(WBhelper.getCreatedAt());
        WB.setWords(WBhelper.getWords());

        Calendar now =Calendar.getInstance();

        Map<String, Object> wordItems = word.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/"+user.getUid()+"/wordboxes/"+WBID+"/words/"+word.getId(), wordItems);

        Map<String, Object> wbGItems = new HashMap<>();
        if(WB.getGBoard()){
            wbGItems.put("boxName", WB.getBoxName());
            wbGItems.put("createdAt",WB.getCreatedAt());
            wbGItems.put("updatedAt",(-1*now.getTimeInMillis()));
            wbGItems.put("createBy",user.getUid());
            wbGItems.put("creatorName",user.getDisplayName());
            wbGItems.put("creatorAvatar",photoU);
            wbGItems.put("words",WB.toMapWords());
            childUpdates.put("global/wordboxes/"+WBID+"/wordbox", wbGItems);
        }

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateWBWord(word, WBID);
                Toast.makeText(context,"Succes postWBW FB",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Fail post FB",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //ADD WB WORD
    private void addWBW(WordBox WB){
        int index=0;
        for(WordBox wb :listWordBox){
            if(wb.getId().equals(WB.getId())){
                listWordBox.get(index).setWords(WB.getWords());
                wbRefCallback.onWBWordsRefSet(listWordBox.get(index).getWords());
            }index++;
        }

    }
    public void addWBWord(Word word){
        final FirebaseUser user = mAuth.getCurrentUser();
        String WBId=word.getId();
        final String WBWKey=mDatabase.child("users").child(user.getUid()).child("wordboxes")
                .child(WBId).child("words").push().getKey();

        String photoU="";
        if(user.getPhotoUrl()!=null)
            photoU= user.getPhotoUrl().toString();


        word.setId(WBWKey);
        if(word.getDefinitions().size()==0){
            word.getDefinitions().add(new Definition("",""));
        }
        if(word.getTranslations().size()==0){
            word.getTranslations().add(new Translation("",""));
        }
        if(word.getTags().size()==0){
            word.getTags().add(new Tag(""));
        }
        final WordBox WB=getWordBox(WBId);
        WB.addWord(word);
        Calendar now =Calendar.getInstance();

        Map<String, Object> wordItems = word.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/"+user.getUid()+"/wordboxes/"+WBId+"/words/"+WBWKey, wordItems);

        Map<String, Object> wbGItems = new HashMap<>();
        if(WB.getGBoard()){
            wbGItems.put("boxName", WB.getBoxName());
            wbGItems.put("createdAt",WB.getCreatedAt());
            wbGItems.put("updatedAt",(-1*now.getTimeInMillis()));
            wbGItems.put("createBy",user.getUid());
            wbGItems.put("creatorName",user.getDisplayName());
            wbGItems.put("creatorAvatar",photoU);
            wbGItems.put("words",WB.toMapWords());
            childUpdates.put("global/wordboxes/"+WBId+"/wordbox", wbGItems);
        }

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addWBW(WB);
                Toast.makeText(context,"Succes postWBW FB",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Fail post FB",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //UPDATE WB
    private void updateWBFav(WordBox w){
        int index=0, posUp=0;
        for(WordBox wb: listWordBox){
            if(wb.getId().equals(w.getId())){
                posUp=index;
            }
            index++;
        }
        listWordBox.get(posUp).setFavorite(!w.getFavorite());
        wbRefCallback.onWordboxesRefSet(listWordBox);
    }
    public void updateWBFavFB(final WordBox wb){
        final FirebaseUser user = mAuth.getCurrentUser();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/"+user.getUid()+"/wordboxes/"+wb.getId()+"/favorite", !wb.getFavorite());

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"WB Update IT",Toast.LENGTH_SHORT).show();
                updateWBFav(wb);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"WB Update FAIL",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateWBGBoard(WordBox w){
        int index=0, posUp=0;
        for(WordBox wb: listWordBox){
            if(wb.getId().equals(w.getId())){
                posUp=index;
            }
            index++;
        }
        listWordBox.get(posUp).setgBoard(!w.getGBoard());
        wbRefCallback.onWordboxesRefSet(listWordBox);
    }
    public void updateWBGBoardFB(final WordBox wb){
        final FirebaseUser user = mAuth.getCurrentUser();
        final String wbKey=wb.getId();
        Calendar now =Calendar.getInstance();
        String photoU="";
        if(user.getPhotoUrl()!=null)
            photoU= user.getPhotoUrl().toString();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/"+user.getUid()+"/wordboxes/"+wb.getId()+"/gBoard", !wb.getGBoard());

        Map<String, Object> wbGItems = new HashMap<>();
        if(!wb.getGBoard()){

            wbGItems.put("boxName",wb.getBoxName());
            wbGItems.put("createdAt",wb.getCreatedAt());
            wbGItems.put("updatedAt",(-1*now.getTimeInMillis()));
            wbGItems.put("createBy",user.getUid());
            wbGItems.put("creatorName",user.getDisplayName());
            wbGItems.put("creatorAvatar",photoU);
            if(wb.getWords().size()==0){
                wbGItems.put("words",false);
            }else{
                wbGItems.put("words",wb.toMapWords());
            }
            childUpdates.put("global/wordboxes/"+wbKey+"/wordbox", wbGItems);

        }else{
            childUpdates.put("global/wordboxes/"+wbKey+"/wordbox", null);
        }

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"WB Update GBo",Toast.LENGTH_SHORT).show();
                updateWBGBoard(wb);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"WB Update FAIL",Toast.LENGTH_SHORT).show();
            }
        });


    }

    //DELETE WB
    private void deleteWB(String id){
        int index=0, posdel=0;
        for(WordBox wb: listWordBox){
            if(wb.getId().equals(id)){
                posdel=index;
            }
            index++;
        }
        listWordBox.remove(posdel);
        wbRefCallback.onWordboxesRefSet(listWordBox);
    }
    public void deleteWBFB(final WordBox wb){
        final FirebaseUser user = mAuth.getCurrentUser();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/"+user.getUid()+"/wordboxes/"+wb.getId(), null);
        childUpdates.put("global/wordboxes/"+wb.getId(),null);

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            Toast.makeText(context,"WB DELETE IT",Toast.LENGTH_SHORT).show();
                deleteWB(wb.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"WB DELETE FAIL",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //LOAD DATA FB
    public void wordboxesLoad(){
        final FirebaseUser user = mAuth.getCurrentUser();

        if(listWordBox.isEmpty()) {
            mDatabase.child("users").child(user.getUid()).child("wordboxes")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    //User user = dataSnapshot.getValue(User.class);
                                    ArrayList<WordBox> listWordBox = new ArrayList<>();
                                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                        String id = dss.getKey();
                                        String boxName = (String) dss.child("boxName").getValue();
                                        long createdAt = (long) dss.child("createdAt").getValue();
                                        boolean favorite = (boolean) dss.child("favorite").getValue();
                                        boolean gBoard = (boolean) dss.child("gBoard").getValue();
                                        long lastCheckedAt = (long) dss.child("lastCheckedAt").getValue();
                                        ArrayList<Word> words = new ArrayList<>();

                                        if (dss.hasChild("words")) {
                                            for (DataSnapshot dssWord : dss.child("words").getChildren()) {
                                                String idW = dssWord.getKey();
                                                String wordTerm = (String) dssWord.child("wordTerm").getValue();
                                                boolean bookmark = (boolean) dssWord.child("bookmark").getValue();
                                                ArrayList<Definition> definitions = new ArrayList<>();
                                                ArrayList<Translation> translations = new ArrayList<>();
                                                ArrayList<Tag> tags = new ArrayList<>();

                                                for (DataSnapshot dssDefs : dssWord.child("definitions").getChildren()) {
                                                    String def = (String) dssDefs.child("def").getValue();
                                                    String tp = (String) dssDefs.child("tp").getValue();
                                                    Definition definition = new Definition(def, tp);
                                                    definitions.add(definition);
                                                }
                                                for (DataSnapshot dssTrans : dssWord.child("translations").getChildren()) {
                                                    String lan = (String) dssTrans.child("lan").getValue();
                                                    String tr = (String) dssTrans.child("tr").getValue();
                                                    Translation translation = new Translation(lan, tr);
                                                    translations.add(translation);
                                                }
                                                for (DataSnapshot dssTags : dssWord.child("tags").getChildren()) {
                                                    String tagS = (String) dssTags.child("tag").getValue();
                                                    Tag tag = new Tag(tagS);
                                                    tags.add(tag);
                                                }


                                                Word word = new Word(idW, wordTerm, bookmark, definitions, translations, tags);
                                                words.add(word);
                                            }
                                        }

                                        WordBox wordBox = new WordBox(id, boxName, createdAt
                                                , lastCheckedAt, favorite, gBoard
                                                , words);

                                        listWordBox.add(wordBox);
                                        setWordBoxList(listWordBox);
                                        wbRefCallback.onWordboxesRefSet(listWordBox);


                           /* Toast.makeText(context,
                                    "Data FB:\n"+
                                    "\nID: "+id+
                                    "\nBoxName: "+boxName+
                                    "\nCreatedAt: "+createdAt+
                                    "\nFavorite: "+favorite+
                                    "\nGBoard"+gBoard+
                                    "\nLastCheckedAt"+lastCheckedAt
                                    ,
                                    Toast.LENGTH_LONG).show();*/
                                    }
                                    // setWordList(listWordBox);

                                    // [START_EXCLUDE]
                                    if (user == null) {
                                        // User is null, error out
                                        //Log.e(TAG, "User " + userId + " is unexpectedly null");
                                        Toast.makeText(context,
                                                "Error: User session empty",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Write new post
                                        //writeNewPost(userId, user.username, title, body);
                                    }

                                    // Finish this Activity, back to the stream
                                    //setEditingEnabled(true);
                                    //finish();
                                    // [END_EXCLUDE]
                              /*  Toast.makeText(context,
                                        "Success: FB The data is Read.",
                                        Toast.LENGTH_SHORT).show();*/
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                    // [START_EXCLUDE]
                                    //setEditingEnabled(true);
                                    // [END_EXCLUDE]
                                    Toast.makeText(context,
                                            "Error: FB Something goes wrong.",
                                            Toast.LENGTH_SHORT).show();
                                    wbRefCallback.onWordboxesRefFail();
                                }
                            });
        }else{
            wbRefCallback.onWordboxesRefSet(listWordBox);
            Toast.makeText(context,
                    "pass: Previews data load",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void wordboxesRefresh(){
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase.child("users").child(user.getUid()).child("wordboxes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        // User user = dataSnapshot.getValue(User.class);
                        ArrayList<WordBox> listWordBox = new ArrayList<>();
                        for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            String id = dss.getKey();
                            String boxName = (String) dss.child("boxName").getValue();
                            long createdAt = (long) dss.child("createdAt").getValue();
                            boolean favorite = (boolean) dss.child("favorite").getValue();
                            boolean gBoard = (boolean) dss.child("gBoard").getValue();
                            long lastCheckedAt = (long) dss.child("lastCheckedAt").getValue();
                            ArrayList<Word> words = new ArrayList<>();

                            if (dss.hasChild("words")) {
                               for (DataSnapshot dssWord : dss.child("words").getChildren()) {
                                   String idW = dssWord.getKey();
                                   String wordTerm = (String) dssWord.child("wordTerm").getValue();
                                   boolean bookmark = (boolean) dssWord.child("bookmark").getValue();
                                   ArrayList<Definition> definitions = new ArrayList<>();
                                   ArrayList<Translation> translations = new ArrayList<>();
                                   ArrayList<Tag> tags = new ArrayList<>();

                                   for (DataSnapshot dssDefs : dssWord.child("definitions").getChildren()) {
                                       String def = (String) dssDefs.child("def").getValue();
                                       String tp = (String) dssDefs.child("tp").getValue();
                                       Definition definition = new Definition(def, tp);
                                       definitions.add(definition);
                                   }
                                   for (DataSnapshot dssTrans : dssWord.child("translations").getChildren()) {
                                       String lan = (String) dssTrans.child("lan").getValue();
                                       String tr = (String) dssTrans.child("tr").getValue();
                                       Translation translation = new Translation(lan, tr);
                                       translations.add(translation);
                                   }
                                   for (DataSnapshot dssTags : dssWord.child("tags").getChildren()) {
                                       String tagS = (String) dssTags.child("tag").getValue();
                                       Tag tag = new Tag(tagS);
                                       tags.add(tag);
                                   }
                                   Word word = new Word(idW, wordTerm, bookmark, definitions, translations, tags);
                                   words.add(word);
                               }
                            }

                        WordBox wordBox = new WordBox(id, boxName, createdAt
                                        , lastCheckedAt, favorite, gBoard
                                        , words);

                        listWordBox.add(wordBox);
                        setWordBoxList(listWordBox);
                        wbRefCallback.onWordboxesRefSet(listWordBox);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        wbRefCallback.onWordboxesRefFail();
                        Toast.makeText(context,
                        "Error: FB Something goes wrong.",
                        Toast.LENGTH_SHORT).show();
                    }
                });

    }

    //CALL BACKS FR
    public void onFrWordboxesActive( FrWordboxes frWordboxes){
        try{
            wbRefCallback=(WBRefCallback)frWordboxes;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Wordboxes Interaction Listener");
        }
    }
    public void onFrWbWordsActive( FrWbWords frWbWords){
        try{
            wbRefCallback=(WBRefCallback)frWbWords;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Wordboxes Interaction Listener");
        }
    }
    public void onFrAllWbWordsActive( FrAllWbWords frAllWbWords){
        try{
            wbRefCallback=(WBRefCallback)frAllWbWords;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Wordboxes Interaction Listener");
        }
    }

    public interface WBRefCallback {
        void onWordboxesRefSet( ArrayList<WordBox> listWordBox);
        void onWordboxesRefFail();
        void onWBWordsRefSet(ArrayList<Word> listWord);
    }
}
