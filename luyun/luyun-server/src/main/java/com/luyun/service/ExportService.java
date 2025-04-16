package com.luyun.service;

import com.luyun.dto.ExportDeviceIdDTO;
import com.luyun.dto.ExportTimeDTO;
import com.luyun.result.PageResult;
import com.luyun.vo.DeviceIdVO;
import com.luyun.vo.ExcelDeviceVO;
import com.luyun.vo.ExcelTimeVO;

import java.util.List;

public interface ExportService {

    public PageResult exportByDeviceId(ExportDeviceIdDTO exportDeviceIdDTO);

    public PageResult exportByTime(ExportTimeDTO exportTimeDTO);

    List<ExcelDeviceVO> excelByDevice(String deviceId);

    List<ExcelTimeVO> excelByDevice(long startTime, long endTime);
}
