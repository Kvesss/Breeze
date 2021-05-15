package com.example.breeze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    private String userID;
    private String loggedUserID;
    private String condition;
    private TextView username;
    private CircleImageView profileImage;
    private Button btnStartChat;
    private DatabaseReference databaseReference;
    private DatabaseReference friendRequestsReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        friendRequestsReference = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        mAuth = FirebaseAuth.getInstance();
        userID = getIntent().getExtras().get("clickedUserID").toString();
        loggedUserID = mAuth.getCurrentUser().getUid();
        condition = "notFriend";
        initializeViews();
        setUserInfo();

    }

    public void initializeViews(){
        username = findViewById(R.id.tvProfileName);
        profileImage = findViewById(R.id.profile_image);
        btnStartChat = findViewById(R.id.btnStartChat);
    }

    public void setUserInfo(){
        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    username.setText(name);
                    if (snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile_picture).into(profileImage);
                    }

                    friendRequestsReference.child(loggedUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(userID)){
                                String requestType = snapshot.child(userID)
                                        .child("request type").getValue().toString();

                                if(requestType.equals("sent")){
                                    condition = "request sent";
                                    btnStartChat.setText("Cancel Friend Request");
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    if(userID.equals(loggedUserID)){
                        btnStartChat.setVisibility(View.INVISIBLE);
                    }
                    else {
                        btnStartChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(condition.equals("notFriend")){

                                    SendFriendRequest();
                                }

                                if (condition.equals("request sent")){
                                    cancelFriendRequest();
                                }
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cancelFriendRequest() {
        friendRequestsReference.child(loggedUserID).child(userID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            friendRequestsReference.child(userID).child(loggedUserID).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                btnStartChat.setEnabled(true);
                                                condition = "notFriend";
                                                btnStartChat.setText("Send Friend Request");
                                                Toast.makeText(UserActivity.this, "Request Canceled", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }
                    }
                });
    }

    private void SendFriendRequest() {

        friendRequestsReference.child(loggedUserID).child(userID)
                .child("request type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    friendRequestsReference.child(userID)
                            .child(loggedUserID).child("request type").setValue("received")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        btnStartChat.setEnabled(true);
                                        condition = "request sent";
                                        btnStartChat.setText("Cancel Friend Request");
                                        Toast.makeText(UserActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}