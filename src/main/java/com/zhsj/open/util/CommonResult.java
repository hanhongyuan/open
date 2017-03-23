package com.zhsj.open.util;

//import com.alibaba.fastjson.JSONObject;

/**
 * Created by xingke on 15/3/25.
 */
public class CommonResult {

    private int    code;
    private String msg;
    private Object data;

    public static CommonResult defaultError(String msg) {
        return CommonResult.build(1, msg);
    }

    public static CommonResult success(String msg) {
        return CommonResult.build(0, msg);
    }

    public static CommonResult success(String msg, Object data) {
        return CommonResult.build(0, msg, data);
    }

    public static CommonResult build(int code, String msg) {
        return CommonResult.build(code, msg, "");
    }

    public static CommonResult build(int code, String msg, Object data) {
        CommonResult r = new CommonResult();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public CommonResult() {
    }

    public CommonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResult(int code, String msg, Object data) {
        this(code, msg);
        this.data = data;
    }

//    public JSONObject toJson() {
//        JSONObject json = new JSONObject();
//        json.put("code", this.code);
//        json.put("msg", this.msg);
//        json.put("data", this.data);
//        return json;
//    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
