package com.luyun.controller;

import com.luyun.dto.ExportDeviceIdDTO;
import com.luyun.dto.ExportTimeDTO;
import com.luyun.result.PageResult;
import com.luyun.result.Result;
import com.luyun.service.ExportService;
import com.luyun.vo.DeviceIdVO;
import com.luyun.vo.ExcelDeviceVO;
import com.luyun.vo.ExcelTimeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 导出功能
 */
@CrossOrigin(origins = "http://localhost:8080") // 跨域请求
@RestController
@RequestMapping("/excel")
@Slf4j
public class ExportController {
    @Autowired
    private ExportService exportService;

    /**
     * 设备ID导出
     */
    @PostMapping("/deviceId")
    public Result<PageResult> ExportByDeviceId(@RequestBody ExportDeviceIdDTO exportDeviceIdDTO)
    {
        log.info("根据设备ID导出数据：{}", exportDeviceIdDTO);
        PageResult pageResult = exportService.exportByDeviceId(exportDeviceIdDTO);
        return Result.success(pageResult);
    }

    /**
     * 时间段导出
     */
    @PostMapping("/time")
    public Result<PageResult> ExportTime(@RequestBody ExportTimeDTO exportTimeDTO)
    {
        log.info("根据时间段导出数据：{}", exportTimeDTO);
        PageResult pageResult = exportService.exportByTime(exportTimeDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/exportDevice")
    public Result<List<ExcelDeviceVO>> ExcelDevice(@RequestParam String deviceId)
    {
        log.info("根据设备ID导出EXCEL：{}", deviceId);
        List<ExcelDeviceVO> deviceVOList = exportService.excelByDevice(deviceId);
        return Result.success(deviceVOList);
    }

    @GetMapping("/exportTime")
    public Result<List<ExcelTimeVO>> ExcelTime(@RequestParam long startTime, @RequestParam long endTime)
    {
        log.info("根据时间段导出EXCEL：{}", startTime, endTime);
        List<ExcelTimeVO> timeVOList = exportService.excelByDevice(startTime, endTime);
        return Result.success(timeVOList);
    }

}