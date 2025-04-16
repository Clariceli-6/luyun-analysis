package com.luyun.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExportDeviceVO {
    @JsonProperty("device_id")
    // 设备ID
    private String deviceId;

    @JsonProperty("device_type")
    // 设备类别
    private Integer deviceType;

    @JsonProperty("timestamp_of_dev_out")
    // 输出时间
    private Long timestampOfDevOut;

    @JsonProperty("timestamp_of_det_in")
    // 计算应用时间
    private Long timestampOfDetIn;

    @JsonProperty("timestamp_of_det_out")
    // 输出结果时间
    private Long timestampOfDetOut;

    @JsonProperty("objective_num")
    // 感知对象数量
    private Short objectiveNum;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Long getTimestampOfDevOut() {
        return timestampOfDevOut;
    }

    public void setTimestampOfDevOut(Long timestampOfDevOut) {
        this.timestampOfDevOut = timestampOfDevOut;
    }

    public Long getTimestampOfDetIn() {
        return timestampOfDetIn;
    }

    public void setTimestampOfDetIn(Long timestampOfDetIn) {
        this.timestampOfDetIn = timestampOfDetIn;
    }

    public Long getTimestampOfDetOut() {
        return timestampOfDetOut;
    }

    public void setTimestampOfDetOut(Long timestampOfDetOut) {
        this.timestampOfDetOut = timestampOfDetOut;
    }

    public Short getObjectiveNum() {
        return objectiveNum;
    }

    public void setObjectiveNum(Short objectiveNum) {
        this.objectiveNum = objectiveNum;
    }
}
