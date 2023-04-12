package com.vetrix.chat_API.file;


import com.vetrix.chat_API.student.Student;
import com.vetrix.chat_API.student.StudentRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Slf4j
@NoArgsConstructor
public class FileService {

	private FileRepository fileRepository;
	private Path fileStorageLocationProduct;
	private StudentRepository studentRepository;

	@Autowired
	public FileService(
			FileRepository fileRepository,
			FileStorageProperties fileStorageProperties,
			StudentRepository studentRepository) {
		super();
		this.fileRepository = fileRepository;
		this.fileStorageLocationProduct = Paths
				.get(System.getProperty("user.dir") + fileStorageProperties.getProductDir()).toAbsolutePath()
				.normalize();
		this.studentRepository = studentRepository;
		log.info("========>Image Path = {}<========", fileStorageLocationProduct);

		try {
			Files.createDirectories(this.fileStorageLocationProduct);
		} catch (Exception ex) {
			throw new RuntimeException("Could not create the directory to upload.");
		}
	}

	public List<FileDto> findImageBySenderReceiver(String sender, String receiver){
		return fileRepository.findBySenderReceiver(sender, receiver);
	};

	public FileDto getImageById(Long imageId) {
		log.info("Service: Fetching Image {}", imageId);
		return fileRepository.findById(imageId).get();
	}

	public FileDto submitImage(MultipartFile file, String sender, String receiver) {

		//GENERATION OF VARCHAR

		String completeName = "abe";
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghijklmnopqrstuvxyz";

		StringBuilder s = new StringBuilder(50);
		for (int i = 0; i < 50; i++) {
			int index = (int)(str.length() * Math.random());
			s.append(str.charAt(index));
		}
		completeName = String.valueOf(s);

		//END OF GENERATION

		log.info("Image Name = {}", file.getOriginalFilename());
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		try {
			Path targetLocation = this.fileStorageLocationProduct.resolve(completeName+fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path(System.getProperty("user.dir") + "/src/main/resources/static/products/").path(completeName + fileName)
					.toUriString();
			return fileRepository.save(
					new FileDto(null, completeName+fileName,fileName, fileDownloadUri, file.getSize(), file.getContentType(), sender,receiver));
		} catch (IOException e) {
			throw new RuntimeException("Could not store file " + completeName+fileName + ". Please try again!", e);
		}
	}

	public Resource loadProfileImage(String fileName) {
		log.info("Load File = {} Successfully", fileName);
		try {
			Path filePath = this.fileStorageLocationProduct.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new RuntimeException("Le fichier: " + fileName + " est introuvable");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Le fichier: " + fileName + " est introuvable");
		}
	}

	public Set<Student> getReceiver(String sender){
		Set<Student> receivers = new LinkedHashSet<Student>();
		List<FileDto> files = fileRepository.findByCurrent(sender);
		for (FileDto file: files){
			if (!Objects.equals(file.getSender(), sender))
				receivers.add(studentRepository.findStudentByNumber(file.getSender()).get(0));
			if (!Objects.equals(file.getReceiver(), sender))
				receivers.add(studentRepository.findStudentByNumber(file.getReceiver()).get(0));
		}
		return receivers;
	}

	public void deleteById(Long id){
		fileRepository.deleteById(id);
	}
}
