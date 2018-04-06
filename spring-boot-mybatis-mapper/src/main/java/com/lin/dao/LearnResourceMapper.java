package com.lin.dao;

import com.lin.domain.LearnResource;
import com.lin.util.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface LearnResourceMapper extends MyMapper<LearnResource> {
    List<LearnResource> queryLearnResourceList(Map<String, Object> params);
}