package com.luyun.mapper;

import com.luyun.entity.Device;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MqttMapper {
    @Insert("INSERT INTO devices (rcu_id, device_id, device_type, timestamp_of_dev_out, " +
            "timestamp_of_det_in, timestamp_of_det_out, objective_num, error)" +
            "VALUES (#{rcuId}, #{deviceId}, #{deviceType}, #{timestampOfDevOut}," +
            "#{timestampOfDetIn}, #{timestampOfDetOut}, #{objectiveNum}, #{error})")
    void insertDevice(Device device);

    @Insert("INSERT INTO objectives (timestamp_of_dev_out, device_id, type, longitude, latitude, speed)" +
            "VALUES (#{timestampOfDevOut}, #{deviceId}, #{type}, #{longitude}, #{latitude}, #{speed})")
    void insertObjective(Object object);



}
