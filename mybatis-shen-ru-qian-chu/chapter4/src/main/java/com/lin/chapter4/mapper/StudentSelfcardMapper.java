package com.lin.chapter4.mapper;

import com.lin.chapter4.po.StudentSelfcardBean;

public interface StudentSelfcardMapper {
    StudentSelfcardBean findStudentSelfcardByStudentId(Long studentId);
}
