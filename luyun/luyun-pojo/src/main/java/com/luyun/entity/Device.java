package com.luyun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Device implements Serializable {
    @JsonProperty("rcu_id")
    private long rcuId;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("device_type")
    private byte deviceType;

    @JsonProperty("timestamp_of_dev_out")
    private long timestampOfDevOut;

    @JsonProperty("timestamp_of_det_in")
    private long timestampOfDetIn;

    @JsonProperty("timestamp_of_det_out")
    private long timestampOfDetOut;

    @JsonProperty("objective_num")
    private short objectiveNum;
    private boolean error;

    public long getRcuId() {
        return rcuId;
    }

    public void setRcuId(long rcuId) {
        this.rcuId = rcuId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(byte deviceType) {
        this.deviceType = deviceType;
    }

    public long getTimestampOfDevOut() {
        return timestampOfDevOut;
    }

    public void setTimestampOfDevOut(long timestampOfDevOut) {
        this.timestampOfDevOut = timestampOfDevOut;
    }

    public long getTimestampOfDetIn() {
        return timestampOfDetIn;
    }

    public void setTimestampOfDetIn(long timestampOfDetIn) {
        this.timestampOfDetIn = timestampOfDetIn;
    }

    public long getTimestampOfDetOut() {
        return timestampOfDetOut;
    }

    public void setTimestampOfDetOut(long timestampOfDetOut) {
        this.timestampOfDetOut = timestampOfDetOut;
    }

    public short getObjectiveNum() {
        return objectiveNum;
    }

    public void setObjectiveNum(short objectiveNum) {
        this.objectiveNum = objectiveNum;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
