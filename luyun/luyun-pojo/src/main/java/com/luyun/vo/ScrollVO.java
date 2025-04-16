package com.luyun.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScrollVO {
    @JsonProperty("rcu_id")
    private long rcuId;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("device_type")
    private int deviceType;

    @JsonProperty("timestamp_of_dev_out")
    private long timestampOfDevOut;

    @JsonProperty("timestamp_of_det_out")
    private long timestampOfDetOut;

    @JsonProperty("objective_num")
    private long objectiveNum;

    private boolean error;

}
