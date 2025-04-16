package com.luyun.mapper;

import com.github.pagehelper.Page;
import com.luyun.dto.ExportDeviceIdDTO;
import com.luyun.dto.ExportTimeDTO;
import com.luyun.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExportMapper {
    Page<ExportDeviceVO> exportDevice(String deviceId,boolean isDeviceType,boolean isObjectiveNum,
                                      boolean isTimestampOfDevOut, boolean isTimestampOfDetIn, boolean isTimestampOfDetOut);

    Page<ExportTimeVO> exportByTime(Long startTime, Long endTime, boolean isDeviceId,
                                    boolean isType, boolean isLongitude, boolean isLatitude,
                                    boolean isSpeed);

    @Select("select device_id, device_type, timestamp_of_dev_out, timestamp_of_det_out, " +
            "timestamp_of_det_in, objective_num from devices " +
            "WHERE device_id LIKE CONCAT('%', #{deviceId}, '%')")
    List<ExcelDeviceVO>  excelByDevice(String deviceId);

    @Select("select device_id, type, longitude, latitude, speed from objectives" +
            " where timestamp_of_dev_out >= #{startTime} and " +
            "timestamp_of_dev_out < #{endTime}")
    List<ExcelTimeVO>  excelByTime(long startTime, long endTime);
}

