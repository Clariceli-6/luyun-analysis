package com.luyun.dto;

import lombok.Data;

@Data
public class PredLocsDTO {
    private double longitude;
    private double latitude;
    private byte posConfidence;
    private short speed;
    private byte speedConfidence;
    private long heading;
    private byte headConfidence;
}
