package com.example.jtechstack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.jtechstack.entity.Dependency;
import com.example.jtechstack.entity.MavenRepo;
import com.example.jtechstack.mapper.DependencyMapper;
import com.example.jtechstack.mapper.MavenRepoMapper;
import com.example.jtechstack.service.DependencyService;
import com.example.jtechstack.utils.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author carl-rabbit
 * @since 2022-05-28
 */
@Service
public class DependencyServiceImpl extends ServiceImpl<DependencyMapper, Dependency> implements DependencyService {

    private final DependencyMapper dependencyMapper;
    private final MavenRepoMapper mavenRepoMapper;

    public DependencyServiceImpl(DependencyMapper dependencyMapper, MavenRepoMapper mavenRepoMapper) {
        this.dependencyMapper = dependencyMapper;
        this.mavenRepoMapper = mavenRepoMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pair<Dependency, MavenRepo>> getRepoDependencies(int repoId) {
        List<Dependency> depList = dependencyMapper
                .selectByMap(new HashMap<String, Object>() {{
                    this.put("repo_id", repoId);
                }});

        List<String> idList = depList.stream()
                .map(Dependency::getMvnRepoId)
                .collect(Collectors.toList());

        Map<String, MavenRepo> mavenRepoMap;
        if (!idList.isEmpty()) {
            List<MavenRepo> mavenRepoList = mavenRepoMapper.selectBatchIds(idList);
            mavenRepoMap = mavenRepoList.stream()
                    .collect(Collectors.toMap(MavenRepo::getId, o -> o));
        } else {
            mavenRepoMap = new HashMap<>();
        }

        return depList.stream()
                .map(d -> new Pair<>(d, mavenRepoMap.get(d.getMvnRepoId())))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateRepoDependencies(int repoId, List<Dependency> dependencyList) {
        List<Dependency> oldDepList = this.getBaseMapper()
                .selectByMap(new HashMap<String, Object>() {{
                    this.put("repo_id", repoId);
                }});
        List<Integer> oldDepIdList = oldDepList.stream()
                .map(Dependency::getId)
                .collect(Collectors.toList());

        if (!oldDepIdList.isEmpty()) {
            dependencyMapper.deleteBatchIds(oldDepIdList);
        }

        this.saveBatch(dependencyList);
    }
}
