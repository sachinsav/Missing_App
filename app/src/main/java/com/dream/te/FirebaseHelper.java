package com.dream.te;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {
    static FirebaseHelper fh = null;

    public FirebaseHelper() {

    }

    public static FirebaseHelper getInstance() {
        if (fh == null) fh = new FirebaseHelper();
        return fh;
    }

    public String getuid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static void add_To_Profile(ProfileDetail details) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = database.getReference("UserProfile").child(uid);
        myRef.setValue(details);
    }

    public static void add_To_Count(String c1,String c2) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = database.getReference("Count").child(uid);
        myRef.child("report").setValue(c1);
        myRef.child("found").setValue(c2);
    }
    public StorageReference storage_ref() {
        return FirebaseStorage.getInstance().getReference().child("images/" + getuid());
    }
}
