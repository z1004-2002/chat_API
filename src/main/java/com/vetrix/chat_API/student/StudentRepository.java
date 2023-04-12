package com.vetrix.chat_API.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    @Query("select s from Student s where s.number = ?1")
    public List<Student> findStudentByNumber(String number);
}
