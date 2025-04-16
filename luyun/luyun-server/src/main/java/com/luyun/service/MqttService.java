package com.luyun.service;
import com.luyun.dto.FilterInfoDTO;
import com.luyun.dto.HistLocsDTO;
import com.luyun.dto.ObjectiveDTO;
import com.luyun.dto.PredLocsDTO;
import com.luyun.entity.Device;
import com.luyun.entity.Objective;
import com.luyun.mapper.MqttMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class MqttService {

    @Value("${mqtt.url}")
    private String brokerUrl;

    @Value("${mqtt.userName}")
    private String userName;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.subTopics}")
    private String subTopics;

    private MqttClient mqttClient;

    @Autowired
    private MqttMapper mqttMapper;

    // Spring 完成依赖注入后执行
    @PostConstruct
    public void init() {
        try {
            System.out.println("brokerUrl: " + brokerUrl);
            System.out.println("userName: " + userName);
            System.out.println("password: " + password);
            System.out.println("clientId: " + clientId);
            System.out.println("subTopics: " + subTopics);

            mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            mqttClient.connect(options);

            // 订阅主题
            mqttClient.subscribe(subTopics, (topic, message) -> {
                System.out.println("Received message from topic: " + topic);
                // 只处理以 "_hex" 结尾的 topic
                if (!topic.endsWith("_hex")) {
                    System.out.println("Topic ignored (not ending with _hex): " + topic);
                    return;
                }

                byte[] payload = message.getPayload();

                // 进行解析
                parseMessage(payload);

            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public void parseMessage(byte[] payload){
        try{
            // 数据
            ByteBuffer buffer = ByteBuffer.wrap(payload);
            // Entity示例创建
            Device device = new Device();


            // 起始符 BYTE
            byte startByte = buffer.get();
            if(startByte != (byte)0xf2){
                throw new IllegalArgumentException("Invalid start byte, except 0xf2");
            }
            // System.out.println("startByte: " + String.format("0x%02X", startByte));

            // 随机生成error
            boolean error = generateError();
            // System.out.println("error: " + error);
            device.setError(error);

            // 数据单元长度 BYTE[4]
            int length = buffer.getInt();
            // System.out.println("length: " + length);

            // 数据类别 BYTE
            byte type = buffer.get();
            // System.out.println("type: " + type);

            // 版本号 BYTE
            byte version = buffer.get();
            // System.out.println("version: " + version);

            // 时间戳 TIMESTAMP
            long timestamp = buffer.getLong();
            // System.out.println("timestamp: " + timestamp);

            // 控制内容 1
            byte control = buffer.get();
            // System.out.println("control: " + control);

            // 数据单元
            // 渠道来源 BYTE
            byte channelId = buffer.get();
            // System.out.println("channelId: " + channelId);

            // RCUID BYTE[8]
            long rcuId = buffer.getLong();
            // System.out.println("rcuId: " + rcuId);
            device.setRcuId(rcuId);

            // 设备类别 BYTE
            byte deviceType = buffer.get();
            // System.out.println("deviceType: " + deviceType);
            device.setDeviceType(deviceType);

            // 感知设备编号 BYTE[11]
            byte[] deviceIdBytes = new byte[11];
            buffer.get(deviceIdBytes);
            StringBuilder deviceIdBuilder = new StringBuilder();
            boolean allZero = true;
            for (byte b : deviceIdBytes) {
                if (b != 0) allZero = false;
                int val = b & 0xFF; // 转成无符号整数
                deviceIdBuilder.append(String.format("%02d", val));
            }
            String deviceId = allZero ? "0000000000000000000000" : deviceIdBuilder.toString();
            // System.out.println("deviceId: " + deviceId);
            device.setDeviceId(deviceId); // 存进去

            // 输出时间戳 TIMESTAMP
            long timestampOfDevOut = buffer.getLong() / 1000;
            // System.out.println("timestampOfDevOut: " + timestampOfDevOut);
            device.setTimestampOfDevOut(timestampOfDevOut);


            // 计算应用时间戳 TIMESTAMP
            long timestampOfDetIn = buffer.getLong() / 1000;
            // System.out.println("timestampOfDetIn: " + timestampOfDetIn);
            device.setTimestampOfDetIn(timestampOfDetIn);

            // 输出坐标结果时间戳 TIMESTAMP
            long timestampOfDetOut = buffer.getLong() / 1000;
            // System.out.println("timestampOfDetOut: " + timestampOfDetOut);
            device.setTimestampOfDetOut(timestampOfDetOut);

            // 坐标系类型 BYTE
            byte gnssType = buffer.get();
            // System.out.println("gnssType: " + gnssType);

            // 感知对象数量 WORD
            short objectiveNum = buffer.getShort();
            // System.out.println("objectiveNum: " + objectiveNum);
            device.setObjectiveNum(objectiveNum);

            // 感知对象列表
            List<ObjectiveDTO> obectiveDTOList = getObjectiveDTOList(buffer, objectiveNum, timestampOfDevOut, deviceId);

            // 存入数据库
            mqttMapper.insertDevice(device);

        }catch(Exception e){
            e.printStackTrace();
            return;
        }
    }

    public List<ObjectiveDTO> getObjectiveDTOList(ByteBuffer buffer, short objectiveNum, long timestampOfDevOut, String deviceId)
    {
        List<ObjectiveDTO> objectiveDTOList = new ArrayList<>();  // 用于存储所有解析的目标对象

        for(short i = 0; i < objectiveNum; i ++)
        {
            ObjectiveDTO objectiveDTO = new ObjectiveDTO();

            //entity示例
            Objective objective = new Objective();
            objective.setTimeStampOfDevOut(timestampOfDevOut);
            objective.setDeviceId(deviceId);

            // TODO：4-10晚上修改
            byte[] uuidBytes = new byte[16];
            buffer.get(uuidBytes);
            StringBuilder uuidBuilder = new StringBuilder();
            boolean allZero = true;
            for (byte b : uuidBytes) {
                if (b != 0) allZero = false;
                int val = b & 0xFF;
                uuidBuilder.append(String.format("%02x", val)); // 用16进制格式，更像UUID
            }
            String uuid = allZero ? "00000000000000000000000000000000" : uuidBuilder.toString();
            // System.out.println("uuid: " + uuid);
            objectiveDTO.setUuid(uuid);

            short objId = buffer.getShort();
            objectiveDTO.setObjId(objId);
            // System.out.println("objId: " + objId);

            byte type = buffer.get();
            objectiveDTO.setType(type);
            // System.out.println("type: " + type);
            objective.setType(type);


            byte status = buffer.get();
            objectiveDTO.setStatus(status);
            // System.out.println("status: " + status);

            short len = buffer.getShort();
            objectiveDTO.setLen(len);
            // System.out.println("len: " + len);

            short width = buffer.getShort();
            objectiveDTO.setWidth(width);
            // System.out.println("width: " + width);

            short height = buffer.getShort();
            objectiveDTO.setHeight(height);
            // System.out.println("height: " + height);

            long longitude = buffer.getInt() & 0xFFFFFFFFL;
            objectiveDTO.setLongitude(longitude);
            // System.out.println("longitude: " + longitude);
            objective.setLongitude(longitude);

            long latitude = buffer.getInt() & 0xFFFFFFFFL;
            objectiveDTO.setLatitude(latitude);
            // System.out.println("latitude: " + latitude);
            objective.setLatitude(latitude);

            // 东西向距离 DWORD
            long locEast = buffer.getInt() & 0xFFFFFFFFL;
            objectiveDTO.setLocEast(locEast);
            // System.out.println("locEast: " + locEast);

            // 南北向距离 DWORD
            long locNorth = buffer.getInt() & 0xFFFFFFFFL;
            objectiveDTO.setLocNorth(locNorth);
            // System.out.println("locNorth: " + locNorth);

            // -1即为0xff
            // 位置精确等级 BYTE
            byte posConfidence = buffer.get();
            objectiveDTO.setPosConfidence(posConfidence);
            // System.out.println("posConfidence: " + posConfidence);
            // System.out.println("posConfidence: " + String.format("0x%02X", posConfidence));

            // 高程 DWORD
            long elevation = buffer.getInt() & 0xFFFFFFFFL;
            objectiveDTO.setElevation(elevation);
            // System.out.println("elevation: " + elevation);

            // 高程精度 BYTE
            byte elevationConfidence = buffer.get();
            objectiveDTO.setElevationConfidence(elevationConfidence);
            // System.out.println("elevationConfidence: " + elevationConfidence);

            // 速度 WORD
            short speed = buffer.getShort();
            objectiveDTO.setSpeed(speed);
            // System.out.println("speed: " + speed);
            objective.setSpeed(speed);

            // 速度精确等级 BYTE
            byte speedConfidence = buffer.get();
            objectiveDTO.setSpeedConfidence(speedConfidence);
            // System.out.println("speedConfidence: " + speedConfidence);

            // 东西向速度 WORD
            short speedEast = buffer.getShort();
            objectiveDTO.setSpeedEast(speedEast);
            // System.out.println("speedEast: " + speedEast);

            // 东西向速度精度等级 BYTE
            byte speedEastConfidence = buffer.get();
            objectiveDTO.setSpeedEastConfidence(speedEastConfidence);
            // System.out.println("speedEastConfidence: " + speedEastConfidence);

            // 南北向速度 WORD
            short speedNorth = buffer.getShort();
            objectiveDTO.setSpeedNorth(speedNorth);
            // System.out.println("speedNorth: " + speedNorth);

            // 南北向速度精度等级 BYTE
            byte speedNorthConfidence = buffer.get();
            objectiveDTO.setSpeedNorthConfidence(speedNorthConfidence);
            // System.out.println("speedNorthConfidence: " + speedNorthConfidence);

            // 航向角 DWORD
            long heading = buffer.getInt() & 0xFFFFFFFFL;
            objectiveDTO.setHeading(heading);
            // System.out.println("heading: " + heading);

            // 航向精度等级 BYTE
            byte headingConfidence = buffer.get();
            objectiveDTO.setHeadingConfidence(headingConfidence);
            // System.out.println("headingConfidence: " + headingConfidence);

            // 目标纵向加速度 WORD
            short acclVert = buffer.getShort();
            objectiveDTO.setAcclVert(acclVert);
            // System.out.println("acclVert: " + acclVert);

            // 目标纵向加速度置精度等级 BYTE
            byte accelVertConfidence = buffer.get();
            objectiveDTO.setAccelVertConfidence(accelVertConfidence);
            // System.out.println("accelVertConfidence: " + accelVertConfidence);

            // 目标跟踪时长 DWORD
            long trackedTimes = buffer.getInt() & 0xFFFFFFFFL;
            objectiveDTO.setTrackedTimes(trackedTimes);
            // System.out.println("trackedTimes: " + trackedTimes);

            // 目标历史轨迹数量 WORD
            short histLocNum = buffer.getShort();
            objectiveDTO.setHistLocNum(histLocNum);
            // System.out.println("histLocNum: " + histLocNum);

            // 目标历史轨迹列表
            List<HistLocsDTO> histLocsList = getHistLocsList(buffer, histLocNum);
            objectiveDTO.setHistLocsList(histLocsList);

            // 目标预测轨迹数量 WORD
            short predLocNum = buffer.getShort();
            objectiveDTO.setPredLocNum(predLocNum);
            // System.out.println("predLocNum: " + predLocNum);

            // 目标预测轨迹列表
            List<PredLocsDTO> predLocsList = getPredLocsList(buffer, predLocNum);
            objectiveDTO.setPredLocsList(predLocsList);

            // 目标所在车道编号 BYTE
            byte laneId = buffer.get();
            objectiveDTO.setLaneId(laneId);
            // System.out.println("laneId: " + laneId);

            // 滤波信息的类型 BYTE
            byte filterInfoType = buffer.get();
            objectiveDTO.setFilterInfoType(filterInfoType);
            // System.out.println("filterInfoType: " + filterInfoType);

            if(filterInfoType == 1)
            {
                // 卡尔曼滤波信息
                FilterInfoDTO filterInfoDTO = getFilterInfoDTO(buffer);
            }

            // 车牌号字节数 BYTE
            byte lenplateNo = buffer.get();
            objectiveDTO.setLenplateNo(lenplateNo);
            // System.out.println("lenplateNo: " + lenplateNo);

            // 车牌号 STRING[n] 汉字直接进行UTF-8编码：沪A12345对应的HEX为E6B2AA413132333435
            // 读取车牌号的字节数据
            byte[] plateNoBytes = new byte[lenplateNo];
            buffer.get(plateNoBytes);
            // 将字节数组转换为字符串，假设车牌号是UTF-8编码
            String plateNo = new String(plateNoBytes, StandardCharsets.UTF_8);
            objectiveDTO.setPlateNo(plateNo);
            // System.out.println("plateNo: " + plateNo);

            // 车牌类型 BYTE
            byte plateType = buffer.get();
            objectiveDTO.setPlateType(plateType);
            // System.out.println("plateType: " + plateType);

            // 车牌颜色 BYTE
            byte plateColor = buffer.get();
            objectiveDTO.setPlateColor(plateColor);
            // System.out.println("plateColor: " + plateColor);

            // 车身颜色 BYTE
            byte objColor = buffer.get();
            objectiveDTO.setObjColor(objColor);
            // System.out.println("objColor: " + objColor);

            objectiveDTOList.add(objectiveDTO);

            // 存入数据库
            mqttMapper.insertObjective(objective);
        }

        return objectiveDTOList;
    }

    public List<HistLocsDTO> getHistLocsList(ByteBuffer buffer, short histLocNum) {
        List<HistLocsDTO> histLocsList = new ArrayList<>();

        for (short i = 0; i < histLocNum; i++) {
            HistLocsDTO histLoc = new HistLocsDTO();

            // 经度 DWORD
            long rawLongitude = buffer.getInt();
            double longitude = convertLongitude(rawLongitude);
            histLoc.setLongitude(longitude);

            // 纬度 DWORD
            long rawLatitude = buffer.getInt();
            double latitude = convertLongitude(rawLongitude);
            histLoc.setLatitude(latitude);

            // 位置精度等级 BYTE
            byte posConfidence = buffer.get();
            histLoc.setPosConfidence(posConfidence);

            // 速度 WORD
            short rawSpeed = buffer.getShort();
            histLoc.setSpeed(rawSpeed);

            // 速度精度等级 BYTE
            byte speedConfidence = buffer.get();
            histLoc.setSpeedConfidence(speedConfidence);

            // 航向角 DWORD
            long rawHeading = buffer.getInt();
            histLoc.setHeading(rawHeading);

            // 航向精度等级 BYTE
            byte headConfidence = buffer.get();
            histLoc.setHeadConfidence(headConfidence);

            // 将解析好的轨迹点数据添加到列表中
            histLocsList.add(histLoc);
        }

        return histLocsList;
    }

    public List<PredLocsDTO> getPredLocsList(ByteBuffer buffer, short predLocNum) {
        List<PredLocsDTO> predLocsList = new ArrayList<>();

        for (short i = 0; i < predLocNum; i++) {
            PredLocsDTO predLoc = new PredLocsDTO();

            // 预测轨迹经度 DWORD
            long rawLongitude = buffer.getInt();
            double longitude = convertLongitude(rawLongitude);
            predLoc.setLongitude(longitude);

            // 预测轨迹纬度 DWORD
            long rawLatitude = buffer.getInt();
            double latitude = convertLatitude(rawLatitude);
            predLoc.setLatitude(latitude);

            // 位置精度等级 BYTE
            byte posConfidence = buffer.get();
            predLoc.setPosConfidence(posConfidence);

            // 预测速度 WORD (单位：0.01 m/s)
            short rawSpeed = buffer.getShort();
            predLoc.setSpeed(rawSpeed);

            // 速度精度等级 BYTE
            byte speedConfidence = buffer.get();
            predLoc.setSpeedConfidence(speedConfidence);

            // 预测航向角 DWORD (单位 1e-4 °)
            long rawHeading = buffer.getInt();
            predLoc.setHeading(rawHeading);

            // 航向精度等级 BYTE
            byte headConfidence = buffer.get();
            predLoc.setHeadConfidence(headConfidence);

            predLocsList.add(predLoc);
        }

        return predLocsList;
    }

    public static FilterInfoDTO getFilterInfoDTO(ByteBuffer buffer) {

        // 添加序号-数据类型映射表
        Map<Integer, String> fieldMapping = new HashMap<Integer, String>() {{
            put(1, "BYTE[16]");
            put(2, "short");
            put(3, "BYTE");
            put(4, "BYTE");
            put(5, "short");
            put(6, "short");
            put(7, "short");
            put(8, "long");
            put(9, "long");
            put(10, "long");
            put(11, "long");
            put(12, "BYTE");
            put(13, "long");
            put(14, "BYTE");
            put(15, "short");
            put(16, "short");
            put(17, "short");
            put(18, "BYTE");
            put(19, "short");
            put(20, "BYTE");
            put(21, "long");
            put(22, "BYTE");
            put(23, "short");
            put(24, "BYTE");
            put(25, "long");
            put(26, "short");
            put(27, ""); // 表18中定义
            put(28, "short");
            put(29, ""); // 表18中定义
            put(30, "BYTE");
            put(31, "BYTE");
            put(32, ""); // 表19中定义
            put(33, "BYTE");
            put(34, "");
            put(35, "BYTE");
            put(36, "BYTE");
            put(37, "BYTE");
        }};

        FilterInfoDTO filterInfo = new FilterInfoDTO();

        // 1. 读取维度（WORD，2字节）
        short dimension = buffer.getShort(); // 转为无符号
        filterInfo.setDimension(dimension);

        if (dimension == 0) {
            return filterInfo; // 无后续数据
        }

        // 2. 读取状态量序号列表（WORD[N]）
        short[] varN_Index = new short[dimension];
        for (short i = 0; i < dimension; i++) {
            varN_Index[i] = buffer.getShort();
        }
        filterInfo.setVarN_Index(varN_Index);

        // 3. 读取协方差矩阵下三角元素（DWORD[]，4字节每个）
        int covSize = dimension * (dimension + 1) / 2;
        long[] covs = new long[covSize];
        for (int i = 0; i < covSize; i++) {
            long rawValue = buffer.getLong(); // 转为无符号
            covs[i] = (long)(rawValue * 1e-6) - 2000;    // 单位转换
        }
        filterInfo.setCovs(covs);

        // 4. 可选：预测协方差矩阵和状态量（根据协议判断是否存在）
        // 示例中假设存在（需根据实际协议调整）
        long[] covs_pred = new long[covSize];
        for (int i = 0; i < covSize; i++) {
            long rawValue = buffer.getLong();
            covs_pred[i] = (long)(rawValue * 1e-6) - 2000;
        }
        filterInfo.setCovs_pred(covs_pred);

        // 5. 可选：预测状态量（动态类型，需根据表17的字段类型解析）
        // 解析每个状态量
        // 用 Map 存储每个预测状态量：key 是 varN_pred，value 是对应的状态量值
        Map<String, Object> predictionStates = new HashMap<>();

        for (short i = 0; i < dimension; i++) {
            int index = varN_Index[i];  // 获取状态量的索引
            String fieldType = fieldMapping.get(index);  // 获取字段类型

            // 依据字段类型进行解析
            if (fieldType != null) {
                Object value = null; // 无类型变量方便后续动态赋予类型
                switch (fieldType) {
                    case "BYTE[16]":
                        byte[] byteArray = new byte[16];
                        buffer.get(byteArray);  // 读取16个字节
                        value = byteArray;
                        break;
                    case "short":
                        value = buffer.getShort();  // 读取 short
                        break;
                    case "BYTE":
                        value = buffer.get();  // 读取 BYTE
                        break;
                    case "long":
                        value = buffer.getLong();  // 读取 long
                        break;
                    case "String":
                        // 假设你有类似车牌号这样的字符串字段，使用UTF-8解析
                        int length = buffer.getShort() & 0xFFFF;
                        byte[] strBytes = new byte[length];
                        buffer.get(strBytes);
                        value = new String(strBytes);  // 字符串类型
                        break;
                    default:
                        // System.out.println("未知的字段类型: " + fieldType);
                        break;
                }

                // 动态生成 varN_pred，存储到 Map 中
                String varName = "var" + (i + 1) + "_pred";  // 生成 var1_pred, var2_pred ...
                predictionStates.put(varName, value);
            }
        }

        filterInfo.setPredictionVars(predictionStates);

        return filterInfo;
    }

    // 随机生成error(10%概率异常)
    public static boolean generateError() {
        Random random = new Random();
        int chance = random.nextInt(100); // [0, 99]
        return chance >= 90; // 90% 为 false（0），10% 为 true（1）
    }

    // 转换经度的方法
    public Double convertLongitude(long longitudeInput) {
        // 异常值处理：如果是 0xFFFFFFFF，返回 null 或其他标识异常的值
        if (longitudeInput == 0xFFFFFFFF) {
            return null;  // 或者返回其他异常值，例如 Double.NaN
        }

        // 转换经度：将单位从 1e-7° 转换为实际的度数，并应用偏移量 -180°
        double longitudeActual = (longitudeInput / 1e7) - 180;
        return longitudeActual;
    }

    // 转换纬度的方法
    public static Double convertLatitude(long latitudeInput) {
        // 异常值处理：如果是 0xFFFFFFFF，返回 null 或其他标识异常的值
        if (latitudeInput == 0xFFFFFFFF) {
            return null;  // 或者返回其他异常值，例如 Double.NaN
        }

        // 转换纬度：将单位从 1e-7° 转换为实际的度数，并应用偏移量 -90°
        double latitudeActual = (latitudeInput / 1e7) - 90;
        return latitudeActual;
    }
}
