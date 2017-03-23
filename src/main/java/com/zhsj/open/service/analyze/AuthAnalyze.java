package com.zhsj.open.service.analyze;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.zhsj.open.service.WXService;
import com.zhsj.open.service.analyze.bean.AuthBean;

public class AuthAnalyze extends ReceiveAnalyze {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthAnalyze.class);

	@Override
	public AuthBean analyze(String msg) {
		AuthBean authBean = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(msg);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);
			Element root = document.getDocumentElement();
			
			String infoType = root.getElementsByTagName("InfoType").item(0).getTextContent();
			String appId = root.getElementsByTagName("AppId").item(0).getTextContent();//第三方平台appid
			String createTime = root.getElementsByTagName("CreateTime").item(0).getTextContent();
			String authorizerAppid = root.getElementsByTagName("AuthorizerAppid").item(0).getTextContent();//>公众号appid
			authBean = new AuthBean();
			authBean.setAppId(appId);
			authBean.setInfoType(infoType);
			authBean.setCreateTime(createTime);
			authBean.setAuthorizerAppid(authorizerAppid);
			if("unauthorized".equals(infoType)){
				logger.info("#AuthAnalyze.analyze# infoType={},appId={},createTime={},authorizerAppid={}",infoType,appId,createTime,authorizerAppid);
			}else if("authorized".equals(infoType)){
				String authorizationCode = root.getElementsByTagName("AuthorizationCode").item(0).getTextContent();//授权码
				String authorizationCodeExpiredTime = root.getElementsByTagName("AuthorizationCodeExpiredTime").item(0).getTextContent();//>过期时间
				authBean.setAuthorizationCode(authorizationCode);
				authBean.setAuthorizationCodeExpiredTime(authorizationCodeExpiredTime);
				logger.info("#AuthAnalyze.analyze# infoType={},appId={},createTime={},authorizerAppid={},authorizationCode={},authorizationCodeExpiredTime={}",
						infoType,appId,createTime,authorizerAppid,authorizationCode,authorizationCodeExpiredTime);
			}else if("updateauthorized".equals(infoType)){
				String authorizationCode = root.getElementsByTagName("AuthorizationCode").item(0).getTextContent();//授权码
				String authorizationCodeExpiredTime = root.getElementsByTagName("AuthorizationCodeExpiredTime").item(0).getTextContent();//>过期时间
				authBean.setAuthorizationCode(authorizationCode);
				authBean.setAuthorizationCodeExpiredTime(authorizationCodeExpiredTime);
				logger.info("#AuthAnalyze.analyze# infoType={},appId={},createTime={},authorizerAppid={},authorizationCode={},authorizationCodeExpiredTime={}",
						infoType,appId,createTime,authorizerAppid,authorizationCode,authorizationCodeExpiredTime);
			}
			
		} catch (Exception e) {
			logger.error("#AuthAnalyze.analyze# e={}",e.getMessage(),e);
		}
		return authBean;
	}

}
