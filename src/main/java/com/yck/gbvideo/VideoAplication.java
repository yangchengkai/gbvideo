package com.yck.gbvideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author YangChengKai
 * @create 2021/12/28 14:56
 */

@SpringBootApplication
public class VideoAplication {

    private static String[] args;
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        VideoAplication.args = args;
        VideoAplication.context = SpringApplication.run(VideoAplication.class,args);
    }

    public static void restart() {
        context.close();
        VideoAplication.context = SpringApplication.run(VideoAplication.class, args);
    }
}
