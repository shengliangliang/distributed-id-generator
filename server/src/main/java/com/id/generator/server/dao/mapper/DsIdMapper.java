package com.id.generator.server.dao.mapper;

import com.id.generator.server.dao.model.DsId;
import org.apache.ibatis.annotations.*;

public interface DsIdMapper {
    /**
     * 根据bizType获取db中的tinyId对象
     * @param bizType
     * @return
     */
    @Select("select id, biz_type, begin_id, max_id,step, delta, remainder, create_time, update_time, version from tiny_id_info where biz_type = #{bizType}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "bizType",  column = "biz_type"),
            @Result(property = "beginId",  column = "begin_id"),
            @Result(property = "maxId",  column = "max_id"),
            @Result(property = "step",  column = "step"),
            @Result(property = "delta",  column = "delta"),
            @Result(property = "remainder",  column = "remainder"),
            @Result(property = "createTime",  column = "create_time"),
            @Result(property = "updateTime",  column = "update_time"),
            @Result(property = "version",  column = "version")
    })
    DsId queryByBizType(@Param("bizType") String bizType);

    /**
     * 根据id、oldMaxId、version、bizType更新最新的maxId
     * @param id
     * @param newMaxId
     * @param oldMaxId
     * @param version
     * @param bizType
     * @return
     */
    //@Update("UPDATE t_order SET order_id=#{orderId},order_desc=#{orderDesc} WHERE order_id =#{orderId}")
    @Update("update tiny_id_info set max_id= #{newMaxId},update_time=now(), version=version+1 where id=#{id} and max_id=#{oldMaxId} and version=#{version} and biz_type=#{bizType}")
    int updateMaxId(Long id, Long newMaxId, Long oldMaxId, Long version, String bizType);
}
