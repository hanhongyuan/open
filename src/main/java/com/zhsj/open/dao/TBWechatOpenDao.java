package com.zhsj.open.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zhsj.open.bean.TBWechatOpen;
import com.zhsj.open.util.db.DS;
import com.zhsj.open.util.db.DynamicDataSource;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBWechatOpenDao {
	
	TBWechatOpen getOne();
	
	int updateByAppId(@Param("appId")String appId,
					  @Param("ticket")String ticket);
}
