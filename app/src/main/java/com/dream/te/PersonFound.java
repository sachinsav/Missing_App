package com.dream.te;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonFound extends AppCompatActivity {
    private TextView tvname, tvmob, tvadd, tvmname;
    private ImageView miv;
    LottieAnimationView progressbar;
    LinearLayout lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_found);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String fuid = intent.getStringExtra("userid");
        tvname = findViewById(R.id.text_view_name);
        tvadd = findViewById(R.id.text_view_add);
        tvmob = findViewById(R.id.text_view_mob);
        tvmname = findViewById(R.id.text_view_mpn);
        lv = findViewById(R.id.pfpage);
        miv = findViewById(R.id.iv_pv);
        progressbar = findViewById(R.id.progress_pf);
        final HashMap<String, String> hm = new HashMap<>();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("UserProfile").child(fuid);
        final String finalFuid = fuid;
        increse_found_count();
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileDetail value = dataSnapshot.getValue(ProfileDetail.class);
                Log.d("firebaseHelper", value.getAdd() + " " + value.getName() + " " + value.getEmail() + " s " + value.getMob());
                tvname.setText(value.getName());
                tvadd.setText(value.getAdd());
                tvmob.setText(value.getMob());

                DatabaseReference dbref2 = FirebaseDatabase.getInstance().getReference("Reports").child(finalFuid);
                dbref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Report_Obj value1 = dataSnapshot.getValue(Report_Obj.class);
                        tvmname.setText(value1.getName());
                        lv.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.INVISIBLE);
                        Picasso.with(PersonFound.this)
                                .load(value1.getImage_url())
                                .placeholder(R.drawable.th)
                                .fit()
                                .centerCrop()
                                .into(miv);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressbar.setVisibility(View.INVISIBLE);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    public void increse_found_count() {
        final ArrayList<String> al = new ArrayList<>();
        final DatabaseReference f_dbref = FirebaseDatabase.getInstance().getReference().child("Count").child(FirebaseHelper.getInstance().getuid());
        f_dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                al.add(dataSnapshot.child("found").getValue(String.class));
                if (al.size() > 0) {
                    String k = (Integer.parseInt(al.get(0)) + 1) + "";
                    f_dbref.child("found").setValue(k);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("foundfb", databaseError.toString());
            }
        });

    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(PersonFound.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
