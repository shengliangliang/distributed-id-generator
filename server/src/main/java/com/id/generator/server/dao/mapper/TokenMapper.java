package com.id.generator.server.dao.mapper;

import com.id.generator.server.dao.model.Token;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TokenMapper {
    /**
     * 查询db中所有的token信息
     * @return
     */
    @Select("select id, token, biz_type, remark, create_time, update_time from tiny_id_token")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "token",  column = "token"),
            @Result(property = "bizType",  column = "biz_type"),
            @Result(property = "remark",  column = "remark"),
            @Result(property = "createTime",  column = "create_time"),
            @Result(property = "updateTime",  column = "update_time")
    })
    List<Token> selectAll();
}
