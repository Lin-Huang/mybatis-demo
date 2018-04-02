package com.lin.dao;

import com.lin.domain.LearnResouce;

import java.util.List;
import java.util.Map;

public interface LearnResourceMapper {
	int add(LearnResouce learnResouce);
	int update(LearnResouce learnResouce);
	int deleteByIds(String...ids);
	LearnResouce queryLearnResouceById(Long id);
	List<LearnResouce> queryLearnResourceList(Map<String, Object> params);
}
