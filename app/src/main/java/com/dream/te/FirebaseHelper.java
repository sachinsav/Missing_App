package com.dream.te;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    public static void add_To_Profile(ProfileDetail details){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = database.getReference("UserProfile").child(uid);
        myRef.setValue(details);
    }
}
