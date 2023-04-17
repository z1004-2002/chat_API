package com.vetrix.chat_API.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileRepository extends JpaRepository<FileDto, Long> {
    @Query("select i from FileDto i where i.sender = ?1 and i.receiver = ?2 or i.sender = ?2 and i.receiver = ?1")
	List<FileDto> findBySenderReceiver(String sender, String receiver);

    @Query("select i from FileDto i where i.sender = ?1 or i.receiver = ?1")
    List<FileDto> findByCurrent(String sender);
}
