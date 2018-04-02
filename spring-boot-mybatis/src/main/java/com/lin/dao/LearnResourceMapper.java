package com.lin.dao;

import com.lin.domain.LearnResouce;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface LearnResourceMapper {
	int add(LearnResouce learnResouce);
	int update(LearnResouce learnResouce);
	int deleteByIds(String...ids);
	LearnResouce queryLearnResouceById(Long id);
	List<LearnResouce> queryLearnResourceList(Map<String, Object> params);
}
