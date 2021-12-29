package com.yck.gbvideo.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author YangChengKai
 * @create 2021/12/28 14:59
 * 信令服务配置信息
 */

@Data
@Component
public class SipConfig {

    @Value("${sip.ip}")
    private String ip;
    @Value("${sip.monitor-ip}")
    private String monitorIp = "0.0.0.0";
    @Value("${sip.port}")
    private Integer port;
    @Value("${sip.domain}")
    private String domain;
    @Value("${sip.id}")
    private String id;
    @Value("${sip.password}")
    private String password;

}
