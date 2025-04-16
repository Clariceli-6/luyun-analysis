package com.luyun.mapper;

import com.luyun.vo.DeviceTypeCountVO;
import com.luyun.vo.ErrorVO;
import com.luyun.vo.ObjectiveTypeCountVO;
import com.luyun.vo.ScrollVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VisualMapper {
    @Select("SELECT rcu_id, device_id, device_type, timestamp_of_dev_out, timestamp_of_det_out, " +
            "objective_num, error " +
            "FROM devices " +
            "WHERE timestamp_of_dev_out >= #{startTime} AND " +
            "timestamp_of_dev_out <= #{startTime} + #{interval}")
    public List<ScrollVO> getScroll(long startTime, int interval);

    List<ErrorVO> selectErrorsByTimeRange(@Param("startTime") long startTime,
                                          @Param("endTime") long endTime);

    List<ObjectiveTypeCountVO>  countObjectiveByType(long startTime, int interval);

    /**
     * 在线设备查询
     * @param
     * @return
     */
    List<DeviceTypeCountVO> countOnlineDevicesByType(long startTime, int interval);

}
