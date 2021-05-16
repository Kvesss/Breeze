package com.example.breeze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.breeze.Adapters.SlidePagerAdapter;
import com.example.breeze.MainFragments.ChatsFragment;
import com.example.breeze.MainFragments.ContactFragment;
import com.example.breeze.MainFragments.GroupFragment;
import com.example.breeze.Utils.DepthPageTransformer;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SlidePagerAdapter slidePagerAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initializeViews();
        setupFragments();
        displayRegistrationAlerter();

    }

    public void initializeViews(){
        this.tabLayout = findViewById(R.id.tabLayout);
        this.viewPager = findViewById(R.id.viewPager);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

    private void displayRegistrationAlerter(){
        Alerter.create(this).setTitle("Welcome!").setText("Chat with your bros on Breeze!")
                .setDuration(3000).setBackgroundResource(R.drawable.border).enableSwipeToDismiss().setIcon(R.drawable.breezelogo)
                .setEnterAnimation(R.anim.alerter_slide_in_from_bottom).setExitAnimation(R.anim.alerter_slide_out_to_top).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(user == null){
            sendToLoginActivity();
        }
        else {
            verifyUserExistence();
        }
    }

    private void verifyUserExistence() {
        String userID = mAuth.getCurrentUser().getUid();
        databaseReference.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("name").exists()){
//                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendToProfileActivity();
                }
                if(!snapshot.child("image").exists()){
                    sendToProfileActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //TODO
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.itemProfile){
            sendToProfileActivity();
        }
        if(item.getItemId() == R.id.itemSettings){
            sendToSettingsActivity();
        }
        if(item.getItemId() == R.id.langMenu){
        }
        if(item.getItemId() == R.id.itemLogout){
            mAuth.signOut();
            sendToLoginActivity();
            Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void sendToProfileActivity() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        finish();
    }

    private void sendToSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void sendToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}