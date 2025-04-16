package com.luyun.service;

import com.luyun.dto.QueryDeviceDTO;
import com.luyun.dto.QueryTimeDTO;
import com.luyun.result.PageResult;

public interface QueryService {
    /**
     * 根据设备ID获取记录
     */
    public PageResult getDeviceIds(QueryDeviceDTO queryDeviceDTO);

    /**
     * 根据时间段获取记录
     */
    public PageResult getTimes(QueryTimeDTO queryTimeDTO);
}
