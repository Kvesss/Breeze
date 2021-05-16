package com.example.breeze.MainFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.breeze.R;
import com.example.breeze.SearchUsersActivity;


public class ContactFragment extends Fragment {


    private View view;
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
        return view;
    }

    private void sendToSearchUsersActivity() {
        Intent intent = new Intent(getActivity(), SearchUsersActivity.class);
        startActivity(intent);
    }
}