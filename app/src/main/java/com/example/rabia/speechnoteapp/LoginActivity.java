package com.example.rabia.speechnoteapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    EditText txEmail;
    EditText txSifre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        txEmail=(EditText) findViewById( R.id.txEmail);
        txSifre=(EditText) findViewById( R.id.txSifre);
        Button  btnGiris =(Button) findViewById(R.id.btnGiris);
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mAuth.signInWithEmailAndPassword(txEmail.getText().toString(), txSifre.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    //yeni sayfayi baslat ana sayfaya gönder

                                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }

                        });


            }
        });

    }

}
