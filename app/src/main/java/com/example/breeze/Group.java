package com.example.breeze;

import de.hdodenhof.circleimageview.CircleImageView;

public class Group {

    private CircleImageView groupImage;
    private String groupName;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public CircleImageView getGroupImage() {
        return groupImage;
    }

    public String getGroupName() {
        return groupName;
    }
}
