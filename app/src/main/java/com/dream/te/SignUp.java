package com.dream.te;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    EditText e1, e2, e3, e4, e5, e6;
    Button btn_signup;
    String TAG = "signuppage";
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        e1 = findViewById(R.id.editText);
        e2 = findViewById(R.id.editText2);
        e3 = findViewById(R.id.editText3);
        e4 = findViewById(R.id.editText4);
        e5 = findViewById(R.id.editText5);
        btn_signup = findViewById(R.id.b_signup);
        mAuth = FirebaseAuth.getInstance();
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = e1.getText().toString();
                String phone = e2.getText().toString();
                String address = e3.getText().toString();
                String email = e4.getText().toString();
                String password = e5.getText().toString();
                if(validated(name,phone,address,email,password)) {
                    progressDialog=new ProgressDialog(SignUp.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Veryfing...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    ProfileDetail detail = new ProfileDetail(name, phone, address, email);
                    createAccount(email, password, detail);
                }
            }
        });

    }
    public boolean validated(String name,String phone,String address,String email,String password){
        boolean valid=true;
        //my code  here
        if (name.isEmpty() || name.length() < 3) {
            e1.setError("at least 3 characters");
            valid = false;
        } else {
            e1.setError(null);
        }
        if(phone.isEmpty() || phone.length()!=10){
            e2.setError("phone should contain 10 degit");
            valid=false;
        }else{
            e2.setError(null);
        }
        if(address.isEmpty() || address.length()>100){
            e3.setError("enter a valid address");
            valid = false;
        }else{
            e3.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e4.setError("enter a valid email address");
            valid = false;
        } else {
            e4.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            e5.setError("between 6 and 20 alphanumeric characters");
            valid = false;
        } else {
            e5.setError(null);
        }

        return valid;
    }
    public void enableAll(){
        e1.setEnabled(true);
        e2.setEnabled(true);
        e3.setEnabled(true);
        e4.setEnabled(true);
        e5.setEnabled(true);
        btn_signup.setAlpha(1f);
        btn_signup.setEnabled(true);
    }
    public void disableAll(){
        e1.setEnabled(false);
        e2.setEnabled(false);
        e3.setEnabled(false);
        e4.setEnabled(false);
        e5.setEnabled(false);
        btn_signup.setAlpha(0.5f);
        btn_signup.setEnabled(false);
    }

    private void createAccount(String email, String password, final ProfileDetail detail) {
        disableAll();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseHelper.add_To_Profile(detail);
                            FirebaseHelper.add_To_Count("0","0");
                            progressDialog.dismiss();
                            enableAll();
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            enableAll();
                        }
                    }
                });
    }

}
