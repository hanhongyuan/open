package com.zhsj.open.service.analyze;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.zhsj.open.bean.TBWechatOpen;
import com.zhsj.open.constants.WeChatConstants;
import com.zhsj.open.dao.TBWechatOpenDao;
import com.zhsj.open.service.WXService;
import com.zhsj.open.task.OpenWeChatToken;

public class VerifyTicketAnalyze extends ReceiveAnalyze {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(VerifyTicketAnalyze.class);
    
	@Override
	public String analyze(String msg) {
		String verifyTicket = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(msg);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);
			Element root = document.getDocumentElement();
			NodeList nodelist3 = root.getElementsByTagName("ComponentVerifyTicket");
			verifyTicket = nodelist3.item(0).getTextContent();
		} catch (Exception e) {
			logger.error("#VerifyTicketAnalyze.analyze# e={}",e.getMessage(),e);
		}
		logger.info("#VerifyTicketAnalyze.analyze# msg={},tickt={}",msg,verifyTicket);
		return verifyTicket;
	}

}
