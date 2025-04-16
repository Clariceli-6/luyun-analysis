package com.luyun.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luyun.dto.ExportDeviceIdDTO;
import com.luyun.dto.ExportTimeDTO;
import com.luyun.mapper.ExportMapper;
import com.luyun.result.PageResult;
import com.luyun.service.ExportService;
import com.luyun.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportMapper exportMapper;

    @Override
    public PageResult exportByDeviceId(ExportDeviceIdDTO exportDeviceIdDTO){
        PageHelper.startPage(exportDeviceIdDTO.getPage(), exportDeviceIdDTO.getPageSize());
        String deviceId = exportDeviceIdDTO.getDeviceId();
        boolean isDeviceType = exportDeviceIdDTO.isDeviceType();
        boolean isTimestampOfDevOut = exportDeviceIdDTO.isTimestampOfDevOut();
        boolean isTimestampOfDetIn = exportDeviceIdDTO.isTimestampOfDetIn();
        boolean isTimestampOfDetOut = exportDeviceIdDTO.isTimestampOfDetOut();
        boolean isObjectiveNum = exportDeviceIdDTO.isObjectiveNum();

        Page<ExportDeviceVO> page = exportMapper.exportDevice(deviceId, isDeviceType, isObjectiveNum,
                isTimestampOfDevOut, isTimestampOfDetIn, isTimestampOfDetOut);

        long total = page.getTotal();
        List<ExportDeviceVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public PageResult exportByTime(ExportTimeDTO exportTimeDTO) {
        PageHelper.startPage(exportTimeDTO.getPage(), exportTimeDTO.getPageSize());
        Long startTime = exportTimeDTO.getStartTime();
        Long endTime = exportTimeDTO.getEndTime();
        boolean isDeviceId = exportTimeDTO.isDeviceId();
        boolean isType = exportTimeDTO.isType();
        boolean isLongitude = exportTimeDTO.isLongitude();
        boolean isLatitude = exportTimeDTO.isLatitude();
        boolean isSpeed = exportTimeDTO.isSpeed();

        Page<ExportTimeVO> page = exportMapper.exportByTime(startTime, endTime,
                isDeviceId, isType, isLongitude, isLatitude, isSpeed);

        long total = page.getTotal();
        List<ExportTimeVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public List<ExcelDeviceVO> excelByDevice(String deviceId) {
        List<ExcelDeviceVO> deviceIdVOList = exportMapper.excelByDevice(deviceId);
        return deviceIdVOList;
    }

    @Override
    public List<ExcelTimeVO> excelByDevice(long startTime, long endTime) {
        List<ExcelTimeVO> excelTimeVOList = exportMapper.excelByTime(startTime, endTime);
        return excelTimeVOList;
    }


}

