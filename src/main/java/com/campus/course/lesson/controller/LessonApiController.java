package com.campus.course.lesson.controller;

import com.campus.course.course.dto.response.CourseResponse;
import com.campus.course.course.entity.CourseEntity;
import com.campus.course.course.exception.CourseNotFoundException;
import com.campus.course.course.repository.CourseRepository;
import com.campus.course.course.routes.CourseRoutes;
import com.campus.course.lesson.dto.request.CreateLessonRequest;
import com.campus.course.lesson.dto.request.EditLessonRequest;
import com.campus.course.lesson.dto.response.LessonFullResponse;
import com.campus.course.lesson.dto.response.LessonResponse;
import com.campus.course.lesson.entity.LessonEntity;
import com.campus.course.lesson.exception.LessonNotFoundException;
import com.campus.course.lesson.repository.LessonRepository;
import com.campus.course.lesson.routes.LessonRoutes;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class LessonApiController {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @PostMapping(LessonRoutes.CREATE)
    public LessonFullResponse create(@RequestBody CreateLessonRequest request) throws CourseNotFoundException {
        CourseEntity course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(CourseNotFoundException::new);

        LessonEntity entity = lessonRepository.save(request.entity());
        return LessonFullResponse.of(entity, course);
    }

    @GetMapping(LessonRoutes.BY_ID)
    public LessonFullResponse byId(@PathVariable Long id) throws CourseNotFoundException, LessonNotFoundException {
        LessonEntity lesson = lessonRepository
                .findById(id)
                .orElseThrow(LessonNotFoundException::new);

        CourseEntity course = lesson.getCourseId() != null ?
                courseRepository.findById(lesson.getCourseId()).orElseThrow(CourseNotFoundException::new)
                : null;

        return LessonFullResponse.of(lesson, course);
    }

    @PutMapping(LessonRoutes.EDIT)
    public LessonFullResponse edit(@PathVariable Long id, @RequestBody EditLessonRequest request) throws LessonNotFoundException, CourseNotFoundException {
        LessonEntity lesson = lessonRepository.findById(id).orElseThrow(LessonNotFoundException::new);

        CourseEntity course = lesson.getCourseId() != null ?
                courseRepository.findById(lesson.getCourseId()).orElseThrow(CourseNotFoundException::new)
                : null;

        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());

        lesson = lessonRepository.save(lesson);

        return LessonFullResponse.of(lesson, course);
    }

    @GetMapping(LessonRoutes.SEARCH)
    public List<LessonResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<LessonEntity> example = Example.of(
                LessonEntity.builder().title(query).title(query).build(),
                ignoringExampleMatcher);

        return lessonRepository
                .findAll(example, pageable)
                .stream()
                .map(LessonResponse::of)
                .collect(Collectors.toList());
    }

    @DeleteMapping(LessonRoutes.BY_ID)
    public String delete(@PathVariable Long id) {
        lessonRepository.deleteById(id);
        return HttpStatus.OK.name();
    }
}
