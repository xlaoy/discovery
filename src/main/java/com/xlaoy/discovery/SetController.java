package com.xlaoy.discovery;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/6/21 0021.
 */
@Api(tags = "设置 API")
@RestController
@RequestMapping("/discovery")
public class SetController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationInfoManager infoManager;

    @PostMapping("/getInstanceInfo")
    @ApiOperation(response = String.class, value = "getInstanceInfo")
    public InstanceInfo getInstanceInfo() {
        return infoManager.getInfo();
    }
}
