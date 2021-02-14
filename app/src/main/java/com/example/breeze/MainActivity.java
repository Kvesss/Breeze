package com.example.breeze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SlidePagerAdapter slidePagerAdapter;
    private FirebaseAuth mAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        initializeViews();
        setupFragments();
    }

    public void initializeViews(){
        this.tabLayout = findViewById(R.id.tabLayout);
        this.viewPager = findViewById(R.id.viewPager);
    }

    public void setupFragments(){
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(ChatsFragment.newInstance());
        fragmentList.add(GroupFragment.newInstance());
        fragmentList.add(ContactFragment.newInstance());
        this.slidePagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), this);
        this.viewPager.setAdapter(slidePagerAdapter);
        this.viewPager.setPageTransformer(true, new DepthPageTransformer());
        tabLayout.setupWithViewPager(this.viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(user == null){
            SendToLoginActivity();
        }
    }

    private void SendToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //TODO
        return super.onOptionsItemSelected(item);
    }
}