package com.luyun.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExportTimeVO {
    @JsonProperty("timestamp_of_dev_out")
    private Long timestampOfDevOut;

    @JsonProperty("device_id")
    private String deviceId;

    private Byte type;

    private Double longitude;

    private Double latitude;

    private Short speed;

    public Long getTimestampOfDevOut() {
        return timestampOfDevOut;
    }

    public void setTimestampOfDevOut(Long timestampOfDevOut) {
        this.timestampOfDevOut = timestampOfDevOut;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Short getSpeed() {
        return speed;
    }

    public void setSpeed(Short speed) {
        this.speed = speed;
    }
}
