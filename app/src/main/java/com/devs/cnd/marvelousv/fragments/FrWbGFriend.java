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
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.adapters.AdapterRcWB;
import com.devs.cnd.marvelousv.adapters.AdapterRcWBF;
import com.devs.cnd.marvelousv.api.FilterAndSort;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.firebase.WordboxesGF;
import com.devs.cnd.marvelousv.objects.WordBox;
import com.devs.cnd.marvelousv.objects.WordBoxG;

import java.util.ArrayList;

/**
 * Created by wunder on 9/16/17.
 */

public class FrWbGFriend extends Fragment implements WordboxesGF.WBGFRefCallback {
    private ClickCallBackMain clickCallBack;


    private MyApp myApp;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rcWordboxes;
    private AdapterRcWBF adapterRcWBF;

    private FilterAndSort filterAndSort;
    private ArrayList<WordBoxG> listWordBoxGF;
    private String currentSrh="";
    private int currentSort=901;
    private int SORT_AZ=901,SORT_FNAME=904;

    public FrWbGFriend() {
        // Required empty public constructor
    }

    public  static FrWbGFriend newInstance(){
        FrWbGFriend frWbGFriend=new FrWbGFriend();
        /*Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);*/
        return frWbGFriend;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterAndSort = new FilterAndSort();
        listWordBoxGF = new ArrayList<>();
        currentSrh="";

        myApp =(MyApp)getContext().getApplicationContext();


        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fr_wordboxes_gf, menu);
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
                Toast.makeText(getContext(),"Searching: "+word,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // Toast.makeText(getContext(),arg0,Toast.LENGTH_LONG).show();
                currentSrh=arg0;
                adapterRcWBF.setListWBG(
                        filterAndSort.WordBoxesGF(listWordBoxGF,currentSrh,currentSort));

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
            case R.id.sortAZ:{
                item.setChecked(true);
                currentSort=SORT_AZ;
                adapterRcWBF.setListWBG(
                        filterAndSort.WordBoxesGF(listWordBoxGF,currentSrh,currentSort));
            }break;
            case R.id.sortFriendName:{
                item.setChecked(true);
                currentSort=SORT_FNAME;
                adapterRcWBF.setListWBG(
                        filterAndSort.WordBoxesGF(listWordBoxGF,currentSrh,currentSort));
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


        FrWbGFriend frWbGFriend =(FrWbGFriend)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor_base);
        //.findFragmentByTag("FrWordboxes");

        /***** SCReen dimention ******/

        // Num Cols need it in the Grid
        int reGrid= colGridNeedIt();

        swipeRefresh=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myApp.wordboxesGF.wordboxesGFLoad(myApp.friendsRF.listFriend);
            }
        });




        rcWordboxes=(RecyclerView)rootView.findViewById(R.id.recycleView);
        adapterRcWBF=new AdapterRcWBF(getContext(),clickCallBack);

       /* GridLayoutManager manager = new GridLayoutManager(getActivity(),
                reGrid,GridLayoutManager.VERTICAL,false);*/

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(reGrid, StaggeredGridLayoutManager.VERTICAL);


        rcWordboxes.setLayoutManager(staggeredGridLayoutManager);

        //Espacio al final de la Vista de menu (RC)
        FrWbGFriend.OffsetDecorationMenu offsetDecorationMenu =
                new FrWbGFriend.OffsetDecorationMenu(75,5,getContext().getResources().getDisplayMetrics().density);
        rcWordboxes.addItemDecoration(offsetDecorationMenu); //pasep

        rcWordboxes.setNestedScrollingEnabled(false);
        rcWordboxes.setSoundEffectsEnabled(true);
        rcWordboxes.setAdapter(adapterRcWBF);


        myApp.wordboxesGF.onFrWordboxesActive(frWbGFriend);
        myApp.wordboxesGF.wordboxesGFLoad(myApp.friendsRF.listFriend);

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

    @Override
    public void onWordboxesGFRefSet(ArrayList<WordBoxG> listWordBoxGF) {
        this.listWordBoxGF=listWordBoxGF;

        adapterRcWBF.setListWBG(listWordBoxGF);
        adapterRcWBF.setListWBG(
                filterAndSort.WordBoxesGF(listWordBoxGF,currentSrh,currentSort));

        if(swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onWordboxesGFRefFail() {
        if(swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
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
