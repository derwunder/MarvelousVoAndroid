package com.devs.cnd.marvelousv.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.adapters.AdapterRcFL;
import com.devs.cnd.marvelousv.api.FilterAndSort;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.firebase.FriendsRF;
import com.devs.cnd.marvelousv.objects.Friend;
import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;

import java.util.ArrayList;

/**
 * Created by wunder on 9/11/17.
 */

public class FrFriendList extends Fragment implements FriendsRF.FriendsRefCallback{

    private ClickCallBackMain clickCallBack;
    private RecyclerView rcFL;

    private SwipeRefreshLayout swipeRefresh;
    private AdapterRcFL adapterRcFL;

    private FilterAndSort filterAndSort;
    private ArrayList<Friend> listFriend;
    private String currentSrh="";


    public FrFriendList() {
        // Required empty public constructor
    }

    public  static FrFriendList newInstance(){
        FrFriendList frFriendList=new FrFriendList();
        /*Bundle args = new Bundle();
        args.putString(ARG_WORDS_ID, wordsId);
        frWbWords.setArguments(args);*/
        return frFriendList;
    }

    private FrFriendList frFriendList;
    public void getInstance(FrFriendList frFriendList){
        this.frFriendList=frFriendList;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterAndSort = new FilterAndSort();
        listFriend = new ArrayList<>();
        currentSrh="";

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fr_words, menu);
        menu.findItem(R.id.bookmark).setVisible(false);
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
                adapterRcFL.setFriendlist(filterAndSort.Friends(listFriend,currentSrh));

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        /*if (bookmark) {
            menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark_white_24dp);
        } else {
            menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark_border_white_24dp);
        }*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       /* switch (id){
            case R.id.bookmark:{
                bookmark=!bookmark;
                adapterRcWBW.setWordList(filterAndSort.Words(listWord,currentSrh,bookmark));
                getActivity().invalidateOptionsMenu();
            }break;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_wordboxes, container, false);

       /* FrFriendList frFriendList =(FrFriendList)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor_base);*/



        swipeRefresh=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(false);

        rcFL =(RecyclerView)rootView.findViewById(R.id.recycleView);
        adapterRcFL =new AdapterRcFL(getContext(),clickCallBack);

       /* GridLayoutManager manager = new GridLayoutManager(getActivity(),
                reGrid,GridLayoutManager.VERTICAL,false);*/

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);


        rcFL.setLayoutManager(staggeredGridLayoutManager);

        //Espacio al final de la Vista de menu (RC)
        OffsetDecorationMenu offsetDecorationMenu =
                new OffsetDecorationMenu(75,5,getContext().getResources().getDisplayMetrics().density);
        rcFL.addItemDecoration(offsetDecorationMenu); //pasep*/

        rcFL.setNestedScrollingEnabled(false);
        rcFL.setSoundEffectsEnabled(true);
        rcFL.setAdapter(adapterRcFL);

        MyApp myApp =(MyApp)getContext().getApplicationContext();
        myApp.friendsRF.onFrFriendListActive(frFriendList);
        myApp.friendsRF.FriendsLoad(frFriendList);


        /*listFriend=filterAndSort.Friends(myApp.friendsRF.getWords(getArguments().getString(ARG_WORDS_ID)),currentSrh);
        adapterRcWBW.setWordList(
                filterAndSort.Words(listWord,currentSrh,bookmark)
        );*/

        //adapterRcFL.setWBID(getArguments().getString(ARG_WORDS_ID));


        return rootView;
    }


        /****************** FriendRF CALL BACK ****************/
    @Override
    public void onFriendsRefSet(ArrayList<Friend> listFriend) {
        this.listFriend=listFriend;

        adapterRcFL.setFriendlist(
                filterAndSort.Friends(listFriend,currentSrh));

    }

    @Override
    public void onFriendsRefFail() {

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
