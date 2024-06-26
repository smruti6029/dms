package com.watsoo.dms.serviceimp;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.watsoo.dms.constant.Constant;
import com.watsoo.dms.dto.DeviceInformationDto;
import com.watsoo.dms.dto.DriverPerformanceDto;
import com.watsoo.dms.dto.EventDto;
import com.watsoo.dms.dto.EventTypeCountDto;
import com.watsoo.dms.dto.PaginatedRequestDto;
import com.watsoo.dms.dto.PaginatedResponseDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.entity.Configuration;
import com.watsoo.dms.entity.CredentialMaster;
import com.watsoo.dms.entity.Driver;
import com.watsoo.dms.entity.Event;
import com.watsoo.dms.entity.Remark;
import com.watsoo.dms.entity.User;
import com.watsoo.dms.entity.Vehicle;
import com.watsoo.dms.enums.EventType;
import com.watsoo.dms.repository.ConfigurationRepository;
import com.watsoo.dms.repository.EventRepository;
import com.watsoo.dms.repository.RemarkRepository;
import com.watsoo.dms.repository.VehicleRepository;
import com.watsoo.dms.restclient.RestClientService;
import com.watsoo.dms.security.JwtUserDetailsService;
import com.watsoo.dms.service.CommandSendDetalisService;
import com.watsoo.dms.service.EventService;
import com.watsoo.dms.service.FileUploadDetailsService;
import com.watsoo.dms.util.ConvertionUtility;
import com.watsoo.dms.util.TimeUtility;
import com.watsoo.dms.validation.Validation;

@Service
public class EventServiceImp implements EventService {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private RestClientService restClientService;

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Autowired
	private CommandSendDetalisService commandSendDetalisService;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private RemarkRepository remarkRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	Logger logger = LoggerFactory.getLogger(EventServiceImp.class);

	String fileUrl;

	Map<Long, String> deviceInformationWithUser = new HashMap<>();
	Map<Long, Date> userWithTime = new HashMap<>();
	String timeConfiguration = "10";

	@Override
	public Event saveEvent(String events, List<Vehicle> vehicles) {

//		logger.info("Inside saveEvent Method - >" + new Date());

		List<Vehicle> allVehicle = vehicleRepository.findAll();
		Map<Integer, Vehicle> vechileMapByDeviceId = allVehicle.stream()
				.collect(Collectors.toMap(Vehicle::getDeviceId, Function.identity()));

		Gson gson = new Gson();
		JsonArray eventList = gson.fromJson(events, JsonArray.class);

		Map<Long, Event> eventsDataMap = new HashMap<>();
		List<Long> positionIdList = new ArrayList<>();

		Remark findByStatus = remarkRepository.findByStatus("PENDING");

		for (JsonElement eventElement : eventList) {
			JsonObject event = eventElement.getAsJsonObject();

			if (event.has("attributes")) {
				JsonObject eventAttributes = event.get("attributes").getAsJsonObject();
				if (eventAttributes.has("alarm")) {
					String eventAlarm = eventAttributes.get("alarm").getAsString();

					EventType eventType = EventType.fromType(eventAlarm);
					if (eventType != null) {
						Event eventObject = new Event();
						eventObject.setEventType(eventType);
						eventObject.setEventTime(new Date());

						eventObject.setEventServerCreateTime(
								TimeUtility.getTimeStringToDateFormat(event.get("eventTime").getAsString()));
						Long deviceId = event.get("deviceId").getAsLong();
						Vehicle vehicle = vechileMapByDeviceId.get(deviceId.intValue());
						Long positionId = event.get("positionId").getAsLong();
						eventObject.setDeviceId(event.get("deviceId").getAsLong());
						eventObject.setPositionId(positionId);
						eventObject.setEventId(event.get("id").getAsLong());

						// Its update When Vechile Service call
						// -->

						eventObject.setChassisNumber(vehicle.getChassisNumber());
						eventObject.setVehicleNo(vehicle.getVehicleNumber());
						eventObject.setVehicleID(vehicle.getId());
						eventObject.setImeiNo(vehicle.getImeiNo());
						eventObject.setRemark(findByStatus.getStatus());
						eventObject.setRemarkId(findByStatus.getId());

						Driver driver = vehicle.getDriver();
						eventObject.setDriverName(driver.getName());
						eventObject.setDriverPhone(driver.getPhoneNumber());
						eventObject.setDlNo(driver.getDlNumber());
						eventObject.setDriverId(driver.getId());
						// <--
						positionIdList.add(positionId);
						eventsDataMap.put(positionId, eventObject);
					}

				}

			}

		}

		List<Event> powerCutEvent = new ArrayList<>();
		List<Event> ListOfevent = new ArrayList<>();

		Map<Long, String> deviceWithProtocolName = new HashMap<>();
		String positions = "";
		if (positionIdList != null && positionIdList.size() > 0) {
			positions = restClientService.getPositions(positionIdList);
			if (positions != null) {
				JsonArray positionObject = gson.fromJson(positions, JsonArray.class);
				for (JsonElement positionElement : positionObject) {
					JsonObject positionData = positionElement.getAsJsonObject();
					String evidenceFiles = "";
					if (positionData.has("id")) {

						Long positionId = positionData.get("id").getAsLong();
						Double latitude = positionData.get("latitude").getAsDouble();
						Double longitude = positionData.get("longitude").getAsDouble();

						Double speed = ConvertionUtility.convertKilonotsTokm(positionData.get("speed").getAsDouble());

						Event event = eventsDataMap.get(positionId);
						event.setLatitude(latitude);
						event.setLongitude(longitude);
						event.setSpeed(speed);

						if (positionData.has("attributes")) {
							JsonObject positionAttributes = positionData.get("attributes").getAsJsonObject();
							if (positionAttributes.has("evidenceFiles")) {
								evidenceFiles = positionAttributes.get("evidenceFiles").getAsString();
								event.setEvidencePhotos(evidenceFiles);

							}
							boolean ignition = positionAttributes.get("ignition").getAsBoolean();
							event.setIgnition(ignition);

						}

						if (event.getEventType().equals(EventType.POWER_CUT)) {
							powerCutEvent.add(event);
						} else {
							ListOfevent.add(event);

						}
					}

				}

			}

		}

		Event event = new Event();
//			List<Event> ListOfevent = new ArrayList<>(eventsDataMap.values());

		if (powerCutEvent != null && powerCutEvent.size() > 0) {

			Collections.sort(powerCutEvent, new Comparator<Event>() {
				@Override
				public int compare(Event e1, Event e2) {
					return e1.getEventServerCreateTime().compareTo(e2.getEventServerCreateTime());
				}
			});

			event = powerCutEvent.get(powerCutEvent.size() - 1);

			Event powerCutEventByPositionId = eventRepository.findEventsByPositionIdAndEventType(event.getPositionId(),
					event.getEventType().name());

			if (powerCutEventByPositionId != null) {
				event = powerCutEventByPositionId;
			}

			ListOfevent.add(event);

		}

		if (ListOfevent != null && ListOfevent.size() > 0) {

			Collections.sort(ListOfevent, new Comparator<Event>() {
				@Override
				public int compare(Event e1, Event e2) {
					return e1.getEventServerCreateTime().compareTo(e2.getEventServerCreateTime());
				}
			});

			List<Event> saveAll = eventRepository.saveAll(ListOfevent);
			event = ListOfevent.get(ListOfevent.size() - 1);

			if (ListOfevent.size() != powerCutEvent.size()) {
				commandSendDetalisService.saveCommandDetalis(saveAll, deviceWithProtocolName);
			}

		}

		return event;
	}

	@Override
	public Response<?> getAllEvent(int pageSize, int pageNo, String vehicleNo, String driverName, String eventType,
			String searchKey, String fromDate, String toDate, String dlNumber, Integer remarkId) {

//			System.out.println("Get all method calling " + new Date());
//		
//		try {
//
//			System.out.println("Inside Scheluder Start " + new Date());
//			Thread thread = new Thread(() -> fileUploadDetailsService.updateFlleDetalisV2());
//			
//			thread.start();
//			
//			System.out.println("Inside Scheluder End " + new Date());
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}

		try {
			PaginatedRequestDto paginatedRequest = new PaginatedRequestDto(pageSize, pageNo, vehicleNo, driverName,
					eventType, searchKey, fromDate, toDate, dlNumber, remarkId);
			Pageable pageable = pageSize > 0 ? PageRequest.of(pageNo, pageSize) : Pageable.unpaged();
			Page<Event> findAllEvent = eventRepository.findAll(paginatedRequest, pageable);
			List<Event> events = findAllEvent.getContent();

			List<Vehicle> allVehicle = vehicleRepository.findAll();
			Set<Long> collectDeviceId = allVehicle.stream().map(vehicle -> vehicle.getDeviceId().longValue())
					.collect(Collectors.toSet());
//
			String deviceInformation = getDeviceInformtaion(collectDeviceId);
//			String deviceInformation="";

			int totalActiveVehicle = 0;
			int totalInActiveVehicle = 0;

			Map<Long, DeviceInformationDto> retrieveDeviceInfoMap = new HashMap<>();
			if (deviceInformation != null && !deviceInformation.equals("")) {
				retrieveDeviceInfoMap = retrieveDeviceInfoMap(deviceInformation);

				List<DeviceInformationDto> listOfDeviceInformation = new ArrayList<>(retrieveDeviceInfoMap.values());
				for (DeviceInformationDto deviceInformationDto : listOfDeviceInformation) {
					if (deviceInformationDto.getStatus().equals("online")) {
						totalActiveVehicle++;
					} else {
						totalInActiveVehicle++;
					}

				}

			}

			// all events for Dashboard

			EventTypeCountDto eventsAllDetalisForDashBoard = getEventsAllDetalisForDashBoard(collectDeviceId);
			eventsAllDetalisForDashBoard.setTotalInActiveVehicle(totalInActiveVehicle);
			eventsAllDetalisForDashBoard.setTotalActiveVehicle(totalActiveVehicle);

			List<EventDto> eventsdto = new ArrayList<>();

			for (Event event : events) {
				EventDto fromEventToEventDto = EventDto.fromEntity(event);
				if (event.getEvidencePhotos() != null) {
					fromEventToEventDto.setEvidencePhotos(convertStringToArray(event.getEvidencePhotos()));
				}

//				if (retrieveDeviceInfoMap != null) {
//
//					fromEventToEventDto.setDeviceInformationDto(retrieveDeviceInfoMap.get(event.getDeviceId()));
//				}

				eventsdto.add(fromEventToEventDto);

			}

			PaginatedResponseDto<Object> paginatedResponseDto = new PaginatedResponseDto<>(
					findAllEvent.getTotalElements(), eventsdto.size(), findAllEvent.getTotalPages(), pageNo, eventsdto,
					eventsAllDetalisForDashBoard);
			return new Response<>("Event List", paginatedResponseDto, HttpStatus.OK.value());

		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Error occurred while processing Event list", null, HttpStatus.BAD_REQUEST.value());

		}

	}

	public String getDeviceInformtaion(Set<Long> allDeviceID) {

		boolean isFirstCall = true;

		Date currentTime = new Date();

		User user = new User();
		Optional<CredentialMaster> userDetails = userDetailsService.getUserDetails();
		if (userDetails.isPresent()) {
			user = userDetails.get().getUser();
		}
		if (deviceInformationWithUser != null && userWithTime != null
				&& deviceInformationWithUser.get(user.getId()) != null) {
			isFirstCall = false;
			currentTime = userWithTime.get(user.getId());
		}

		if (isFirstCall || (!isFirstCall
				&& (new Date(currentTime.getTime() + Integer.parseInt(timeConfiguration) * 1000).before(new Date())))) {
			Optional<Configuration> findByKey = configurationRepository.findByKey("DEVICE_INFORMATION_API_CALL_TIME");
			if (findByKey.isPresent() && findByKey.get() != null) {
				timeConfiguration = findByKey.get().getValue();
			}

			String deviceInformation = restClientService.getDeviceInformation(allDeviceID);
			deviceInformationWithUser.put(user.getId(), deviceInformation);
			userWithTime.put(user.getId(), new Date());
			return deviceInformation;

		} else {
			return deviceInformationWithUser.get(user.getId());
		}

	}

	private EventTypeCountDto getEventsAllDetalisForDashBoard(Set<Long> allDeviceID) {

		List<Event> allEvents = eventRepository.findAll();
		int numberOfEvent = 4;
		Optional<Configuration> findByKey = configurationRepository.findByKey("DEFAULTER_DRIVER");
		if (findByKey.isPresent() && findByKey.get() != null) {
			numberOfEvent = Integer.valueOf(findByKey.get().getValue());
		}

		int countDefaulterDriver = 0;
		long yawningCount = 0;
		long mobileUsageCount = 0;
		long distractionCount = 0;
		long smokingCount = 0;
		long closeEyesCount = 0;
		long noFaceCount = 0;
		long lowHeadCount = 0;
		long drinkingCount = 0;
		int tamperedDevices = 0;

		int noActionTakenRemark = 0;
		Integer pendingRemark = 0;
		Integer actionTakenRemark = 0;

		int totalEvent = 0;

		Map<String, List<Event>> categorizeEventsByDlNo = categorizeEventsByDlNo(allEvents);
		for (String dlNumber : categorizeEventsByDlNo.keySet()) {

			List<Event> list = categorizeEventsByDlNo.get(dlNumber);
			if (list.size() >= numberOfEvent) {
				countDefaulterDriver++;
			}

		}
		Map<Long, Event> lastEventByDevice = getLastEventByDevice(allEvents, allDeviceID);

		for (Long x : lastEventByDevice.keySet()) {
			Event event = lastEventByDevice.get(x);

			if (event.getEventType().name().equals(EventType.POWER_CUT.name())) {
				tamperedDevices++;
			}

		}

		for (Event event : allEvents) {

			EventType eventType = event.getEventType();

			if (!event.getEventType().equals(EventType.POWER_CUT)) {

				totalEvent++;

				if (event.getRemarkId() == 1) {
					pendingRemark++;
				} else if (event.getRemarkId() == 2) {
					actionTakenRemark++;
				} else {
					noActionTakenRemark++;

				}
			}

			if (eventType != null) {
				if (eventType == EventType.YAWNING) {
					yawningCount++;
				} else if (eventType == EventType.MOBILE_USE) {
					mobileUsageCount++;
				} else if (eventType == EventType.DISTRACTION) {
					distractionCount++;
				} else if (eventType == EventType.SMOKING) {
					smokingCount++;
				} else if (eventType == EventType.CLOSE_EYES) {
					closeEyesCount++;
				} else if (eventType == EventType.NO_FACE) {
					noFaceCount++;
				} else if (eventType == EventType.LOW_HEAD) {
					lowHeadCount++;
				} else if (eventType == EventType.DRINKING) {
					drinkingCount++;
				} else {
					// handle default case (if needed)
				}

			}

		}
		EventTypeCountDto eventTypeCountDto = new EventTypeCountDto(yawningCount, mobileUsageCount, distractionCount,
				smokingCount, closeEyesCount, noFaceCount, lowHeadCount, drinkingCount, totalEvent);
		eventTypeCountDto.setTamperedDevices(tamperedDevices);
		eventTypeCountDto.setCountDefaulterDriver(countDefaulterDriver);
		eventTypeCountDto.setPendingRemark(pendingRemark);
		eventTypeCountDto.setActionTakenRemark(actionTakenRemark);
		eventTypeCountDto.setNoActionTakenRemark(noActionTakenRemark);
		return eventTypeCountDto;
	}

	public static Map<Long, Event> getLastEventByDevice(List<Event> allEvents, Set<Long> allDeviceIDs) {
		Map<Long, Event> lastEventsByDevice = new HashMap<>();

		// Filter events by device IDs and group by device ID
		Map<Long, List<Event>> eventsByDevice = allEvents.stream()
				.filter(event -> allDeviceIDs.contains(event.getDeviceId()))
				.collect(Collectors.groupingBy(Event::getDeviceId));

		// Find the last added event for each device ID
		for (Map.Entry<Long, List<Event>> entry : eventsByDevice.entrySet()) {
			List<Event> events = entry.getValue();
			events.sort(Comparator.comparing(Event::getEventServerCreateTime));
			lastEventsByDevice.put(entry.getKey(), events.get(events.size() - 1));
		}

		return lastEventsByDevice;
	}

	public static Map<String, List<Event>> categorizeEventsByDlNo(List<Event> allEvents) {
		Map<String, List<Event>> eventsByDlNo = new HashMap<>();

		for (Event event : allEvents) {
			String dlNo = event.getDlNo();
			eventsByDlNo.computeIfAbsent(dlNo, k -> new ArrayList<>()).add(event);
		}

		return eventsByDlNo;
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
	public Response<?> getEventById(Long eventId) {
		EventDto dto = new EventDto();
		Optional<Event> eventById = eventRepository.findById(eventId);
		if (eventById.isPresent() && eventById.get() != null) {
			Event event = eventById.get();
			dto = EventDto.fromEntity(event);

			if (event.getEvidencePhotos() != null) {
				dto.setEvidencePhotos(convertStringToArray(event.getEvidencePhotos()));
			}

		}
		return new Response<>("Event fetched successfully", dto, 200);

	}

	private List<String> convertStringToArray(String evidencePhotos) {

		Optional<Configuration> fileAccessUrl = configurationRepository.findByKey("FILE_EXISTIANCE_VERFICATION");
//		Optional<Configuration> fileAccessUrl = configurationRepository.findByKey("FILE_ACCESS_BASE_URL");

		if (fileAccessUrl.isPresent() && fileAccessUrl.get() != null) {
			fileUrl = fileAccessUrl.get().getValue();
		}
		String[] urlArray = evidencePhotos.split(",");
		List<String> fullUrls = new ArrayList<>();

		for (String url : urlArray) {
			fullUrls.add(fileUrl + url);
		}

		return fullUrls;
	}

	@Override
	public Response<EventTypeCountDto> fetchDashBoardCounts(String value) {

//		Optional<CredentialMaster> master = userDetailsService.getUserDetails();

		List<Event> eventList = new ArrayList<>();

		try {
			if (value != null && !value.equals("")) {
				String fromDate = "";
				String toDate = "";
				String addedFromTime = "00:00:00";
				String addedToTime = "23:59:59";
				if (value.equals(Constant.TODAY)) {
					Date todayDate = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String formattedDate = formatter.format(todayDate);
					fromDate = formattedDate + " " + addedFromTime;
					toDate = formattedDate + " " + addedToTime;
				} else if (value.equals(Constant.YESTERDAY)) {
					Calendar today = Calendar.getInstance();
					Calendar yesterday = (Calendar) today.clone();
					yesterday.add(Calendar.DAY_OF_MONTH, -1);
					Date utilYesterday = yesterday.getTime();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String formattedDate = formatter.format(utilYesterday);
					fromDate = formattedDate + " " + addedFromTime;
					toDate = formattedDate + " " + addedToTime;
				} else if (value.equals(Constant.THIS_MONTH)) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					Date startDate = calendar.getTime();
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					Date endDate = calendar.getTime();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String formattedStartDate = dateFormat.format(startDate);
					String formattedEndDate = dateFormat.format(endDate);
					fromDate = formattedStartDate + " " + addedFromTime;
					toDate = formattedEndDate + " " + addedToTime;
				} else if (value.equals(Constant.THIS_WEEK)) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
					Date startDate = calendar.getTime();
					calendar.add(Calendar.DAY_OF_WEEK, 6);
					Date endDate = calendar.getTime();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String formattedStartDate = dateFormat.format(startDate);
					String formattedEndDate = dateFormat.format(endDate);
					fromDate = formattedStartDate + " " + addedFromTime;
					toDate = formattedEndDate + " " + addedToTime;

				} else if (value.equals(Constant.LAST_MONTH)) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					calendar.add(Calendar.MONTH, -1);
					Date lastMonthStartDate = calendar.getTime();
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					Date lastMonthEndDate = calendar.getTime();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String formattedLastMonthStartDate = dateFormat.format(lastMonthStartDate);
					String formattedLastMonthEndDate = dateFormat.format(lastMonthEndDate);
					fromDate = formattedLastMonthStartDate + " " + addedFromTime;
					toDate = formattedLastMonthEndDate + " " + addedToTime;
				}

				else {
					Date todayDate = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String formattedDate = formatter.format(todayDate);
					fromDate = formattedDate + " " + addedFromTime;
					toDate = formattedDate + " " + addedToTime;
				}

				if (value.equals(Constant.TILL_TODAY)) {
					eventList = eventRepository.findAll();
				} else {
					eventList = eventRepository.findEventsBetweenDates(fromDate, toDate);
				}
			} else {
				eventList = eventRepository.findAll();
			}

			long yawningCount = 0;
			long mobileUsageCount = 0;
			long distractionCount = 0;
			long smokingCount = 0;
			long closeEyesCount = 0;
			long noFaceCount = 0;
			long lowHeadCount = 0;
			long drinkingCount = 0;
			long totalEventCount = 0;
			int totalActiveVehicle = 0;
			int totalInActiveVehicle = 0;

			List<Vehicle> allVehicle = vehicleRepository.findAll();
//			Set<Long> collectDeviceId = allVehicle.stream().map(vehicle -> vehicle.getDeviceId().longValue())
//					.collect(Collectors.toSet());

//			String deviceInformation = getDeviceInformtaion(collectDeviceId);

//			String deviceInformation="";
//			Map<Long, DeviceInformationDto> retrieveDeviceInfoMap = new HashMap<>();
//			if (deviceInformation != null && !deviceInformation.equals("")) {
//				retrieveDeviceInfoMap = retrieveDeviceInfoMap(deviceInformation);
//
//				List<DeviceInformationDto> listOfDeviceInformation = new ArrayList<>(retrieveDeviceInfoMap.values());
//				for (DeviceInformationDto deviceInformationDto : listOfDeviceInformation) {
//					if (deviceInformationDto.getStatus().equals("online")) {
//						totalActiveVehicle++;
//					} else {
//						totalInActiveVehicle++;
//					}
//
//				}
//
//			}

			Set<Long> vechileList = new HashSet<>();

			Map<String, Integer> vechileEventCountMap = new HashMap<>();
			if (eventList != null && eventList.size() > 0) {
				for (Event event : eventList) {
					if (!event.getEventType().equals(EventType.POWER_CUT)) {
						totalEventCount++;
						String vehicleNo = event.getVehicleNo();

						if (vechileEventCountMap == null || vechileEventCountMap.get(vehicleNo) == null) {
							vechileEventCountMap.putIfAbsent(vehicleNo, 0);
						} else {
							vechileEventCountMap.put(vehicleNo, vechileEventCountMap.get(vehicleNo) + 1);
						}

						vechileList.add(event.getVehicleID());

						EventType eventType = event.getEventType();
						if (eventType != null) {
							if (eventType.equals(EventType.YAWNING)) {
								yawningCount++;
							} else if (eventType.equals(EventType.MOBILE_USE)) {
								mobileUsageCount++;
							} else if (eventType.equals(EventType.DISTRACTION)) {
								distractionCount++;
							} else if (eventType.equals(EventType.SMOKING)) {
								smokingCount++;
							} else if (eventType.equals(EventType.CLOSE_EYES)) {
								closeEyesCount++;
							} else if (eventType.equals(EventType.NO_FACE)) {
								noFaceCount++;
							} else if (eventType.equals(EventType.LOW_HEAD)) {
								lowHeadCount++;
							} else if (eventType.equals(EventType.DRINKING)) {
								drinkingCount++;
							} else {
								// handle default case (if needed)
							}

						}
					}
				}
			}

			EventTypeCountDto eventTypeCountDto = new EventTypeCountDto(yawningCount, mobileUsageCount,
					distractionCount, smokingCount, closeEyesCount, noFaceCount, lowHeadCount, drinkingCount,
					totalEventCount);
			eventTypeCountDto.setTotalActiveVehicle(totalActiveVehicle);
			eventTypeCountDto.setTotalInActiveVehicle(totalInActiveVehicle);
			eventTypeCountDto.setTotalVehicle(allVehicle.size());
			eventTypeCountDto.setVehicleEventCountMap(vechileEventCountMap);
			eventTypeCountDto.setVehiclesEngaged(vechileList.size());

			return new Response<>("Events Counts fetched successfully", eventTypeCountDto, 200);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Something Went Wrong", null, 400);

		}
	}

	@Override
	public Response<?> getAllEventType() {
//		List<EventType> eventTypeList = Arrays.asList(EventType.values());
		List<EventType> eventTypeList = Stream.of(EventType.values()).filter(event -> event != EventType.POWER_CUT)
				.collect(Collectors.toList());
		return new Response<>("All event types fetched successfully", eventTypeList, 200);
	}

	@Override
	public Response<?> updateEvent(EventDto eventDto) {

		Response<?> checkEventUpdateDto = Validation.checkEventUpdateDto(eventDto);
		if (checkEventUpdateDto != null) {
			return checkEventUpdateDto;

		}

		Optional<Remark> findByIdRemark = remarkRepository.findById(eventDto.getRemarkId());
		if (!findByIdRemark.isPresent() && findByIdRemark.get() == null) {
			return new Response<>("The remark id must be Valid ", null, 400);
		}

		if (eventDto.getRemark() == null) {
			return new Response<>("The remark Is requried", null, 400);

		}

		Remark remark = findByIdRemark.get();

		Optional<Event> eventFindById = eventRepository.findById(eventDto.getId());
		if (eventFindById.isPresent() && eventFindById.get() != null) {
			Event event = eventFindById.get();
			if (eventDto.getRemark() != null) {
				event.setRemark(eventDto.getRemark());
				event.setRemarkId(remark.getId());
				eventRepository.save(event);

			}

		}

		return new Response<>("Event Update Successfully", null, 200);
	}

	@Override
	public Response<?> getEventDetalisForDashBoard() {

		return null;
	}

	@Override
	public Response<?> getEventDetalisForDriverPerfomance(String value, String dlNumber) {
		String fromDate = "";
		String toDate = "";
		String addedFromTime = "00:00:00";
		String addedToTime = "23:59:59";

		String fromPriviosStartDateTime = "";
		String toPriviosEndDateTime = "";

		String fromCurrentStartDateTime = "";
		String toCurrentEndDateTime = "";

		if (value == null) {
			return new Response<>("Value Must Be required", null, 400);
		}

		try {

			if (value.equals(Constant.LAST_TWO_MONTHS)) {
				Calendar calendar = Calendar.getInstance();

				// Previous Month Start Date
				calendar.add(Calendar.MONTH, -1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				Date previousMonthStartDate = calendar.getTime();

				// Previous Month End Date
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				Date previousMonthEndDate = calendar.getTime();

				// Current Month Start Date
				calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				Date currentMonthStartDate = calendar.getTime();

				// Current Month End Date
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				Date currentMonthEndDate = calendar.getTime();

				// Formatting dates
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String formattedPreviousMonthStartDate = dateFormat.format(previousMonthStartDate);
				String formattedPreviousMonthEndDate = dateFormat.format(previousMonthEndDate);
				String formattedCurrentMonthStartDate = dateFormat.format(currentMonthStartDate);
				String formattedCurrentMonthEndDate = dateFormat.format(currentMonthEndDate);

				fromPriviosStartDateTime = formattedPreviousMonthStartDate + " " + addedFromTime;
				toPriviosEndDateTime = formattedPreviousMonthEndDate + " " + addedToTime;

				fromCurrentStartDateTime = formattedCurrentMonthStartDate + " " + addedFromTime;
				toCurrentEndDateTime = formattedCurrentMonthEndDate + " " + addedToTime;

				fromDate = formattedPreviousMonthStartDate + " " + addedFromTime;
				toDate = formattedCurrentMonthEndDate + " " + addedToTime;

			} else if (value.equals(Constant.LAST_TWO_WEEK)) {

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.WEEK_OF_YEAR, -1); // Move to last week
				calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Move to first day of last week
				Date previousWeekStartDate = calendar.getTime();
				calendar.add(Calendar.DAY_OF_WEEK, 6); // Move to last day of last week
				Date previousWeekEndDate = calendar.getTime();
				calendar.add(Calendar.DAY_OF_WEEK, 1); // Move to first day of current week
				Date currentWeekStartDate = calendar.getTime();
				calendar.add(Calendar.DAY_OF_WEEK, 6); // Move to last day of current week
				Date currentWeekEndDate = calendar.getTime();

				String formattedPreviousWeekStartDate = dateFormat.format(previousWeekStartDate);
				String formattedPreviousWeekEndDate = dateFormat.format(previousWeekEndDate);

				String formattedCurrentWeekStartDate = dateFormat.format(currentWeekStartDate);
				String formattedCurrentWeekEndDate = dateFormat.format(currentWeekEndDate);

				fromPriviosStartDateTime = formattedPreviousWeekStartDate + " " + addedFromTime;
				toPriviosEndDateTime = formattedPreviousWeekEndDate + " " + addedToTime;

				fromCurrentStartDateTime = formattedCurrentWeekStartDate + " " + addedFromTime;
				toCurrentEndDateTime = formattedCurrentWeekEndDate + " " + addedToTime;

				fromDate = formattedPreviousWeekStartDate + " " + addedFromTime;
				toDate = formattedCurrentWeekEndDate + " " + addedToTime;

			} else if (value.equals(Constant.LAST_TWO_DAYS)) {

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				Date todayEndDate = calendar.getTime();
				String formattedTodayEndDate = dateFormat.format(todayEndDate);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				Date todayStartDate = calendar.getTime();
				String formattedTodayStartDate = dateFormat.format(todayStartDate);
				calendar.add(Calendar.DAY_OF_YEAR, -1);
				Date yesterdayEndDate = calendar.getTime();
				String formattedYesterdayEndDate = dateFormat.format(yesterdayEndDate);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				Date yesterdayStartDate = calendar.getTime();
				String formattedYesterdayStartDate = dateFormat.format(yesterdayStartDate);

				fromPriviosStartDateTime = formattedYesterdayStartDate + " " + addedFromTime;
				toPriviosEndDateTime = formattedYesterdayEndDate + " " + addedToTime;
				fromCurrentStartDateTime = formattedTodayStartDate + " " + addedFromTime;
				toCurrentEndDateTime = formattedTodayEndDate + " " + addedToTime;
				fromDate = formattedYesterdayStartDate + " " + addedFromTime;
				toDate = formattedTodayEndDate + " " + addedToTime;

			} else {

				return new Response<>("Provide The Valid Data in  Value Key ", null, 400);
			}

			List<Event> findEventsBetweenDates = new ArrayList<>();
			if (dlNumber == null || dlNumber.isEmpty()) {
				findEventsBetweenDates = eventRepository.findEventsBetweenDates(fromDate, toDate);
			} else {
				findEventsBetweenDates = eventRepository.findEventsBetweenDatesAndDriver(fromDate, toDate, dlNumber);
			}

			Map<String, Integer> twoDateBetweenEvent = getDataBetweenTwoDates(findEventsBetweenDates,
					fromPriviosStartDateTime, toPriviosEndDateTime, fromCurrentStartDateTime, toCurrentEndDateTime);

			Map<String, List<Event>> categorizeEventsByDlNo = categorizeEventsByDlNo(findEventsBetweenDates);

			List<DriverPerformanceDto> driverPerformanceDto = new ArrayList<>();

			for (String driver : categorizeEventsByDlNo.keySet()) {

				DriverPerformanceDto obj = new DriverPerformanceDto();

				List<Event> list = categorizeEventsByDlNo.get(driver);
				Map<String, Integer> countEventsByMonth = countEventsByRange(list, twoDateBetweenEvent,
						fromPriviosStartDateTime, toPriviosEndDateTime, fromCurrentStartDateTime, toCurrentEndDateTime);

				obj.setDriverName(list.get(0).getDriverName() + " (" + driver + ")");
				for (Map.Entry<String, Integer> rangeCountByDriver : countEventsByMonth.entrySet()) {
					String key = rangeCountByDriver.getKey();

					if (key.equals("previousRangeCount")) {
						obj.setPreviousRangeCount(rangeCountByDriver.getValue());
					}
					if (key.equals("currentRangeCount")) {
						obj.setCurrentRangeCount(rangeCountByDriver.getValue());
					}

				}

				driverPerformanceDto.add(obj);

//				driverEvenntCountMonth.put(list.get(0).getDriverName() + " (" + dlNumber + ")", countEventsByMonth);

			}

			return new Response<>("Success", driverPerformanceDto, 200);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Some Thing Went Wrong", null, 400);
		}
	}

	private Map<String, Integer> countEventsByRange(List<Event> eventList, Map<String, Integer> twoDateBetweenEvent,
			String fromPriviosStartDateTime, String toPriviosEndDateTime, String fromCurrentStartDateTime,
			String toCurrentEndDateTime) {

		Map<String, Integer> eventCountByMonth = new HashMap<>();

		for (Event event : eventList) {

			String key = getRangeCount(event.getEventServerCreateTime(), fromPriviosStartDateTime, toPriviosEndDateTime,
					fromCurrentStartDateTime, toCurrentEndDateTime);

			eventCountByMonth.put(key, eventCountByMonth.getOrDefault(key, 0) + 1);
		}

		Map<String, Integer> eventCountByMonthWise = new HashMap<>();

		for (String key : eventCountByMonth.keySet()) {

//			Integer baseMonthEvent = twoDateBetweenEvent.get(key);
			Integer actualEvent = eventCountByMonth.get(key);

//			eventCountByMonthWise.put(key, calculatePerformance(actualEvent, baseMonthEvent));
			eventCountByMonthWise.put(key, actualEvent);

		}

		return eventCountByMonthWise;

	}

	public Map<String, Integer> getDataBetweenTwoDates(List<Event> findEventsBetweenDates,
			String fromPreviousStartDateTime, String toPreviousEndDateTime, String fromCurrentStartDateTime,
			String toCurrentEndDateTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date fromPreviousStartDate = dateFormat.parse(fromPreviousStartDateTime);
			Date toPreviousEndDate = dateFormat.parse(toPreviousEndDateTime);
			Date fromCurrentStartDate = dateFormat.parse(fromCurrentStartDateTime);
			Date toCurrentEndDate = dateFormat.parse(toCurrentEndDateTime);
			long previousRangeCount = findEventsBetweenDates.stream().filter(
					event -> isWithinRange(event.getEventServerCreateTime(), fromPreviousStartDate, toPreviousEndDate))
					.count();

			long currentRangeCount = findEventsBetweenDates.stream().filter(
					event -> isWithinRange(event.getEventServerCreateTime(), fromCurrentStartDate, toCurrentEndDate))
					.count();
			Map<String, Integer> result = new HashMap<>();
			result.put("previousRangeCount", (int) previousRangeCount);
			result.put("currentRangeCount", (int) currentRangeCount);

			return result;

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean isWithinRange(Date eventDate, Date startDate, Date endDate) {
		return eventDate != null && !eventDate.before(startDate) && !eventDate.after(endDate);
	}

	public String getRangeCount(Date serverCreateTime, String fromPreviousStartDateTime, String toPreviousEndDateTime,
			String fromCurrentStartDateTime, String toCurrentEndDateTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date fromPreviousStartDate = dateFormat.parse(fromPreviousStartDateTime);
			Date toPreviousEndDate = dateFormat.parse(toPreviousEndDateTime);
			Date fromCurrentStartDate = dateFormat.parse(fromCurrentStartDateTime);
			Date toCurrentEndDate = dateFormat.parse(toCurrentEndDateTime);
			if (isWithinRange(serverCreateTime, fromPreviousStartDate, toPreviousEndDate)) {
				return "previousRangeCount";
			}

			if (isWithinRange(serverCreateTime, fromCurrentStartDate, toCurrentEndDate)) {
				return "currentRangeCount";
			}

			return "outOfRange";

		} catch (ParseException e) {
			e.printStackTrace();
			return "error";
		}
	}

	public static Double calculatePerformance(int actualEvents, int baseMonthEvents) {
		if (baseMonthEvents == 0) {
			throw new IllegalArgumentException("baseMonthEvents cannot be zero");
		}

		double performance = ((double) actualEvents / baseMonthEvents) * 100;
		DecimalFormat df = new DecimalFormat("#0.00");

		return Double.valueOf(df.format(performance));
	}

}
