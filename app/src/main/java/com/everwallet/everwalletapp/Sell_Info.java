package com.everwallet.everwalletapp;

public class Sell_Info {

    public String dollerType;
    public String bdtAmount;
    public String usdAmount;
    public String sendingEmail;
    public String receivedEmail;
    public String paymentNumber;
    public String paymentType;
    public String phoneNumber;
    public String userName;
    public String date;
    public String status;
    public String userId;
    public String pushKey;
    public String orderType;

    public Sell_Info(String dollerType, String bdtAmount, String usdAmount, String sendingEmail, String receivedEmail, String paymentNumber, String paymentType, String phoneNumber, String userName, String date, String status, String userId, String pushKey, String orderType) {
        this.dollerType = dollerType;
        this.bdtAmount = bdtAmount;
        this.usdAmount = usdAmount;
        this.sendingEmail = sendingEmail;
        this.receivedEmail = receivedEmail;
        this.paymentNumber = paymentNumber;
        this.paymentType = paymentType;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.date = date;
        this.status = status;
        this.userId = userId;
        this.pushKey = pushKey;
        this.orderType = orderType;
    }

    public Sell_Info() {
    }

    public String getDollerType() {
        return dollerType;
    }

    public void setDollerType(String dollerType) {
        this.dollerType = dollerType;
    }

    public String getBdtAmount() {
        return bdtAmount;
    }

    public void setBdtAmount(String bdtAmount) {
        this.bdtAmount = bdtAmount;
    }

    public String getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(String usdAmount) {
        this.usdAmount = usdAmount;
    }

    public String getSendingEmail() {
        return sendingEmail;
    }

    public void setSendingEmail(String sendingEmail) {
        this.sendingEmail = sendingEmail;
    }

    public String getReceivedEmail() {
        return receivedEmail;
    }

    public void setReceivedEmail(String receivedEmail) {
        this.receivedEmail = receivedEmail;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
