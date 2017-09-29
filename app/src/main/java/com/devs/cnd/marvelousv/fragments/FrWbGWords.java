package com.devs.cnd.marvelousv.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.adapters.AdapterRcComment;
import com.devs.cnd.marvelousv.adapters.AdapterRcWBGW;
import com.devs.cnd.marvelousv.api.FilterAndSort;
import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.customview.RoundImage;
import com.devs.cnd.marvelousv.dialogs.DialogWBAdd;
import com.devs.cnd.marvelousv.dialogs.DialogWBGSave;
import com.devs.cnd.marvelousv.events.ClickCallBackMain;
import com.devs.cnd.marvelousv.firebase.WordboxesGF;
import com.devs.cnd.marvelousv.objects.Comment;
import com.devs.cnd.marvelousv.objects.Reply;
import com.devs.cnd.marvelousv.objects.WordBoxG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wunder on 9/23/17.
 */

public class FrWbGWords extends Fragment implements WordboxesGF.WBGWRefCallBack {

    private static final String ARG_WBGID = "wbgid";
    private static final String ARG_PREV_FR="prevFR";

    private int FR_WBG_FRIEND=3004, FR_WB_GLOBAL=3006;
    private int FR_WBGU_FRIEND= 3044;


    private ClickCallBackMain clickCallBack;

    InputMethodManager imm;

    private MyApp myApp;
    private FirebaseAuth mAuth;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rcWBGWords;
    private RecyclerView rcWBGComments;
    private AdapterRcWBGW adapterRcWBGW;
    private AdapterRcComment adapterRcComment;
    private LinearLayout linearBasicInfo;

    private FilterAndSort filterAndSort;
    private WordBoxG wordBoxG;
    private boolean bookmark=false;
    private String currentSrh="";

    ImageView icLike, icDislike;
    TextView txLike, txDislike;
    private int TYPE_LK=30,TYPE_DLK=31;


    public FrWbGWords() {
        // Required empty public constructor
    }

    public  static FrWbGWords newInstance(String id, int mainFr){
        FrWbGWords frWbGWords=new FrWbGWords();
        Bundle args = new Bundle();
        args.putString(ARG_WBGID, id);
        args.putInt(ARG_PREV_FR,mainFr);
        frWbGWords.setArguments(args);
        return frWbGWords;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterAndSort = new FilterAndSort();
        wordBoxG = new WordBoxG();
        currentSrh="";

        myApp =(MyApp)getContext().getApplicationContext();
        mAuth = FirebaseAuth.getInstance();

        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fr_words, menu);
        menu.findItem(R.id.search);

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

                adapterRcWBGW.setWordList(filterAndSort.Words(wordBoxG.getWords(),currentSrh,bookmark));
                if(currentSrh.equals(""))
                    linearBasicInfo.setVisibility(View.VISIBLE);
                else
                    linearBasicInfo.setVisibility(View.GONE);

                /*adapterRcWBGW.setListWBG(
                        filterAndSort.WordBoxesGFU(wordBoxG,currentSrh));*/

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
        }    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.bookmark:{
                bookmark=!bookmark;
                adapterRcWBGW.setWordList(filterAndSort.Words(wordBoxG.getWords(),currentSrh,bookmark));
                getActivity().invalidateOptionsMenu();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_wordboxes_g,container,false);
        //logo=(ImageView)rootView.findViewById(R.id.imgEmptyList);
        //logo.setColorFilter(Color.RED);


        FrWbGWords frWbGWords =(FrWbGWords)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor_base);
        //.findFragmentByTag("FrWordboxes");

        /***** SCReen dimention ******/

        // Num Cols need it in the Grid
        int reGrid= colGridNeedIt();

        if(getArguments().getInt(ARG_PREV_FR)==FR_WB_GLOBAL){
            wordBoxG= myApp.wordboxesGF.getWordBoxG(getArguments().getString(ARG_WBGID));
        }else if(getArguments().getInt(ARG_PREV_FR)==FR_WBG_FRIEND){
            wordBoxG= myApp.wordboxesGF.getWordBoxGF(getArguments().getString(ARG_WBGID));
        }else if(getArguments().getInt(ARG_PREV_FR)==FR_WBGU_FRIEND){
            wordBoxG= myApp.wordboxesGF.getWordBoxGF(getArguments().getString(ARG_WBGID));
        }



        iniBoxInfo(rootView);
        iniCommentBox(rootView);

        swipeRefresh=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
       /* swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //myApp.wordboxesGF.wordboxesGFLoadById(getArguments().getString(ARG_FID));
            }
        });*/
        swipeRefresh.setEnabled(false);





        rcWBGWords =(RecyclerView)rootView.findViewById(R.id.recycleView);
        adapterRcWBGW =new AdapterRcWBGW(getContext(),clickCallBack);

       /* GridLayoutManager manager = new GridLayoutManager(getActivity(),
                reGrid,GridLayoutManager.VERTICAL,false);*/

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);


        rcWBGWords.setLayoutManager(staggeredGridLayoutManager);

        //Espacio al final de la Vista de menu (RC)
        FrWbGFriend.OffsetDecorationMenu offsetDecorationMenu =
                new FrWbGFriend.OffsetDecorationMenu(5,5,getContext().getResources().getDisplayMetrics().density);
        FrWbGFriend.OffsetDecorationMenu offsetDecorationMenu2 =
                new FrWbGFriend.OffsetDecorationMenu(75,5,getContext().getResources().getDisplayMetrics().density);
        rcWBGWords.addItemDecoration(offsetDecorationMenu); //pasep

        rcWBGWords.setNestedScrollingEnabled(false);
        rcWBGWords.setSoundEffectsEnabled(true);
        rcWBGWords.setAdapter(adapterRcWBGW);

        adapterRcWBGW.setWordList(filterAndSort.Words(wordBoxG.getWords(),currentSrh,bookmark));
        //adapterRcWBGW.setFID(getArguments().getString(ARG_FID));

        /*Toast.makeText(getContext(),
                "comment: "+wordBoxG.getComments().get(0).getComment(),Toast.LENGTH_SHORT).show();*/
        //myApp.wordboxesGF.onFrWordboxesGUFActive(frWbGWords);
        //myApp.wordboxesGF.wordboxesGFLoadById(getArguments().getString(ARG_FID));

        /*wordboxes=new Wordboxes(getContext());
        wordboxes.wordboxesLoad();
        wordboxes.onFrWordboxesActive(frWordboxes);*/

        rcWBGComments=(RecyclerView)rootView.findViewById(R.id.recycleView2);
        adapterRcComment=new AdapterRcComment(getContext(),clickCallBack);
        StaggeredGridLayoutManager staggeredGridLayoutManager2 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rcWBGComments.setLayoutManager(staggeredGridLayoutManager2);
        rcWBGComments.addItemDecoration(offsetDecorationMenu2);
        rcWBGComments.setNestedScrollingEnabled(false);
        rcWBGComments.setSoundEffectsEnabled(true);
        rcWBGComments.setAdapter(adapterRcComment);
        adapterRcComment.setIdWBG(wordBoxG.getId());
        adapterRcComment.setCommentlist(filterAndSort.Comments(wordBoxG.getComments()));


        myApp.wordboxesGF.onFrWordboxesGWActive(frWbGWords);

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


    private void iniBoxInfo(View v){
        final FirebaseUser user = mAuth.getCurrentUser();

        ImageView uPhoto;
        TextView uName, txTitle, txWords, txDownloads, txTime;

        FloatingActionButton dlBox;

        linearBasicInfo=(LinearLayout)v.findViewById(R.id.linearBasicInfo);
        uPhoto= (ImageView)v.findViewById(R.id.UPhoto);
        icLike=(ImageView)v.findViewById(R.id.icLike);
        icDislike=(ImageView)v.findViewById(R.id.icDislike);
        uName=(TextView)v.findViewById(R.id.UName);
        txTitle=(TextView)v.findViewById(R.id.txTitle);
        txWords=(TextView)v.findViewById(R.id.txWords);
        txDownloads=(TextView)v.findViewById(R.id.txDownloads);
        txTime=(TextView)v.findViewById(R.id.txTime);
        txLike=(TextView)v.findViewById(R.id.txLike);
        txDislike=(TextView)v.findViewById(R.id.txDislike);

        dlBox=(FloatingActionButton)v.findViewById(R.id.dlBox);

        Picasso.with(getContext()).load(wordBoxG.getCreatorAvatar())
                .transform(new RoundImage()).into(uPhoto);

        uName.setText(wordBoxG.getCreatorName());
        txTitle.setText(wordBoxG.getBoxName());

        String txW=wordBoxG.getWords().size()+" Words";
        txWords.setText(txW);

        String txDL=wordBoxG.getDownloads().size()+" Downloads";
        txDownloads.setText(txDL);

        String txLK=wordBoxG.getLikes().size()+"";
        txLike.setText(txLK);

        String txDLK=wordBoxG.getDisLikes().size()+"";
        txDislike.setText(txDLK);

        PrettyTime p = new PrettyTime();
        txTime.setText(p.format(new Date(wordBoxG.getUpdatedAt())));

        checkLkStatus(icLike,icDislike);
        icLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myApp.wordboxesGF.postNewLKStatus(wordBoxG.getId(),TYPE_LK,wordBoxG);
            }
        });
        icDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myApp.wordboxesGF.postNewLKStatus(wordBoxG.getId(),TYPE_DLK,wordBoxG);
            }
        });


        dlBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DialogWBGSave dialogWBGSave = new DialogWBGSave();
                dialogWBGSave.setWordBoxGToSave(wordBoxG);
                dialogWBGSave.show(fragmentManager, "dialog");


                /*Snackbar.make(v, "New Wordbox", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        if(wordBoxG.getCreateBy().equals(user.getUid()))
            dlBox.setVisibility(View.GONE);
    }
    private void iniCommentBox(View v){
        final LinearLayout commentBox,LinearBttOn;
        final ImageView UAvatar;
        final EditText UComment;
        Button btComment, btCancelCm;

        final NestedScrollView nesScroll;
        nesScroll=(NestedScrollView)v.findViewById(R.id.nesScroll);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        LinearBttOn=(LinearLayout)v.findViewById(R.id.LinearBttOn);
        commentBox=(LinearLayout)v.findViewById(R.id.commentBox);
        UAvatar=(ImageView)v.findViewById(R.id.UAvatar);
        UComment=(EditText)v.findViewById(R.id.UComment);
        btCancelCm=(Button)v.findViewById(R.id.btCancelCm);
        btComment=(Button)v.findViewById(R.id.btComment);

        FirebaseUser user = mAuth.getCurrentUser();

        UComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    TranslateAnimation animate = new TranslateAnimation(0,0,LinearBttOn.getWidth(),0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    LinearBttOn.startAnimation(animate);
                    LinearBttOn.setVisibility(View.VISIBLE);

              }else if(!hasFocus ){
                    TranslateAnimation animate = new TranslateAnimation(0,0,0,LinearBttOn.getWidth());
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    LinearBttOn.startAnimation(animate);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LinearBttOn.setVisibility(View.GONE);
                        }
                    }, 550);

                }
            }
        });
        btCancelCm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UComment.setFocusableInTouchMode(false);
                UComment.setFocusable(false);
                UComment.setFocusableInTouchMode(true);
                UComment.setFocusable(true);
                UComment.setText("");
               //UComment.setSelected(false);

                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });

        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UComment.setFocusableInTouchMode(false);
                UComment.setFocusable(false);
                UComment.setFocusableInTouchMode(true);
                UComment.setFocusable(true);

                String txComment= UComment.getText().toString();

                myApp.wordboxesGF.postComment(wordBoxG.getId(),txComment);
                UComment.setText("");
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });

        Picasso.with(getContext()).load(user.getPhotoUrl())
                .transform(new RoundImage()).into(UAvatar);

    }

    private void checkLkStatus(ImageView icLK, ImageView icDLK){
        FirebaseUser user = mAuth.getCurrentUser();

        for(String s: wordBoxG.getLikes()){
            if(s.equals(user.getUid())){
                icLK.setColorFilter(
                        ResourcesCompat.getColor(getContext().getResources(),R.color.teal600,null));
            }
        }
        for(String s:wordBoxG.getDisLikes()){
            if(s.equals(user.getUid())){
                icDLK.setColorFilter(
                        ResourcesCompat.getColor(getContext().getResources(),R.color.teal600,null));
            }
        }
    }
    private void newLkStatus(int type){
        FirebaseUser user = mAuth.getCurrentUser();

        if(type==TYPE_LK){
            boolean flLK=false;
            for(int i=0; i<wordBoxG.getLikes().size();i++){
                if(wordBoxG.getLikes().get(i).equals(user.getUid())){
                    flLK=true;
                }
            }
            if(!flLK)
                wordBoxG.getLikes().add(user.getUid());

            for(int i=0;i<wordBoxG.getDisLikes().size();i++){
                if(wordBoxG.getDisLikes().get(i).equals(user.getUid())){
                    wordBoxG.getDisLikes().remove(i);
                }
            }
            String txLK=wordBoxG.getLikes().size()+"";
            txLike.setText(txLK);

            String txDLK=wordBoxG.getDisLikes().size()+"";
            txDislike.setText(txDLK);
            icLike.setColorFilter(
                    ResourcesCompat.getColor(getContext().getResources(),R.color.teal600,null));
            icDislike.setColorFilter(
                    ResourcesCompat.getColor(getContext().getResources(),R.color.grey600,null));
        }
        else if(type==TYPE_DLK){
            boolean flDLK=false;
            for(int i=0; i<wordBoxG.getDisLikes().size();i++){
                if(wordBoxG.getDisLikes().get(i).equals(user.getUid())){
                    flDLK=true;
                }
            }
            if(!flDLK)
                wordBoxG.getDisLikes().add(user.getUid());

            for(int i=0;i<wordBoxG.getLikes().size();i++){
                if(wordBoxG.getLikes().get(i).equals(user.getUid())){
                    wordBoxG.getLikes().remove(i);
                }
            }
            String txLK=wordBoxG.getLikes().size()+"";
            txLike.setText(txLK);

            String txDLK=wordBoxG.getDisLikes().size()+"";
            txDislike.setText(txDLK);
            icDislike.setColorFilter(
                    ResourcesCompat.getColor(getContext().getResources(),R.color.teal600,null));
            icLike.setColorFilter(
                    ResourcesCompat.getColor(getContext().getResources(),R.color.grey600,null));
        }

    }

    @Override
    public void onWBWCommentRefSet(Comment comment) {
       // Toast.makeText(getContext(),"check an update lol",Toast.LENGTH_SHORT).show();
        adapterRcComment.addComment(comment);
    }

    @Override
    public void onWBWCommentDLRefSet(String idCm) {
        adapterRcComment.deleteComment(idCm);
    }

    @Override
    public void onWBWReplyRefSet(String idCm, Reply reply) {
        adapterRcComment.addReply(idCm,reply);
    }

    @Override
    public void onWBWReplyDLRefSet(String idCm, String idRp) {
        adapterRcComment.deleteReply(idCm,idRp);
    }


    @Override
    public void onWBWCommentRefFail() {

    }

    @Override
    public void onWBWStatusLKRefSet(int type) {
        newLkStatus(type);
    }


    /*** PAUSE INTERFACE ******
    @Override
    public void onWordboxesGFRefSet(ArrayList<WordBoxG> listWordBoxGF) {
        this.wordBoxG =listWordBoxGF;

        adapterRcWBGW.setListWBG(listWordBoxGF);
        adapterRcWBGW.setListWBG(
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

    EN PAUSE INTERFACE ***/


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
