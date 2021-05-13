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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 123;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button siginbutton, googlesignin;
    EditText name,email,password;
    TextView signupintent;
    GoogleSignInClient mGoogleSignInClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.SigninEmailAddress);
        password = findViewById(R.id.SigninPassword);
        signupintent = findViewById(R.id.sigupintent);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Logging in Account");
        siginbutton = findViewById(R.id.signinbutton);
        googlesignin = findViewById(R.id.googlesignin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        siginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(email.getText().toString().isEmpty())
                {
                    progressDialog.dismiss();
                    email.setError("Fill email");

                    return;
                }
                if(password.getText().toString().isEmpty())
                {
                    progressDialog.dismiss();
                    password.setError("Fill email");
                    return;
                }
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            Intent intent  = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(SignInActivity.this, task.getException().toString() ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        if(auth.getCurrentUser()!=null)
        {
            Intent intent  = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
        signupintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });
        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();

                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);

                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilepic(user.getPhotoUrl().toString());

                            database.getReference().child("Users").child(user.getUid()).setValue(users);

                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }
}