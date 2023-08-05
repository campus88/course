package com.campus.course.student.dto.response;

import com.campus.course.student.entity.StudentEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentResponse {
    protected Long id;
    protected String firstName;
    protected String lastName;
    protected String email;

    public static StudentResponse of(StudentEntity entity){
        return StudentResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .build();
    }
}
