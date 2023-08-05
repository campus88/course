package com.campus.course.course.dto.response;

import com.campus.course.course.entity.CourseEntity;
import com.campus.course.lesson.dto.response.LessonResponse;
import com.campus.course.lesson.entity.LessonEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class CourseFullResponse extends CourseEntity {
    private List<LessonResponse> lessons = new ArrayList<>();
    public static CourseFullResponse of(CourseEntity entity, List<LessonEntity> lessonEntities) {
        return CourseFullResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .lessons(lessonEntities.stream().map(LessonResponse::of).collect(Collectors.toList()))
                .build();
    }
}
