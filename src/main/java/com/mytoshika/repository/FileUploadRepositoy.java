package com.mytoshika.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytoshika.models.FileUpload;

public interface FileUploadRepositoy extends JpaRepository<FileUpload, Long> {
	
}
