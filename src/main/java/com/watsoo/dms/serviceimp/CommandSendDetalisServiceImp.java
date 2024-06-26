package com.watsoo.dms.serviceimp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.watsoo.dms.dto.CommanddetalisSendDto;
import com.watsoo.dms.dto.DeviceInformationDto;
import com.watsoo.dms.dto.PaginatedRequestDto;
import com.watsoo.dms.dto.PaginatedResponseDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.entity.Command;
import com.watsoo.dms.entity.CommandSendDetails;
import com.watsoo.dms.entity.Event;
import com.watsoo.dms.enums.CommandStatus;
import com.watsoo.dms.repository.CommandRepository;
import com.watsoo.dms.repository.CommandSendDetalisRepository;
import com.watsoo.dms.repository.VehicleRepository;
import com.watsoo.dms.restclient.RestClientService;
import com.watsoo.dms.service.CommandSendDetalisService;
import com.watsoo.dms.service.CommandSendTrailService;
import com.watsoo.dms.service.FileUploadDetailsService;
import com.watsoo.dms.util.ConvertionUtility;
import com.watsoo.dms.util.Utility;
import com.watsoo.dms.validation.Validation;

@Service
public class CommandSendDetalisServiceImp implements CommandSendDetalisService {

	@Autowired
	private CommandSendDetalisRepository commandSendDetalisRepository;

	@Autowired
	private FileUploadDetailsService fileUploadDetailsService;

	@Autowired
	private RestClientService restClientService;

	@Autowired
	private CommandRepository commandRepository;

	@Autowired
	private CommandSendTrailService commandSendTrailService;

	@Autowired
	private VehicleRepository vehicleRepository;

	Logger logger = LoggerFactory.getLogger(CommandSendDetalisServiceImp.class);

	@Override
	public void saveCommandDetalis(List<Event> allEvent, Map<Long, String> deviceWithProtocolName) {

		try {
			List<CommandSendDetails> commandSendDetailsList = new ArrayList<>();

//		commandRepository.findBy(null, null);

			Set<String> vechileNumbers = allEvent.stream().map(Event::getVehicleNo).collect(Collectors.toSet());

			List<Command> findCommandsByVehicleIds = commandRepository.findCommandsByVehicleNumbers(vechileNumbers,
					"UPLOADFILE");

			Map<String, Command> commandMapByVEchileNumber = findCommandsByVehicleIds.stream()
					.collect(Collectors.toMap(Command::getVehicleNumber, Function.identity()));

			for (Event event : allEvent) {

				if (event.getEvidencePhotos() != null) {
					CommandSendDetails obj = new CommandSendDetails();
					Command command = new Command();
					if (commandMapByVEchileNumber != null) {
						command = commandMapByVEchileNumber.get(event.getVehicleNo());
					}

					if (command != null) {
						obj.setBaseCommand(command.getBaseCommand());
						obj.setCommand(command.getBaseCommand() + event.getEvidencePhotos() + command.getEndCommand());
					} else {
						obj.setBaseCommand("UPLOADFILE");
						obj.setCommand("UPLOADFILE," + event.getEvidencePhotos() + "#");
					}

					obj.setEvidenceFiles(event.getEvidencePhotos());
					obj.setDeviceId(event.getDeviceId());
					obj.setEventId(event.getId());
					obj.setEventType(event.getEventType().name());
					obj.setPositionId(event.getPositionId());
					obj.setCreateOn(LocalDateTime.now());
					obj.setImeiNumber(event.getImeiNo());
					obj.setUpdatedOn(LocalDateTime.now());
					obj.setStatus(CommandStatus.UNABLE_TO_UPLOAD);

					List<String> convertStringToArray = Utility.convertStringToArray(event.getEvidencePhotos());
					obj.setNoOfFileReq(convertStringToArray.size());

					commandSendDetailsList.add(obj);

				}

			}
			List<CommandSendDetails> saveAllCommandDetalis = commandSendDetalisRepository
					.saveAll(commandSendDetailsList);
			if (saveAllCommandDetalis != null && saveAllCommandDetalis.size() > 0) {
				fileUploadDetailsService.saveFileDetalis(saveAllCommandDetalis);

			}
		} catch (Exception e) {

		}

	}

	@Override
	public Response<?> getAllCommandDetalis(int pageSize, int pageNo) {
		try {
			PaginatedRequestDto paginatedRequest = new PaginatedRequestDto(pageSize, pageNo);

			Pageable pageable = pageSize > 0 ? PageRequest.of(pageNo, pageSize) : Pageable.unpaged();
			Page<CommandSendDetails> findAllCommandsSendDetalis = commandSendDetalisRepository.findAll(paginatedRequest,
					pageable);

			List<CommandSendDetails> allCommands = findAllCommandsSendDetalis.getContent();
			List<CommanddetalisSendDto> commandDetailsDtoList = new ArrayList<>();
			if (allCommands != null && !allCommands.isEmpty()) {
				commandDetailsDtoList = allCommands.stream().map(CommanddetalisSendDto::entityToDto)
						.collect(Collectors.toList());

			}
			PaginatedResponseDto<Object> paginatedResponseDto = new PaginatedResponseDto<>(
					findAllCommandsSendDetalis.getTotalElements(), allCommands.size(),
					findAllCommandsSendDetalis.getTotalPages(), pageNo, commandDetailsDtoList);
			return new Response<>("Success", paginatedResponseDto, 200);
		} catch (Exception e) {
			return new Response<>("Something Went Wrong", null, 400);

		}
	}

	@Override
	public Response<?> sendCommandManually(CommanddetalisSendDto commanddetalisSendDto) {

		Response<?> checkCommandDetalis = Validation.checkCommandDetalis(commanddetalisSendDto);

		if (checkCommandDetalis != null) {
			return checkCommandDetalis;
		}

		try {
			CommandSendDetails commandSendDetails = new CommandSendDetails();
			commandSendDetails.setDeviceId(commanddetalisSendDto.getDeviceId());
			commandSendDetails.setCommand(commanddetalisSendDto.getCommand());
			restClientService.sendHttpPostRequestForCommand(commandSendDetails);
			commandSendTrailService.saveManualCommand(commanddetalisSendDto);
			return new Response<>("Command Send Successfully", null, 200);
		} catch (Exception e) {
			return new Response<>("Something Went Wrong", null, 400);
		}

	}

	@Override
	public void sendCommand(int reCallCount, int processSleepTime) {

		try {
			List<CommandSendDetails> allCommands = commandSendDetalisRepository
					.findByNoOfFileUploadedIsNullAndReCallCountLessThanRecallOrReCallCountIsNull(reCallCount);

			if (allCommands != null && !allCommands.isEmpty()) {
				Set<Long> allDeviceID = allCommands.stream().map(CommandSendDetails::getDeviceId)
						.collect(Collectors.toSet());

				String deviceInformation = restClientService.getDeviceInformation(allDeviceID);
				Map<Long, DeviceInformationDto> retrieveDeviceInfoMap = new HashMap<>();
				if (deviceInformation != null && !deviceInformation.isEmpty()) {
					retrieveDeviceInfoMap = retrieveDeviceInfoMap(deviceInformation);
				}

				Map<Long, List<CommandSendDetails>> deviceIdWithMap = new HashMap<>();
				for (CommandSendDetails commandSendDetails : allCommands) {

					if (commandSendDetails.getNoOfFileUploaded() == null) {

						if (commandSendDetails.getReCallCount() == null
								|| commandSendDetails.getReCallCount() < reCallCount) {
							if (retrieveDeviceInfoMap != null
									&& retrieveDeviceInfoMap.get(commandSendDetails.getDeviceId()) != null
									&& "online".equals(
											retrieveDeviceInfoMap.get(commandSendDetails.getDeviceId()).getStatus())) {

								deviceIdWithMap
										.computeIfAbsent(commandSendDetails.getDeviceId(), k -> new ArrayList<>())
										.add(commandSendDetails);

							}
							Integer countRecall = commandSendDetails.getReCallCount() == null ? 0
									: commandSendDetails.getReCallCount() + 1;
							commandSendDetails.setReCallCount(countRecall);
							commandSendDetails.setReCallOn(LocalDateTime.now());
							commandSendDetails.setUpdatedOn(LocalDateTime.now());

						} else {
							commandSendDetails.setStatus(CommandStatus.FAILED);
						}

					} else if (commandSendDetails.getNoOfFileReq() != commandSendDetails.getNoOfFileUploaded()) {
						commandSendDetails.setStatus(CommandStatus.PARTIALY_SUCCESS);
					} else {
						commandSendDetails.setStatus(CommandStatus.COMPLETE_SUCCESS);

					}

				}

				if (deviceIdWithMap != null && !deviceIdWithMap.isEmpty()) {
					commandSendDetalisRepository.saveAll(allCommands);
					processCommand(deviceIdWithMap, processSleepTime);
				}
			}
		} catch (Exception e) {

		}
	}

	public void processCommand(Map<Long, List<CommandSendDetails>> deviceIdWithMap, int processSleepTime) {

		ExecutorService executorService = Executors.newFixedThreadPool(deviceIdWithMap.size());

		CountDownLatch latch = new CountDownLatch(deviceIdWithMap.values().stream().mapToInt(List::size).sum());

		for (Map.Entry<Long, List<CommandSendDetails>> entry : deviceIdWithMap.entrySet()) {
			List<CommandSendDetails> commandList = entry.getValue();

			executorService.submit(() -> {
				for (CommandSendDetails commandSendDetails : commandList) {

					if (commandSendDetails.getNoOfFileUploaded() == null
							|| commandSendDetails.getNoOfFileUploaded() > 0) {

						restClientService.sendHttpPostRequestForCommand(commandSendDetails);

//						logger.info("Inside Command Send Thread " + commandSendDetails.getCommand());

					}

					try {
						Thread.sleep(processSleepTime * 1000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					} finally {
						// Count down the latch after each command is processed
						latch.countDown();
					}
				}
			});
		}

		// Wait for all commands to complete before shutting down the executor service
		try {
			latch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Shutdown the executor service
		executorService.shutdown();
		System.out.println(executorService.isShutdown());
	}

	private Map<Long, DeviceInformationDto> retrieveDeviceInfoMap(String deviceInformation) {
		Map<Long, DeviceInformationDto> deviceInformationByDeviceId = new HashMap<>();
		try {

			Gson gson = new Gson();
			JsonArray deviceInformationJson = gson.fromJson(deviceInformation, JsonArray.class);
			for (JsonElement jsonElement : deviceInformationJson) {

				DeviceInformationDto deviceInformationDto = new DeviceInformationDto();
				JsonObject deviceInformationJsonObject = jsonElement.getAsJsonObject();
				if (deviceInformationJsonObject.has("id")) {
					long deviceId = deviceInformationJsonObject.get("id").getAsLong();

					if (deviceInformationJsonObject.has("status")) {
						String status = deviceInformationJsonObject.get("status").getAsString();
						deviceInformationDto.setStatus(status);
						deviceInformationByDeviceId.put(deviceId, deviceInformationDto);

					}

				}

			}

		} catch (Exception e) {

		}
		return deviceInformationByDeviceId;
	}

	@Override
	public Long updateCommandResponse(Long batchSize, Long positionId) {

		Long positionIdForConfiguration = 0L;

		try {
			String reportFrom = restClientService.getPositionByPositionIdAndBatchSize(batchSize, positionId);
			List<String> allFilename = new ArrayList<>();
			Map<String, String> filenameWithMessage = new HashMap<>();

			if (reportFrom != null) {

				Gson gson = new Gson();
				JsonArray listRouteObject = gson.fromJson(reportFrom, JsonArray.class);

				for (JsonElement element : listRouteObject) {
					JsonObject routeObject = element.getAsJsonObject();

					positionIdForConfiguration = routeObject.get("id").getAsLong();

					String fileName = "";
					String meassage = "";

					if (routeObject.has("attributes")) {
						JsonObject attributes = routeObject.get("attributes").getAsJsonObject();
						if (attributes.has("textMessage")) {

							String textMeassage = attributes.get("textMessage").getAsString();
							JsonObject meassageObject = ConvertionUtility.convertStringToJson(textMeassage);

//							JsonObject meassageObject = gson.fromJson(textMeassage, JsonObject.class);
							if (meassageObject.has("Filename")) {
								JsonArray files = meassageObject.get("Filename").getAsJsonArray();

								List<String> fileList = new ArrayList<>();
								for (JsonElement fileElement : files) {
									fileList.add(fileElement.getAsString());
								}
								fileName = String.join(",", fileList);

								if (meassageObject.has("Message")) {
									meassage = meassageObject.get("Message").getAsString();
									allFilename.add(fileName);

									if (filenameWithMessage.get(fileName) != null) {

										String message = filenameWithMessage.get(fileName);
										if (!message.equals("File not exist")) {

											filenameWithMessage.put(fileName, meassage);
										}

									} else {

										filenameWithMessage.put(fileName, meassage);
									}

								}

							}

						}

					}
				}

			}

			if (allFilename != null && allFilename.size() > 0) {

				List<CommandSendDetails> findAlCommandlByEvidenceFiles = commandSendDetalisRepository
						.findAllByEvidenceFiles(allFilename);

				if (findAlCommandlByEvidenceFiles != null && findAlCommandlByEvidenceFiles.size() > 0) {

					for (CommandSendDetails commandSendDetails : findAlCommandlByEvidenceFiles) {
						if (commandSendDetails.getRemarks() == null
								|| !commandSendDetails.getRemarks().equals("File not exist")) {

							if (commandSendDetails.getNoOfFileUploaded() == null
									|| ((commandSendDetails.getNoOfFileReq() != null && commandSendDetails
											.getNoOfFileUploaded() < commandSendDetails.getNoOfFileReq()))) {
								commandSendDetails
										.setRemarks(filenameWithMessage.get(commandSendDetails.getEvidenceFiles()));
							}

						}

					}

					commandSendDetalisRepository.saveAll(findAlCommandlByEvidenceFiles);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return positionIdForConfiguration;
	}

}
