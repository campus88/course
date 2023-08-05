package com.campus.course.student.controller;

import com.campus.course.student.dto.request.EditStudentRequest;
import com.campus.course.student.dto.request.RegistrationRequest;
import com.campus.course.student.dto.response.StudentResponse;
import com.campus.course.student.entity.StudentEntity;
import com.campus.course.student.exception.BadRequestException;
import com.campus.course.student.exception.StudentAlreadyExistException;
import com.campus.course.student.exception.StudentNotFoundException;
import com.campus.course.student.repository.StudentRepository;
import com.campus.course.student.routes.StudentRoutes;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class StudentApiController {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping(StudentRoutes.REGISTRATION)
    public StudentResponse registration(@RequestBody RegistrationRequest request) throws BadRequestException, StudentAlreadyExistException {
        request.validate();

        Optional<StudentEntity> check = studentRepository.findByEmail(request.getEmail());
        if (check.isPresent()) throw new StudentAlreadyExistException();

        StudentEntity student = StudentEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        student = studentRepository.save(student);

        return StudentResponse.of(student);
    }

    @PutMapping(StudentRoutes.EDIT)
    public StudentResponse edit(Principal principal, @RequestBody EditStudentRequest edit) throws StudentNotFoundException {
        StudentEntity student = studentRepository
                .findByEmail(principal.getName())
                .orElseThrow(StudentNotFoundException::new);

        student.setFirstName(edit.getFirstName());
        student.setLastName(edit.getLastName());

        studentRepository.save(student);
        return StudentResponse.of(student);
    }

    @GetMapping(StudentRoutes.BY_ID)
    public StudentResponse byId(@PathVariable Long id) throws StudentNotFoundException {
        StudentEntity student = studentRepository
                .findById(id)
                .orElseThrow(StudentNotFoundException::new);
        return StudentResponse.of(student);
    }

    @GetMapping(StudentRoutes.SEARCH)
    public List<StudentResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<StudentEntity> example = Example.of(
                StudentEntity.builder().lastName(query).firstName(query).email(query).build(),
                exampleMatcher);

        return studentRepository
                .findAll(example, pageable)
                .stream()
                .map(StudentResponse::of)
                .collect(Collectors.toList());
    }
}
