package com.example.jtechstack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.jtechstack.entity.Contributor;
import com.example.jtechstack.mapper.ContributorMapper;
import com.example.jtechstack.service.ContributorService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author carl-rabbit
 * @since 2022-05-29
 */
@Service
public class ContributorServiceImpl extends ServiceImpl<ContributorMapper, Contributor> implements ContributorService {

}
