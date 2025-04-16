package com.luyun.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExcelTimeVO {
    @JsonProperty("device_id")
    private String deviceId;

    private byte type;

    private double longitude;

    private double latitude;

    private short speed;
}
