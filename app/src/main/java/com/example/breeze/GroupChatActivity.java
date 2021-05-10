package com.example.breeze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {


    private ImageButton sendButton;
    private EditText etMessage;
    private ScrollView scrollView;
    private TextView textMessage;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference userReference;
    private DatabaseReference groupnameReference;
    private DatabaseReference groupMessageKeyReference;
    private String username;
    private String userID;
    private String currentDate;
    private String currentTime;
    private String groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.userID = user.getUid();
        this.scrollView = findViewById(R.id.groupChatScrollView);
        this.etMessage = findViewById(R.id.etSendMessage);
        this.sendButton = findViewById(R.id.ibtnSendMessage);
        this.textMessage = findViewById(R.id.tvGroupMessage);
        this.groupName = getIntent().getExtras().getString("groupname");
        this.setTitle(groupName);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        sendButton.setOnClickListener(v -> {
            saveMessageToDatabase();
            etMessage.setText("");
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        });

        sendButton.setOnLongClickListener(v -> false);

        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        groupnameReference = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName);

        getUserInfo();


    }

    @Override
    protected void onStart() {
        super.onStart();
        groupnameReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    displayMessages(snapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    displayMessages(snapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void displayMessages(DataSnapshot snapshot) {
        Iterator iterator = snapshot.getChildren().iterator();
        while (iterator.hasNext()){
            String chatDate = (String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String)((DataSnapshot)iterator.next()).getValue();
            String chatName = (String)((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String)((DataSnapshot)iterator.next()).getValue();

            textMessage.append(chatName + "\n" + chatMessage + " \n\n\n");

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    private void saveMessageToDatabase() {
        String message = etMessage.getText().toString();
        String messageKey = groupnameReference.push().getKey();

        if(TextUtils.isEmpty(message)){
            Toast.makeText(this, "Please enter message...", Toast.LENGTH_SHORT).show();
        }
        else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(calendar.getTime());

            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm");
            currentTime = currentTimeFormat.format(calendar.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupnameReference.updateChildren(groupMessageKey);
            groupMessageKeyReference = groupnameReference.child(messageKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", username);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);
            groupMessageKeyReference.updateChildren(messageInfoMap);


        }
    }

    private void getUserInfo() {
        userReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        username = snapshot.child("name").getValue().toString();
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
            Toast.makeText(GroupChatActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        }

        return true;
    }


    private void sendToProfileActivity() {
        Intent intent = new Intent(GroupChatActivity.this, ProfileActivity.class);
        startActivity(intent);

    }

    private void sendToSettingsActivity() {
        Intent intent = new Intent(GroupChatActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void sendToLoginActivity() {
        Intent intent = new Intent(GroupChatActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}