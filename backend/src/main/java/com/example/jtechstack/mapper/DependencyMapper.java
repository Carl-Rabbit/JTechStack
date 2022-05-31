package com.example.jtechstack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.jtechstack.entity.Dependency;
import com.example.jtechstack.entity.MavenRepo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author carl-rabbit
 * @since 2022-05-28
 */
@Mapper
public interface DependencyMapper extends BaseMapper<Dependency> {

}
