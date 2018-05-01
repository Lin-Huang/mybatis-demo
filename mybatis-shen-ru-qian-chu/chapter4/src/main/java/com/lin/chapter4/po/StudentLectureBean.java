package com.lin.chapter4.po;

import java.io.Serializable;

/**
 * 学生课程成绩Bean
 */
public class StudentLectureBean implements Serializable {
    private Long id;
    private Long studentId;
    private Integer grade;
    private String note;
    // 与课程一对一的关系
    private LectureBean lecture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LectureBean getLecture() {
        return lecture;
    }

    public void setLecture(LectureBean lecture) {
        this.lecture = lecture;
    }
}
