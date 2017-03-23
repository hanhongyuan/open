package com.zhsj.open.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zhsj.open.bean.TBWechatInfo;
import com.zhsj.open.util.db.DS;
import com.zhsj.open.util.db.DynamicDataSource;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBWechatInfoDao {
	
	List<TBWechatInfo> getList();
	
	int updateByAppId(@Param("token")String token,
					  @Param("refreshToke")String refreshToke,
					  @Param("appId")String appId);
	
}
