package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsappclone.Adapter.ChatAdapter;
import com.example.whatsappclone.Model.MessageModel;
import com.example.whatsappclone.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatActivity.this , MainActivity.class);
                startActivity(intent);

            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ArrayList<MessageModel> messageModels = new ArrayList<>();

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.username.setText("Friends Group");

        final ChatAdapter adapter = new ChatAdapter(messageModels, this);
        binding.messagesrecycler.setAdapter(adapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.messagesrecycler.setLayoutManager(linearLayoutManager);

        database.getReference().child("Group Chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            messageModels.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
        binding.sendmssgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.messagesbyuser.getText().toString();
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimeStamp(new Date().getTime());

                binding.messagesbyuser.setText("");

                database.getReference().child("Group Chat").push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

            }
        });
    }
}