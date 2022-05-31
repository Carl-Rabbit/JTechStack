package com.example.jtechstack.mapper;

import com.example.jtechstack.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author carl-rabbit
 * @since 2022-05-29
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
