package com.luyun.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeVO {

    @JsonProperty("device_id")
    private String deviceId;

    private byte type;

    private double longitude;

    private double latitude;

    private short speed;
}
