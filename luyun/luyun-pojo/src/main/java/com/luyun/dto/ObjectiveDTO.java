package com.luyun.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ObjectiveDTO implements Serializable {
    // 唯一编号 BYTE[16]
    private String uuid;

    // 对象编号 WORD
    private short objId;

    // 类型 BYTE
    private byte type;

    // 状态 BYTE
    private byte status;

    // 长度 WORD
    private short len;

    // 宽度 WORD
    private short width;

    // 高度 WORD
    private short height;

    // 经度 DWORD
    private long longitude;

    // 纬度 DWORD
    private long latitude;

    // 东西向距离 DWORD
    private long locEast;

    // 南北向距离 DWORD
    private long locNorth;

    // 位置精确等级 BYTE
    private byte posConfidence;

    // 高程 DWORD
    private long elevation;

    // 高程精度 BYTE
    private byte elevationConfidence;

    // 速度 WORD
    private short speed;

    // 速度精确等级 BYTE
    private byte speedConfidence;

    // 东西向速度 WORD
    private short speedEast;

    // 东西向速度精度等级 BYTE
    private byte speedEastConfidence;

    // 南北向速度 WORD
    private short speedNorth;

    // 南北向速度精度等级 BYTE
    private byte speedNorthConfidence;

    // 航向角 DWORD
    private long heading;

    // 航向精度等级 BYTE
    private byte headingConfidence;

    // 目标纵向加速度 WORD
    private short acclVert;

    // 目标纵向加速度置精度等级 BYTE
    private byte accelVertConfidence;

    // 目标跟踪时长 DWORD
    private long trackedTimes;

    // 目标历史轨迹数量 WORD
    private short histLocNum;

    // 目标历史轨迹列表
    private List<HistLocsDTO> histLocsList;

    // 目标预测轨迹数量 WORD
    private short predLocNum;

    // 目标预测轨迹列表
    private List<PredLocsDTO> predLocsList;

    // 目标所在车道编号 BYTE
    private byte laneId;

    // 滤波信息的类型 BYTE
    private byte filterInfoType;

    // 卡尔曼滤波信息
    private FilterInfoDTO filterInfoDTO;

    // 车牌号字节数 BYTE
    private byte lenplateNo;

    // 车牌号 STRING[n] 汉字直接进行UTF-8编码：沪A12345对应的HEX为E6B2AA413132333435
    private String plateNo;

    // 车牌类型 BYTE
    private byte plateType;

    // 车牌颜色 BYTE
    private byte plateColor;

    // 车身颜色 BYTE
    private byte objColor;
}
