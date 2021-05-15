package com.example.breeze;


public class Message {
    private String date;
    private String messageText;
    private String senderName;
    private String profileImage;
    private String time;
    private String userID;

    public Message(String date, String messageText, String senderName, String profileImage, String time, String userID) {
        this.date = date;
        this.messageText = messageText;
        this.senderName = senderName;
        this.profileImage = profileImage;
        this.time = time;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
