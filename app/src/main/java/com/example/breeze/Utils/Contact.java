package com.example.breeze.Utils;

public class Contact {
    private String name;
    private String image;

    public Contact(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
