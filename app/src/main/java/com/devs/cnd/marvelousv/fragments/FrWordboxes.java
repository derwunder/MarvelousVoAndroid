package com.devs.cnd.marvelousv.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
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
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.adapters.AdapterRcWB;
import com.devs.cnd.marvelousv.api.FilterAndSort;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.events.ClickCallBack;
import com.devs.cnd.marvelousv.events.ClickCallBackFr;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.firebase.Wordboxes;
import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;

import java.util.ArrayList;


public class FrWordboxes extends Fragment implements Wordboxes.WBRefCallback{

    //Firebase  Ref
    private Wordboxes wordboxes;


    private ClickCallBackMain clickCallBack;


    private ImageView logo;
    private MyApp myApp;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rcWordboxes;
    private AdapterRcWB adapterRcWB;


    private FilterAndSort filterAndSort;
    private ArrayList<WordBox> listWordBox;
    private boolean favorite=false, gBoard=false;
    private String currentSrh="";
    private int currentSort=901;
    private int SORT_AZ=901,SORT_RCNT=902,SORT_ADD=903;


    public FrWordboxes() {
        // Required empty public constructor
    }

    public  static FrWordboxes newInstance(){
        FrWordboxes frWordboxes=new FrWordboxes();
        /*Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);*/
        return frWordboxes;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterAndSort = new FilterAndSort();
        listWordBox = new ArrayList<>();
        favorite=false; gBoard=false;
        currentSrh="";
        currentSort=SORT_AZ;

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fr_wordboxes, menu);
        menu.findItem(R.id.search);
        menu.findItem(R.id.sortAZ).setChecked(true);
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
                adapterRcWB.setWordBoxList(
                        filterAndSort.WordBoxes(listWordBox,currentSrh,currentSort,favorite,gBoard));

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.favorite:{
                item.setChecked(!favorite);
                favorite=!favorite;
                adapterRcWB.setWordBoxList(
                        filterAndSort.WordBoxes(listWordBox,currentSrh,currentSort,favorite,gBoard));
            }break;
            case R.id.gBorad:{
                item.setChecked(!gBoard);
                gBoard=!gBoard;
                adapterRcWB.setWordBoxList(
                        filterAndSort.WordBoxes(listWordBox,currentSrh,currentSort,favorite,gBoard));
            }break;
            case R.id.sortAZ:{
                item.setChecked(true);
                currentSort=SORT_AZ;
                adapterRcWB.setWordBoxList(
                        filterAndSort.WordBoxes(listWordBox,currentSrh,currentSort,favorite,gBoard));
            }break;
            case R.id.sortRecent:{
                item.setChecked(true);
                currentSort=SORT_RCNT;
                adapterRcWB.setWordBoxList(
                        filterAndSort.WordBoxes(listWordBox,currentSrh,currentSort,favorite,gBoard));
            }break;
            case R.id.sortAdded:{
                item.setChecked(true);
                currentSort=SORT_ADD;
                adapterRcWB.setWordBoxList(
                        filterAndSort.WordBoxes(listWordBox,currentSrh,currentSort,favorite,gBoard));
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


        FrWordboxes frWordboxes =(FrWordboxes)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor_base);
        //.findFragmentByTag("FrWordboxes");

        /***** SCReen dimention ******/

        // Num Cols need it in the Grid
        int reGrid= colGridNeedIt();

        swipeRefresh=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myApp.wordboxes.wordboxesRefresh();
            }
        });




        rcWordboxes=(RecyclerView)rootView.findViewById(R.id.recycleView);
        adapterRcWB=new AdapterRcWB(getContext(),clickCallBack);

       /* GridLayoutManager manager = new GridLayoutManager(getActivity(),
                reGrid,GridLayoutManager.VERTICAL,false);*/

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(reGrid, StaggeredGridLayoutManager.VERTICAL);


        rcWordboxes.setLayoutManager(staggeredGridLayoutManager);

        //Espacio al final de la Vista de menu (RC)
        OffsetDecorationMenu offsetDecorationMenu =
                new OffsetDecorationMenu(75,5,getContext().getResources().getDisplayMetrics().density);
        rcWordboxes.addItemDecoration(offsetDecorationMenu); //pasep

        rcWordboxes.setNestedScrollingEnabled(false);
        rcWordboxes.setSoundEffectsEnabled(true);
        rcWordboxes.setAdapter(adapterRcWB);

        myApp =(MyApp)getContext().getApplicationContext();
        myApp.wordboxes.onFrWordboxesActive(frWordboxes);
        myApp.wordboxes.wordboxesLoad();

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


    /********************* WORD BOXES REF ***************************/
    @Override
    public void onWordboxesRefSet(ArrayList<WordBox> listWordBox) {
        this.listWordBox=listWordBox;

        adapterRcWB.setWordBoxList(
                filterAndSort.WordBoxes(listWordBox,currentSrh,currentSort,favorite,gBoard));

        if(swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }

    }

    @Override
    public void onWordboxesRefFail() {
        if(swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }

    }

    @Override
    public void onWBWordsRefSet(ArrayList<Word> listWord) {

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
