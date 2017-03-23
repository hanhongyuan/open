package com.zhsj.open.util;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.zhsj.open.util.weixin.WXBizMsgCrypt;

/**
 * Created by lcg on 17/3/7.
 */
public class Test {
    public static void main(String[] args) throws Exception{
    	
    	//
		// 第三方回复公众平台
		//

		// 需要加密的明文
		String encodingAesKey = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG";
		String token = "ajiferbuiwnreveowrnceowrbin";
		String timestamp = "1409304348";
		String nonce = "xxxxxx";
		String appId = "wx4f6a40c48f6da430";
		String replyMsg = " 中文<xml><ToUserName><![CDATA[oia2TjjewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";

		WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
		String mingwen = pc.encryptMsg(replyMsg, timestamp, nonce);
		System.out.println("加密后: " + mingwen);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		StringReader sr = new StringReader(mingwen);
		InputSource is = new InputSource(sr);
		Document document = db.parse(is);

		Element root = document.getDocumentElement();
		NodeList nodelist1 = root.getElementsByTagName("Encrypt");
		NodeList nodelist2 = root.getElementsByTagName("MsgSignature");

		String encrypt = nodelist1.item(0).getTextContent();
		String msgSignature = nodelist2.item(0).getTextContent();
//
		String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
		String fromXML = String.format(format, encrypt);

		//
		// 公众平台发送消息给第三方，第三方处理
		//

		
		// 第三方收到公众号平台发送的消息
//		si<xml>
//	    <ToUserName><![CDATA[gh_20caac393f70]]></ToUserName>
//	    <Encrypt><![CDATA[FMtOq0BomfrHyui5UHXwp02+lvpRuEOkSnBDZTK0N2JZjyo1yevf2gIrerA6V6+WHZBpe2jwbpqdeHp3ZopJkp+FBXk5QH9DTrGileutjUMvRtL1EoQGUNXL1hvB8hPgLntpyfhb20EkLH8ywRLf6eZ3oCizRz+Ef9VTIeQUH3QD246/Cwh2h8+TZZMbTCC+pWSkELr5VEXFNvkaan5mEsy+ikVtJ/6R7lqv+3YyNFgpM8Ljln3ZS4htYGkd1lysF+WP+jbSOV16vjhvRdoi/ynUWu7n1veEdSggLuYn9s1uPPPHruMzGLivsrR/eQxjvBrAYDspF7zaL3fs90pvH1vm6gCHEd4hHieQBd23E9pVi6gbyLu3dCnmiNtBZKDAsTmIA21jOKXLGNQxE5P7/lsLh2X5mdWbQYayM5QKuFOQZyGoXxIwDcnDU8ToWvGVm5RYQCIEfedfwOugjVqZetucLrCvXN4+taW9rO5gVYGEBpJWzR+2BqFKXkSijH+8us6KmkZLR/XX4tL7lfXuD5mN+BRFG1K7HJ8sT/ob5Vg=]]></Encrypt>
//	</xml>
//
//	2017-03-22 11:08:35 com.zhsj.open.controller.BaseController.callback(87) | appId=====/wx79bd044fd98536f4
//	2017-03-22 11:08:35 com.zhsj.open.controller.BaseController.callback(87) | signature=====2e93809fb177e128369dcef7d185926504418e08
//	2017-03-22 11:08:35 com.zhsj.open.controller.BaseController.callback(87) | timestamp=====1490152115
//	2017-03-22 11:08:35 com.zhsj.open.controller.BaseController.callback(87) | nonce=====1929368771
//	2017-03-22 11:08:35 com.zhsj.open.controller.BaseController.callback(87) | openid=====o5pmes0VtCAr0xT04YQq8BekRVN8
//	2017-03-22 11:08:35 com.zhsj.open.controller.BaseController.callback(87) | encrypt_type=====aes
//	2017-03-22 11:08:35 com.zhsj.open.controller.BaseController.callback(87) | msg_signature=====0e7195c3c71d5caf75a31358912a49352e726c8d
//		 fromXML = "<xml>"+
//					    "<AppId><![CDATA[wx4f6a40c48f6da430]]></AppId>"+
//					    "<Encrypt><![CDATA[fO9f2yIKz5XE+RBRnaemE3ootWnpH71Z1orKzx+1LC8sDez3yor1bPKAi7NvddINDJbAIhx0aOXmedLEhmtQn+lc3oEtkYZU1z8E+AdqxagegHKxC//JkBFEww3VvNWSAfvW+muDLkwy8Kxx+Dm4BlxbBk++5UTd7HCZOpZP0TeoDf7Pp4ehSkNaMkMmPSgoFJ80tpVOwLPs5fVKnW1M28CC9zVAhavGOMVwcEgbbfz6ZB/2oCjlV89XeTX4AuQEg7b/XgQ5YPap3Rw2fb1vBtHXAKxStP1O3f6pfevEbAF+7YSxHTS6WaliTvC9ycL3gdyCkT0ilAYcwnnI1NBHNsys7gauqOv+66xmPnvzG9xV+I9Hm7h64CE5h1dZ018CXlTbwq3e32+SXLMEiTcL5eupe4nfP+g4f9+OsFvr2hQRErvK/HlTgWdIXNksw9WtX6HSuu9OcSaUg/3NSNJpCg==]]></Encrypt>";
		fromXML = String.format(format, "fO9f2yIKz5XE+RBRnaemE3ootWnpH71Z1orKzx+1LC8sDez3yor1bPKAi7NvddINDJbAIhx0aOXmedLEhmtQn+lc3oEtkYZU1z8E+AdqxagegHKxC//JkBFEww3VvNWSAfvW+muDLkwy8Kxx+Dm4BlxbBk++5UTd7HCZOpZP0TeoDf7Pp4ehSkNaMkMmPSgoFJ80tpVOwLPs5fVKnW1M28CC9zVAhavGOMVwcEgbbfz6ZB/2oCjlV89XeTX4AuQEg7b/XgQ5YPap3Rw2fb1vBtHXAKxStP1O3f6pfevEbAF+7YSxHTS6WaliTvC9ycL3gdyCkT0ilAYcwnnI1NBHNsys7gauqOv+66xmPnvzG9xV+I9Hm7h64CE5h1dZ018CXlTbwq3e32+SXLMEiTcL5eupe4nfP+g4f9+OsFvr2hQRErvK/HlTgWdIXNksw9WtX6HSuu9OcSaUg/3NSNJpCg==");
	    
		 fromXML = "<xml><ToUserName><![CDATA[gh_20caac393f70]]></ToUserName><Encrypt><![CDATA[FMtOq0BomfrHyui5UHXwp02+lvpRuEOkSnBDZTK0N2JZjyo1yevf2gIrerA6V6+WHZBpe2jwbpqdeHp3ZopJkp+FBXk5QH9DTrGileutjUMvRtL1EoQGUNXL1hvB8hPgLntpyfhb20EkLH8ywRLf6eZ3oCizRz+Ef9VTIeQUH3QD246/Cwh2h8+TZZMbTCC+pWSkELr5VEXFNvkaan5mEsy+ikVtJ/6R7lqv+3YyNFgpM8Ljln3ZS4htYGkd1lysF+WP+jbSOV16vjhvRdoi/ynUWu7n1veEdSggLuYn9s1uPPPHruMzGLivsrR/eQxjvBrAYDspF7zaL3fs90pvH1vm6gCHEd4hHieQBd23E9pVi6gbyLu3dCnmiNtBZKDAsTmIA21jOKXLGNQxE5P7/lsLh2X5mdWbQYayM5QKuFOQZyGoXxIwDcnDU8ToWvGVm5RYQCIEfedfwOugjVqZetucLrCvXN4+taW9rO5gVYGEBpJWzR+2BqFKXkSijH+8us6KmkZLR/XX4tL7lfXuD5mN+BRFG1K7HJ8sT/ob5Vg=]]></Encrypt></xml>";
		msgSignature = "0e7195c3c71d5caf75a31358912a49352e726c8d";
		timestamp = "1490152115";
		nonce = "1929368771";
		
		WXBizMsgCrypt pc2 = new WXBizMsgCrypt(token, "MbsgOhfc4IqP62SGJ2rJsStqbxzDfYEn2lcSa386J3Q", appId);
		
		String result2 = pc2.decryptMsg(msgSignature, timestamp, nonce, fromXML);
		
		System.out.println("解密后明文: " + result2);
		
		//pc.verifyUrl(null, null, null, null);
    }
}
