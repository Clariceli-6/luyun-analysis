package com.luyun.controller;

import com.luyun.dto.VisualDTO;
import com.luyun.entity.Objective;
import com.luyun.result.Result;
import com.luyun.service.VisualService;
import com.luyun.vo.DeviceTypeCountVO;
import com.luyun.vo.ErrorVO;
import com.luyun.vo.ObjectiveTypeCountVO;
import com.luyun.vo.ScrollVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据监控模块
 */
@CrossOrigin(origins = "http://localhost:8080") // 跨域请求
@RestController
@RequestMapping("/visual")
@Slf4j
public class VisualController {
    @Autowired
    private VisualService visualService;

    /**
     * 在线设备查询
     */
    @PostMapping("/onlineDevice")
    public Result<List<DeviceTypeCountVO>> VisualOnlineDeviceCount(@RequestBody VisualDTO visualDTO)
    {
        log.info("在线设备数量：{}", visualDTO);
        List<DeviceTypeCountVO> deviceTypeCountVOSList = visualService.getOnlineDeviceCount(visualDTO);
        return Result.success(deviceTypeCountVOSList);
    }

    @PostMapping("/scroll")
    public Result<List<ScrollVO>> Scroll(@RequestBody VisualDTO visualDTO)
    {
        log.info("开始实时滚动：{}", visualDTO);
        List<ScrollVO> scrollVOList = visualService.getScroll(visualDTO);
        return Result.success(scrollVOList);
    }

    @PostMapping("/object")
    public Result<List<ObjectiveTypeCountVO>> getObjectiveList(@RequestBody VisualDTO visualDTO)
    {
        log.info("目标对象数量：{}", visualDTO);
        List<ObjectiveTypeCountVO> objectiveTypeCountVOList = visualService.getObjectiveTypeCount(visualDTO);
        return Result.success(objectiveTypeCountVOList);
    }

    @PostMapping("/exception")
    public Result<List<ErrorVO>> getErrorList(@RequestBody VisualDTO visualDTO) {
        log.info("查询异常记录：{}", visualDTO);
        List<ErrorVO> ErrorVOlist = visualService.getErrorsByTime(visualDTO.getStartTime(), visualDTO.getInterval());
        return Result.success(ErrorVOlist);
    }

}
