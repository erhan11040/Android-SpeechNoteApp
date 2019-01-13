package com.example.rabia.speechnoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class FragmentOpenNote extends Fragment {

    ListView liste;
    private FirebaseAuth mAuth;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private ArrayList<String> idList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_open_note, container, false);
        liste=(ListView) rootView.findViewById(R.id.liste);
        arrayList = new ArrayList<String>();
        idList = new ArrayList<String>();
        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);

        // Here, you set the data in your ListView
        liste.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference myRef = database.getReference("notlar/"+currentUser.getUid());
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String pos=Integer.toString(position);
                String ids=Long.toString(id);
                String fbid=idList.get(position);
                String fbStr=arrayList.get(position);
                Intent myIntent = new Intent(getActivity(), Detail.class);
                myIntent.putExtra("fbid",fbid);
                myIntent.putExtra("fbStr",fbStr);
                startActivity(myIntent);
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idList.clear();
                arrayList.clear();
                for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {
                    String value = zoneSnapshot.getValue(String.class);
                    arrayList.add(value);
                    idList.add(zoneSnapshot.getKey());
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), "Error 404.",
                        Toast.LENGTH_SHORT).show();
    }
});
        return rootView;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String aa="aaa";
    }


}
