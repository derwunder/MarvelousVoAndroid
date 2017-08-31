package com.devs.cnd.marvelousv.api;

import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by wunder on 7/20/17.
 */

public class FilterAndSort {
    private int SORT_AZ=901,SORT_RCNT=902,SORT_ADD=903;

    public FilterAndSort() {
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
                if(fav){
                    if(wb.getFavorite())
                        filteredLt.add(wb);
                }else if(gBoard){
                    if(wb.getGBoard())
                        filteredLt.add(wb);
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
}





