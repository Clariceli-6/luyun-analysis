package com.luyun.mapper;

import com.github.pagehelper.Page;
import com.luyun.dto.QueryDeviceDTO;
import com.luyun.dto.QueryTimeDTO;
import com.luyun.vo.DeviceIdVO;
import com.luyun.vo.TimeVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QueryMapper {
    /**
     * 设备ID查询
     * @param querydeviceDTO
     * @return
     */
    Page<DeviceIdVO> queryDevice(QueryDeviceDTO querydeviceDTO);

    /**
     * 时间段查询
     * @param queryTimeDTO
     * @return
     */
    Page<TimeVO> queryTime(QueryTimeDTO queryTimeDTO);
}
