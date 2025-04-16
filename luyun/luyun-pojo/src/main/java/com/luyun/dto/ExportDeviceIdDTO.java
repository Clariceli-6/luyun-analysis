package com.luyun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExportDeviceIdDTO implements Serializable {

    @JsonProperty("device_id")
    // 设备ID
    private String deviceId;

    @JsonProperty("page")
    // 页码
    private int page;

    @JsonProperty("page_size")
    // 每页记录数量
    private int pageSize;

    @JsonProperty("is_device_type")
    // 是否导出设备类别
    private boolean isDeviceType;

    @JsonProperty("is_timestamp_of_dev_out")
    // 是否导出输出时间
    private boolean isTimestampOfDevOut;

    @JsonProperty("is_timestamp_of_det_in")
    // 是否导出计算应用时间
    private boolean isTimestampOfDetIn;

    @JsonProperty("is_timestamp_of_det_out")
    // 是否导出输出结果时间
    private boolean isTimestampOfDetOut;

    @JsonProperty("is_objective_num")
    // 是否导出感知对象数量
    private boolean isObjectiveNum;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isDeviceType() {
        return isDeviceType;
    }

    public void setDeviceType(boolean deviceType) {
        isDeviceType = deviceType;
    }

    public boolean isTimestampOfDevOut() {
        return isTimestampOfDevOut;
    }

    public void setTimestampOfDevOut(boolean timestampOfDevOut) {
        isTimestampOfDevOut = timestampOfDevOut;
    }

    public boolean isTimestampOfDetIn() {
        return isTimestampOfDetIn;
    }

    public void setTimestampOfDetIn(boolean timestampOfDetIn) {
        isTimestampOfDetIn = timestampOfDetIn;
    }

    public boolean isTimestampOfDetOut() {
        return isTimestampOfDetOut;
    }

    public void setTimestampOfDetOut(boolean timestampOfDetOut) {
        isTimestampOfDetOut = timestampOfDetOut;
    }

    public boolean isObjectiveNum() {
        return isObjectiveNum;
    }

    public void setObjectiveNum(boolean objectiveNum) {
        isObjectiveNum = objectiveNum;
    }
}
