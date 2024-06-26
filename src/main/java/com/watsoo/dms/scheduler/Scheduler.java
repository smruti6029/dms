package com.watsoo.dms.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.watsoo.dms.entity.Configuration;
import com.watsoo.dms.entity.Event;
import com.watsoo.dms.entity.Vehicle;
import com.watsoo.dms.repository.ConfigurationRepository;
import com.watsoo.dms.repository.VehicleRepository;
import com.watsoo.dms.restclient.RestClientService;
import com.watsoo.dms.service.CommandSendDetalisService;
import com.watsoo.dms.service.EventService;
import com.watsoo.dms.service.FileUploadDetailsService;

@Component
@EnableScheduling
public class Scheduler {

	@Autowired
	private RestClientService restClientService;

	@Autowired
	private EventService eventService;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Autowired
	private CommandSendDetalisService commandSendDetalisService;

	@Autowired
	private FileUploadDetailsService fileUploadDetailsService;

//	List<Configuration> allConfigurations = new ArrayList<>();
//
//	Map<String, String> allConfigurationKey = new ConcurrentHashMap<>();

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

//	private static final ReentrantLock lock = new ReentrantLock();

	String fromTime = "2024-05-29T18:30:00.000Z";
	String toTime = null;

	int schedulerTime = 10;

	Date currentTime = new Date();

//	boolean firstCall = true;;
//
//	boolean processEvent = false;
//
//	boolean processCommand = false;
//
//	boolean processCommandResponse = false;
//
//	boolean processCheckFiles = false;

	private AtomicBoolean firstCall = new AtomicBoolean(true);
	private AtomicBoolean processEvent = new AtomicBoolean(false);
	private AtomicBoolean processCommand = new AtomicBoolean(false);
	private AtomicBoolean processCommandResponse = new AtomicBoolean(false);
	private AtomicBoolean processCheckFiles = new AtomicBoolean(false);

	Logger logger = LoggerFactory.getLogger(Scheduled.class);

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

	@Scheduled(fixedRate = 1000, initialDelay = 2000)
	public void startScheduler() {
//		scheduler.scheduleAtFixedRate(this::processEvent, 0, 2, TimeUnit.SECONDS);
		scheduler.scheduleAtFixedRate(this::processCommandSend, 0, 2, TimeUnit.SECONDS);
//		scheduler.scheduleAtFixedRate(this::processCheckFiles, 0, 3, TimeUnit.SECONDS);
//		scheduler.scheduleAtFixedRate(this::processCommandResponse, 0, 10, TimeUnit.SECONDS);
	}

	@Scheduled(cron = "*/2 * * * * *")
	public void processEvent() {
//		if (lock.tryLock()) {

		if (!processEvent.get()) {
			processEvent.set(true);

			try {
				LocalDateTime now = LocalDateTime.now();
				toTime = now.format(DATE_TIME_FORMATTER);

				if (fromTime != null && ChronoUnit.SECONDS.between(LocalDateTime.parse(fromTime, DATE_TIME_FORMATTER),
						now) >= schedulerTime) {


					Optional<Configuration> schedulerTimeCOnfig = configurationRepository
							.findByKey("EVENT_SCHEDULER_TIME");
					if (schedulerTimeCOnfig.isPresent() && !schedulerTimeCOnfig.isEmpty()) {

						schedulerTime = Integer.parseInt(schedulerTimeCOnfig.get().getValue());
					}

					List<Vehicle> vehicles = vehicleRepository.findAll();
					List<Integer> vehiclesDeviceIds = vehicles.stream().map(Vehicle::getDeviceId)
							.collect(Collectors.toList());
					if (vehiclesDeviceIds != null && vehiclesDeviceIds.size() > 0) {
						Optional<Configuration> eventFromTime = configurationRepository.findByKey("EVENT_FROM_TIME");
						if (eventFromTime.isPresent() && eventFromTime.get().getValue() != null) {
							fromTime = eventFromTime.get().getValue();
						}
						String events = restClientService.fetchEventDataFromExternalService(vehiclesDeviceIds, "alarm",
								fromTime, toTime);
						if (events != null) {
							Event saveEvent = eventService.saveEvent(events, vehicles);
							if (eventFromTime.isPresent() && saveEvent != null
									&& saveEvent.getEventServerCreateTime() != null) {
								Configuration configuration = eventFromTime.get();
								if (saveEvent != null) {
									Date date = saveEvent.getEventServerCreateTime();

									// Convert Date to LocalDateTime
									LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(),
											ZoneId.systemDefault());

									// Add one millisecond
									localDateTime = localDateTime.plusNanos(1_000_000); // Adding one millisecond in
																						// nanoseconds

									// Convert LocalDateTime back to Date
									Date updatedDate = Date
											.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

									// Update configuration with the new date-time
									configuration.setValue(updatedDate.toInstant().toString()); // Assuming
																								// configuration.setValue()
																								// expects a String

									// Save the updated configuration
									configurationRepository.save(configuration);
								}
							}
						}

					}
				}
			} catch (Exception e) {

			} finally {
				System.gc();
			}


			processEvent.set(false);
		}

	}
//		finally {
//		lock.unlock();
//	}
//}

//	@Scheduled(cron = "*/2 * * * * *")
	public void processCommandSend() {

		if (!processCommand.get()) {
			processCommand.set(true);


			try {
				int reCallCount = 0;
				Integer processCommand = 0;
				int processSleepTime = 0;


				List<Configuration> allConfigurations = configurationRepository.findAll();
				for (Configuration configuration : allConfigurations) {
					if (configuration.getKey().equals("RE_CAll_COUNT")) {
						reCallCount = Integer.valueOf(configuration.getValue());
					}
					if (configuration.getKey().equals("PROCESS_COMMAND_TIME")) {
						processCommand = Integer.valueOf(configuration.getValue());
					}
					if (configuration.getKey().equals("PROCESS_COMMAND_SLEEP_TIME")) {
						processSleepTime = Integer.valueOf(configuration.getValue());
					}
				}

				if (firstCall.get() || (new Date(currentTime.getTime() + processCommand * 1000).before(new Date()))) {
					currentTime = new Date();
					firstCall.set(false);

					commandSendDetalisService.sendCommand(reCallCount, processSleepTime);
//					fileUploadDetailsService.updateFlleDetalis(reCallCount);

				}

			} catch (Exception e) {

			} finally {
				System.gc();
			}

			processCommand.set(false);
		}

//		catch (Exception e) {
//			
//		}
	}

	@Scheduled(cron = "*/3 * * * * *")
	public void processCheckFiles() {


		try {

			if (!processCheckFiles.get()) {
				processCheckFiles.set(true);
				fileUploadDetailsService.updateFlleDetalisV2();
				processCheckFiles.set(false);
			}
		} catch (Exception e) {

		}

//		catch (Exception e) {
//			
//		}
	}

	@Scheduled(cron = "*/10 * * * * *")
	private void processCommandResponse() {

		if (!processCommandResponse.get()) {


			processCommandResponse.set(true);

			try {

				List<Configuration> allConfigurations = configurationRepository.findAll();

				Map<String, Configuration> configurationMap = allConfigurations.stream()
						.filter(config -> config.getKey() != null && config.getValue() != null)
						.collect(Collectors.toMap(Configuration::getKey, config -> config));

				Configuration batchSizeConfiguration = configurationMap.get("POSITION_BATCH_SIZE");
				Configuration positionConfigurat = configurationMap.get("POSITION_ID");

				Long updateCommandResponse = commandSendDetalisService.updateCommandResponse(
						Long.valueOf(batchSizeConfiguration.getValue()), Long.valueOf(positionConfigurat.getValue()));

				if (updateCommandResponse != null && updateCommandResponse > 0) {
					positionConfigurat.setValue(updateCommandResponse.toString());
					configurationRepository.save(positionConfigurat);
				}

			} catch (Exception e) {
			} finally {
				System.gc();
			}

			processCommandResponse.set(false);

		}

	}

}
