package com.vetrix.chat_API.student;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "Student")
@CrossOrigin("*")
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping(path = "/register")
    @Operation(summary = "Add new student")
    public Student register(@RequestBody Student student){
        return studentService.addStudent(student);
    }

    @GetMapping(path = "/matricule")
    @Operation(summary = "get student's information by number registration")
    public Student getByNumber(@RequestParam(name = "matricule") String number){
        List<Student> students = studentService.findByNumber(number);
        if (students.size() == 0)
            throw new IllegalStateException("this number is not present in database");
        return students.get(0);
    }

    @GetMapping
    @Operation(summary = "get All Students")
    public List<Student> getAllStudent(){
        return studentService.findAllStudent();
    }

    @PutMapping("/update")
    @Operation(summary = "update student by his number registration")
    public Student updateStudent(@RequestParam(name = "matricule") String number,@RequestBody Student student){
        return studentService.updateStudent(student, number);
    }

    @DeleteMapping("/delete/{number}")
    @Operation(summary = "delete Student by his number registration")
    public ResponseEntity<String> deleteStudent(@PathVariable String number){
        studentService.deleteStudent(number);
        return new ResponseEntity<>("Success delete", HttpStatus.ACCEPTED);
    }
}
