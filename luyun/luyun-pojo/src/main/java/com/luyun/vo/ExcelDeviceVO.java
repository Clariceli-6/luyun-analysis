package com.luyun.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExcelDeviceVO {
    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("device_type")
    private int deviceType;

    @JsonProperty("timestamp_of_dev_out")
    private long timestampOfDevOut;

    @JsonProperty("timestamp_of_det_out")
    private long timestampOfDetOut;

    @JsonProperty("timestamp_of_det_in")
    private long timestampOfDetIn;

    @JsonProperty("objective_num")
    private short objectiveNum;
}
