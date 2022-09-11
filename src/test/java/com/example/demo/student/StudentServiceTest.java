package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // -> ez is elég a 19, 24 és a teljes tearDown() helyett
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
//    private AutoCloseable autoCloseable;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }

    @Test
    void canGetAllStudents() {
        // when
        underTest.getAllStudents();
        // then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        // Given
        String email = "jamilia@gmail.com";
        Student student = new Student("Jamila", email, Gender.FEMALE);
        // When
        underTest.addStudent(student);
        // Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // Given
        String email = "jamilia@gmail.com";
        Student student = new Student("Jamila", email, Gender.FEMALE);
        given(studentRepository.selectExistsEmail(student.getEmail()))
                .willReturn(true);
        // When
        // Then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void canDeleteStudent() {
        // Given
        Student student = new Student("Jamila", "jamilia@gmail.com", Gender.FEMALE);
        student.setId(1234L);
        given(studentRepository.existsById(student.getId()))
                .willReturn(true);
        // When
        underTest.deleteStudent(student.getId());
        // Then
        verify(studentRepository).deleteById(student.getId());
    }

    @Test
    void canNotDeleteStudent() {
        // Given
        Student student = new Student("Jamila", "jamilia@gmail.com", Gender.FEMALE);
        student.setId(1234L);
        given(studentRepository.existsById(student.getId()))
                .willReturn(false);
        // When
        // Then
        assertThatThrownBy(() -> underTest.deleteStudent(student.getId()))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + student.getId() + " does not exists");
        verify(studentRepository, never()).deleteById(student.getId());
    }

}