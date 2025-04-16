package com.luyun.service.impl;

import com.luyun.dto.VisualDTO;
import com.luyun.mapper.VisualMapper;
import com.luyun.result.PageResult;
import com.luyun.service.VisualService;
import com.luyun.vo.DeviceTypeCountVO;
import com.luyun.vo.ErrorVO;
import com.luyun.vo.ObjectiveTypeCountVO;
import com.luyun.vo.ScrollVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisualServiceImpl implements VisualService {

    @Autowired
    private VisualMapper visualMapper;

    @Override
    public List<DeviceTypeCountVO> getOnlineDeviceCount(VisualDTO visualDTO){
        long startTime = visualDTO.getStartTime();
        int interval = visualDTO.getInterval();

        List<DeviceTypeCountVO> deviceTypeCountVOList = visualMapper.countOnlineDevicesByType(startTime, interval);
        return deviceTypeCountVOList;
    }

    @Override
    public List<ScrollVO> getScroll(VisualDTO visualDTO) {
        long startTime = visualDTO.getStartTime();
        int interval = visualDTO.getInterval();
        List<ScrollVO> scrollVOList = visualMapper.getScroll(startTime, interval);
        return scrollVOList;
    }

    @Override
    public List<ObjectiveTypeCountVO> getObjectiveTypeCount(VisualDTO visualDTO) {
        long startTime = visualDTO.getStartTime();
        int interval = visualDTO.getInterval();
        List<ObjectiveTypeCountVO> objectiveTypeCountVOList =
                visualMapper.countObjectiveByType(startTime, interval);
        return objectiveTypeCountVOList;
    }

    @Override
    public List<ErrorVO> getErrorsByTime(long startTime, int interval) {
        long endTime = startTime + interval;
        return visualMapper.selectErrorsByTimeRange(startTime, endTime);
    }
}
