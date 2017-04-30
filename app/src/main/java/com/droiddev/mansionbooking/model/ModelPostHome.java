package com.droiddev.mansionbooking.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelPostHome {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("water")
    @Expose
    private String water;
    @SerializedName("elect")
    @Expose
    private String elect;
    @SerializedName("price")
    @Expose
    private ModelPostRatePrice price;
    @SerializedName("image")
    @Expose
    private ArrayList<ModelPostImg> image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getElect() {
        return elect;
    }

    public void setElect(String elect) {
        this.elect = elect;
    }

    public ModelPostRatePrice getPrice() {
        return price;
    }

    public void setPrice(ModelPostRatePrice price) {
        this.price = price;
    }

    public ArrayList<ModelPostImg> getImage() {
        return image;
    }

    public void setImage(ArrayList<ModelPostImg> image) {
        this.image = image;
    }
}