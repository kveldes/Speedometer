package com.example.speedometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.speedometer.R.layout.support_simple_spinner_dropdown_item;

public class ListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    UserLoginAttributes usersAttributes;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Intent intent = getIntent();
        userID = intent.getStringExtra("key");



        usersAttributes = new UserLoginAttributes();

        listView = findViewById(R.id.listView);
        list = new ArrayList<String>();

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();




        myRef = FirebaseDatabase.getInstance().getReference().child("Users");



        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, support_simple_spinner_dropdown_item,list);

        listView.setAdapter(adapter);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue().toString());



                }
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}