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
import com.devs.cnd.marvelousv.adapters.AdapterRcWBF;
import com.devs.cnd.marvelousv.adapters.AdapterRcWBUF;
import com.devs.cnd.marvelousv.api.FilterAndSort;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.firebase.WordboxesGF;
import com.devs.cnd.marvelousv.objects.WordBoxG;

import java.util.ArrayList;

/**
 * Created by wunder on 9/21/17.
 */

public class FrWbGUFriend extends Fragment implements WordboxesGF.WBGFRefCallback{

    private static final String ARG_FID = "fid";
    private ClickCallBackMain clickCallBack;


    private MyApp myApp;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rcWordboxes;
    private AdapterRcWBUF adapterRcWBUF;

    private FilterAndSort filterAndSort;
    private ArrayList<WordBoxG> listWordBoxGF;
    private String currentSrh="";

    public FrWbGUFriend() {
        // Required empty public constructor
    }

    public  static FrWbGUFriend newInstance(String fid){
        FrWbGUFriend frWbGUFriend=new FrWbGUFriend();
        Bundle args = new Bundle();
        args.putString(ARG_FID, fid);
        frWbGUFriend.setArguments(args);
        return frWbGUFriend;
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
        menu.findItem(R.id.sort).setVisible(false);
       // menu.findItem(R.id.sortAZ).setChecked(true);
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
                adapterRcWBUF.setListWBG(
                        filterAndSort.WordBoxesGFU(listWordBoxGF,currentSrh));

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
       /* switch (id){
            case R.id.sortAZ:{
                item.setChecked(true);
                currentSort=SORT_AZ;
                adapterRcWBUF.setListWBG(
                        filterAndSort.WordBoxesGF(listWordBoxGF,currentSrh,currentSort));
            }break;
            case R.id.sortFriendName:{
                item.setChecked(true);
                currentSort=SORT_FNAME;
                adapterRcWBUF.setListWBG(
                        filterAndSort.WordBoxesGF(listWordBoxGF,currentSrh,currentSort));
            }break;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_wordboxes,container,false);
        //logo=(ImageView)rootView.findViewById(R.id.imgEmptyList);
        //logo.setColorFilter(Color.RED);


        FrWbGUFriend frWbGUFriend =(FrWbGUFriend)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor_base);
        //.findFragmentByTag("FrWordboxes");

        /***** SCReen dimention ******/

        // Num Cols need it in the Grid
        int reGrid= colGridNeedIt();

        swipeRefresh=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myApp.wordboxesGF.wordboxesGFLoadById(getArguments().getString(ARG_FID));
            }
        });




        rcWordboxes=(RecyclerView)rootView.findViewById(R.id.recycleView);
        adapterRcWBUF =new AdapterRcWBUF(getContext(),clickCallBack);

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
        rcWordboxes.setAdapter(adapterRcWBUF);


        adapterRcWBUF.setFID(getArguments().getString(ARG_FID));
        myApp.wordboxesGF.onFrWordboxesGUFActive(frWbGUFriend);
        myApp.wordboxesGF.wordboxesGFLoadById(getArguments().getString(ARG_FID));

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

        adapterRcWBUF.setListWBG(listWordBoxGF);
        adapterRcWBUF.setListWBG(
                filterAndSort.WordBoxesGFU(listWordBoxGF,currentSrh));

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
