package com.watsoo.dms.serviceimp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.watsoo.dms.constant.HttpApi;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.entity.CommandSendDetails;
import com.watsoo.dms.entity.Configuration;
import com.watsoo.dms.entity.FileUploadDetails;
import com.watsoo.dms.enums.CommandStatus;
import com.watsoo.dms.repository.CommandSendDetalisRepository;
import com.watsoo.dms.repository.ConfigurationRepository;
import com.watsoo.dms.repository.FileUploadDetailsRepository;
import com.watsoo.dms.restclient.RestClientService;
import com.watsoo.dms.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private Path dirLocation;

	@Autowired
	private FileUploadDetailsRepository fileUploadDetailsRepository;

	@Autowired
	private CommandSendDetalisRepository commandSendDetalisRepository;

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Autowired
	private RestClientService restClientService;

	@Value("${file.dawnload.url}")
	String fileUrl;

	public FileServiceImpl(@Value("${file.upload.dir}") String directory) {
		this.dirLocation = Paths.get(directory).toAbsolutePath().normalize();
		;
	}

	@Override
	public Response<?> storeFileInLocalDirectoryResponseIsDownloadUrl(MultipartFile file, Long currentDate) {
		String fileName = StringUtils.cleanPath(currentDate + file.getOriginalFilename());

		try {
			Path targetLocation = this.dirLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/getFile/")
					.path(fileName).toUriString();
			return new Response<>("File successfull uploaded", fileDownloadUri, HttpStatus.OK.value());
		} catch (IOException ex) {
			return new Response<>("File fail to uploaded", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> downloadDocument(String fileName) {

		try {

			if (fileName == null) {
				return new Response<>("File Must BE Requried", null, 400);
			}
			FileUploadDetails findByFileName = fileUploadDetailsRepository.findByFileName(fileName);

			String message = "File Not found";

			if (findByFileName == null) {
				return new Response<>(message, null, 400);
			}

			List<Configuration> allConfigurations = configurationRepository.findAll();

			Map<String, String> configurationMap = allConfigurations.stream()
					.filter(config -> config.getKey() != null && config.getValue() != null)
					.collect(Collectors.toMap(Configuration::getKey, Configuration::getValue));

			int reCallCount = Integer.valueOf(configurationMap.get("RE_CAll_COUNT"));

			Optional<CommandSendDetails> commandSendDetalis = commandSendDetalisRepository
					.findById(findByFileName.getCommandSendId());

			CommandSendDetails commandSendDetails = new CommandSendDetails();

			if (commandSendDetalis.isPresent() && commandSendDetalis.get() != null) {
				commandSendDetails = commandSendDetalis.get();
				message = commandSendDetalis.get().getRemarks() != null
						&& !commandSendDetalis.get().getRemarks().trim().equals("")
								? commandSendDetalis.get().getRemarks()
								: message;
			}

			if (findByFileName != null && !findByFileName.getIsFileExist() && commandSendDetails != null
					&& (commandSendDetails.getRemarks() != null
							|| (commandSendDetails.getStatus().equals(CommandStatus.PARTIALY_SUCCESS)
									&& commandSendDetails.getReCallCount().intValue() == reCallCount))) {
				return new Response<>(message, null, 400);

			} else if (findByFileName != null && !findByFileName.getIsFileExist() && commandSendDetails != null
					&& commandSendDetails.getRemarks() == null) {
				return new Response<>("File will be available soon. Please wait for some time.", null, 400);

			}

			return new Response<>("Image featch ", configurationMap.get("FILE_ACCESS_BASE_URL") + fileName, 200);

		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Something went wrong ", null, 400);
		}

	}

	@Override
	public Resource downloadDocumentFromServer(String filename) {

		try {

			Resource checkFile = restClientService.getFile(filename);

			return checkFile;

		} catch (Exception e) {
			throw new RuntimeException("Could not download file");
		}
	}
}
