package com.devs.cnd.marvelousv.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.adapters.AdapterRcWBW;
import com.devs.cnd.marvelousv.api.FilterAndSort;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.firebase.Wordboxes;
import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by wunder on 7/16/17.
 */


public class FrWbWords extends Fragment implements Wordboxes.WBRefCallback{

    private static final String ARG_WORDS_ID = "words_id";

    //Firebase  Ref
    private Wordboxes wordboxes;


    private ClickCallBackMain clickCallBack;


    private ImageView logo;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rcWbWords;
    private AdapterRcWBW adapterRcWBW;


    private FilterAndSort filterAndSort;
    private ArrayList<Word> listWord;
    private boolean bookmark=false;
    private String currentSrh="";


    public FrWbWords() {
        // Required empty public constructor
    }

    public  static FrWbWords newInstance(String wordsId){
        FrWbWords frWbWords=new FrWbWords();
        Bundle args = new Bundle();
        args.putString(ARG_WORDS_ID, wordsId);
        frWbWords.setArguments(args);
        return frWbWords;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterAndSort = new FilterAndSort();
        listWord = new ArrayList<>();
        bookmark=false;
        currentSrh="";

        setHasOptionsMenu(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        setMenuVisibility(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setMenuVisibility(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fr_words, menu);
        menu.findItem(R.id.search);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setBackgroundResource(R.drawable.bg_searchview);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String word) {
                //Toast.makeText(getContext(),word,Toast.LENGTH_LONG).show();
                //search
                //Intent search = new Intent(getActivity(), SearchResulsstsActivity.class);
                //search.putExtra("SEARCH", word);
                //startActivity(search);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
               // Toast.makeText(getContext(),arg0,Toast.LENGTH_LONG).show();
                currentSrh=arg0;
                adapterRcWBW.setWordList(filterAndSort.Words(listWord,currentSrh,bookmark));

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
            if (bookmark) {
                menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark_white_24dp);
            } else {
                menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark_border_white_24dp);
            }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.bookmark:{
               bookmark=!bookmark;
                adapterRcWBW.setWordList(filterAndSort.Words(listWord,currentSrh,bookmark));
               getActivity().invalidateOptionsMenu();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_wordboxes,container,false);
        //logo=(ImageView)rootView.findViewById(R.id.imgEmptyList);
        //logo.setColorFilter(Color.RED);


        FrWbWords frWordboxes =(FrWbWords)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor_base);
        //.findFragmentByTag("FrWordboxes");

        /***** SCReen dimention ******/

        // Num Cols need it in the Grid
        int reGrid= colGridNeedIt();

        swipeRefresh=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(false);


        rcWbWords =(RecyclerView)rootView.findViewById(R.id.recycleView);
        adapterRcWBW =new AdapterRcWBW(getContext(),clickCallBack);

       /* GridLayoutManager manager = new GridLayoutManager(getActivity(),
                reGrid,GridLayoutManager.VERTICAL,false);*/

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);


        rcWbWords.setLayoutManager(staggeredGridLayoutManager);

        //Espacio al final de la Vista de menu (RC)
        OffsetDecorationMenu offsetDecorationMenu =
                new OffsetDecorationMenu(75,5,getContext().getResources().getDisplayMetrics().density);
        rcWbWords.addItemDecoration(offsetDecorationMenu); //pasep*/

        rcWbWords.setNestedScrollingEnabled(false);
        rcWbWords.setSoundEffectsEnabled(true);
        rcWbWords.setAdapter(adapterRcWBW);

        MyApp myApp =(MyApp)getContext().getApplicationContext();
        myApp.wordboxes.onFrWbWordsActive(frWordboxes);

        listWord=filterAndSort.Words(myApp.wordboxes.getWords(getArguments().getString(ARG_WORDS_ID)),currentSrh,bookmark);
        adapterRcWBW.setWordList(
                filterAndSort.Words(listWord,currentSrh,bookmark)
        );
        adapterRcWBW.setWBID(getArguments().getString(ARG_WORDS_ID));
       // listWord=sortAZ(myApp.wordboxes.getWords(getArguments().getInt(ARG_WORDS_ID)));

        /*wordboxes=new Wordboxes(getContext());
        wordboxes.wordboxesLoad();
        wordboxes.onFrWordboxesActive(frWordboxes);*/




        return rootView;
    }


    private int colGridNeedIt(){
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);

        /*    SCREEN DENSITY
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int screenDensity = metrics.densityDpi;*/

        Display display =wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        //int height = size.y;

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, r.getDisplayMetrics());

        return Math.round(width/px);
    }


    //Sort Words
    private ArrayList<Word> sortAZ(ArrayList<Word> ltW){
        Collections.sort(ltW, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getWordTerm().compareTo(o2.getWordTerm());
            }
        });
        return ltW;
    }


    /********************* WORD BOXES REF ***************************/
    @Override
    public void onWordboxesRefSet(ArrayList<WordBox> listWordBox) {

    }
    @Override
    public void onWordboxesRefFail() {

    }

    @Override
    public void onWBWordsRefSet(ArrayList<Word> listWord) {
        this.listWord=filterAndSort.Words(listWord,currentSrh,bookmark);
        adapterRcWBW.setWordList(
                filterAndSort.Words(listWord,currentSrh,bookmark));
    }


    static class OffsetDecorationMenu extends RecyclerView.ItemDecoration {
        private int mBottomOffset;
        private int mTopOffset;
        public OffsetDecorationMenu(int bottomOffset, int topOffset, float density) {
            mBottomOffset =(int)(bottomOffset * density);
            mTopOffset = (int)(topOffset * density);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int position = parent.getChildAdapterPosition(view);



            StaggeredGridLayoutManager grid = (StaggeredGridLayoutManager)parent.getLayoutManager();
            /*if ((dataSize - position) <= grid.getSpanCount()) {
                outRect.set(0, 0, 0, mBottomOffset);
            } else {
                outRect.set(0, 0, 0, 0);
            }*/

            if((dataSize - position) <= grid.getSpanCount()){
                outRect.set(0, 0, 0, mBottomOffset);
            }

            /*if(parent.getChildAdapterPosition(view)==0){
                outRect.set(0, 0, 0, 0);
            }*/

        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            clickCallBack=(ClickCallBackMain) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Fragment Interaction Listener");
        }
    }
}
