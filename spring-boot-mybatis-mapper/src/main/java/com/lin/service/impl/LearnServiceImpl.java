package com.lin.service.impl;

import com.github.pagehelper.PageHelper;
import com.lin.dao.LearnResourceMapper;
import com.lin.domain.LearnResource;
import com.lin.model.QueryLearnListReq;
import com.lin.service.LearnService;
import com.lin.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LearnServiceImpl extends BaseServiceImpl<LearnResource> implements LearnService {
    @Autowired
    private LearnResourceMapper learnResourceMapper;

    @Override
    public List<LearnResource> queryLearnResourceList(Page<QueryLearnListReq> page) {
        PageHelper.startPage(page.getPage(), page.getRows());
        return learnResourceMapper.queryLearnResourceList(page.getCondition());
    }

    @Override
    public void deleteBatch(Long[] ids) {
        Arrays.stream(ids).forEach(id -> learnResourceMapper.deleteByPrimaryKey(id));
    }
}
