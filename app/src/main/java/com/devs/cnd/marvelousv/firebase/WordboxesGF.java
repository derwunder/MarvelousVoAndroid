package com.devs.cnd.marvelousv.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.devs.cnd.marvelousv.fragments.FrWbGFriend;
import com.devs.cnd.marvelousv.fragments.FrWbGUFriend;
import com.devs.cnd.marvelousv.fragments.FrWbGWords;
import com.devs.cnd.marvelousv.fragments.FrWbGlobal;
import com.devs.cnd.marvelousv.fragments.FrWordboxes;
import com.devs.cnd.marvelousv.objects.Comment;
import com.devs.cnd.marvelousv.objects.Definition;
import com.devs.cnd.marvelousv.objects.Friend;
import com.devs.cnd.marvelousv.objects.Maker;
import com.devs.cnd.marvelousv.objects.Reply;
import com.devs.cnd.marvelousv.objects.Tag;
import com.devs.cnd.marvelousv.objects.Translation;
import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;
import com.devs.cnd.marvelousv.objects.WordBoxG;
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
 * Created by wunder on 9/16/17.
 */

public class WordboxesGF {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Context context;
    private WBGFRefCallback wbgfRefCallback;
    private WBGRefCallback wbgRefCallback;
    private WBGWRefCallBack wbgwRefCallBack;

    public ArrayList<WordBoxG> listWordBoxGF = new ArrayList<>();
    public ArrayList<WordBoxG> listWordBoxG = new ArrayList<>();

    private String GlobalKeyLoad="";
    private String GlobalKeyName="";

    private String SORT_AZ="wordbox/boxName",SORT_DL="downloadsCount",
            SORT_LK="likeCount",SORT_UP="wordbox/updatedAt",SORT_PS="wordbox/createBy";

    public WordboxesGF(Context context){
        this.context=context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setWordBoxGList(ArrayList<WordBoxG> listWordBoxG){
        this.listWordBoxG=listWordBoxG;
    }
    public void setWordBoxGFIDList(ArrayList<WordBoxG> listWordBoxGF){
        this.listWordBoxGF=listWordBoxGF;
    }
    public void addWBGList(WordBoxG wordBoxG){listWordBoxG.add(wordBoxG);}
    public void addWBGFList(WordBoxG wordBoxG){
        listWordBoxGF.add(wordBoxG);
    }

    public WordBoxG getWordBoxGF(String id){
        int index=0;
        for(WordBoxG wb: listWordBoxGF){
            if(wb.getId().equals(id)){
                return listWordBoxGF.get(index);
            }
            index++;
        }
        return new WordBoxG();
    }
    public WordBoxG getWordBoxG(String id){
        int index=0;
        for(WordBoxG wb: listWordBoxG){
            if(wb.getId().equals(id)){
                return listWordBoxG.get(index);
            }
            index++;
        }
        return new WordBoxG();
    }

    public ArrayList<Comment> addWBGComment(String idWBG , Comment c){
        int postWBG=0; boolean flgUp=false; int inxUP=0;
        for(WordBoxG WBG: listWordBoxG){
            if(WBG.getId().equals(idWBG)){
                inxUP=postWBG;
            }
            postWBG++;
        }
        if(flgUp){
            listWordBoxG.get(inxUP).getComments().add(c);
        }
        return listWordBoxG.get(inxUP).getComments();
    }


    //SAVE WORD BOX
    public void saveWordBox(WordBoxG wordBoxG){
        final FirebaseUser user = mAuth.getCurrentUser();
        final String wbKey=mDatabase.child("users")
                .child(user.getUid())
                .child("wordboxes").push().getKey();
        Calendar now =Calendar.getInstance();

        final String idWBG = wordBoxG.getId();

        final WordBox wordBox = new WordBox();
        wordBox.setId(wbKey);
        wordBox.setBoxName(wordBoxG.getBoxName());
        wordBox.setFavorite(false);
        wordBox.setgBoard(false);
        wordBox.setCreatedAt(now.getTimeInMillis());
        wordBox.setLastCheckedAt(now.getTimeInMillis());
        wordBox.setWords(wordBoxG.getWords());


        Map<String, Object> wbItems = wordBox.toMap();
        wbItems.put("words",wordBox.toMapWords());
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> wbGItems = new HashMap<>();
        childUpdates.put("users/"+user.getUid()+"/wordboxes/"+wbKey, wbItems);
        childUpdates.put("global/wordboxes/"+idWBG+"/downloads/"+user.getUid(),true);
        childUpdates.put("global/wordboxes/"+idWBG+"/downloadsCount",-1*wordBoxG.getDownloads().size());

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //wbgwRefCallBack.onWBWReplyRefSet(idCm,reply);
                Toast.makeText(context,"Succes Save WordBox",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context,"Fail Save WordBox: "+e,Toast.LENGTH_LONG).show();
            }
        });

    }

    //POST / ADD
    public void postComment(final String idWBG, String txComment){
        final FirebaseUser user = mAuth.getCurrentUser();
        final String wbCmKey=mDatabase
                .child("global").child("wordboxes")
                .child(idWBG).child("comments").push().getKey();

        Calendar now =Calendar.getInstance();
        String photoU="";
        if(user.getPhotoUrl()!=null)
            photoU= user.getPhotoUrl().toString();

        final Comment comment = new Comment();
        comment.setId(wbCmKey);
        comment.setComment(txComment);
        comment.setCommentBy(user.getUid());
        comment.setCommentUAvatar(photoU);
        comment.setCommentUName(user.getDisplayName());
        comment.setCommentTime(now.getTimeInMillis());


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("global/wordboxes/"+idWBG+"/comments/"+wbCmKey, comment.toMap());

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                wbgwRefCallBack.onWBWCommentRefSet(comment);
                Toast.makeText(context,"Succes post comment",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context,"Fail post Commet: "+e,Toast.LENGTH_LONG).show();
            }
        });

    }
    public void postReply(final String idWBG,final String idCm, String txReply){
        final FirebaseUser user = mAuth.getCurrentUser();
        final String wbRpKey=mDatabase
                .child("global").child("wordboxes")
                .child(idWBG).child("comments")
                .child(idCm).child("replys").push().getKey();

        Calendar now =Calendar.getInstance();
        String photoU="";
        if(user.getPhotoUrl()!=null)
            photoU= user.getPhotoUrl().toString();

        final Reply reply= new Reply();
        reply.setId(wbRpKey);
        reply.setReply(txReply);
        reply.setReplyBy(user.getUid());
        reply.setReplyUAvatar(photoU);
        reply.setReplyUName(user.getDisplayName());
        reply.setReplyTime(now.getTimeInMillis());


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("global/wordboxes/"+idWBG+
                "/comments/"+idCm+
                "/replys/"+wbRpKey, reply.toMap());

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                wbgwRefCallBack.onWBWReplyRefSet(idCm,reply);
                Toast.makeText(context,"Succes post reply",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context,"Fail post reply: "+e,Toast.LENGTH_LONG).show();
            }
        });

    }
    public void postNewLKStatus(final String idWBG, final int type, final WordBoxG wbg){
        int TYPE_LK=30,TYPE_DLK=31;

        final FirebaseUser user = mAuth.getCurrentUser();

        int sizeLK=wbg.getLikes().size();
        int sizeDLK=wbg.getDisLikes().size();
        boolean fLK=false,fDLK=false;

        for(int i=0; i<wbg.getLikes().size();i++){
            if(wbg.getLikes().get(i).equals(user.getUid())){
                fLK=true;
            }
        }
        for(int i=0; i<wbg.getDisLikes().size();i++){
            if(wbg.getDisLikes().get(i).equals(user.getUid())){
                fDLK=true;
            }
        }
        if(type==TYPE_LK){
            if(!fLK)
                sizeLK+=1;
            if(fDLK)
                sizeDLK-=1;
        }else if(type==TYPE_DLK){
            if(fLK)
                sizeLK-=1;
            if(!fDLK)
                sizeDLK+=1;
        }




        Map<String, Object> childUpdates = new HashMap<>();
        if(type==TYPE_LK){
            childUpdates.put("global/wordboxes/"+idWBG+"/like/"+user.getUid(), true);
            childUpdates.put("global/wordboxes/"+idWBG+"/dislike/"+user.getUid(), null);
            childUpdates.put("global/wordboxes/"+idWBG+"/likeCount", -sizeLK);
            childUpdates.put("global/wordboxes/"+idWBG+"/dislikeCount", -sizeDLK);
        }
        else if(type==TYPE_DLK){
            childUpdates.put("global/wordboxes/"+idWBG+"/like/"+user.getUid(), null);
            childUpdates.put("global/wordboxes/"+idWBG+"/dislike/"+user.getUid(), true);
            childUpdates.put("global/wordboxes/"+idWBG+"/likeCount", -sizeLK);
            childUpdates.put("global/wordboxes/"+idWBG+"/dislikeCount", -sizeDLK);
        }

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                wbgwRefCallBack.onWBWStatusLKRefSet(type);
                Toast.makeText(context,"Succes post new Status LK",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context,"Fail post new Status LK: "+e,Toast.LENGTH_LONG).show();
            }
        });
    }

    //DELETE
    public void deleteComment(final String idWBG, final String idCm){

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("global/wordboxes/"+idWBG+"/comments/"+idCm, null);

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                wbgwRefCallBack.onWBWCommentDLRefSet(idCm);
                Toast.makeText(context,"Succes delete comment",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context,"Fail delete Commet: "+e,Toast.LENGTH_LONG).show();
            }
        });

    }
    public void deleteReply(final String idWBG, final String idCm, final String idRp){

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("global/wordboxes/"+idWBG+"/comments/"+idCm+
                "/replys/"+idRp, null);

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                wbgwRefCallBack.onWBWReplyDLRefSet(idCm,idRp);
                Toast.makeText(context,"Succes delete Reply",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context,"Fail delete Reply"+e,Toast.LENGTH_LONG).show();
            }
        });

    }


    private void globalTags(String SORT_TYPE, WordBoxG WBG){
        if(SORT_TYPE.equals(SORT_AZ)){
            GlobalKeyLoad=WBG.getId();
            GlobalKeyName=WBG.getBoxName();
        }
        else if(SORT_TYPE.equals(SORT_DL)){
            GlobalKeyLoad=WBG.getId();
            GlobalKeyName= Long.toString(WBG.getDownloadsCount());
        }
        else if(SORT_TYPE.equals(SORT_LK)){
            GlobalKeyLoad=WBG.getId();
            GlobalKeyName= Long.toString(WBG.getLikeCount());
        }
        else if(SORT_TYPE.equals(SORT_UP)){
            GlobalKeyLoad=WBG.getId();
            GlobalKeyName= Long.toString(WBG.getUpdatedAt());
        }





    }

    public void wordboxesGFLoad(ArrayList<Friend> listF){
        listWordBoxGF= new ArrayList<>();
        for(Friend friend: listF){
            mDatabase.child("global").child("wordboxes").orderByChild("wordbox/createBy")
                    .equalTo(friend.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<WordBoxG> listWBG = new ArrayList<>();

                            for(DataSnapshot dss : dataSnapshot.getChildren()){
                                WordBoxG WBG= new WordBoxG();
                                ArrayList<String>likes= new ArrayList<>();
                                ArrayList<String>disLikes= new ArrayList<>();
                                ArrayList<String>downloads= new ArrayList<>();
                                ArrayList<Comment>comments=new ArrayList<>();

                                long downloadsCount=0, likeCount=0, dislikeCount=0;
                                if(dss.hasChild("downloadsCount")){
                                    downloadsCount=(long) dss.child("downloadsCount").getValue();
                                }
                                if(dss.hasChild("likeCount")){
                                    likeCount=(long) dss.child("likeCount").getValue();
                                }
                                if(dss.hasChild("dislikeCount")){
                                    dislikeCount=(long) dss.child("dislikeCount").getValue();
                                }
                                if(dss.hasChild("like")){
                                    for(DataSnapshot dssLikes: dss.child("like").getChildren()){
                                        likes.add(dssLikes.getKey());
                                    }
                                }
                                if(dss.hasChild("dislike")){
                                    for(DataSnapshot dssDisLikes: dss.child("dislike").getChildren()){
                                        disLikes.add(dssDisLikes.getKey());
                                    }
                                }
                                if(dss.hasChild("downloads")){
                                    for(DataSnapshot dssDownloads: dss.child("downloads").getChildren()){
                                        downloads.add(dssDownloads.getKey());
                                    }
                                }
                                if(dss.hasChild("comments")){
                                    for(DataSnapshot dssComment: dss.child("comments").getChildren()){
                                        String idCm= dssComment.getKey();
                                        String comment= (String) dssComment.child("comment").getValue();
                                        String commentBy= (String) dssComment.child("commetBy").getValue();
                                        String commentUName= (String)dssComment.child("commentUName").getValue();
                                        String commentUAvatar= (String) dssComment.child("commentUAvatar").getValue();
                                        long commentTime=(long) dssComment.child("commentTime").getValue();

                                        ArrayList<Reply> replys= new ArrayList<>();
                                        if(dssComment.hasChild("replys")){
                                            for(DataSnapshot dssReply: dssComment.child("replys").getChildren()){
                                                String idRp= dssReply.getKey();
                                                String reply= (String) dssReply.child("reply").getValue();
                                                String replyBy= (String) dssReply.child("replyBy").getValue();
                                                String replyUName= (String)dssReply.child("replyUName").getValue();
                                                String replyUAvatar= (String) dssReply.child("replyUAvatar").getValue();
                                                long replyTime=(long) dssReply.child("replyTime").getValue();

                                                replys.add(new Reply(idRp,reply,replyBy,replyUName,replyUAvatar,replyTime));
                                            }
                                        }
                                        comments.add(new Comment(idCm,comment,commentBy,commentUName,commentUAvatar,commentTime,replys));
                                    }
                                }
                                if(dss.hasChild("wordbox")){
                                    DataSnapshot dssWB = dss.child("wordbox");
                                    WBG.setBoxName((String) dssWB.child("boxName").getValue());
                                    WBG.setCreateBy((String) dssWB.child("createBy").getValue());
                                    WBG.setCreatorName((String) dssWB.child("creatorName").getValue());
                                    WBG.setCreatorAvatar((String) dssWB.child("creatorAvatar").getValue());
                                    WBG.setCreatedAt((long) dssWB.child("createdAt").getValue());
                                    WBG.setUpdatedAt((long) dssWB.child("updatedAt").getValue()*(-1));

                                    ArrayList<Maker> makers = new ArrayList<>();
                                    if(dssWB.hasChild("makers")){
                                        for(DataSnapshot dssMaker: dssWB.child("makers").getChildren()){
                                            makers.add(new Maker(
                                                    (String) dssMaker.child("makerAvatar").getValue(),
                                                    (String) dssMaker.child("makerName").getValue(),
                                                    (String) dssMaker.child("makerUID").getValue()
                                            ));
                                        }
                                    }
                                    ArrayList<Word> words= new ArrayList<>();
                                    if(dssWB.hasChild("words")){
                                        for (DataSnapshot dssWord : dssWB.child("words").getChildren()) {
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
                                    WBG.setMakers(makers);
                                    WBG.setWords(words);
                                }
                                WBG.setLikes(likes); WBG.setLikeCount(likeCount);
                                WBG.setDisLikes(disLikes); WBG.setDislikeCount(dislikeCount);
                                WBG.setDownloads(downloads); WBG.setDownloadsCount(downloadsCount);
                                WBG.setComments(comments);
                                WBG.setId(dss.getKey());
                                listWBG.add(WBG);
                                addWBGFList(WBG);
                                wbgfRefCallback.onWordboxesGFRefSet(listWordBoxGF);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context,
                                    "Error: FB Something goes wrong.",
                                    Toast.LENGTH_SHORT).show();
                            wbgfRefCallback.onWordboxesGFRefFail();
                        }
                    });
        }
    }
    public void wordboxesGFLoadById(String FID){
        listWordBoxGF= new ArrayList<>();
        mDatabase.child("global").child("wordboxes").orderByChild("wordbox/createBy")
                .equalTo(FID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<WordBoxG> listWBG = new ArrayList<>();

                        for(DataSnapshot dss : dataSnapshot.getChildren()){
                            WordBoxG WBG= new WordBoxG();
                            ArrayList<String>likes= new ArrayList<>();
                            ArrayList<String>disLikes= new ArrayList<>();
                            ArrayList<String>downloads= new ArrayList<>();
                            ArrayList<Comment>comments=new ArrayList<>();

                            long downloadsCount=0, likeCount=0, dislikeCount=0;
                            if(dss.hasChild("downloadsCount")){
                                downloadsCount=(long) dss.child("downloadsCount").getValue();
                            }
                            if(dss.hasChild("likeCount")){
                                likeCount=(long) dss.child("likeCount").getValue();
                            }
                            if(dss.hasChild("dislikeCount")){
                                dislikeCount=(long) dss.child("dislikeCount").getValue();
                            }
                            if(dss.hasChild("like")){
                                for(DataSnapshot dssLikes: dss.child("like").getChildren()){
                                    likes.add(dssLikes.getKey());
                                }
                            }
                            if(dss.hasChild("dislike")){
                                for(DataSnapshot dssDisLikes: dss.child("dislike").getChildren()){
                                    disLikes.add(dssDisLikes.getKey());
                                }
                            }
                            if(dss.hasChild("downloads")){
                                for(DataSnapshot dssDownloads: dss.child("downloads").getChildren()){
                                    downloads.add(dssDownloads.getKey());
                                }
                            }
                            if(dss.hasChild("comments")){
                                for(DataSnapshot dssComment: dss.child("comments").getChildren()){
                                    String idCm= dssComment.getKey();
                                    String comment= (String) dssComment.child("comment").getValue();
                                    String commentBy= (String) dssComment.child("commetBy").getValue();
                                    String commentUName= (String)dssComment.child("commentUName").getValue();
                                    String commentUAvatar= (String) dssComment.child("commentUAvatar").getValue();
                                    long commentTime=(long) dssComment.child("commentTime").getValue();

                                    ArrayList<Reply> replys= new ArrayList<>();
                                    if(dssComment.hasChild("replys")){
                                        for(DataSnapshot dssReply: dssComment.child("replys").getChildren()){
                                            String idRp= dssReply.getKey();
                                            String reply= (String) dssReply.child("reply").getValue();
                                            String replyBy= (String) dssReply.child("replyBy").getValue();
                                            String replyUName= (String)dssReply.child("replyUName").getValue();
                                            String replyUAvatar= (String) dssReply.child("replyUAvatar").getValue();
                                            long replyTime=(long) dssReply.child("replyTime").getValue();

                                            replys.add(new Reply(idRp,reply,replyBy,replyUName,replyUAvatar,replyTime));
                                        }
                                    }
                                    comments.add(new Comment(idCm,comment,commentBy,commentUName,commentUAvatar,commentTime,replys));
                                }
                            }
                            if(dss.hasChild("wordbox")){
                                DataSnapshot dssWB = dss.child("wordbox");
                                WBG.setBoxName((String) dssWB.child("boxName").getValue());
                                WBG.setCreateBy((String) dssWB.child("createBy").getValue());
                                WBG.setCreatorName((String) dssWB.child("creatorName").getValue());
                                WBG.setCreatorAvatar((String) dssWB.child("creatorAvatar").getValue());
                                WBG.setCreatedAt((long) dssWB.child("createdAt").getValue());
                                WBG.setUpdatedAt((long) dssWB.child("updatedAt").getValue()*(-1));

                                ArrayList<Maker> makers = new ArrayList<>();
                                if(dssWB.hasChild("makers")){
                                    for(DataSnapshot dssMaker: dssWB.child("makers").getChildren()){
                                        makers.add(new Maker(
                                                (String) dssMaker.child("makerAvatar").getValue(),
                                                (String) dssMaker.child("makerName").getValue(),
                                                (String) dssMaker.child("makerUID").getValue()
                                        ));
                                    }
                                }
                                ArrayList<Word> words= new ArrayList<>();
                                if(dssWB.hasChild("words")){
                                    for (DataSnapshot dssWord : dssWB.child("words").getChildren()) {
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
                                WBG.setMakers(makers);
                                WBG.setWords(words);
                            }
                            WBG.setLikes(likes); WBG.setLikeCount(likeCount);
                            WBG.setDisLikes(disLikes); WBG.setDislikeCount(dislikeCount);
                            WBG.setDownloads(downloads); WBG.setDownloadsCount(downloadsCount);
                            WBG.setComments(comments);
                            WBG.setId(dss.getKey());
                            listWBG.add(WBG);
                            //addWBGFList(WBG);
                        }
                        setWordBoxGFIDList(listWBG);
                        wbgfRefCallback.onWordboxesGFRefSet(listWBG);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context,
                                "Error: FB Something goes wrong.",
                                Toast.LENGTH_SHORT).show();
                        wbgfRefCallback.onWordboxesGFRefFail();
                    }
                });
    }

    public void wordboxesGLoad(final String SORT_TYPE){
        listWordBoxG= new ArrayList<>();
        mDatabase.child("global").child("wordboxes").orderByChild(SORT_TYPE)
                .limitToFirst(6)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<WordBoxG> listWBG = new ArrayList<>();
                        WordBoxG WBG= new WordBoxG();
                        for(DataSnapshot dss : dataSnapshot.getChildren()){
                            WBG= new WordBoxG();
                            ArrayList<String>likes= new ArrayList<>();
                            ArrayList<String>disLikes= new ArrayList<>();
                            ArrayList<String>downloads= new ArrayList<>();
                            ArrayList<Comment>comments=new ArrayList<>();
                            long downloadsCount=0, likeCount=0, dislikeCount=0;

                            if(dss.hasChild("downloadsCount")){
                                downloadsCount=(long) dss.child("downloadsCount").getValue();
                            }
                            if(dss.hasChild("likeCount")){
                                likeCount=(long) dss.child("likeCount").getValue();
                            }
                            if(dss.hasChild("dislikeCount")){
                                dislikeCount=(long) dss.child("dislikeCount").getValue();
                            }
                            if(dss.hasChild("like")){
                                for(DataSnapshot dssLikes: dss.child("like").getChildren()){
                                    likes.add(dssLikes.getKey());
                                }
                            }
                            if(dss.hasChild("dislike")){
                                for(DataSnapshot dssDisLikes: dss.child("dislike").getChildren()){
                                    disLikes.add(dssDisLikes.getKey());
                                }
                            }
                            if(dss.hasChild("downloads")){
                                for(DataSnapshot dssDownloads: dss.child("downloads").getChildren()){
                                    downloads.add(dssDownloads.getKey());
                                }
                            }
                            if(dss.hasChild("comments")){
                                for(DataSnapshot dssComment: dss.child("comments").getChildren()){
                                    String idCm= dssComment.getKey();
                                    String comment= (String) dssComment.child("comment").getValue();
                                    String commentBy= (String) dssComment.child("commetBy").getValue();
                                    String commentUName= (String)dssComment.child("commentUName").getValue();
                                    String commentUAvatar= (String) dssComment.child("commentUAvatar").getValue();
                                    long commentTime=(long) dssComment.child("commentTime").getValue();

                                    ArrayList<Reply> replys= new ArrayList<>();
                                    if(dssComment.hasChild("replys")){
                                        for(DataSnapshot dssReply: dssComment.child("replys").getChildren()){
                                            String idRp= dssReply.getKey();
                                            String reply= (String) dssReply.child("reply").getValue();
                                            String replyBy= (String) dssReply.child("replyBy").getValue();
                                            String replyUName= (String)dssReply.child("replyUName").getValue();
                                            String replyUAvatar= (String) dssReply.child("replyUAvatar").getValue();
                                            long replyTime=(long) dssReply.child("replyTime").getValue();

                                            replys.add(new Reply(idRp,reply,replyBy,replyUName,replyUAvatar,replyTime));
                                        }
                                    }
                                    comments.add(new Comment(idCm,comment,commentBy,commentUName,commentUAvatar,commentTime,replys));
                                }
                            }
                            if(dss.hasChild("wordbox")){
                                DataSnapshot dssWB = dss.child("wordbox");
                                WBG.setBoxName((String) dssWB.child("boxName").getValue());
                                WBG.setCreateBy((String) dssWB.child("createBy").getValue());
                                WBG.setCreatorName((String) dssWB.child("creatorName").getValue());
                                WBG.setCreatorAvatar((String) dssWB.child("creatorAvatar").getValue());
                                WBG.setCreatedAt((long) dssWB.child("createdAt").getValue());
                                WBG.setUpdatedAt((long) dssWB.child("updatedAt").getValue()*(-1));

                                ArrayList<Maker> makers = new ArrayList<>();
                                if(dssWB.hasChild("makers")){
                                    for(DataSnapshot dssMaker: dssWB.child("makers").getChildren()){
                                        makers.add(new Maker(
                                                (String) dssMaker.child("makerAvatar").getValue(),
                                                (String) dssMaker.child("makerName").getValue(),
                                                (String) dssMaker.child("makerUID").getValue()
                                        ));
                                    }
                                }
                                ArrayList<Word> words= new ArrayList<>();
                                if(dssWB.hasChild("words")){
                                    for (DataSnapshot dssWord : dssWB.child("words").getChildren()) {
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
                                WBG.setMakers(makers);
                                WBG.setWords(words);
                            }
                            WBG.setLikes(likes); WBG.setLikeCount(likeCount);
                            WBG.setDisLikes(disLikes); WBG.setDislikeCount(dislikeCount);
                            WBG.setDownloads(downloads); WBG.setDownloadsCount(downloadsCount);
                            WBG.setComments(comments);
                            WBG.setId(dss.getKey());
                            listWBG.add(WBG);
                            setWordBoxGList(listWBG);


                        }
                        globalTags(SORT_TYPE,WBG);
                        wbgRefCallback.onWordboxesGRefSet(listWBG);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context,
                                "Error: FB Something goes wrong.",
                                Toast.LENGTH_SHORT).show();
                        wbgRefCallback.onWordboxesGRefFail();
                    }
                });

    }
    public void wordboxesGLoadById(){
        final FirebaseUser user = mAuth.getCurrentUser();
        listWordBoxGF= new ArrayList<>();
        mDatabase.child("global").child("wordboxes").orderByChild("wordbox/createBy")
                .equalTo(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<WordBoxG> listWBG = new ArrayList<>();

                        for(DataSnapshot dss : dataSnapshot.getChildren()){
                            WordBoxG WBG= new WordBoxG();
                            ArrayList<String>likes= new ArrayList<>();
                            ArrayList<String>disLikes= new ArrayList<>();
                            ArrayList<String>downloads= new ArrayList<>();
                            ArrayList<Comment>comments=new ArrayList<>();

                            long downloadsCount=0, likeCount=0, dislikeCount=0;
                            if(dss.hasChild("downloadsCount")){
                                downloadsCount=(long) dss.child("downloadsCount").getValue();
                            }
                            if(dss.hasChild("likeCount")){
                                likeCount=(long) dss.child("likeCount").getValue();
                            }
                            if(dss.hasChild("dislikeCount")){
                                dislikeCount=(long) dss.child("dislikeCount").getValue();
                            }
                            if(dss.hasChild("like")){
                                for(DataSnapshot dssLikes: dss.child("like").getChildren()){
                                    likes.add(dssLikes.getKey());
                                }
                            }
                            if(dss.hasChild("dislike")){
                                for(DataSnapshot dssDisLikes: dss.child("dislike").getChildren()){
                                    disLikes.add(dssDisLikes.getKey());
                                }
                            }
                            if(dss.hasChild("downloads")){
                                for(DataSnapshot dssDownloads: dss.child("downloads").getChildren()){
                                    downloads.add(dssDownloads.getKey());
                                }
                            }
                            if(dss.hasChild("comments")){
                                for(DataSnapshot dssComment: dss.child("comments").getChildren()){
                                    String idCm= dssComment.getKey();
                                    String comment= (String) dssComment.child("comment").getValue();
                                    String commentBy= (String) dssComment.child("commetBy").getValue();
                                    String commentUName= (String)dssComment.child("commentUName").getValue();
                                    String commentUAvatar= (String) dssComment.child("commentUAvatar").getValue();
                                    long commentTime=(long) dssComment.child("commentTime").getValue();

                                    ArrayList<Reply> replys= new ArrayList<>();
                                    if(dssComment.hasChild("replys")){
                                        for(DataSnapshot dssReply: dssComment.child("replys").getChildren()){
                                            String idRp= dssReply.getKey();
                                            String reply= (String) dssReply.child("reply").getValue();
                                            String replyBy= (String) dssReply.child("replyBy").getValue();
                                            String replyUName= (String)dssReply.child("replyUName").getValue();
                                            String replyUAvatar= (String) dssReply.child("replyUAvatar").getValue();
                                            long replyTime=(long) dssReply.child("replyTime").getValue();

                                            replys.add(new Reply(idRp,reply,replyBy,replyUName,replyUAvatar,replyTime));
                                        }
                                    }
                                    comments.add(new Comment(idCm,comment,commentBy,commentUName,commentUAvatar,commentTime,replys));
                                }
                            }
                            if(dss.hasChild("wordbox")){
                                DataSnapshot dssWB = dss.child("wordbox");
                                WBG.setBoxName((String) dssWB.child("boxName").getValue());
                                WBG.setCreateBy((String) dssWB.child("createBy").getValue());
                                WBG.setCreatorName((String) dssWB.child("creatorName").getValue());
                                WBG.setCreatorAvatar((String) dssWB.child("creatorAvatar").getValue());
                                WBG.setCreatedAt((long) dssWB.child("createdAt").getValue());
                                WBG.setUpdatedAt((long) dssWB.child("updatedAt").getValue()*(-1));

                                ArrayList<Maker> makers = new ArrayList<>();
                                if(dssWB.hasChild("makers")){
                                    for(DataSnapshot dssMaker: dssWB.child("makers").getChildren()){
                                        makers.add(new Maker(
                                                (String) dssMaker.child("makerAvatar").getValue(),
                                                (String) dssMaker.child("makerName").getValue(),
                                                (String) dssMaker.child("makerUID").getValue()
                                        ));
                                    }
                                }
                                ArrayList<Word> words= new ArrayList<>();
                                if(dssWB.hasChild("words")){
                                    for (DataSnapshot dssWord : dssWB.child("words").getChildren()) {
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
                                WBG.setMakers(makers);
                                WBG.setWords(words);
                            }
                            WBG.setLikes(likes); WBG.setLikeCount(likeCount);
                            WBG.setDisLikes(disLikes); WBG.setDislikeCount(dislikeCount);
                            WBG.setDownloads(downloads); WBG.setDownloadsCount(downloadsCount);
                            WBG.setComments(comments);
                            WBG.setId(dss.getKey());
                            listWBG.add(WBG);
                            addWBGFList(WBG);
                            wbgRefCallback.onWordboxesGRefSet(listWBG);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context,
                                "Error: FB Something goes wrong.",
                                Toast.LENGTH_SHORT).show();
                        wbgRefCallback.onWordboxesGRefFail();
                    }
                });
    }
    public void wordboxesGLoadMore(final String SORT_TYPE){
        //listWordBoxG= new ArrayList<>();
        Toast.makeText(context,"gTags: "+GlobalKeyName+" & "+GlobalKeyLoad,Toast.LENGTH_SHORT).show();


        mDatabase.child("global").child("wordboxes").orderByChild(SORT_TYPE)
                .startAt(GlobalKeyName,GlobalKeyLoad)

                .limitToFirst(6)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<WordBoxG> listWBG = new ArrayList<>();
                        WordBoxG WBG = new WordBoxG(); boolean limitFlag= true; int count=0;
                        for(DataSnapshot dss : dataSnapshot.getChildren()) {
                            count++;
                            if (!dss.getKey().equals(GlobalKeyLoad)) {
                                WBG = new WordBoxG();
                                ArrayList<String> likes = new ArrayList<>();
                                ArrayList<String> disLikes = new ArrayList<>();
                                ArrayList<String> downloads = new ArrayList<>();
                                ArrayList<Comment>comments=new ArrayList<>();
                                long downloadsCount = 0, likeCount = 0, dislikeCount = 0;

                                if (dss.hasChild("downloadsCount")) {
                                    downloadsCount = (long) dss.child("downloadsCount").getValue();
                                }
                                if (dss.hasChild("likeCount")) {
                                    likeCount = (long) dss.child("likeCount").getValue();
                                }
                                if (dss.hasChild("dislikeCount")) {
                                    dislikeCount = (long) dss.child("dislikeCount").getValue();
                                }
                                if (dss.hasChild("like")) {
                                    for (DataSnapshot dssLikes : dss.child("like").getChildren()) {
                                        likes.add(dssLikes.getKey());
                                    }
                                }
                                if (dss.hasChild("dislike")) {
                                    for (DataSnapshot dssDisLikes : dss.child("dislike").getChildren()) {
                                        disLikes.add(dssDisLikes.getKey());
                                    }
                                }
                                if (dss.hasChild("downloads")) {
                                    for (DataSnapshot dssDownloads : dss.child("downloads").getChildren()) {
                                        downloads.add(dssDownloads.getKey());
                                    }
                                }
                                if(dss.hasChild("comments")){
                                    for(DataSnapshot dssComment: dss.child("comments").getChildren()){
                                        String idCm= dssComment.getKey();
                                        String comment= (String) dssComment.child("comment").getValue();
                                        String commentBy= (String) dssComment.child("commetBy").getValue();
                                        String commentUName= (String)dssComment.child("commentUName").getValue();
                                        String commentUAvatar= (String) dssComment.child("commentUAvatar").getValue();
                                        long commentTime=(long) dssComment.child("commentTime").getValue();

                                        ArrayList<Reply> replys= new ArrayList<>();
                                        if(dssComment.hasChild("replys")){
                                            for(DataSnapshot dssReply: dssComment.child("replys").getChildren()){
                                                String idRp= dssReply.getKey();
                                                String reply= (String) dssReply.child("reply").getValue();
                                                String replyBy= (String) dssReply.child("replyBy").getValue();
                                                String replyUName= (String)dssReply.child("replyUName").getValue();
                                                String replyUAvatar= (String) dssReply.child("replyUAvatar").getValue();
                                                long replyTime=(long) dssReply.child("replyTime").getValue();

                                                replys.add(new Reply(idRp,reply,replyBy,replyUName,replyUAvatar,replyTime));
                                            }
                                        }
                                        comments.add(new Comment(idCm,comment,commentBy,commentUName,commentUAvatar,commentTime,replys));
                                    }
                                }
                                if (dss.hasChild("wordbox")) {
                                    DataSnapshot dssWB = dss.child("wordbox");
                                    WBG.setBoxName((String) dssWB.child("boxName").getValue());
                                    WBG.setCreateBy((String) dssWB.child("createBy").getValue());
                                    WBG.setCreatorName((String) dssWB.child("creatorName").getValue());
                                    WBG.setCreatorAvatar((String) dssWB.child("creatorAvatar").getValue());
                                    WBG.setCreatedAt((long) dssWB.child("createdAt").getValue());
                                    WBG.setUpdatedAt((long) dssWB.child("updatedAt").getValue() * (-1));

                                    ArrayList<Maker> makers = new ArrayList<>();
                                    if (dssWB.hasChild("makers")) {
                                        for (DataSnapshot dssMaker : dssWB.child("makers").getChildren()) {
                                            makers.add(new Maker(
                                                    (String) dssMaker.child("makerAvatar").getValue(),
                                                    (String) dssMaker.child("makerName").getValue(),
                                                    (String) dssMaker.child("makerUID").getValue()
                                            ));
                                        }
                                    }
                                    ArrayList<Word> words = new ArrayList<>();
                                    if (dssWB.hasChild("words")) {
                                        for (DataSnapshot dssWord : dssWB.child("words").getChildren()) {
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
                                    WBG.setMakers(makers);
                                    WBG.setWords(words);
                                }
                                WBG.setLikes(likes);
                                WBG.setLikeCount(likeCount);
                                WBG.setDisLikes(disLikes);
                                WBG.setDislikeCount(dislikeCount);
                                WBG.setDownloads(downloads);
                                WBG.setDownloadsCount(downloadsCount);
                                WBG.setComments(comments);
                                WBG.setId(dss.getKey());
                                listWBG.add(WBG);
                                addWBGList(WBG);
                                //wbgRefCallback.onWordboxesGRefSet(listWBG);
                            }
                        }
                        if(count>=6){
                            globalTags(SORT_TYPE,WBG);
                        }else{
                            limitFlag=false;
                        }

                        wbgRefCallback.onWordBoxesGRefAdd(listWordBoxG,limitFlag);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context,
                                "Error: FB Something goes wrong.",
                                Toast.LENGTH_SHORT).show();
                        wbgRefCallback.onWordboxesGRefFail();
                    }
                });

    }
    public void wordboxesGLoadMoreN(final String SORT_TYPE){
        //listWordBoxG= new ArrayList<>();
        Toast.makeText(context,"gTags: "+GlobalKeyName+" & "+GlobalKeyLoad,Toast.LENGTH_SHORT).show();

        double helper = Double.parseDouble(GlobalKeyName);
        if(SORT_TYPE.equals(SORT_UP)){
            helper=helper*(-1);
        }
        mDatabase.child("global").child("wordboxes").orderByChild(SORT_TYPE)
                .startAt(helper,GlobalKeyLoad)
                .limitToFirst(6)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<WordBoxG> listWBG = new ArrayList<>();
                        WordBoxG WBG = new WordBoxG(); boolean limitFlag= true; int count=0;
                        for(DataSnapshot dss : dataSnapshot.getChildren()) {
                            count++;
                            if (!dss.getKey().equals(GlobalKeyLoad)) {
                                WBG = new WordBoxG();
                                ArrayList<String> likes = new ArrayList<>();
                                ArrayList<String> disLikes = new ArrayList<>();
                                ArrayList<String> downloads = new ArrayList<>();
                                ArrayList<Comment>comments=new ArrayList<>();
                                long downloadsCount = 0, likeCount = 0, dislikeCount = 0;

                                if (dss.hasChild("downloadsCount")) {
                                    downloadsCount = (long) dss.child("downloadsCount").getValue();
                                }
                                if (dss.hasChild("likeCount")) {
                                    likeCount = (long) dss.child("likeCount").getValue();
                                }
                                if (dss.hasChild("dislikeCount")) {
                                    dislikeCount = (long) dss.child("dislikeCount").getValue();
                                }
                                if (dss.hasChild("like")) {
                                    for (DataSnapshot dssLikes : dss.child("like").getChildren()) {
                                        likes.add(dssLikes.getKey());
                                    }
                                }
                                if (dss.hasChild("dislike")) {
                                    for (DataSnapshot dssDisLikes : dss.child("dislike").getChildren()) {
                                        disLikes.add(dssDisLikes.getKey());
                                    }
                                }
                                if (dss.hasChild("downloads")) {
                                    for (DataSnapshot dssDownloads : dss.child("downloads").getChildren()) {
                                        downloads.add(dssDownloads.getKey());
                                    }
                                }
                                if(dss.hasChild("comments")){
                                    for(DataSnapshot dssComment: dss.child("comments").getChildren()){
                                        String idCm= dssComment.getKey();
                                        String comment= (String) dssComment.child("comment").getValue();
                                        String commentBy= (String) dssComment.child("commetBy").getValue();
                                        String commentUName= (String)dssComment.child("commentUName").getValue();
                                        String commentUAvatar= (String) dssComment.child("commentUAvatar").getValue();
                                        long commentTime=(long) dssComment.child("commentTime").getValue();

                                        ArrayList<Reply> replys= new ArrayList<>();
                                        if(dssComment.hasChild("replys")){
                                            for(DataSnapshot dssReply: dssComment.child("replys").getChildren()){
                                                String idRp= dssReply.getKey();
                                                String reply= (String) dssReply.child("reply").getValue();
                                                String replyBy= (String) dssReply.child("replyBy").getValue();
                                                String replyUName= (String)dssReply.child("replyUName").getValue();
                                                String replyUAvatar= (String) dssReply.child("replyUAvatar").getValue();
                                                long replyTime=(long) dssReply.child("replyTime").getValue();

                                                replys.add(new Reply(idRp,reply,replyBy,replyUName,replyUAvatar,replyTime));
                                            }
                                        }
                                        comments.add(new Comment(idCm,comment,commentBy,commentUName,commentUAvatar,commentTime,replys));
                                    }
                                }
                                if (dss.hasChild("wordbox")) {
                                    DataSnapshot dssWB = dss.child("wordbox");
                                    WBG.setBoxName((String) dssWB.child("boxName").getValue());
                                    WBG.setCreateBy((String) dssWB.child("createBy").getValue());
                                    WBG.setCreatorName((String) dssWB.child("creatorName").getValue());
                                    WBG.setCreatorAvatar((String) dssWB.child("creatorAvatar").getValue());
                                    WBG.setCreatedAt((long) dssWB.child("createdAt").getValue());
                                    WBG.setUpdatedAt((long) dssWB.child("updatedAt").getValue() * (-1));

                                    ArrayList<Maker> makers = new ArrayList<>();
                                    if (dssWB.hasChild("makers")) {
                                        for (DataSnapshot dssMaker : dssWB.child("makers").getChildren()) {
                                            makers.add(new Maker(
                                                    (String) dssMaker.child("makerAvatar").getValue(),
                                                    (String) dssMaker.child("makerName").getValue(),
                                                    (String) dssMaker.child("makerUID").getValue()
                                            ));
                                        }
                                    }
                                    ArrayList<Word> words = new ArrayList<>();
                                    if (dssWB.hasChild("words")) {
                                        for (DataSnapshot dssWord : dssWB.child("words").getChildren()) {
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
                                    WBG.setMakers(makers);
                                    WBG.setWords(words);
                                }
                                WBG.setLikes(likes);
                                WBG.setLikeCount(likeCount);
                                WBG.setDisLikes(disLikes);
                                WBG.setDislikeCount(dislikeCount);
                                WBG.setDownloads(downloads);
                                WBG.setDownloadsCount(downloadsCount);
                                WBG.setComments(comments);
                                WBG.setId(dss.getKey());
                                listWBG.add(WBG);
                                addWBGList(WBG);
                                //wbgRefCallback.onWordboxesGRefSet(listWBG);
                            }
                        }
                        if(count>=6){
                            globalTags(SORT_TYPE,WBG);
                        }else{
                            limitFlag=false;
                        }

                        wbgRefCallback.onWordBoxesGRefAdd(listWordBoxG,limitFlag);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context,
                                "Error: FB Something goes wrong.",
                                Toast.LENGTH_SHORT).show();
                        wbgRefCallback.onWordboxesGRefFail();
                    }
                });

    }



    public void onFrWordboxesActive( FrWbGFriend frWbGFriend){
        try{
            wbgfRefCallback=(WordboxesGF.WBGFRefCallback)frWbGFriend;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Wordboxes Interaction Listener");
        }
    }
    public void onFrWordboxesGActive( FrWbGlobal frWbGlobal){
        try{
            wbgRefCallback=(WordboxesGF.WBGRefCallback)frWbGlobal;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Wordboxes Interaction Listener");
        }
    }

    public void onFrWordboxesGUFActive(FrWbGUFriend frWbGUFriend){
        try{
            wbgfRefCallback=(WordboxesGF.WBGFRefCallback)frWbGUFriend;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Wordboxes Interaction Listener");
        }
    }

    public void onFrWordboxesGWActive(FrWbGWords frWbGWords){
        try{
            wbgwRefCallBack=(WordboxesGF.WBGWRefCallBack)frWbGWords;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Wordboxes Interaction Listener");
        }
    }


    public interface WBGFRefCallback {
        void onWordboxesGFRefSet( ArrayList<WordBoxG> listWordBoxGF);
        void onWordboxesGFRefFail();
    }

    public interface WBGRefCallback {
        void onWordboxesGRefSet( ArrayList<WordBoxG> listWordBoxG);
        void onWordBoxesGRefAdd(ArrayList<WordBoxG> listWordBoxG, boolean endData);
        void onWordboxesGRefFail();
    }

    public interface WBGWRefCallBack{
        void onWBWCommentRefSet(Comment comment);
        void onWBWCommentDLRefSet(String idCm);
        void onWBWReplyRefSet(String idCm,Reply reply);
        void onWBWReplyDLRefSet(String idCm, String idRp);
        void onWBWCommentRefFail();
        void onWBWStatusLKRefSet(int type);
    }
}
