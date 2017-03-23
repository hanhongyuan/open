package com.zhsj.open.task;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.zhsj.open.bean.TBWechatInfo;
import com.zhsj.open.constants.WeChatConstants;
import com.zhsj.open.dao.TBWechatInfoDao;
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
public class WeChatToken {

    private static final Logger logger = LoggerFactory.getLogger(WeChatToken.class);

    public static Map<String,String> TOKEN_MAP = Collections.synchronizedMap(new HashMap<String,String>());
    // 轮询
    private ScheduledExecutorService refreshExecutorService = Executors.newScheduledThreadPool(1);

    @Autowired
    private WXService wxService;
    @Autowired
    private TBWechatInfoDao tbWechatInfoDao;

    public void process() {
        logger.info("#WeChatToken#============");
        refresh();
        refreshExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    refresh();
                } catch (Exception e) {
                    logger.error("#WeChatToken.process# e={}",e.getMessage(), e);
                }
            }
        }, 10, 3600, TimeUnit.SECONDS);
    }

    public void refresh(){
        try {
        	List<TBWechatInfo> list = tbWechatInfoDao.getList();
        	if(CollectionUtils.isEmpty(list)){
        		return;
        	}
        	for(TBWechatInfo info : list){
        		String token = wxService.getAuthToken(OpenWeChatToken.OPEN_WECHAT_INFO.get(WeChatConstants.APPID), info.getAppId(), info.getRefreshToken(), OpenWeChatToken.OPEN_WECHAT_INFO.get(WeChatConstants.TOKEN));
        		logger.info("#WeChatToken.refresh# appId={},token={}",info.getAppId(),token);
        		if(StringUtils.isNotEmpty(token)){
        			TOKEN_MAP.put(info.getAppId(), token);
        		}
        	}
        	
        }catch (Exception e){
            logger.error("#WeChatToken.refresh# e={}",e.getMessage(),e);
        }

    }
}
