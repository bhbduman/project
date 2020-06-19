package com.example.a222latest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {
    private View chatsFragmentView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> chats;
    private ArrayList<String> chatKeys;
    private DatabaseReference privateMessagesRef;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatsFragmentView = inflater.inflate(R.layout.fragment_chats, container, false);

        String email = "deneme@gmail.com";
        String password = "123456";

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "logged in", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        });


        //TEST
        String userId = FirebaseAuth.getInstance().getUid();

        privateMessagesRef = FirebaseDatabase.getInstance().getReference().
                child("Users").child(userId).child("privateMessages");
        initalizeFields();

        retrieveAndDisplayChatHistory();

        return chatsFragmentView;
    }


    private void retrieveAndDisplayChatHistory() {
        privateMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateChats(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateChats(DataSnapshot dataSnapshot) {
        for (DataSnapshot d : dataSnapshot.getChildren()) {
            String chatName = (String) d.getValue();
            String chatKey = d.getKey();
            chats.add(chatName);
            chatKeys.add(chatKey);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    private void initalizeFields() {
        chats = new ArrayList<>();
        chatKeys = new ArrayList<>();
        listView = chatsFragmentView.findViewById(R.id.listViewChats);
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, chats);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String key = chatKeys.get(position);
            String name = (String) parent.getItemAtPosition(position);
            Intent intent = new Intent(getContext(), PrivateMessagingActivity.class);
            intent.putExtra("receiverName", name);
            intent.putExtra("messagingKey", key);
            startActivity(intent);
        });
    }
}