package com.example.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappclone.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    Button signupbutton;
    ProgressDialog progressDialog;
    EditText name,email,password;
    TextView signintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        signupbutton = findViewById(R.id.signupbutton);
        name = findViewById(R.id.SignupName);
        email = findViewById(R.id.SignupEmailAddress);
        password = findViewById(R.id.SignupPassword);
        signintent = findViewById(R.id.siginintent);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Account Creating");
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty()|| password.getText().toString().isEmpty() )
                {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Fill entries properly", Toast.LENGTH_SHORT).show();
                }
                else
                {
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();

                            Users user = new Users(name.getText().toString(), email.getText().toString(), password.getText().toString());

                            String id = task.getResult().getUser().getUid();
                            firebaseDatabase.getReference().child("Users").child(id).setValue(user);

                            Toast.makeText(SignUpActivity.this,"Created Succesfully",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, task.getException().toString() ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });}
            }

        });

        signintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);

            }
        });





    }
}