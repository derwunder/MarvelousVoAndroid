package com.devs.cnd.marvelousv.api;

import com.devs.cnd.marvelousv.objects.Comment;
import com.devs.cnd.marvelousv.objects.Friend;
import com.devs.cnd.marvelousv.objects.FriendRq;
import com.devs.cnd.marvelousv.objects.Reply;
import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;
import com.devs.cnd.marvelousv.objects.WordBoxG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by wunder on 7/20/17.
 */

public class FilterAndSort {
    private int SORT_AZ=901,SORT_RCNT=902,SORT_ADD=903, SORT_FNAME=904;
    private int SORT_DL=905, SORT_LK=906,SORT_UP=907,SORT_PS=908;

    public FilterAndSort() {
    }

    public  ArrayList<FriendRq> FriendRqs(ArrayList<FriendRq> ltFriendRq, String txSrh){
        ArrayList<FriendRq> filteredLt = new ArrayList<>();

        for(FriendRq rq : ltFriendRq){
            boolean flMatch= rq.getReqUName().toLowerCase().contains(txSrh.toLowerCase());
            boolean flMatchE= rq.getReqUEmail().toLowerCase().contains(txSrh.toLowerCase());
            if(flMatch){
                filteredLt.add(rq);
            }else if(flMatchE){
                filteredLt.add(rq);
            }
        }
        Collections.sort(filteredLt, new Comparator<FriendRq>() {
            @Override
            public int compare(FriendRq o1, FriendRq o2) {
                if(o1.getReqUTime()<o2.getReqUTime()){
                    return 1;
                }else if(o1.getReqUTime()>o2.getReqUTime()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        return filteredLt;
    }
    public ArrayList<Friend> Friends(ArrayList<Friend> ltFriend, String txSrh){
        ArrayList<Friend> filteredLt= new ArrayList<>();

        for(Friend f : ltFriend){
            boolean flMatch= f.getFrName().toLowerCase().contains(txSrh.toLowerCase());
            boolean flMatchE= f.getFrEmail().toLowerCase().contains(txSrh.toLowerCase());
            if(flMatch){
                filteredLt.add(f);
            }else if(flMatchE){
                filteredLt.add(f);
            }
        }
        Collections.sort(filteredLt, new Comparator<Friend>() {
            @Override
            public int compare(Friend o1, Friend o2) {
                return o1.getFrName().toLowerCase().compareTo(o2.getFrName().toLowerCase());
            }
        });
        return filteredLt;
    }

    public ArrayList<Word> Words(ArrayList<Word> ltWords, String txSrh, boolean bookmark) {
        ArrayList<Word> filteredLt = new ArrayList<>();
        for(Word w: ltWords){
            boolean flMatch= w.getWordTerm().toLowerCase().contains(txSrh.toLowerCase());
            if(flMatch){
                if(bookmark){
                    if(w.getBookmark())
                        filteredLt.add(w);
                }else{
                    filteredLt.add(w);
                }
            }
        }

        Collections.sort(filteredLt, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getWordTerm().toLowerCase().compareTo(o2.getWordTerm().toLowerCase());
            }
        });
        return filteredLt;
    }

    public ArrayList<WordBox> WordBoxes(ArrayList<WordBox> ltWB,String txSrh, int sort, boolean fav, boolean gBoard){
        ArrayList<WordBox> filteredLt = new ArrayList<>();

        for (WordBox wb: ltWB){
            boolean flMatch= wb.getBoxName().toLowerCase().contains(txSrh.toLowerCase());
            if(flMatch){
                if(fav || gBoard){
                    if(wb.getFavorite() && wb.getGBoard() && fav && gBoard){
                        filteredLt.add(wb);
                    }else if(wb.getFavorite() && fav && !gBoard){
                        filteredLt.add(wb);
                    }else if(wb.getGBoard() && gBoard && !fav){
                        filteredLt.add(wb);
                    }
                }else{
                    filteredLt.add(wb);
                }
            }
        }

        if(sort==SORT_AZ){
            Collections.sort(filteredLt, new Comparator<WordBox>() {
                @Override
                public int compare(WordBox o1, WordBox o2) {
                    return o1.getBoxName().toLowerCase().compareTo(o2.getBoxName().toLowerCase());
                }
            });
        }else if(sort==SORT_RCNT){
            Collections.sort(filteredLt, new Comparator<WordBox>() {
                @Override
                public int compare(WordBox o1, WordBox o2) {
                    if(o1.getLastCheckedAt()<o2.getLastCheckedAt()){
                        return 1;
                    }else if(o1.getLastCheckedAt()>o2.getLastCheckedAt()){
                        return -1;
                    }else{
                        return 0;
                    }
                }
            });
        }else if(sort==SORT_ADD){
            Collections.sort(filteredLt, new Comparator<WordBox>() {
                @Override
                public int compare(WordBox o1, WordBox o2) {
                    if(o1.getCreatedAt()<o2.getCreatedAt()){
                        return 1;
                    }else if(o1.getCreatedAt()>o2.getCreatedAt()){
                        return -1;
                    }else{
                        return 0;
                    }
                }
            });
        }




        return filteredLt;
    }
    public ArrayList<WordBoxG> WordBoxesGF(ArrayList<WordBoxG> ltWBGF, String txSrh, int sort){
        ArrayList<WordBoxG> filteredLt= new ArrayList<>();

        for(WordBoxG wb: ltWBGF){
            boolean flMatch= wb.getBoxName().toLowerCase().contains(txSrh.toLowerCase());
            boolean flMatch2= wb.getCreatorName().toLowerCase().contains(txSrh.toLowerCase());
            if(flMatch){
                filteredLt.add(wb);
            }else if(flMatch2){
                filteredLt.add(wb);
            }
        }

        if(sort==SORT_AZ){
            Collections.sort(filteredLt, new Comparator<WordBoxG>() {
                @Override
                public int compare(WordBoxG o1, WordBoxG o2) {
                    return o1.getBoxName().toLowerCase().compareTo(o2.getBoxName().toLowerCase());
                }
            });
        }else if(sort==SORT_FNAME){
            Collections.sort(filteredLt, new Comparator<WordBoxG>() {
                @Override
                public int compare(WordBoxG o1, WordBoxG o2) {
                    return o1.getCreatorName().toLowerCase().compareTo(o2.getCreatorName().toLowerCase());
                }
            });
        }
        return filteredLt;
    }
    public ArrayList<WordBoxG> WordBoxesGFU(ArrayList<WordBoxG> ltWBGF, String txSrh){
        ArrayList<WordBoxG> filteredLt= new ArrayList<>();

        for(WordBoxG wb: ltWBGF){
            boolean flMatch= wb.getBoxName().toLowerCase().contains(txSrh.toLowerCase());
            if(flMatch){
                filteredLt.add(wb);
            }
        }

        Collections.sort(filteredLt, new Comparator<WordBoxG>() {
            @Override
            public int compare(WordBoxG o1, WordBoxG o2) {
               return o1.getBoxName().toLowerCase().compareTo(o2.getBoxName().toLowerCase());
            }
        });

        return filteredLt;
    }

    public ArrayList<Comment> Comments(ArrayList<Comment> ltCM){
        ArrayList<Comment> filteredLt = new ArrayList<>();

        for(Comment c: ltCM){
            filteredLt.add(c);
        }
        Collections.sort(filteredLt, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                if(o1.getCommentTime()<o2.getCommentTime()){
                    return 1;
                }else if(o1.getCommentTime()>o2.getCommentTime()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        return  filteredLt;
    }
    public ArrayList<Reply> Replys(ArrayList<Reply> ltRP){
        ArrayList<Reply> filteredLt = new ArrayList<>();

        for(Reply r: ltRP){
            filteredLt.add(r);
        }
        Collections.sort(filteredLt, new Comparator<Reply>() {
            @Override
            public int compare(Reply o1, Reply o2) {
                if(o1.getReplyTime()<o2.getReplyTime()){
                    return 1;
                }else if(o1.getReplyTime()>o2.getReplyTime()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        return  filteredLt;
    }

}





