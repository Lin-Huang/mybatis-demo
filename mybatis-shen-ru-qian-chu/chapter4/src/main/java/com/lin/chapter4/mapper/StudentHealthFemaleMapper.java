package com.lin.chapter4.mapper;

import com.lin.chapter4.po.StudentHealthFemaleBean;

import java.util.List;

public interface StudentHealthFemaleMapper {
    List<StudentHealthFemaleBean> findStudentHealthFemaleByStuId(int studentId);
}
