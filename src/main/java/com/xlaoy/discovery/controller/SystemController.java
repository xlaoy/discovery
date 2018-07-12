package com.xlaoy.discovery.controller;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.resources.StatusResource;
import com.netflix.eureka.util.StatusInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/10 0010.
 */
@Api(tags = "系统设置 API")
@RestController
public class SystemController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/system/getApplicationList")
    @ApiOperation(response = String.class, value = "获取服务列表")
    public List<Application> getApplicationList() {
        List<Application> applicationList = EurekaServerContextHolder.getInstance().getServerContext().getRegistry().getSortedApplications();
        return applicationList;
    }

    @PostMapping("/system/setApplicationStatus")
    @ApiOperation(response = String.class, value = "设置服务状态")
    public String setApplicationStatus(@RequestBody StatusDTO statusDTO) {
        List<Application> applicationList = this.getApplicationList();
        InstanceInfo instanceInfo = null;
        for(Application application : applicationList) {
            instanceInfo = application.getByInstanceId(statusDTO.getInstanceId());
            if(instanceInfo != null) {
                break;
            }
        }
        if(instanceInfo == null) {
            throw new RuntimeException("实例没找到");
        }
        String status = "";
        if(statusDTO.getStatus() == 1) {
            status = InstanceInfo.InstanceStatus.UP.name().toLowerCase();
        } else {
            status = InstanceInfo.InstanceStatus.DOWN.name().toLowerCase();
        }

        String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/actuator/set_instance_status/" + status;

        logger.info("发送设置状态请求：" + url);

        restTemplate.postForObject(url, "", String.class);

        logger.info(statusDTO.getInstanceId() + " 设置 " + status);
        return "ok";
    }



}
