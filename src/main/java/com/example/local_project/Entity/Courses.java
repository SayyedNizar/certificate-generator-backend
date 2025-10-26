package com.example.local_project.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

    @Entity
    @Table(name="courses")
    @Getter
    @Setter
    @Data
    public class Courses {
        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        @Column(name="course_id")
        private Long courseId;
        @Column(name="course_name")
        private String courseName;
        @Column(name="course_code")
        private String courseCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Users instructor;

    }
