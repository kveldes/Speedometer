package com.example.speedometer;

public class UserLoginAttributes {

    //UserLoginAttributes userAttributes = new UserLoginAttributes(userID,latitude,longitude,speed,time);

    private float speedLimit = 1;
    private String timestamp;
    private String userID;
    private String Latitude;
    private String Longitude;
    private float currentSpeed;


    public UserLoginAttributes() {
    }

    public UserLoginAttributes(String userID, String latitude, String longitude, float currentSpeed,String timestamp) {
        this.userID = userID;
        Latitude = latitude;
        Longitude = longitude;
        this.currentSpeed = currentSpeed;
        this.timestamp = timestamp;
        this.speedLimit = 1;
    }



    public float getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
