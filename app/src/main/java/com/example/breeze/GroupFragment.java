package com.example.breeze;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class GroupFragment extends Fragment {


    private View groupView;
    private RecyclerView groupsRecyclerView;
    private List<Group> groupList = new ArrayList<>();
    private GroupsAdapter groupsAdapter;
    private ImageButton createGroup;

    DatabaseReference databaseReference;



    public static GroupFragment newInstance() {
        return new GroupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupView = inflater.inflate(R.layout.fragment_group, container, false);
        createGroup = groupView.findViewById(R.id.ibtnCreateGroup);
        createGroup.setOnClickListener(v -> {
            createNewGroup();
            Toast.makeText(getContext(), "Create Group", Toast.LENGTH_SHORT).show();
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups");
        initializeViews();

        displayGroups();

        return groupView;
    }

    private void displayGroups() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<Group> set = new HashSet<>();
                Iterator iterator = snapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(new Group(((DataSnapshot)iterator.next()).getKey()));
                }


                groupList.clear();
                groupList.addAll(set);
                groupsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeViews() {
        groupsRecyclerView = groupView.findViewById(R.id.groupsRecyclerView);
        groupsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        groupsAdapter = new GroupsAdapter(groupList, getContext());
        groupsRecyclerView.setAdapter(groupsAdapter);



    }

    public void createNewGroup() {
        final EditText etGroupName = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        builder.setTitle("Group Name:");
        builder.setView(etGroupName).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = etGroupName.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(getActivity(), "Please set Group Name", Toast.LENGTH_SHORT).show();
                }else{
                    instantiateGroup(groupName);
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.Maroon);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.Maroon);
            }
        });

        dialog.show();

        etGroupName.setHint("Kolodvorska 34...");


    }

    private void instantiateGroup(String groupName) {
        databaseReference.child(groupName).setValue("").addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(getContext(), "You have successfully created "+ groupName, Toast.LENGTH_SHORT).show();
            }
        });
    }
}