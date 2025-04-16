package com.luyun.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.luyun.dto.QueryDeviceDTO;
import com.luyun.dto.QueryTimeDTO;
import com.luyun.mapper.QueryMapper;
import com.luyun.result.PageResult;
import com.luyun.service.QueryService;
import com.luyun.vo.DeviceIdVO;
import com.luyun.vo.TimeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryServiceImpl implements QueryService {
    @Autowired
    private QueryMapper queryMapper;

    @Override
    public PageResult getDeviceIds(QueryDeviceDTO querydeviceDTO) {
        PageHelper.startPage(querydeviceDTO.getPage(), querydeviceDTO.getPageSize());
        Page<DeviceIdVO> page = queryMapper.queryDevice(querydeviceDTO);

        long total = page.getTotal();
        List<DeviceIdVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public PageResult getTimes(QueryTimeDTO queryTimeDTO) {
        PageHelper.startPage(queryTimeDTO.getPage(), queryTimeDTO.getPageSize());
        Page<TimeVO> page = queryMapper.queryTime(queryTimeDTO);
        System.out.println(page);

        long total = page.getTotal();
        List<TimeVO> records = page.getResult();
        return new PageResult(total, records);
    }
}
