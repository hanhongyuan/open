package com.zhsj.open.task;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhsj.open.bean.TBWechatOpen;
import com.zhsj.open.constants.WeChatConstants;
import com.zhsj.open.dao.TBWechatOpenDao;
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
public class OpenWeChatInit implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(OpenWeChatInit.class);

    @Autowired
    private WXService wxService;
    @Autowired
    private OpenWeChatToken openWeChatToken;
    @Autowired
    private TBWechatOpenDao tbWechatOpenDao;
    

    @Override
    public void afterPropertiesSet() throws Exception {
       logger.info("#OpenWeChatInit#============");
       try{
    	   //load db
    	   TBWechatOpen tbWechatOpen = tbWechatOpenDao.getOne();
    	   if(tbWechatOpen != null){
    		   OpenWeChatToken.OPEN_WECHAT_INFO.put(WeChatConstants.APPID,tbWechatOpen.getAppId());
    		   OpenWeChatToken.OPEN_WECHAT_INFO.put(WeChatConstants.APPSECRET, tbWechatOpen.getAppSecret());
    		   OpenWeChatToken.OPEN_WECHAT_INFO.put(WeChatConstants.VERIFY_TICKET, tbWechatOpen.getComponentVerifyTicket());
    	   }
    	   //赋值
    	   String componentToken = wxService.getComponentToken(tbWechatOpen.getAppId(), tbWechatOpen.getAppSecret(), tbWechatOpen.getComponentVerifyTicket());
    	   if(StringUtils.isNotEmpty(componentToken)){
    		   OpenWeChatToken.OPEN_TLKEN_LOKER = true;
    		   openWeChatToken.process();
    	   }
       }catch(Exception e){
    	   logger.info("#OpenWeChatInit.afterPropertiesSet# e={}",e.getMessage(),e);
       }
    }

}
