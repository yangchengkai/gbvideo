package com.yck.gbvideo.sip;

import com.yck.gbvideo.conf.SipConfig;
import gov.nist.javax.sip.SipProviderImpl;
import gov.nist.javax.sip.SipStackImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.sip.*;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author YangChengKai
 * @create 2021/12/28 15:10
 * 信令服务初始化
 */

@Slf4j
@Component
public class SipLayer {

    /**
     * sip通信相关参考
     * https://www.oracle.com/cn/technical-resources/articles/enterprise-architecture/introduction-jain-sip-part1.html
     */

    @Autowired
    SipConfig sipConfig;

    @Autowired
    SipProcessor sipProcessor;

    private SipFactory sipFactory;
    private SipStackImpl sipStack;

    /**
     * 加载bean时初始化消息处理器线程池
     */
    private ThreadPoolExecutor processThreadPool;

    public SipLayer() {
        int processThreadNum = Runtime.getRuntime().availableProcessors() * 10;
        LinkedBlockingQueue<Runnable> processQueue = new LinkedBlockingQueue<>(10000);
        processThreadPool = new ThreadPoolExecutor(processThreadNum,processThreadNum,
                0L, TimeUnit.MILLISECONDS,processQueue,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean("sipFactory")
    private SipFactory createSipFactory() {
        //反射
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");
        return sipFactory;
    }

    @Bean("sipStack")
    @DependsOn({"sipFactory"})
    private SipStack createSipStack() throws PeerUnavailableException {
        Properties properties = new Properties();
        properties.setProperty("javax.sip.STACK_NAME", "GB28181_SIP");
        properties.setProperty("javax.sip.IP_ADDRESS", sipConfig.getMonitorIp());
        properties.setProperty("gov.nist.javax.sip.LOG_MESSAGE_CONTENT", "false");
        properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "0");
        properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "sip_server_log");
        properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "sip_debug_log");
        sipStack = (SipStackImpl) sipFactory.createSipStack(properties);
        return sipStack;
    }


    @Bean("tcpSipProvider")
    @DependsOn("sipStack")
    private SipProviderImpl tcpSipProvider() {
        ListeningPoint tcpListeningPoint = null;
        SipProviderImpl tcpSipProvider  = null;
        try {
            tcpListeningPoint = sipStack.createListeningPoint(sipConfig.getMonitorIp(), sipConfig.getPort(), "TCP");
            tcpSipProvider = (SipProviderImpl)sipStack.createSipProvider(tcpListeningPoint);
            tcpSipProvider.addSipListener(sipProcessor);
            log.info("Sip Server TCP 启动成功 [{}:{}]",sipConfig.getMonitorIp(),sipConfig.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tcpSipProvider;
    }

    @Bean("udpSipProvider")
    @DependsOn("sipStack")
    private SipProviderImpl udpSipProvider() {
        ListeningPoint udpListeningPoint = null;
        SipProviderImpl udpSipProvider  = null;
        try {
            udpListeningPoint = sipStack.createListeningPoint(sipConfig.getMonitorIp(), sipConfig.getPort(), "UDP");
            udpSipProvider = (SipProviderImpl)sipStack.createSipProvider(udpListeningPoint);
            udpSipProvider.addSipListener(sipProcessor);
            log.info("Sip Server UDP 启动成功 [{}:{}]",sipConfig.getMonitorIp(),sipConfig.getPort());
        }catch (Exception e){
            e.printStackTrace();
        }
        return udpSipProvider;
    }

}
