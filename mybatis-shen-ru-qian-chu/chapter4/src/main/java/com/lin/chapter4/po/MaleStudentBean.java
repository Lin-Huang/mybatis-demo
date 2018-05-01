package com.lin.chapter4.po;

import java.util.List;

/**
 * 男学生
 */
public class MaleStudentBean extends StudentBean {
    private List<StudentHealthMaleBean> studentHealthMaleBeanList;

    public List<StudentHealthMaleBean> getStudentHealthMaleBeanList() {
        return studentHealthMaleBeanList;
    }

    public void setStudentHealthMaleBeanList(List<StudentHealthMaleBean> studentHealthMaleBeanList) {
        this.studentHealthMaleBeanList = studentHealthMaleBeanList;
    }
}
