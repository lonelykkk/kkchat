package com.kkk.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *  数据库操作接口
 */
@Mapper
public interface GroupInfoMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据GroupId更新
	 */
	 Integer updateByGroupId(@Param("bean") T t,@Param("groupId") String groupId);


	/**
	 * 根据GroupId删除
	 */
	 Integer deleteByGroupId(@Param("groupId") String groupId);


	/**
	 * 根据GroupId获取对象
	 */
	 T selectByGroupId(@Param("groupId") String groupId);


}
