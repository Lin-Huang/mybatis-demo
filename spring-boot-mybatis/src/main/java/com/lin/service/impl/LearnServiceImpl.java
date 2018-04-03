package com.lin.service.impl;

import com.github.pagehelper.PageHelper;
import com.lin.dao.LearnResourceMapper;
import com.lin.domain.LearnResouce;
import com.lin.service.LearnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LearnServiceImpl implements LearnService {
    @Autowired
    private LearnResourceMapper learnResourceMapper;

    @Override
    public int add(LearnResouce learnResouce) {
        return this.learnResourceMapper.add(learnResouce);
    }

    @Override
    public int update(LearnResouce learnResouce) {
        return this.learnResourceMapper.update(learnResouce);
    }

    @Override
    public int deleteByIds(String... ids) {
        return this.learnResourceMapper.deleteByIds(ids);
    }

    @Override
    public LearnResouce queryLearnResouceById(Long id) {
        return this.learnResourceMapper.queryLearnResouceById(id);
    }

    @Override
    public List<LearnResouce> queryLearnResourceList(Map<String, Object> params) {
        PageHelper.startPage(Integer.parseInt(params.get("page").toString()),
                Integer.parseInt(params.get("rows").toString()));
        return this.learnResourceMapper.queryLearnResourceList(params);
    }
}
