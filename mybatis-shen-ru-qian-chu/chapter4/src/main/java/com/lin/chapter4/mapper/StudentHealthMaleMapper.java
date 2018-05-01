package com.lin.chapter4.mapper;

import com.lin.chapter4.po.StudentHealthMaleBean;

import java.util.List;

public interface StudentHealthMaleMapper {
    List<StudentHealthMaleBean> findStudentHealthMaleByStuId(int studentId);
}
