package com.luyun.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorVO {

    @JsonProperty("timestamp_of_dev_out")
    private long timestampOfDevOut;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("device_type")
    private byte deviceType;

    private int error;


}