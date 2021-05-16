package com.example.breeze.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.breeze.Utils.Group;
import com.example.breeze.GroupChatActivity;
import com.example.breeze.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder> {

    private final Context context;
    private final List<Group> groupList;


    public GroupsAdapter(List<Group> groupList, Context context) {
        this.groupList = groupList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groupsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, parent, false);
        return new GroupsViewHolder(groupsView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsViewHolder holder, int position) {
        holder.setGroupView(groupList.get(position).getGroupName(), groupList.get(position).getGroupImage());

    }

    @Override
    public int getItemCount() {
        return this.groupList.size();
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder {

        private final TextView groupName;
        private final CircleImageView groupImage;
        private Context context;

        public GroupsViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            groupName = itemView.findViewById(R.id.tvGroupName);
            groupImage = itemView.findViewById(R.id.groupIcon);
            this.context = context;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GroupChatActivity.class);
                    intent.putExtra("groupname", groupName.getText().toString());
                    context.startActivity(intent);

                }
            });
        }

        public void setGroupView(String groupName, CircleImageView groupIcon){
            this.groupName.setText(groupName);
//            this.groupImage.setImageIcon(groupIcon);
        }


    }
}
