package com.luyun.controller;

import com.github.pagehelper.Page;
import com.luyun.dto.QueryDeviceDTO;
import com.luyun.dto.QueryTimeDTO;
import com.luyun.result.PageResult;
import com.luyun.result.Result;
import com.luyun.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/query")
@Slf4j
public class QueryController {
    @Autowired
    private QueryService queryService;

    /**
     * 设备ID查询
     */
    @PostMapping("/deviceId")
    public Result<PageResult> QueryDeviceId(@RequestBody QueryDeviceDTO queryDeviceDTO)
    {
        log.info("设备ID查询：{}", queryDeviceDTO);
        PageResult pageResult = queryService.getDeviceIds(queryDeviceDTO);
        return Result.success(pageResult);
    }

    /**
     * 时间段查询
     */
    @PostMapping("/time")
    public Result<PageResult> QueryTime(@RequestBody QueryTimeDTO queryTimeDTO)
    {
        log.info("时间段查询：{}", queryTimeDTO);
        PageResult pageResult = queryService.getTimes(queryTimeDTO);
        return Result.success(pageResult);
    }

}
