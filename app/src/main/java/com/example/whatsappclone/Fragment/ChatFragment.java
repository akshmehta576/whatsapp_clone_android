package com.example.whatsappclone.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapter.UsersAdapter;
import com.example.whatsappclone.Model.Users;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ChatFragment extends Fragment {


    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;

    public ChatFragment() {
        // Required empty public constructor
    }

    RecyclerView chatrecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        chatrecycler = root.findViewById(R.id.chatrecycler);
        UsersAdapter usersAdapter = new UsersAdapter(list, getContext());
        chatrecycler.setAdapter(usersAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatrecycler.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        list.add(users);
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return root;
    }
}