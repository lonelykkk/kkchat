package com.kkk.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 联系人 数据库操作接口
 */
@Mapper
public interface UserContactMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据UserIdAndContactId更新
	 */
	 Integer updateByUserIdAndContactId(@Param("bean") T t,@Param("userId") String userId,@Param("contactId") String contactId);


	/**
	 * 根据UserIdAndContactId删除
	 */
	 Integer deleteByUserIdAndContactId(@Param("userId") String userId,@Param("contactId") String contactId);


	/**
	 * 根据UserIdAndContactId获取对象
	 */
	 T selectByUserIdAndContactId(@Param("userId") String userId,@Param("contactId") String contactId);


}
