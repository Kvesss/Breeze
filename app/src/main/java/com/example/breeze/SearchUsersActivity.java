package com.example.breeze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUsersActivity extends AppCompatActivity {


    private RecyclerView usersRecyclerView;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("Search Users");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contact> options = new FirebaseRecyclerOptions.Builder<Contact>().setQuery(userDatabaseReference, Contact.class)
                .build();

        FirebaseRecyclerAdapter<Contact, SearchUsersViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Contact, SearchUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchUsersViewHolder searchUsersViewHolder, int i, @NonNull Contact contact) {

                searchUsersViewHolder.name.setText(contact.getName());
                Picasso.get().load(contact.getImage()).placeholder(R.drawable.profile_picture).into(searchUsersViewHolder.profileImage);

                searchUsersViewHolder.itemView.setOnClickListener(v -> {
                    searchUsersViewHolder.itemView.setBackgroundResource(R.color.LightGrey);
                    String clickedUserID = getRef(i).getKey();
                    Intent intent = new Intent(SearchUsersActivity.this, UserActivity.class);
                    intent.putExtra("clickedUserID", clickedUserID);
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public SearchUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
                return new SearchUsersViewHolder(view);
            }
        };
        usersRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();

    }


    public static class SearchUsersViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private CircleImageView profileImage;

        public SearchUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.tvUserName);
            this.profileImage = itemView.findViewById(R.id.ivUserProfile);
        }

    }
}