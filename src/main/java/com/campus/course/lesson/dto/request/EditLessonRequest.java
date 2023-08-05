package com.campus.course.lesson.dto.request;

import lombok.Getter;

@Getter
public class EditLessonRequest {
    private Long id;
    private String title;
    private String description;
}
