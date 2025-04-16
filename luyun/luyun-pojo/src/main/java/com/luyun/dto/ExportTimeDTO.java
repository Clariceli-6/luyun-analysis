package com.luyun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExportTimeDTO {
    @JsonProperty("start_time")
    private Long startTime;

    @JsonProperty("end_time")
    private Long endTime;

    private Integer page;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("is_device_id")
    private boolean isDeviceId;

    @JsonProperty("is_type")
    private boolean isType;

    @JsonProperty("is_longitude")
    private boolean isLongitude;

    @JsonProperty("is_latitude")
    private boolean isLatitude;

    @JsonProperty("is_speed")
    private boolean isSpeed;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isDeviceId() {
        return isDeviceId;
    }

    public void setDeviceId(boolean deviceId) {
        isDeviceId = deviceId;
    }

    public boolean isType() {
        return isType;
    }

    public void setType(boolean type) {
        isType = type;
    }

    public boolean isLongitude() {
        return isLongitude;
    }

    public void setLongitude(boolean longitude) {
        isLongitude = longitude;
    }

    public boolean isLatitude() {
        return isLatitude;
    }

    public void setLatitude(boolean latitude) {
        isLatitude = latitude;
    }

    public boolean isSpeed() {
        return isSpeed;
    }

    public void setSpeed(boolean speed) {
        isSpeed = speed;
    }
}
