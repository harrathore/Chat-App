package com.example.mywhastapp.Models;

public class Users {

    String profilePicture, username, email, password, userId, lastmsg, status;


    public Users(String profilePicture, String username, String email, String password, String userId, String lastmsg, String status) {
        this.profilePicture = profilePicture;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.lastmsg = lastmsg;
        this.status = status;
    }

    public Users(){}

    //signUp Constructor
    public Users(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }



}
