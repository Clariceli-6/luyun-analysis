package com.luyun.vo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 查询设备ID返回的记录格式
 */
@Data
@Builder
public class DeviceIdVO {
    @JsonProperty("device_type")
    private String deviceType;

    @JsonProperty("timestamp_of_dev_out")
    private long timestampOfDevOut;

    @JsonProperty("timestamp_of_det_in")
    private long timestampOfDetIn;

    @JsonProperty("timestamp_of_det_out")
    private long timestampOfDetOut;

    @JsonProperty("objective_num")
    private short objectiveNum;

    @JsonProperty("device_id")
    private String deviceId;

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
