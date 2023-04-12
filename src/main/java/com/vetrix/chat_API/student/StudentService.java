package com.vetrix.chat_API.student;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private final StudentRepository studentRepository;

    public Student addStudent(Student student){
        studentRepository.save(student);
        return student;
    }

    public List<Student> findByNumber(String number){
        return studentRepository.findStudentByNumber(number);
    }

    public List<Student> findAllStudent(){
        return studentRepository.findAll();
    }

    public Student updateStudent(Student student,String number){
        List<Student> students = findByNumber(number);
        if (students.size() == 0)
            throw new IllegalStateException("this number is not present in database");
        Optional<Object> s = studentRepository.findById(students.get(0).getId()).map(stu -> {
            stu.setNumber(student.getNumber());
            stu.setUserName(student.getUserName());
            return studentRepository.save(stu);
        });
        return student;
    }

    public void deleteStudent(String number){
        List<Student> students = findByNumber(number);
        if (students.size() == 0)
            throw new IllegalStateException("this number is not present in database");
        studentRepository.deleteById(students.get(0).getId());
    }
}
