package com.example.jtechstack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.jtechstack.entity.RepoTopic;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author carl-rabbit
 * @since 2022-05-30
 */
public interface RepoTopicService extends IService<RepoTopic> {

    boolean updateRepoTopics(int repoId, List<RepoTopic> topics);
}
