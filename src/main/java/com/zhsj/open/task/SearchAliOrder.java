package com.zhsj.open.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lcg on 17/1/14.
 */
@Service
public class SearchAliOrder implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SearchAliOrder.class);

    // 轮询
    private ScheduledExecutorService refreshExecutorService = Executors.newScheduledThreadPool(1);
    // 轮询间隔
    private long refreshPeriod = 300; // seconds


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("#SearchOrder#============");
        refresh();
        refreshExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    refresh();
                } catch (Exception e) {
                    logger.error("pull failed!", e);
                }
            }
        }, 60, refreshPeriod, TimeUnit.SECONDS);
    }

    public void refresh(){

    }
}
