package com.lin.chapter4.po;

import java.util.List;

/**
 * 女学生
 */
public class FemaleStudentBean extends StudentBean {
    private List<StudentHealthFemaleBean> studentHealthFemaleBeanList;

    public List<StudentHealthFemaleBean> getStudentHealthFemaleBeanList() {
        return studentHealthFemaleBeanList;
    }

    public void setStudentHealthFemaleBeanList(List<StudentHealthFemaleBean> studentHealthFemaleBeanList) {
        this.studentHealthFemaleBeanList = studentHealthFemaleBeanList;
    }
}
