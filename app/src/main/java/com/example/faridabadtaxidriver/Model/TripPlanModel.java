package com.example.faridabadtaxidriver.Model;

public class TripPlanModel {
    private String rider,driver;
    private DriverInfoModel driverInfoModel;
    private RiderModel riderModel;
    private String origin,originString;
    private String destination,destinationString;
    private String distancePickup,distanceDestination;
    private String durationPickup,durationDestination;
    private double currentLat,currentLng;
    private boolean isDone,isCancel;
    //value and fee
    private double totalFee;
    private int distanceValue,durationValue;

    //Time in text
    private String timeText;
    //Distance and Duration text
    private String distanceText,durationText;

    public TripPlanModel() {
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public DriverInfoModel getDriverInfoModel() {
        return driverInfoModel;
    }

    public void setDriverInfoModel(DriverInfoModel driverInfoModel) {
        this.driverInfoModel = driverInfoModel;
    }

    public RiderModel getRiderModel() {
        return riderModel;
    }

    public void setRiderModel(RiderModel riderModel) {
        this.riderModel = riderModel;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginString() {
        return originString;
    }

    public void setOriginString(String originString) {
        this.originString = originString;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationString() {
        return destinationString;
    }

    public void setDestinationString(String destinationString) {
        this.destinationString = destinationString;
    }

    public String getDistancePickup() {
        return distancePickup;
    }

    public void setDistancePickup(String distancePickup) {
        this.distancePickup = distancePickup;
    }

    public String getDistanceDestination() {
        return distanceDestination;
    }

    public void setDistanceDestination(String distanceDestination) {
        this.distanceDestination = distanceDestination;
    }

    public String getDurationPickup() {
        return durationPickup;
    }

    public void setDurationPickup(String durationPickup) {
        this.durationPickup = durationPickup;
    }

    public String getDurationDestination() {
        return durationDestination;
    }

    public void setDurationDestination(String durationDestination) {
        this.durationDestination = durationDestination;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public double getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(double currentLng) {
        this.currentLng = currentLng;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public int getDistanceValue() {
        return distanceValue;
    }

    public void setDistanceValue(int distanceValue) {
        this.distanceValue = distanceValue;
    }

    public int getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(int durationValue) {
        this.durationValue = durationValue;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }
}
