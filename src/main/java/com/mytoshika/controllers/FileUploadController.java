package com.mytoshika.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mytoshika.models.Employee;
import com.mytoshika.models.FileUpload;
import com.mytoshika.models.User;
import com.mytoshika.repository.FileUploadRepositoy;
import com.mytoshika.repository.UserRepository;

/**
 * Class FileUploadController
 */
@Controller
public class FileUploadController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileUploadRepositoy fileUploadRepositoy;

	@Autowired
	private Environment env;

	/**
	 * POST /uploadFile -> receive and locally save a file.
	 * 
	 * @param uploadfile The uploaded file as Multipart file parameter in the 
	 * HTTP request. The RequestParam name must be the same of the attribute 
	 * "name" in the input tag with type file.
	 * 
	 * @return An http OK status in case of success, an http 4xx status in case 
	 * of errors.
	 */


	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(
			@RequestParam("uploadfile") MultipartFile uploadfile) {

		try {
			// Get the filename and build the local file path
			String filename = uploadfile.getOriginalFilename();
			String directory = env.getProperty("mytoshika.paths.uploadedFiles");
			String filepath = Paths.get(directory, filename).toString();	   
			
			// =============Save the file locally====================
			BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
			
			// save it into file_upload_details table DB
			String path = "/image/";
			FileUpload fileUpload = new FileUpload();
			fileUpload.setFileName(filename);
			fileUpload.setFilePath(path+filename);
			fileUpload.setFileType(uploadfile.getContentType());
			User user = userRepository.findById(new Long(1)).orElse(null);
			fileUpload.setUser(user);
			fileUploadRepositoy.save(fileUpload); 
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/getUploadedImages", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getUploadedImages() {

		return new ResponseEntity<>(fileUploadRepositoy.findAll(), HttpStatus.OK);
	} 
	
	@RequestMapping(value = "/submitData", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> submitData(@RequestBody Employee employee) {

		try {
			System.out.println(employee);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

} 
