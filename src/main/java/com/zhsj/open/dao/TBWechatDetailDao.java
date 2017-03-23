package com.zhsj.open.dao;

import org.springframework.stereotype.Component;

import com.zhsj.open.util.db.DS;
import com.zhsj.open.util.db.DynamicDataSource;


@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBWechatDetailDao {

}
