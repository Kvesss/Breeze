package com.example.breeze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.breeze.Utils.Contact;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsActivity extends AppCompatActivity {


    private RecyclerView requestsRecyclerView;
    private DatabaseReference receivedRequestsRef;
    private DatabaseReference usersRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        receivedRequestsRef = FirebaseDatabase.getInstance().getReference().child("Friend Requests").child(user.getUid());
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contact> options = new FirebaseRecyclerOptions.Builder<Contact>().setQuery(receivedRequestsRef, Contact.class).build();
        FirebaseRecyclerAdapter<Contact, RequestsViewHolder> adapter = new FirebaseRecyclerAdapter<Contact, RequestsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestsViewHolder requestsViewHolder, int i, @NonNull Contact contact) {
                String friendID = getRef(i).getKey();
                usersRef.child(friendID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("image")){
                            Picasso.get().load(snapshot.child("image").getValue().toString()).into(requestsViewHolder.profileImage);
                        }
                        requestsViewHolder.name.setText(snapshot.child("name").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_display_layout, parent, false);
                return new RequestsViewHolder(view);
            }
        };
        requestsRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private static class RequestsViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profileImage;
        private TextView name;

        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.ivUserProfile);
            name = itemView.findViewById(R.id.tvUserName);
        }
    }
}