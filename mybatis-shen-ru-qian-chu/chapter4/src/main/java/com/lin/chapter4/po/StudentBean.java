package com.lin.chapter4.po;

import com.lin.chapter4.enums.Sex;

public class StudentBean {
    private Long id;
    private String cnname;
    private Sex sex;
    private String note;
    // 级联，一对一关系
    private StudentSelfcardBean studentSelfcard;

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
