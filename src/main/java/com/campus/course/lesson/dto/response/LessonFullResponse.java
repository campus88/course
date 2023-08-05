package com.campus.course.lesson.dto.response;

import com.campus.course.course.dto.response.CourseResponse;
import com.campus.course.course.entity.CourseEntity;
import com.campus.course.lesson.entity.LessonEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonFullResponse {
    protected Long id;
    protected String title;
    protected String description;
    protected Long courseId;
    protected CourseResponse course;

    public static LessonFullResponse of(LessonEntity lesson, CourseEntity course){
        return LessonFullResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .courseId(lesson.getCourseId())
                .course(CourseResponse.of(course))
                .build();
    }
}
