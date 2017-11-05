package com.devs.cnd.marvelousv.firebase;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.devs.cnd.marvelousv.fragments.FrUserProfile;
import com.devs.cnd.marvelousv.objects.FSearch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wunder on 9/29/17.
 */

public class UserDataRF {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private Context context;

    private FSrhRefCallback fSrhRefCallback;
    private FSearch fSearch;
    private boolean userSearchAble=false;

    public UserDataRF(Context context){
        this.context=context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        fSearch = new FSearch();
    }

    public boolean getUserSearhAble(){
        return userSearchAble;
    }
    public void UserDataLoad(){
        final FirebaseUser user = mAuth.getCurrentUser();
            mDatabase.child("fsearch").child("users").child(user.getUid())
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    //User user = dataSnapshot.getValue(User.class);
                                    userSearchAble = dataSnapshot.exists();


                                    fSearch.setId(dataSnapshot.getKey());
                                    fSearch.setUserName((String) dataSnapshot.child("userName").getValue());
                                    fSearch.setUserEmail((String) dataSnapshot.child("userEmail").getValue());
                                    fSearch.setUserPhoto((String) dataSnapshot.child("userPhoto").getValue());

                                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                        String id = dss.getKey();
                                        //String boxName = (String) dss.child("boxName").getValue();
                                        //setWordBoxList(listWordBox);
                                       // wbRefCallback.onWordboxesRefSet(listWordBox);
                                    }
                                    fSrhRefCallback.onFSearchRefSet(fSearch,userSearchAble);
                                    Toast.makeText(context,
                                            "Call correctly USEDATA",
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    Toast.makeText(context,
                                            "Error: FB USERDATA.",
                                            Toast.LENGTH_SHORT).show();
                                   // wbRefCallback.onWordboxesRefFail();
                                }
                            });

    }

    public void userDataSearhAbleUpdate(boolean searchAble){
        final FirebaseUser user = mAuth.getCurrentUser();

        Map<String, Object> childUpdates = new HashMap<>();
        if(searchAble){
            fSearch.setId(user.getUid());
            fSearch.setUserName(user.getDisplayName());
            fSearch.setUserEmail(user.getEmail());
            String photoU="";
            if(user.getPhotoUrl()!=null)
                photoU= user.getPhotoUrl().toString();

            for(UserInfo profile : user.getProviderData()) {
                if (profile.getProviderId().equals("facebook.com")) {
                    photoU="https://graph.facebook.com/" + profile.getUid() + "/picture?height=500";
                }
            }
            fSearch.setUserPhoto(photoU);
            childUpdates.put("fsearch/users/"+user.getUid(), fSearch.toMap());
        }else{
            childUpdates.put("fsearch/users/"+user.getUid(), null);
        }

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Succes update User DT Search",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Fail update User DT Search: "+e,Toast.LENGTH_LONG).show();
            }
        });

    }

    public void userPhotoUpdate(byte[] img, final String userName, final boolean UDSearchable){
        final FirebaseUser user = mAuth.getCurrentUser();

        UploadTask uploadTask= mStorage.child("users").child(user.getUid()).child("proPic").putBytes(img);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String dlURL= taskSnapshot.getDownloadUrl().toString();
                Toast.makeText(context,"Succes: "+dlURL,Toast.LENGTH_SHORT).show();
                //fSrhRefCallback.onUserDataUpdate(dlURL);
                userDataUpdate(userName,dlURL,UDSearchable);
            }
        });

    }

    public void userDataUpdate(String nameUpdate, String userPhoto, final boolean UDSearchable){
        final FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameUpdate)
                .setPhotoUri(Uri.parse(userPhoto))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "User Data Change", Toast.LENGTH_SHORT).show();
                            if(UDSearchable)
                                userDataSearhAbleUpdate(UDSearchable);
                            fSrhRefCallback.onUserDataUpdate();
                        }
                    }
                });
    }


    public void onFrUserProfileActive( FrUserProfile frUserProfile){
        try{
            fSrhRefCallback=(FSrhRefCallback) frUserProfile;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+
                    " Must implement fSrhRefCallback Interaction Listener");
        }
    }

    public interface FSrhRefCallback {
        void onFSearchRefSet( FSearch fSearch, boolean searchAble);
        void onFSearchRefFail();
        void onUserDataUpdate();
    }
}
