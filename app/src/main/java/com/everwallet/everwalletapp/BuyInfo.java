package com.everwallet.everwalletapp;

public class BuyInfo {

    public String bKashNumber;
    public String bdtAmount;
    public String date;
    public String dollerType;
    public String lastDigit;
    public String orderType;
    public String pushKey;
    public String sendingEmail;
    public String status;
    public String usdAmount;
    public String userId;
    public String userName;
    public String userPhoneNumber;

    public BuyInfo(String bKashNumber, String bdtAmount, String date, String dollerType, String lastDigit, String orderType, String pushKey, String sendingEmail, String status, String usdAmount, String userId, String userName, String userPhoneNumber) {
        this.bKashNumber = bKashNumber;
        this.bdtAmount = bdtAmount;
        this.date = date;
        this.dollerType = dollerType;
        this.lastDigit = lastDigit;
        this.orderType = orderType;
        this.pushKey = pushKey;
        this.sendingEmail = sendingEmail;
        this.status = status;
        this.usdAmount = usdAmount;
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getbKashNumber() {
        return bKashNumber;
    }

    public void setbKashNumber(String bKashNumber) {
        this.bKashNumber = bKashNumber;
    }

    public String getBdtAmount() {
        return bdtAmount;
    }

    public void setBdtAmount(String bdtAmount) {
        this.bdtAmount = bdtAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDollerType() {
        return dollerType;
    }

    public void setDollerType(String dollerType) {
        this.dollerType = dollerType;
    }

    public String getLastDigit() {
        return lastDigit;
    }

    public void setLastDigit(String lastDigit) {
        this.lastDigit = lastDigit;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getSendingEmail() {
        return sendingEmail;
    }

    public void setSendingEmail(String sendingEmail) {
        this.sendingEmail = sendingEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(String usdAmount) {
        this.usdAmount = usdAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
