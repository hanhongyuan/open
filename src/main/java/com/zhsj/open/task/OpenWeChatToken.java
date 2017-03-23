package com.zhsj.open.task;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhsj.open.constants.WeChatConstants;
import com.zhsj.open.service.WXService;
import com.zhsj.open.util.MtConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lcg on 17/1/14.
 */
@Service
public class OpenWeChatToken {

    private static final Logger logger = LoggerFactory.getLogger(OpenWeChatToken.class);
    
    public static Map<String,String> OPEN_WECHAT_INFO = Collections.synchronizedMap(new HashMap<String,String>());

    public static boolean OPEN_TLKEN_LOKER = false;
    // 轮询
    private ScheduledExecutorService refreshExecutorService = Executors.newScheduledThreadPool(1);


    @Autowired
    private WXService wxService;
    @Autowired
    private WeChatToken weChatToken;

    //启动定时,刷新token
    public void process(){
        logger.info("#OpenWeChatToken#============");
        if(!OPEN_TLKEN_LOKER){
        	logger.info("#OpenWeChatToken# not allow");
        	return;
        }
        refreshExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    refresh();
                } catch (Exception e) {
                    logger.error("#OpenWeChatToken.process# e={},e", e.getMessage(),e);
                }
            }
        }, 100, 3600, TimeUnit.SECONDS);
    }

    public void refresh(){
        try {
        	String componentToken = wxService.getComponentToken(OPEN_WECHAT_INFO.get(WeChatConstants.APPID), 
        														OPEN_WECHAT_INFO.get(WeChatConstants.APPSECRET),
        														OPEN_WECHAT_INFO.get(WeChatConstants.VERIFY_TICKET));
      	   if(StringUtils.isNotEmpty(componentToken)){
      		   OPEN_WECHAT_INFO.put(WeChatConstants.TOKEN, componentToken);
      		   weChatToken.process();
      	   }
        }catch (Exception e){
            logger.error("#OpenWeChatToken.refresh# e={}",e.getMessage(),e);
        }
    }
}
