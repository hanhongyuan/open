package com.zhsj.open.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class AbcService {

    public String abc(){
        return "testHello";
    }

    public static void main(String[] args){
       String xml = "<xml>"+
    		   "<AppId><![CDATA[wx4f6a40c48f6da430]]></AppId>"+
    		   "<Encrypt><![CDATA[U8PQ9w0JSfv+X5B0d3ncq0rnOx1O3JWO6V0vXXbY70zAiCYlcOtsBWGzk1iAuYPgD3XakrDKo7ZS0BNKX/dei7NMghTIlALUvKsMXbhCh6L5PLmjyofnHjFaKYZV4pSAT2PAllDwRX9UO0ylq0hC86j14WDBL8vYJse2lTDyYfqwU2wJbpOaJ/1gDaOeU1sBRphcUaXo2iFIH6xwCYnNqnvzI/bT383PdgLr8cUSa5945uB6ngU5d0M4UZP6CcqW0jFRa4TO9MweW+5ulv1st7t6qy/kyPRtZpdWxQUrRENdz3YDp79M1cZPsC8f3ZhEHbxLmObqTJ31vd3PXtegcf3UWK2hzeYqj+7HUDPpz941LrRS35vgHU/qvquz0GM72751PzlE5iKqmLBeQwztQYm+28QnTLX3IhRD0bIxRinP5OvEnPKiEUlpPwucMBzVNOfvqY4+rbRVTz0vUy7fFg==]]></Encrypt>"+
    		   "</xml>";
       

    }
}
