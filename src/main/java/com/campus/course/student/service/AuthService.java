package com.campus.course.student.service;

import com.campus.course.student.entity.StudentEntity;
import com.campus.course.student.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthService implements UserDetailsService {
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<StudentEntity> studentEntityOptional = studentRepository.findByEmail(email);
        if(studentEntityOptional.isEmpty()) throw new UsernameNotFoundException("Student not exit");

        StudentEntity student = studentEntityOptional.get();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("student"));

        return new User(student.getEmail(), student.getPassword(), authorities);
    }
}
