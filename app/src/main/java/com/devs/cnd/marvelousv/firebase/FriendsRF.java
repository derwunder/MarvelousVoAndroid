package com.devs.cnd.marvelousv.firebase;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.devs.cnd.marvelousv.fragments.FrFriendList;
import com.devs.cnd.marvelousv.fragments.FrFriendRqList;
import com.devs.cnd.marvelousv.fragments.FrWordboxes;
import com.devs.cnd.marvelousv.objects.Definition;
import com.devs.cnd.marvelousv.objects.Friend;
import com.devs.cnd.marvelousv.objects.FriendRq;
import com.devs.cnd.marvelousv.objects.Tag;
import com.devs.cnd.marvelousv.objects.Translation;
import com.devs.cnd.marvelousv.objects.Word;
import com.devs.cnd.marvelousv.objects.WordBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by wunder on 9/6/17.
 */

public class FriendsRF {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Context context;

    private FriendsRefCallback friendsRefCallback;
    private FriendRqsRefCallback friendRqsRefCallback;

    public ArrayList<Friend> listFriend = new ArrayList<>();
    public ArrayList<FriendRq> listFriendRq = new ArrayList<>();

    public FriendsRF(Context context){
        this.context=context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setFriendList(ArrayList<Friend> listFriend){
        this.listFriend=listFriend;

        Toast.makeText(context,
                "Friend List load.",
                Toast.LENGTH_SHORT).show();
    }

    public void setFriendRqList(ArrayList<FriendRq> listFriendRq){
        this.listFriendRq=listFriendRq;
    }

    public void FriendsLoad(final Fragment currentFragment){
        final FirebaseUser user = mAuth.getCurrentUser();

        if(listFriend.isEmpty()) {
            mDatabase.child("users").child(user.getUid()).child("friends")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    //User user = dataSnapshot.getValue(User.class);
                                    ArrayList<Friend> listFriend = new ArrayList<>();
                                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                        String id = dss.getKey();
                                        String frName = (String) dss.child("frName").getValue();
                                        String frEmail = (String) dss.child("frEmail").getValue();
                                        String frPhoto = (String) dss.child("frPhoto").getValue();


                                        Friend friend= new Friend(id,frName,frEmail,frPhoto);

                                        listFriend.add(friend);
                                    }
                                    setFriendList(listFriend);
                                    if(currentFragment instanceof FrFriendList)
                                        friendsRefCallback.onFriendsRefSet(listFriend);


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

                                    // [END_EXCLUDE]
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
                                    friendsRefCallback.onFriendsRefFail();
                                }
                            });
        }else{
            if(currentFragment instanceof FrFriendList){
                friendsRefCallback.onFriendsRefSet(listFriend);
            }
            Toast.makeText(context,
                    "pass: Previews data load",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void FriendsLoadMain(){
        final FirebaseUser user = mAuth.getCurrentUser();

        if(listFriend.isEmpty()) {
            mDatabase.child("users").child(user.getUid()).child("friends")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    //User user = dataSnapshot.getValue(User.class);
                                    ArrayList<Friend> listFriend = new ArrayList<>();
                                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                        String id = dss.getKey();
                                        String frName = (String) dss.child("frName").getValue();
                                        String frEmail = (String) dss.child("frEmail").getValue();
                                        String frPhoto = (String) dss.child("frPhoto").getValue();


                                        Friend friend= new Friend(id,frName,frEmail,frPhoto);

                                        listFriend.add(friend);
                                    }
                                    setFriendList(listFriend);

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
                                    friendsRefCallback.onFriendsRefFail();
                                }
                            });
        }else{
            //friendsRefCallback.onFriendsRefSet(listFriend);
            Toast.makeText(context,
                    "pass: Previews data load",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void FriendRqaLoad(final Fragment currentFragment){
        final FirebaseUser user = mAuth.getCurrentUser();

            mDatabase.child("frequest").child("users").child(user.getUid()).child("requests")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    //User user = dataSnapshot.getValue(User.class);
                                    ArrayList<FriendRq> listFriendRq = new ArrayList<>();
                                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                        String id = dss.getKey();
                                        String reqUName = (String) dss.child("reqUName").getValue();
                                        String reqUEmail = (String) dss.child("reqUEmail").getValue();
                                        String reqUPhoto = (String) dss.child("reqUPhoto").getValue();
                                        long reqUTime = (long) dss.child("reqUTime").getValue();


                                        FriendRq friendRq= new FriendRq(id,reqUName,reqUEmail,reqUPhoto,reqUTime);

                                        listFriendRq.add(friendRq);
                                    }
                                    setFriendRqList(listFriendRq);
                                   // if(friendRqsRefCallback)
                                    if(currentFragment instanceof FrFriendRqList)
                                        friendRqsRefCallback.onFriendRqsRefSet(listFriendRq);

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
                                    friendRqsRefCallback.onFriendRqsRefFail();
                                }
                            });

    }



    //CALL BACK FR Active
    public void onFrFriendListActive( FrFriendList frFriendList){
        try{
            friendsRefCallback=(FriendsRF.FriendsRefCallback)frFriendList;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Wordboxes Interaction Listener");
        }
    }

    public void onFrFriendRqListActive( FrFriendRqList frFriendRqList){
        try{
            friendRqsRefCallback=(FriendsRF.FriendRqsRefCallback)frFriendRqList;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement Wordboxes Interaction Listener");
        }
    }



    public interface FriendsRefCallback {
        void onFriendsRefSet( ArrayList<Friend> listFriend);
        void onFriendsRefFail();
    }

    public interface FriendRqsRefCallback {
        void onFriendRqsRefSet( ArrayList<FriendRq> listFriendRq);
        void onFriendRqsRefFail();
    }
}
