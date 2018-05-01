package com.lin.chapter4.po;

import com.lin.chapter4.enums.Sex;

import java.io.Serializable;
import java.util.List;

/**
 * 学生Bean
 * 一个学生可能有多门课程，在学生确定的前提下每一门课程都有自己的分数，
 * 所以每一个学生的课程成绩只能对应一门课程
 */
public class StudentBean implements Serializable{
    private Long id;
    private String cnname;
    private Sex sex;
    private String note;
    // 级联，一对一关系
    private StudentSelfcardBean studentSelfcard;
    // 学生与学生课程成绩是一对多的关系
    private List<StudentLectureBean> studentLectureList;

    public List<StudentLectureBean> getStudentLectureList() {
        return studentLectureList;
    }

    public void setStudentLectureList(List<StudentLectureBean> studentLectureList) {
        this.studentLectureList = studentLectureList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public StudentSelfcardBean getStudentSelfcard() {
        return studentSelfcard;
    }

    public void setStudentSelfcard(StudentSelfcardBean studentSelfcard) {
        this.studentSelfcard = studentSelfcard;
    }
}
