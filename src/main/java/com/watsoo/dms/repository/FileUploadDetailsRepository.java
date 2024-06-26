package com.watsoo.dms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.watsoo.dms.entity.FileUploadDetails;

public interface FileUploadDetailsRepository extends JpaRepository<FileUploadDetails, Long> {

	List<FileUploadDetails> findByIsFileExistIsNullOrIsFileExistFalse();

	@Query(value = "SELECT * FROM file_upload_details WHERE status IS NULL OR status = 'PARTIALY_SUCCESS'", nativeQuery = true)
	List<FileUploadDetails> findByStatusIsNullOrStatusIsPartiallySuccess();

	FileUploadDetails findByFileName(String fileName);

}
