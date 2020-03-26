package com.dream.te;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Report_Missing extends AppCompatActivity {
    FirebaseAuth mAuth;
    private Button btn, breg;
    private ImageView imageview;
    private EditText ename, emob, eadd;
    private static final String IMAGE_DIRECTORY = "/demoTE";
    private int GALLERY = 1, CAMERA = 2;
    private Uri img_uri;
    private LottieAnimationView progressBar;
    FirebaseHelper fh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__missing);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestMultiplePermissions();
        fh = FirebaseHelper.getInstance();
        btn = findViewById(R.id.btn);
        imageview = findViewById(R.id.iv);
        ename = findViewById(R.id.name);
        emob = findViewById(R.id.mob_no);
        eadd = findViewById(R.id.address);
        breg = findViewById(R.id.reg);
        progressBar = findViewById(R.id.progressBar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_report();
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
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(Report_Missing.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                img_uri = contentURI;
                try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        imageview.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Report_Missing.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
             img_uri=data.getData();
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(thumbnail);
            if(img_uri==null)
                img_uri = getImageUri(this, thumbnail);
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                           Log.d("permisiion","all permisiion is granted");
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void disableAll() {
        eadd.setEnabled(false);
        ename.setEnabled(false);
        emob.setEnabled(false);
        btn.setEnabled(false);
        breg.setEnabled(false);
        btn.setAlpha(0.5f);
        breg.setAlpha(0.5f);
        imageview.setAlpha(0.5f);
    }

    private void enableAll() {
        eadd.setEnabled(true);
        ename.setEnabled(true);
        emob.setEnabled(true);
        btn.setEnabled(true);
        breg.setEnabled(true);
        btn.setAlpha(1f);
        breg.setAlpha(1f);
         imageview.setAlpha(1f);
    }
    private void clearAll(){
        eadd.getText().clear();
        ename.getText().clear();
        emob.getText().clear();
        imageview.setImageResource(R.drawable.th);
        img_uri=null;
    }
    private void register_report() {
        final String nname = ename.getText().toString();
        final String nmob = emob.getText().toString();
        final String naddress = eadd.getText().toString();
        if(!validate(nname,nmob,naddress,img_uri)){
            return;
        }

        try {
            Log.d("uploadfile", "just start");
            if (img_uri != null) {
                Log.d("uploadfile", "just start2");
                progressBar.setVisibility(View.VISIBLE);
                disableAll();
                final StorageReference imgRef = fh.storage_ref();
                imgRef.putFile(img_uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Task<Uri> downloadUr = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                while (!downloadUr.isSuccessful()) ;
                                Uri downloadUrl = downloadUr.getResult();

                                // String dbref = FirebaseDatabase.getInstance().getReference("images").push().getKey();
                                Report_Obj report_obj = new Report_Obj(nname, nmob, naddress, downloadUrl.toString());
                                FirebaseDatabase.getInstance().getReference("Reports").child(fh.getuid()).setValue(report_obj);
                                Snackbar snackbar=Snackbar.make(breg,"Report Registered Successfully!!",Snackbar.LENGTH_LONG);
                                increse_report_count();

                                snackbar.show();
                                // TODO: Call to API here

                                progressBar.setVisibility(View.INVISIBLE);
                                enableAll();
                                clearAll();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Log.d("exception inside", exception.toString());
                                progressBar.setVisibility(View.INVISIBLE);
                                enableAll();
                            }
                        });

            }
        } catch (Exception e) {
            Log.d("uploadfile", "atend" + e.toString());
        }
    }

    private boolean validate(String nname, String nmob, String naddress, Uri img_uri) {
        boolean validate=true;
        if(nname.isEmpty()||nname.length()<3){
            ename.setError("name must contain atleast 3 character");
            validate=false;
        }else{
            ename.setError(null);
        }
        if(nmob.isEmpty()||nmob.length()!=10){
            emob.setError("mobile must contain 10 degit");
            validate=false;
        }else{
            emob.setError(null);
        }
        if(naddress.isEmpty()||naddress.length()<6||naddress.length()>100){
            eadd.setError("address must contain character between 6 to 100");
            validate=false;
        }else{
            eadd.setError(null);
        }
        if(img_uri==null){
            validate=false;
        }
        return validate;
    }

    Map<String,String> map=new HashMap<>();
    private void increse_report_count() {
        DatabaseReference dbref2 = FirebaseDatabase.getInstance().getReference("Count").child(fh.getuid());
        dbref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Log.d("firebase data",ds.getKey()+" "+ds.getValue());
                    map.put(ds.getKey(),ds.getValue().toString());
                }
                Log.d("single value even","again run");
                String new_r_count=(Integer.parseInt(map.get("report").toString())+1)+"";
                FirebaseHelper.add_To_Count(new_r_count,map.get("found").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
