package com.watsoo.dms.service;

import com.watsoo.dms.dto.DriverDto;
import com.watsoo.dms.dto.Response;

public interface DriverService {

	Response<?> saveDriver(DriverDto driverDto);

	Response<?> getDriverById(Long driverId);

	Response<?> getAllDriversWithPerfomance(String fromDate, String toDate, Integer pageSize, Integer pageNo,
			String dlNumber, String eventType);


	Response<?> getAllDrivers(Integer pageSize, Integer pageNo, String dlNumber);

	Response<?> getDriverEventsCount(Long driverId);

}
