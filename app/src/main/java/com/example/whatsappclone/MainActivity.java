package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.whatsappclone.Adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);

        auth = FirebaseAuth.getInstance();
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case (R.id.logout):
                auth.signOut();
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);

            break;
            case (R.id.setting):
            {
                Intent intent2 = new Intent(this, SettingActivity.class);
                startActivity(intent2);
                break;
            }
            case (R.id.groupchat):
            {
                Intent intent1 = new Intent(this, GroupChatActivity.class);
                startActivity(intent1);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}