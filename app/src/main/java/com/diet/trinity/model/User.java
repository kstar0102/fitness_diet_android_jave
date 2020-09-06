package com.diet.trinity.model;

import com.diet.trinity.Common;

public class User {
    int mId;
    String mUsername;
    String mName, mEmail, mPassword;
    String mAvatar;
    String mGrade, mRole;
    int mRecordCount;


    public User(int id, String username, String name, String email, String password, String avatar, String grade, String role, int record_count){
        mId = id;
        mUsername = username;
        mName = name;
        mEmail = email;
        mPassword = password;
        mAvatar = avatar;
        mGrade = grade;
        mRole = role;
        mRecordCount = record_count;
    }

    public int getmId() {
        return mId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public String getmName() {
        return mName;
    }

    public String getmAvatar() {
        return Common.getInstance().getBaseURL()+mAvatar;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmGrade() {
        return mGrade;
    }

    public void setmGrade(String mGrade) {
        this.mGrade = mGrade;
    }

    public int getmRecordCount() {
        return mRecordCount;
    }

    public String getmRole(){
        return mRole;
    }
}
