package com.lin.service;

import com.lin.domain.LearnResource;
import com.lin.model.QueryLearnListReq;
import com.lin.util.Page;

import java.util.List;

public interface LearnService extends IService<LearnResource> {
    List<LearnResource> queryLearnResourceList(Page<QueryLearnListReq> page);
    void deleteBatch(Long[] ids);
}
