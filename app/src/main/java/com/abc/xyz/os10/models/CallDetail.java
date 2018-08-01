package com.abc.xyz.os10.models;

import java.util.Date;

/**
 * Created by Admin on 7/8/2016.
 */
public class CallDetail {
    private String phNumber,callType, callDate, callName, callDuration, dir;
    private Date callDayTime;
    private String photoID;
    public CallDetail() {
    }

    public Date getCallDayTime() {
        return callDayTime;
    }

    public void setCallDayTime(Date callDayTime) {
        this.callDayTime = callDayTime;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public CallDetail(String phNumber, String callType, String callDate, String callName, String callDuration, String dir, Date callDayTime, String photoID) {

        this.phNumber = phNumber;
        this.callType = callType;
        this.callDate = callDate;
        this.callName = callName;
        this.callDuration = callDuration;
        this.dir = dir;
        this.callDayTime = callDayTime;
        this.photoID = photoID;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }



    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
