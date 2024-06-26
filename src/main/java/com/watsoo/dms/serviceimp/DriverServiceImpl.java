package com.watsoo.dms.serviceimp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.watsoo.dms.dto.CategoryDto;
import com.watsoo.dms.dto.DriverDto;
import com.watsoo.dms.dto.DriverPerformanceDto;
import com.watsoo.dms.dto.PaginatedRequestDto;
import com.watsoo.dms.dto.PaginatedResponseDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.entity.Category;
import com.watsoo.dms.entity.Driver;
import com.watsoo.dms.entity.Event;
import com.watsoo.dms.repository.CategoryRepository;
import com.watsoo.dms.repository.DriverRepository;
import com.watsoo.dms.repository.EventRepository;
import com.watsoo.dms.service.DriverService;

@Service
public class DriverServiceImpl implements DriverService {

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Response<?> saveDriver(DriverDto driverDto) {

		Driver convertDtoToEntity = DriverDto.convertDtoToEntity(driverDto);

		Driver save = driverRepository.save(convertDtoToEntity);

		return new Response<>("Driver Add Succesfully", DriverDto.convertEntityToDto(save), HttpStatus.OK.value());
	}

	@Override
	public Response<?> getDriverById(Long driverId) {

		try {
			if (driverId == null) {
				return new Response<>("Driver Id required ", null, 400);
			}

			Optional<Driver> findById = driverRepository.findById(driverId);
			if (findById.isEmpty() || findById.get() == null) {
				return new Response<>("Driver not found ", null, 400);
			}

			List<Category> allCatagory = categoryRepository.findAll();

			Driver driver = findById.get();
			Long eventListBydriverId = 0L;
			eventListBydriverId = eventRepository.countByDriverId(driverId);

			DriverDto convertEntityToDto = DriverDto.convertEntityToDto(driver);

			Category category = findEventRangeToCalculateCategory(allCatagory, eventListBydriverId.intValue());

			if (category != null) {

				convertEntityToDto.setCategoryDto(CategoryDto.toDTO(category));
			}

			convertEntityToDto.setTotalEvent(eventListBydriverId);

			return new Response<>("Success ", convertEntityToDto, 200);
		} catch (Exception e) {
			return new Response<>("Something went wrong ", null, 400);

		}
	}

	@Override
	public Response<?> getAllDriversWithPerfomance(String fromDate, String toDate, Integer pageSize, Integer pageNo,
			String dlNumber, String eventType) {

		try {

			PaginatedRequestDto paginatedRequest = new PaginatedRequestDto(pageSize, pageNo, dlNumber, eventType,
					fromDate, toDate);
			Pageable pageable = pageSize > 0 ? PageRequest.of(pageNo, pageSize) : Pageable.unpaged();
			Page<Event> findAllEvents = eventRepository.findAll(paginatedRequest, pageable);

			List<Event> allEvents = findAllEvents.getContent();
			Map<Long, List<Event>> eventsByDriverId = allEvents.stream()
					.collect(Collectors.groupingBy(Event::getDriverId));

			List<Driver> driverList = new ArrayList<>();

			if (dlNumber == null || dlNumber.trim().equals("")) {
				driverList = driverRepository.findByIsActiveTrue();
			} else {
				Driver driverByName = driverRepository.findByDlNumber(dlNumber);
				if (driverByName == null) {
					return new Response<>("Driver Not Found", null, 400);
				}

				driverList.add(driverByName);

			}

			List<Category> allCatagory = categoryRepository.findAll();

			List<DriverPerformanceDto> driverPerformanceDtos = new ArrayList<>();
			for (Driver driver : driverList) {

				DriverPerformanceDto obj = new DriverPerformanceDto();

				List<Event> listOFEvents = eventsByDriverId.get(driver.getId());

				int totalEventByDriver = 0;
				if (listOFEvents != null && listOFEvents.size() > 0) {
					totalEventByDriver = listOFEvents.size();

				}

				obj.setPerfomance(totalEventByDriver);
				Category category = findEventRangeToCalculateCategory(allCatagory, totalEventByDriver);

				if (category != null) {
					obj.setCategoryDto(CategoryDto.toDTO(category));
				}

				obj.setDriverName(driver.getName());
				obj.setDriverDlNumber(driver.getDlNumber());
				obj.setJoinDate(driver.getJoinDate());
				obj.setDriverPhone(driver.getPhoneNumber());

				driverPerformanceDtos.add(obj);

			}

//			long totalElement, long itemsPerPage, long totalPage, long pageNo, T data

			PaginatedResponseDto<Object> paginatedResponseDto = new PaginatedResponseDto<>(driverRepository.count(),
					driverPerformanceDtos.size(), findAllEvents.getTotalPages(), pageNo, driverPerformanceDtos);

			return new Response<>("Succes To fetch Driver Performance", paginatedResponseDto, 200);

		} catch (Exception e) {

			return new Response<>("Something went wrong", null, 400);

		}
	}

	public Category findEventRangeToCalculateCategory(List<Category> allCategories, int totalEventByDriver) {
		Category worstCategory = null;

		for (Category category : allCategories) {
			if (category.getMin() == null && category.getMax() == null) {
				worstCategory = category;
			} else if (category.getMin() != null && category.getMax() != null) {
				if (totalEventByDriver >= category.getMin() && totalEventByDriver <= category.getMax()) {
					return category;
				}
			} else if (category.getMin() != null && category.getMax() == null) {
				if (totalEventByDriver >= category.getMin()) {
					return category;
				}

			}
		}

		return worstCategory;
	}

	@Override
	public Response<?> getAllDrivers(Integer pageSize, Integer pageNo, String dlNumber) {

		try {

			PaginatedRequestDto paginatedRequest = new PaginatedRequestDto(pageSize, pageNo, dlNumber);
			Pageable pageable = pageSize > 0 ? PageRequest.of(pageNo, pageSize) : Pageable.unpaged();
			Page<Driver> findAllEvent = driverRepository.findAll(paginatedRequest, pageable);
			List<Driver> drivers = findAllEvent.getContent();
			List<DriverDto> driverDtos = drivers.stream().map(DriverDto::convertEntityToDto)
					.collect(Collectors.toList());

			return new Response<>("Succes To fetch Driver Performance", driverDtos, 200);
		} catch (Exception e) {
			return new Response<>("Something went wrong ", null, 400);
		}
	}

	@Override
	public Response<?> getDriverEventsCount(Long driverId) {

		try {
			if (driverId == null) {
				return new Response<>("Driver Id required ", null, 400);
			}

			Optional<Driver> findById = driverRepository.findById(driverId);
			if (findById.isEmpty() || findById.get() == null) {
				return new Response<>("Driver not found ", null, 400);
			}

			List<Category> allCatagory = categoryRepository.findAll();

			Map<String, Object> driversEventCount = new HashMap<>();

			Driver driver = findById.get();
			Long eventListBydriverId = 0L;
			eventListBydriverId = eventRepository.countByDriverId(driverId);

			Category category = findEventRangeToCalculateCategory(allCatagory, eventListBydriverId.intValue());

			if (category != null) {
				driversEventCount.put("driverRating", category);
			}

			driversEventCount.put("totalEvent", eventListBydriverId);
			driversEventCount.put("dateOfJoin", driver.getJoinDate());

			return new Response<>("Success ", driversEventCount, 200);
		} catch (Exception e) {
			return new Response<>("Something went wrong ", null, 400);

		}
	}

}
