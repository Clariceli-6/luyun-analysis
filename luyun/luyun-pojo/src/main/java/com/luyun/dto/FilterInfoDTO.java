package com.luyun.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class FilterInfoDTO {
    // 协方差矩阵维度（WORD）
    private short dimension;

    // 状态量序号列表（WORD[N]）
    private short[] varN_Index;

    // 协方差矩阵下三角元素（DWORD[]）
    private long[] covs;

    // 预测协方差矩阵（DWORD[]，可选）
    private long[] covs_pred;

    private Map<String, Object> predictionVars = new HashMap<>();
}