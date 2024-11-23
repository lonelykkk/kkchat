package com.kkk.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * app发布 数据库操作接口
 */
@Mapper
public interface AppUpdateMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据Id更新
     */
    Integer updateById(@Param("bean") T t, @Param("id") Integer id);


    /**
     * 根据Id删除
     */
    Integer deleteById(@Param("id") Integer id);


    /**
     * 根据Id获取对象
     */
    T selectById(@Param("id") Integer id);


    /**
     * 根据Version更新
     */
    Integer updateByVersion(@Param("bean") T t, @Param("version") String version);


    /**
     * 根据Version删除
     */
    Integer deleteByVersion(@Param("version") String version);


    /**
     * 根据Version获取对象
     */
    T selectByVersion(@Param("version") String version);

    T selectLatestUpdate(@Param("version") String appVersion, @Param("uid") String uid);
}
