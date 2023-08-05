package com.campus.course.course.dto.request;

import lombok.Data;

@Data
public class EditCourseRequest {
    private Long id;
    private String title;
    private String description;
}
