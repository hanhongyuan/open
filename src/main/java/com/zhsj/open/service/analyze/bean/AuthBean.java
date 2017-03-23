package com.zhsj.open.service.analyze.bean;

public class AuthBean {
	private String infoType;
	private String appId;
	private String createTime;
	private String authorizerAppid;
	private String authorizationCode;
	private String authorizationCodeExpiredTime;
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAuthorizerAppid() {
		return authorizerAppid;
	}
	public void setAuthorizerAppid(String authorizerAppid) {
		this.authorizerAppid = authorizerAppid;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getAuthorizationCodeExpiredTime() {
		return authorizationCodeExpiredTime;
	}
	public void setAuthorizationCodeExpiredTime(String authorizationCodeExpiredTime) {
		this.authorizationCodeExpiredTime = authorizationCodeExpiredTime;
	}
	
}
