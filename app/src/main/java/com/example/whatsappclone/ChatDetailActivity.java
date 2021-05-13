package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsappclone.Adapter.ChatAdapter;
import com.example.whatsappclone.Model.MessageModel;
import com.example.whatsappclone.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding activityChatDetailBinding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatDetailBinding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(activityChatDetailBinding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        final String senderId = auth.getUid();
        String recieverId  = getIntent().getStringExtra("userId");
        String username  = getIntent().getStringExtra("userName");
        String profilepic  = getIntent().getStringExtra("profilePic");

        activityChatDetailBinding.username.setText(username);
        Picasso.get().load(profilepic).placeholder(R.drawable.user).into(activityChatDetailBinding.profileImage);





        activityChatDetailBinding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, recieverId);
        activityChatDetailBinding.messagesrecycler.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activityChatDetailBinding.messagesrecycler.setLayoutManager(layoutManager);

        final String senderRoom = senderId + recieverId;
        final String recieverRoom = recieverId + senderId;

        database.getReference().child("Chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        messageModels.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                            MessageModel model1 =  snapshot1.getValue(MessageModel.class);
                            model1.setMessageId(snapshot1.getKey());
                            messageModels.add(model1);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        activityChatDetailBinding.sendmssg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message =
                activityChatDetailBinding.messagesbyuser.getText().toString();



                final MessageModel messageModel = new MessageModel(senderId, message);
                messageModel.setTimeStamp(new Date().getTime());

                activityChatDetailBinding.messagesbyuser.setText("");

                database.getReference().child("Chats").child(senderRoom).push()
                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Chats")
                                .child(recieverRoom)
                                .push()
                                .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
    }
}