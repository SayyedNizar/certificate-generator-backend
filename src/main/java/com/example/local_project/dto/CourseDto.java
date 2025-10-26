package com.example.local_project.dto;

import lombok.Data;

@Data
public class CourseDto {
    private Long courseId;
    private String courseName;
    private String courseCode;
    private UserDto instructor; // Will hold simplified instructor info
}
