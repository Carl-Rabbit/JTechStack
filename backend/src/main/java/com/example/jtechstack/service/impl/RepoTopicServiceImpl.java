package com.example.jtechstack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.jtechstack.entity.RepoTopic;
import com.example.jtechstack.mapper.RepoTopicMapper;
import com.example.jtechstack.service.RepoTopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author carl-rabbit
 * @since 2022-05-30
 */
@Service
public class RepoTopicServiceImpl extends ServiceImpl<RepoTopicMapper, RepoTopic> implements RepoTopicService {

    @Transactional
    @Override
    public boolean updateRepoTopics(int repoId, List<RepoTopic> topics) {
        remove(new QueryWrapper<RepoTopic>().eq("repo_id", repoId));
        return saveBatch(topics);
    }
}
