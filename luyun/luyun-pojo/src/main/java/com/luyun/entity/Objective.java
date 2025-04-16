package com.luyun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Objective implements Serializable {
    @JsonProperty("timestamp_of_dev_out")
    private long timestampOfDevOut;

    @JsonProperty("device_id")
    private String deviceId;

    private long type;

    private long longitude;

    private long latitude;

    private long speed;

    public long getTimeStampOfDevOut() {
        return timestampOfDevOut;
    }

    public void setTimeStampOfDevOut(long timeStampOfDevOut) {
        this.timestampOfDevOut = timeStampOfDevOut;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }
}
