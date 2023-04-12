package com.vetrix.chat_API.file;

import com.vetrix.chat_API.student.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Tag(name = "File")
@CrossOrigin("*")
@RequestMapping(path = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

	@Autowired
	private FileService imageService;

	@PostMapping(path = "/send/{sender}/to/{receiver}")
	@Operation(summary = "Send new file")
	public FileDto submitImage(
			@RequestParam("file") MultipartFile file,
			@PathVariable String sender,
			@PathVariable String receiver
	) {
		log.info("Controller save File ({})", file);
		return imageService.submitImage(file, sender,receiver);
	}

	@GetMapping(path = "/id")
	@Operation(summary = "Get file by File id")
	public FileDto fetchingImageById(@RequestParam(name = "FileId") Long fileId) {
		log.info("Controller: Fetching Image by Id {}", fileId);
		return imageService.getImageById(fileId);
	}

	@GetMapping(path = "/{sender}/to/{receiver}")
	@Operation(summary = "Fetching file by sender and receiver")
	public List<FileDto> FetchingImageByProduct(
			@PathVariable String sender,
			@PathVariable String receiver
	) {
		log.info("Controller: {} to {}", sender,receiver);
		return imageService.findImageBySenderReceiver(sender,receiver);
	}

	@GetMapping(path = "/{sender}")
	@Operation(summary = "Fetching All number registration of student who write to current")
	public Set<Student> getReceiver(@PathVariable String sender){
		return imageService.getReceiver(sender);
	}

	@GetMapping("/download/{reelName}/{fileName}")
	@Operation(summary = "Get File")
	public ResponseEntity<Resource> downloadProfileImage(
			@PathVariable String reelName,
			@PathVariable String fileName,
			HttpServletRequest request
	) {
		Resource resource = imageService.loadProfileImage(fileName);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Could Not Determine file ");
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+reelName+"\"")
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);
	}

	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete File")
	public void deleteFile(@PathVariable long id){
		imageService.deleteById(id);
	}
}
