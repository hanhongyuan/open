package com.zhsj.open.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhsj.open.constants.WeChatConstants;
import com.zhsj.open.service.AbcService;
import com.zhsj.open.service.WXService;
import com.zhsj.open.task.OpenWeChatToken;
import com.zhsj.open.util.CommonResult;

@Controller
public class BaseController {
    Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private WXService wxService;

    @RequestMapping(value = "/")
    public String riderTrack(Model model, HttpServletRequest request) {
            return "index2";
    }

    @RequestMapping(value = "/receive", method = {RequestMethod.GET,RequestMethod.POST})
    public void receive(HttpServletRequest request,HttpServletResponse response) {
    	InputStream is= null;     
    	String contentStr="";     
    	PrintWriter pw = null;
		try {
			pw = response.getWriter();
			is = request.getInputStream();         
            contentStr= IOUtils.toString(is, "utf-8");
            logger.info("#BaseController.receive# content={}",contentStr);
			Map<String,Object> map = request.getParameterMap();
			for(String name: map.keySet()){
				logger.info("#BaseController.receive#"+name+"====="+request.getParameter(name));
			}
			
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String encryptType = request.getParameter("encrypt_type");
			String msgSignature = request.getParameter("msg_signature");
			wxService.analyzeReceiveMsg(signature, timestamp, nonce, encryptType, msgSignature, contentStr);
		} catch (IOException e) {
			logger.error("#BaseController.receive# e={}",e.getMessage(),e );
		}finally{
			pw.append("success");
		    pw.flush();
		    pw.close();
		}
       
    }
    
    @RequestMapping(value = "/callback", method = {RequestMethod.GET,RequestMethod.POST})
    public void callback(HttpServletRequest request,HttpServletResponse response) {
    	InputStream is= null;     
    	String contentStr="";     
    	PrintWriter pw = null;
		try {
			is = request.getInputStream();         
            contentStr= IOUtils.toString(is, "utf-8");
            logger.info("#BaseController.callback# content={}",contentStr);
			Map<String,Object> map = request.getParameterMap();
			for(String name: map.keySet()){
				 logger.info("#BaseController.callback#"+name+"====="+request.getParameter(name));
			}
			
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String encrypt_type = request.getParameter("encrypt_type");
			String msg_signature = request.getParameter("msg_signature");
			pw = response.getWriter();
		} catch (IOException e) {
			logger.error("#BaseController.callback# e={}",e.getMessage(),e );
		}
        pw.append("success");
        pw.flush();
        pw.close();
    }

    @RequestMapping(value = "/authWeChat", method = RequestMethod.GET)
    @ResponseBody
    public Object authWeChat(String appId) {
    	PrintWriter pw = null;
        try {
        	String token = OpenWeChatToken.OPEN_WECHAT_INFO.get(WeChatConstants.TOKEN);
        	return CommonResult.build(0, "",wxService.getPreAuthCode(appId, token));
        } catch (Exception e) {
            return CommonResult.build(1, "后台异常");
        }
    }
    
    @RequestMapping(value = "/getOpenId", method = RequestMethod.GET)
    @ResponseBody
    public Object getOpenId(String appId,String code) {
    	logger.info("#BaseController.getOpenId# appId={},code={}",appId,code);
        try {
        	return CommonResult.build(0, "",wxService.getOpenId(appId, code));
        } catch (Exception e) {
            return CommonResult.build(1, "后台异常");
        }
    }
    
    @RequestMapping(value = "/getWeChatUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public Object getWeChatUserInfo(String appId,String openId) {
    	logger.info("#BaseController.getWeChatUserInfo# appId={},openId={}",appId,openId);
        try {
        	return CommonResult.build(0, "",wxService.getWeixinUserInfo(appId, openId));
        } catch (Exception e) {
            return CommonResult.build(1, "后台异常");
        }
    }
    
    //发送消息
    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
    @ResponseBody
    public Object sendMessage(String appId,String openIds,String message,String url) {
    	logger.info("#BaseController.sendMessage# appId={},openIds={}",appId,openIds);
        try {
        	if(StringUtils.isEmpty(openIds)){
        		return  CommonResult.build(1, "数据异常");
        	}
        	String[] ids = openIds.split(",");
        	return CommonResult.build(0, "",wxService.sendMessage(appId, ids, message, url));
        } catch (Exception e) {
            return CommonResult.build(1, "后台异常");
        }
    }
    

}
