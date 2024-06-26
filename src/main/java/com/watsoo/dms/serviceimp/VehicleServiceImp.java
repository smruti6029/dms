package com.watsoo.dms.serviceimp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.watsoo.dms.dto.PaginatedRequestDto;
import com.watsoo.dms.dto.PaginatedResponseDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.dto.VehicleDto;
import com.watsoo.dms.entity.Vehicle;
import com.watsoo.dms.repository.VehicleRepository;
import com.watsoo.dms.service.VehicleService;

@Service
public class VehicleServiceImp implements VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;

	public Response<?> getAllVehicle(int pageSize, int pageNo, String vehicleNumber, String vehicleName,
			String imeiNumber) {
		try {
			PaginatedRequestDto paginatedRequest = new PaginatedRequestDto(pageSize, pageNo, vehicleNumber,
					vehicleName,imeiNumber);	

			Pageable pageable = pageSize > 0 ? PageRequest.of(pageNo, pageSize) : Pageable.unpaged();

			Page<Vehicle> findAllVehicle = vehicleRepository.findAll(paginatedRequest, pageable);
			List<Vehicle> allVehicles = findAllVehicle.getContent();

			List<VehicleDto> vehicleDtoList = allVehicles.stream().map(VehicleDto::fromEntity)
					.collect(Collectors.toList());

			PaginatedResponseDto<Object> paginatedResponseDto = new PaginatedResponseDto<>(vehicleRepository.count(),
					allVehicles.size(), findAllVehicle.getTotalPages(), pageNo, vehicleDtoList);

			return new Response<>("Success", paginatedResponseDto, 200);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Error occurred while processing vehicle list", null, 400);
		}
	}
}
