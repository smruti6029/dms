package com.watsoo.dms.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.watsoo.dms.dto.Response;

public interface FileService {

	Response<?> storeFileInLocalDirectoryResponseIsDownloadUrl(MultipartFile file, Long currentDate);

	Response<?> downloadDocument(String fileName);

	Resource downloadDocumentFromServer(String filename);

}
