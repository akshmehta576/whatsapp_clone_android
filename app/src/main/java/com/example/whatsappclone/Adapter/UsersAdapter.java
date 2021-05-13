package com.example.whatsappclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.ChatDetailActivity;
import com.example.whatsappclone.Model.Users;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    ArrayList<Users> users;
    Context context;
    public UsersAdapter(ArrayList<Users> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.user_name.setText(users.get(position).getUserName());
        Picasso.get().load(users.get(position).getProfilepic()).placeholder(R.drawable.user).into(holder.profile_img);

        FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(FirebaseAuth.getInstance().getUid() + users.get(position).getUserId())
                .orderByChild("timeStamp").limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {

                                holder.last_message.setText(dataSnapshot.child("message").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", users.get(position).getUserId());
                intent.putExtra("profilePic", users.get(position).getProfilepic());
                intent.putExtra("userName", users.get(position).getUserName());
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView user_name, last_message;
        CircleImageView profile_img;

        public ViewHolder(@NonNull @NotNull View itemView) {

            super(itemView);

            user_name = itemView.findViewById(R.id.name_user);
            last_message = itemView.findViewById(R.id.last_mssg_user);
            profile_img = itemView.findViewById(R.id.profile_image);
        }
    }
}
