package com.example.theislamicapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogActivity extends FragmentActivity {
    private FirebaseAuth mAuth;
    public DatabaseReference refdb;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        mAuth = FirebaseAuth.getInstance();

    }

    public void loginorSign(View view){
        TextView user = findViewById(R.id.user);
        TextView pass = findViewById(R.id.pass);

        if(!(user.getText().length()==0) && !(pass.getText().length()==0)){
            if(((TextView)view).getText().equals("LOG IN")){

                mAuth.signInWithEmailAndPassword(user.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user.getUid());
                                } else {
                                    Toast.makeText(LogActivity.this,
                                            "Wrong Details",Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }else{
                mAuth.createUserWithEmailAndPassword(user.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DBmodifier db = new DBmodifier();
                                    db.createdb(user.getUid());
                                    updateUI(user.getUid());

                                } else {
                                    Toast.makeText(LogActivity.this,
                                            task.getException().getMessage(),Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        }else{
            Toast.makeText(this,"Please enter data",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null)
            updateUI(currentUser.getUid());
    }

    private void updateUI(String currentUser) {
        Intent intent = new Intent(LogActivity.this,MainActivity.class);
        intent.putExtra("user",currentUser);
        startActivity(intent);
    }


}