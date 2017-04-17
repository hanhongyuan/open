package com.zhsj.open.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhsj.open.bean.AuthorizationInfo;
import com.zhsj.open.bean.WeixinUserBean;
import com.zhsj.open.constants.WeChatConstants;
import com.zhsj.open.dao.TBWechatInfoDao;
import com.zhsj.open.dao.TBWechatOpenDao;
import com.zhsj.open.service.analyze.AuthAnalyze;
import com.zhsj.open.service.analyze.VerifyTicketAnalyze;
import com.zhsj.open.service.analyze.bean.AuthBean;
import com.zhsj.open.task.OpenWeChatToken;
import com.zhsj.open.task.WeChatToken;
import com.zhsj.open.util.MtConfig;
import com.zhsj.open.util.SSLUtil;
import com.zhsj.open.util.weixin.WXBizMsgCrypt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.lang.String;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class WXService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WXService.class);
    
    @Autowired
    private TBWechatOpenDao tbWechatOpenDao;
    @Autowired
    private TBWechatInfoDao tbWechatInfoDao;
    @Autowired
    private OpenWeChatToken openWeChatToken;
    
    /**
     * 获取用户openId
     * @param appId
     * @param code
     * @return
     */
    public String getOpenId(String appId,String code ){
    	logger.info("#WXService.getOpenId# appId={},code={}",appId,code);
    	String openId = "";
    	try{
    		String url = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid="+appId+
    						"&code="+code+"&grant_type=authorization_code&component_appid="+OpenWeChatToken.OPEN_WECHAT_INFO.get(WeChatConstants.APPID)+
    						"&component_access_token="+OpenWeChatToken.OPEN_WECHAT_INFO.get(WeChatConstants.TOKEN);
    		String tokenJson = SSLUtil.getSSL(url);
    		Map<String,String> map = JSON.parseObject(tokenJson, Map.class);
            logger.info("#WXService.getOpenId# result={}",map);
            if(map.get("openid") != null){
            	openId = map.get("openid");
            }
    	}catch(Exception e){
    		logger.error("#WXService.getOpenId# error appId={},code={}",appId,code,e);
    	}
    	return openId;
    }
    
    public WeixinUserBean getWeixinUserInfo(String appId,String openId){
    	logger.info("#WXService.getWeixinUserInfo# appId={},openId={}",appId,openId);
    	WeixinUserBean userInfo = null;
    	try{
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.weixin.qq.com/cgi-bin/user/info?access_token=")
                    .append(WeChatToken.TOKEN_MAP.get(appId))
                    .append("&openid=")
                    .append(openId)
                    .append("&lang=zh_CN");
            String json = SSLUtil.getSSL(stringBuilder.toString());
            logger.info("#WXService.getOpenId # userinf2o result={}",json);
            userInfo = JSON.parseObject(json, WeixinUserBean.class);
            if(StringUtils.isNotEmpty(userInfo.getNickname())){
            	userInfo.setNickname(filterEmoji(userInfo.getNickname()));
            }
    	}catch(Exception e){
    		logger.error("#WXService.getWeixinUserInfo# error appId={},openId={}",appId,openId,e);
    	}
    	return userInfo;
    	
    }
    
    public boolean sendMessage(String appId,String[] openIds,String message,String url){
    	try{
    		boolean flag = true;
    		 String token = WeChatToken.TOKEN_MAP.get(appId);
             String _url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
             for(String openId:openIds){
                 if(StringUtils.isEmpty(openId)){
                     continue;
                 }
                 String msg = message;
                 msg = msg.replace("_openId",openId);
                 msg = msg.replace("_url",url);
                 logger.info("#WXService.sendMessage# url={},msg={}",_url,msg);
                 String result = SSLUtil.postSSL(_url, msg);
                 logger.info("#WXService.sendMessage# result orderId={},result={}",result);
                 Map<String,Object> map = JSON.parseObject(result,Map.class);
                 if((Integer)map.get("errcode") != null && (Integer)map.get("errcode") ==0 && flag){
                	 flag = true;
                 }else{
                	 flag = false;
                 }
             }
             return flag;
    	}catch(Exception e){
    		logger.error("#WXService.sendMessage# e={}",e.getMessage(),e);
    	}
    	return false;
    }
    
    ///////========================第三方平台方法=========================
    
    //
    /**
     * 获取第三方平台component_access_token
     * @param appId   第三方平台appid
     * @param secret  第三方平台appsecret
     * @param ticket  微信后台推送的ticket，此ticket会定时推送，具体请见本页的推送说明
     * @return
     */
    public String getComponentToken(String appId,String secret,String ticket){
    	logger.info("#WXService.getToken# appid={}.secrt={},ticket={}",appId,secret,ticket);
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
            JSONObject json = new JSONObject();
            json.put("component_appid", appId);
            json.put("component_appsecret", secret);
            json.put("component_verify_ticket", ticket);
            String tokenJson = SSLUtil.postSSL(url, json.toJSONString());
            Map<String,String> map = JSON.parseObject(tokenJson, Map.class);
            logger.info("#WXService.getComponentToken# result={}",map);
            if(map.get("component_access_token") != null){
                return map.get("component_access_token");
            }
        }catch (Exception e){
            logger.error("#WXService.getComponentToken# e={}",e.getMessage(),e);
        }
        return "";
    }

    /**
     * 获取预授权码pre_auth_code
     * @param appId 第三方平台方appid
     * @param token 第三方平台access_token
     * @return
     */
    public String getPreAuthCode(String appId,String token){
    	logger.info("#WXService.getPreAuthCode# appid={},token={}",appId,token);
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+token;
            JSONObject json = new JSONObject();
            json.put("component_appid", appId);
            String tokenJson = SSLUtil.postSSL(url, json.toJSONString());
            Map<String,String> map = JSON.parseObject(tokenJson, Map.class);
            logger.info("#WXService.getPreAuthCode# result={}",map);
            if(map.get("pre_auth_code") != null){
                return map.get("pre_auth_code");
            }
        }catch (Exception e){
            logger.error("#WXService.getPreAuthCode# e={}",e.getMessage(),e);
        }
        return "";
    }
    
    
    /**
     * 使用授权码换取公众号的接口调用凭据和授权信息
     * @param appId 第三方平台appid
     * @param authCode 授权code,会在授权成功时返回给第三方平台，详见第三方平台授权流程说明
     * @param token  第三方平台access_token
     */
    public AuthorizationInfo apiQueryAuth(String appId,String authCode,String token){
    	AuthorizationInfo auth= null;
    	logger.info("#WXService.apiQueryAuth# appid={},authCode={},token={}",appId,authCode,token);
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="+token;
            JSONObject json = new JSONObject();
            json.put("component_appid", appId);
            json.put("authorization_code", authCode);
            String tokenJson = SSLUtil.postSSL(url, json.toJSONString());
            Map<String,Object> map = JSON.parseObject(tokenJson, Map.class);
            logger.info("#WXService.apiQueryAuth# result={}",map);
            if(map.get("authorization_info") != null){
            	json = (JSONObject) map.get("authorization_info");
            	auth = JSON.parseObject(json.toJSONString(),AuthorizationInfo.class);
            	return auth;
            }
        }catch (Exception e){
            logger.error("#WXService.apiQueryAuth# e={}",e.getMessage(),e);
        }
        return null;
    }
    
    /**
     * 获取（刷新）授权公众号的接口调用凭据（令牌）
     * @param appId 第三方平台appid
     * @param authAppId 授权方appid
     * @param authRefreshToken  刷新令牌
     * @param token  第三方平台access_token
     */
    public String getAuthToken(String appId,String authAppId,String authRefreshToken,String token){
    	logger.info("#WXService.getAuthToken# appid={},authAppId={},authRefreshToken={},token={}",appId,authAppId,authRefreshToken,token);
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="+token;
            JSONObject json = new JSONObject();
            json.put("component_appid", appId);
            json.put("authorizer_appid", authAppId);
            json.put("authorizer_refresh_token", authRefreshToken);
            String tokenJson = SSLUtil.postSSL(url, json.toJSONString());
            Map<String,String> map = JSON.parseObject(tokenJson, Map.class);
            logger.info("#WXService.getAuthToken# result={}",map);
            if(map.get("authorizer_access_token") != null){
                return map.get("authorizer_access_token");
            }
        }catch (Exception e){
            logger.error("#WXService.getAuthToken# e={}",e.getMessage(),e);
        }
        return "";
    }
    
    /**
     * 获取授权方的公众号帐号基本信息
     * @param appId   服务appid 
     * @param authAppId  授权方appid
     * @param token  第三方平台access_token
     */
    public void getAuthorizerInfo(String appId,String authAppId,String token){
    	logger.info("#WXService.getAuthorizerInfo# appid={},authAppId={},token={}",appId,authAppId,token);
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token="+token;
            JSONObject json = new JSONObject();
            json.put("component_appid", appId);
            json.put("authorizer_appid", authAppId);
            String tokenJson = SSLUtil.postSSL(url, json.toJSONString());
            Map<String,String> map = JSON.parseObject(tokenJson, Map.class);
            logger.info("#WXService.getAuthToken# result={}",map);
            if(map.get("authorizer_access_token") != null){
            	
            }
        }catch (Exception e){
            logger.error("#WXService.getAuthToken# e={}",e.getMessage(),e);
        }
    }
    
    
    public void analyzeReceiveMsg(String signature ,String timestamp ,String nonce,String encryptType,String msgSignature,String content){
    	logger.info("#WXService.analyzeReceiveMsg# signature={},timestamp={},nonce={},encryptType={},msgSignature={},content={}",
    			signature,timestamp,nonce,encryptType,msgSignature,content);
    	try{
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		StringReader sr = new StringReader(content);
    		InputSource is = new InputSource(sr);
    		Document document = db.parse(is);
    		Element root = document.getDocumentElement();
    		NodeList nodelist1 = root.getElementsByTagName("AppId");
    		NodeList nodelist2 = root.getElementsByTagName("Encrypt");
    		String appId = nodelist1.item(0).getTextContent();
    		String encrypt = nodelist2.item(0).getTextContent();
    		
    		String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
    		String fromXML = String.format(format, encrypt);
    		String token = MtConfig.getProperty(appId+"Token", "");
    		String key = MtConfig.getProperty(appId+"Key", "");
    		WXBizMsgCrypt pc = new WXBizMsgCrypt(token,key, appId);
    		logger.info("=======msgSignature={},timestamp={},nonce={},fromXML={}",msgSignature,timestamp,nonce,fromXML);
    		String result = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
    		
    		sr = new StringReader(result);
    		is = new InputSource(sr);
    		document = db.parse(is);
    		root = document.getDocumentElement();
    		NodeList nodelist3 = root.getElementsByTagName("InfoType");
    		String infoType = nodelist3.item(0).getTextContent();
    		if("component_verify_ticket".equals(infoType)){
    			String verifyTicket = new VerifyTicketAnalyze().analyze(result);
    			if(StringUtils.isNotEmpty(verifyTicket)){
    				//保存数据DB
    				int num = tbWechatOpenDao.updateByAppId(appId, verifyTicket);
    				//设置新值
    				OpenWeChatToken.OPEN_WECHAT_INFO.put(WeChatConstants.VERIFY_TICKET, verifyTicket);
    				//是否要启动轮询
    				if(!OpenWeChatToken.OPEN_TLKEN_LOKER){
    					OpenWeChatToken.OPEN_TLKEN_LOKER = true;
    					openWeChatToken.process();
    				}
    			}
    		}if("unauthorized".equals(infoType)||"authorized".equals(infoType) || "updateauthorized".equals(infoType)){
    			//认证
    			AuthBean authBean = new AuthAnalyze().analyze(result);
    			if("authorized".equals(authBean.getInfoType())){
    				//存db
    				AuthorizationInfo auth = this.apiQueryAuth(appId, authBean.getAuthorizationCode(), OpenWeChatToken.OPEN_WECHAT_INFO.get(WeChatConstants.TOKEN));
    				if(auth == null){
    					logger.info("#WXService.analyzeReceiveMsg# error auth is null");
    					return;
    				}
    				tbWechatInfoDao.updateByAppId(auth.getAuthorizer_access_token(), auth.getAuthorizer_refresh_token(), auth.getAuthorizer_appid());
    			}
    		}
    	}catch(Exception e){
    		logger.error("#WXService.analyzeReceiveMsg# error signature={},timestamp={},nonce={},encryptType={},msgSignature={},content={}",
    			signature,timestamp,nonce,encryptType,msgSignature,content,e);
    	}
    }

 
    public static void main(String[] args) throws Exception {
    	String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=56vuc-6VD7BRUfrH1R3LBB-s_4f-IIiX7vgFlhvFYGLxDbsTH03XGv1yoMwmcarL-Z7zlRztHuloV6rRGdfK9XSjSyAgCqZh2-M4Mp0cRwMBsAbxWLAWK_IP2Z_Q1VQIWVNcCGAGFX&openid=o5pmes4UQPdXawhFrFdAVFvuuQg0";
    	 String json = SSLUtil.getSSL(url);
         logger.info("#WXService.getOpenId # userinf2o result={}",json);
         WeixinUserBean  userInfo = JSON.parseObject(json, WeixinUserBean.class);
         System.out.println("=="+userInfo.getNickname()+"==");
         System.out.println("=="+filterEmoji(null)+"==");
    }
    
    public static String filterEmoji(String source) {  
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find()) 
            {
                source = emojiMatcher.replaceAll("*");
                return source ; 
            }
        return source;
       }
       return source;  
    }

}
