package com.zhsj.open.service.analyze;

public abstract class ReceiveAnalyze<T> {
	public abstract T analyze(String msg);
}
