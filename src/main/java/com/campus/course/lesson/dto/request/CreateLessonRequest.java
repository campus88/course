package com.campus.course.lesson.dto.request;

import com.campus.course.lesson.entity.LessonEntity;
import lombok.Getter;

@Getter
public class CreateLessonRequest {
    private String title;
    private String description;
    private Long courseId;

    public LessonEntity entity(){
        return LessonEntity.builder()
                .title(title)
                .description(description)
                .courseId(courseId)
                .build();
    }
}
