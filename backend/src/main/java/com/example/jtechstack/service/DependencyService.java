package com.example.jtechstack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.jtechstack.entity.Dependency;
import com.example.jtechstack.entity.MavenRepo;
import com.example.jtechstack.utils.Pair;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author carl-rabbit
 * @since 2022-05-28
 */
public interface DependencyService extends IService<Dependency> {

    List<Pair<Dependency, MavenRepo>> getRepoDependencies(int repoId);

    void updateRepoDependencies(int repoId, List<Dependency> dependencyList);
}
