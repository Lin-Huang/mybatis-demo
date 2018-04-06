package com.lin.service;

import java.util.List;

public interface IService<T> {
    T selectKey(Object key);
    int save(T entity);
    int delete(Object key);
    int updateAll(T entity);
    int updateNotNull(T entity);
    List<T> selectByExample(Object example);
}
