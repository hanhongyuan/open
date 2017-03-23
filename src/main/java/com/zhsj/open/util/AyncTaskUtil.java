package com.zhsj.open.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 */
@Component
public class AyncTaskUtil {
    private static final Logger logger = LoggerFactory.getLogger(AyncTaskUtil.class);
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public void commitAyncTask(Runnable runnable) {
        if(null != runnable){
            executor.execute(runnable);
        }
    }

    public void commitAyncTask(List<Runnable> runnableList) {
        if(null != runnableList){
            for (Runnable runnable : runnableList){
                commitAyncTask(runnable);
            }
        }
    }

}
