package com.example.denisdemin.frogogotest.data.Api.model;

import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class User {

    private final String dateFormat = "dd/MM/yy HH:mm";

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("url")
    @Expose
    private String url;

    private boolean isExtraVisible = false;

    public boolean isExtraVisible() {
        return isExtraVisible;
    }

    public void setExtraVisible(boolean extraVisible) {
        isExtraVisible = extraVisible;
    }

    public int extraViewVisibility(){
        if(isExtraVisible){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCreatedAt() {
        DateTime dt = new DateTime(createdAt);
        return dt.toString(dateFormat);
    }

    public String getUpdatedAt() {
        DateTime dt = new DateTime(updatedAt);
        return dt.toString(dateFormat);
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
