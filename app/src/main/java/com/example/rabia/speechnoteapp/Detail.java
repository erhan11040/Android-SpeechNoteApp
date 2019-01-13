package com.example.rabia.speechnoteapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Detail extends AppCompatActivity {

    EditText editText2;
    Button button1;
    Button button2;
    String fbid;
    String fbstr;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        editText2 = (EditText) findViewById(R.id.editText2);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        Bundle extras = getIntent().getExtras();
        fbstr = extras.getString("fbStr");
        fbid = extras.getString("fbid");
        Toast.makeText(Detail.this, "fbstr:"+fbstr+"fbid:"+fbid,
                Toast.LENGTH_SHORT).show();


        editText2.setText(fbstr);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //push().getKey()  -- notlar/curuseruuid/fbid
                DatabaseReference myRef = database.getReference("notlar/"+currentUser.getUid()+"/"+fbid);

                myRef.setValue(editText2.getText().toString());
                Toast.makeText(Detail.this, "Saved.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("notlar/"+currentUser.getUid()+"/"+fbid);
                myRef.removeValue();
                Toast.makeText(Detail.this, "Removed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
