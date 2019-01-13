package com.example.rabia.speechnoteapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentNewNote extends Fragment {

    EditText edtnewnote;
    TextView txtnewnote;
    Button btnkaydet;
    Button btnSave;
    private  FirebaseAuth mAuth;
    private TextView mText;
    private SpeechRecognizer sr;
    TextToSpeech t1;
    TextView ed1;
    Button b1;
    int sayac =0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_note, container, false);
        edtnewnote = (EditText) view.findViewById(R.id.edtnewnote);
        txtnewnote = (TextView) view.findViewById(R.id.textView2);
        btnkaydet = (Button)view.findViewById(R.id.btnkaydet);
        btnSave= (Button)view.findViewById(R.id.btn_save);
        permissionHandler();
        mText = edtnewnote;
        btnkaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnkaydet)
                {
                    if(sayac==0)
                    {
                        startListen();
                        Toast.makeText(getActivity(), "Started",
                                Toast.LENGTH_SHORT).show();
                        sayac=1;
                    }
                    else
                    {
                        sayac=0;
                        if(t1 !=null){
                            t1.stop();
                            t1.shutdown();
                            Toast.makeText(getActivity(), "Stopped",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Error 404.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
        sr = SpeechRecognizer.createSpeechRecognizer(getContext());
        sr.setRecognitionListener(new listener());

        t1=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ROOT);
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //push().getKey()  -- notlar/curuseruuid/fbid
                DatabaseReference myRef = database.getReference("notlar/"+currentUser.getUid()).push();

                myRef.setValue(edtnewnote.getText().toString());
                Toast.makeText(getActivity(), "Saved.",
                        Toast.LENGTH_SHORT).show();
            }
        });



        return view;

    }
    public void permissionHandler()
    {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        527);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request. (In this example I just punched in
                // the value 527)
            }}
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
    class listener implements RecognitionListener
    {
        public void onReadyForSpeech(Bundle params)
        {
            Log.d("", "onReadyForSpeech");
        }
        public void onBeginningOfSpeech()
        {
            Log.d("", "onBeginningOfSpeech");
        }
        public void onRmsChanged(float rmsdB)
        {
            Log.d("", "onRmsChanged");
        }
        public void onBufferReceived(byte[] buffer)
        {
            Log.d("", "onBufferReceived");
        }
        public void onEndOfSpeech()
        {
            Log.d("", "onEndofSpeech");
        }
        public void onError(int error)
        {
            Log.d("",  "error " +  error);
            mText.setText(mText.getText().toString()+" ? ");
            if(sayac==1)
                startListen();
        }
        public void onResults(Bundle results)
        {
            String str = new String();

            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < data.size(); i++)
            {
                Log.d("", "result " + data.get(i));
                str += data.get(i);
            }
            mText.setText(mText.getText().toString()+" "+data.get(0));
            if(sayac==1)
                startListen();
        }
        public void onPartialResults(Bundle partialResults)
        {
            Log.d("", "onPartialResults");
        }
        public void onEvent(int eventType, Bundle params)
        {
            Log.d("", "onEvent " + eventType);
        }
    }

    public void startListen()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
        sr.startListening(intent);
        Log.i("111111","11111111");

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 527: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
