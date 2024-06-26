package com.watsoo.dms.service;

import com.watsoo.dms.dto.Response;

public interface VehicleService {

	Response<?> getAllVehicle(int pageSize, int pageNo, String vehicleNumber, String vehicleName, String imeiNumber);

	

}
