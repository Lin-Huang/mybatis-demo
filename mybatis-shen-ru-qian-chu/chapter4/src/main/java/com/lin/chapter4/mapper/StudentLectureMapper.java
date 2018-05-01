package com.lin.chapter4.mapper;

import com.lin.chapter4.po.StudentLectureBean;

public interface StudentLectureMapper {
    /*
        一对多关系查询
     */
    StudentLectureBean findStudentLectureByStuId(int id);
}
