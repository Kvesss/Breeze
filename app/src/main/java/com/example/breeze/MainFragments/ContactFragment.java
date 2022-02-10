package com.example.breeze.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.breeze.R;
import com.example.breeze.SearchUsersActivity;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactFragment extends Fragment {

    private View view;
    private RecyclerView friendsRecyclerView;
    private DatabaseReference friendsReference;
    private DatabaseReference friendUserReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button btnAddContact;

    public static ContactFragment newInstance(){
        return new ContactFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        // Inflate the layout for this fragment
        btnAddContact = view.findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(v -> sendToSearchUsersActivity());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        friendsReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(user.getUid());
        friendUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
        initializeRecyclerView();
        displayFriends();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contact> options = new FirebaseRecyclerOptions.Builder<Contact>().setQuery(friendsReference, Contact.class).build();

        FirebaseRecyclerAdapter<Contact, FriendsViewHolder> adapter = new FirebaseRecyclerAdapter<Contact, FriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendsViewHolder friendsViewHolder, int i, @NonNull Contact contact) {
                String friendID = getRef(i).getKey();
                    friendUserReference.child(friendID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("image")){
                                Picasso.get().load(snapshot.child("image").getValue().toString()).into(friendsViewHolder.profileImage);


                            }
                            friendsViewHolder.name.setText(snapshot.child("name").getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
                return new FriendsViewHolder(view);

            }
        };
        friendsRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void sendToSearchUsersActivity() {
        Intent intent = new Intent(getActivity(), SearchUsersActivity.class);
        startActivity(intent);
    }


    private void initializeRecyclerView(){
        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //TODO
    }

    private void displayFriends(){

    }

    private static class FriendsViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView profileImage;
        private TextView name;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvUserName);
            profileImage = itemView.findViewById(R.id.ivUserProfile);
        }
    }
}