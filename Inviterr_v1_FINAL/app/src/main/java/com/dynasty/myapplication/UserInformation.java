package com.dynasty.myapplication;

public class UserInformation {

    private String mName = "Not found";
    private String mPhoneNumber = "Not found";
    private String mPassword= "Not found";
    private String mOTP = "Not found";
    private String mIPAddress = "Not found";
    private String mToken= "Not found";

    public UserInformation(String mPhoneNumber, String mPassword, String mToken) {
        this.mPhoneNumber = mPhoneNumber;
        this.mPassword = mPassword;
        this.mToken = mToken;
    }

    public UserInformation(String mName, String mPhoneNumber, String mPassword, String mOTP, String mIPAddress, String mToken) {
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mPassword = mPassword;
        this.mOTP = mOTP;
        this.mIPAddress = mIPAddress;
        this.mToken = mToken;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getOTP() {
        return mOTP;
    }

    public void setOTP(String mOTP) {
        this.mOTP = mOTP;
    }

    public String getIPAddress() {
        return mIPAddress;
    }

    public void setIPAddress(String mIPAddress) {
        this.mIPAddress = mIPAddress;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }
}
