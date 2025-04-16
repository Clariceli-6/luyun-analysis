package com.luyun.service;

import com.luyun.dto.VisualDTO;
import com.luyun.result.PageResult;
import com.luyun.vo.DeviceTypeCountVO;
import com.luyun.vo.ErrorVO;
import com.luyun.vo.ObjectiveTypeCountVO;
import com.luyun.vo.ScrollVO;

import java.util.List;

public interface VisualService {
    /**
     * 获取实时滚动记录
     * @param visualDTO
     * @return
     */
    public List<ScrollVO> getScroll(VisualDTO visualDTO);

    List<ErrorVO> getErrorsByTime(long startTime, int interval);

    /**
     * 根据interval定期分类查询在线设备数量
     */
    public List<DeviceTypeCountVO> getOnlineDeviceCount(VisualDTO visualDTO);

    List<ObjectiveTypeCountVO> getObjectiveTypeCount(VisualDTO visualDTO);
}
