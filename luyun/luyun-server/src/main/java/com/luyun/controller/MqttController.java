package com.luyun.controller;

import com.luyun.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mqtt")
public class MqttController {

    private final MqttService mqttService;

    @Autowired
    public MqttController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @GetMapping("/status")
    public String getStatus() {
        return "MQTT client is connected: " + mqttService.getMqttClient().isConnected();
    }
}
