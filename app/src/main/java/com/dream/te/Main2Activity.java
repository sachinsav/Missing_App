package com.dream.te;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    EditText t1,t2,t3,t4;
    TextView rtv,ftv;
    LinearLayout ready;
    LottieAnimationView progressbar,userimg;
    Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressbar =findViewById(R.id.animation_view);
        userimg= findViewById(R.id.animation_view2);
        ready = findViewById(R.id.ready);
        ready.setVisibility(View.INVISIBLE);
        t1=findViewById(R.id.ename);
        t2=findViewById(R.id.eemail);
        t3=findViewById(R.id.emob);
        t4=findViewById(R.id.eaddress);
        edit=findViewById(R.id.edit);
        rtv=findViewById(R.id.id_noofreport);
        ftv=findViewById(R.id.no_offound);
        edit.setEnabled(false);
        disableAll();
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("UserProfile").child(uid);
        dbref.addValueEventListener(new ValueEventListener() {
            ProfileDetail pd=null;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd=dataSnapshot.getValue(ProfileDetail.class);
                updateUi(pd);
                edit.setEnabled(true);
                ready.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference dbref2 = FirebaseDatabase.getInstance().getReference("Count").child(uid);
        dbref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> map=new HashMap<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    map.put(ds.getKey(),ds.getValue().toString());
                }
                ftv.setText(map.get("found"));
                rtv.setText(map.get("report"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(edit.getText().toString().equals("edit")) {
                   edit.setText("save");
                   enableAll();
               }else{
                   edit.setText("edit");
                   savetofb();
                   disableAll();
               }
            }
        });
    }

    private void savetofb() {
        String name=t1.getText().toString();
        String email=t2.getText().toString();
        String mob=t3.getText().toString();
        String address=t4.getText().toString();
        ProfileDetail pd=new ProfileDetail(name,mob,address,email);
        FirebaseHelper.add_To_Profile(pd);
    }

    private void enableAll(){
        t1.setEnabled(true);
        t3.setEnabled(true);
        t4.setEnabled(true);
    }
    private void disableAll(){
        t1.setEnabled(false);
        t2.setEnabled(false);
        t3.setEnabled(false);
        t4.setEnabled(false);
    }
    private void updateUi(ProfileDetail pd) {

        t1.setText(pd.getName().toUpperCase());
        t2.setText(pd.getEmail());
        t3.setText(pd.getMob());
        t4.setText(pd.getAdd());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                Toast.makeText(this, "Thanks For Sharing Our App!!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(Main2Activity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void userclicked(View view) {
        userimg.playAnimation();
    }
}
