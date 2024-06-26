package com.watsoo.dms.repository;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watsoo.dms.dto.PaginatedRequestDto;
import com.watsoo.dms.entity.Event;
import com.watsoo.dms.enums.EventType;

import javax.persistence.criteria.Predicate;

public interface EventRepository extends JpaRepository<Event, Long> {

	@Query(value = "SELECT * FROM event e WHERE DATE(e.event_server_create_time) BETWEEN :startDate AND :endDate AND e.event_type != 'POWER_CUT'", nativeQuery = true)
	List<Event> findEventsBetweenDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

	@Query(value = "SELECT COUNT(id) FROM event e WHERE e.event_type <> :excludedEventType", nativeQuery = true)
	long countEventsExcludingType(@Param("excludedEventType") String excludedEventType);

	@Query(value = "SELECT * FROM event e WHERE DATE(e.event_server_create_time) BETWEEN :startDate AND :endDate AND e.dl_number = :dlNumber AND e.event_type != 'POWER_CUT'", nativeQuery = true)
	List<Event> findEventsBetweenDatesAndDriver(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("dlNumber") String dlNumber);

	List<Event> findByDriverId(Long driverId);

	@Query(value = "SELECT * FROM event WHERE position_id = ?1 AND event_type = ?2", nativeQuery = true)
	Event findEventsByPositionIdAndEventType(Long positionId, String eventType);

	@Query(value = "SELECT COUNT(*) FROM event WHERE driver_id = :driverId", nativeQuery = true)
	Long countByDriverId(@Param("driverId") Long driverId);

	public static Specification<Event> search(PaginatedRequestDto paginatedRequest) {
		return (root, cq, cb) -> {
			Predicate predicate = cb.conjunction();

			predicate = cb.and(predicate, cb.notEqual(root.get("eventType"), EventType.valueOf("POWER_CUT")));

			if (paginatedRequest.getFromDate() != null && paginatedRequest.getToDate() != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
				try {
					ZonedDateTime fromDate = ZonedDateTime.parse(paginatedRequest.getFromDate(), formatter);
					ZonedDateTime toDate = ZonedDateTime.parse(paginatedRequest.getToDate(), formatter);

					Date from = Date.from(fromDate.toInstant());
					Date to = Date.from(toDate.toInstant());
					predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("eventServerCreateTime"), from),
							cb.lessThanOrEqualTo(root.get("eventServerCreateTime"), to));
				} catch (DateTimeParseException e) {

					e.printStackTrace();
				}
			}

			if (paginatedRequest.getDlNumber() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("dlNo"), paginatedRequest.getDlNumber()));
			}

			if (paginatedRequest.getRemarkId() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("remarkId"), paginatedRequest.getRemarkId()));
			}

			if (paginatedRequest.getDriverName() != null && paginatedRequest.getDriverName().trim() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("driverName"), paginatedRequest.getDriverName()));
			}
			if (paginatedRequest.getVehicleNo() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("vehicleNo"), paginatedRequest.getVehicleNo()));
			}
			if (paginatedRequest.getEventType() != null

					&& !paginatedRequest.getEventType().trim().equals("")

					&& !paginatedRequest.getEventType().equals(EventType.ALL.name())) {
				try {
					EventType eventType = EventType.valueOf(paginatedRequest.getEventType());
					predicate = cb.and(predicate, cb.equal(root.get("eventType"), eventType));
				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
				}
			}

			if (paginatedRequest.getSearchKey() != null && !paginatedRequest.getSearchKey().equals("")) {
				Predicate searchPredicate = cb.or(
						cb.like(root.get("driverName"), "%" + paginatedRequest.getSearchKey() + "%"),
						cb.like(root.get("vehicleNo"), "%" + paginatedRequest.getSearchKey() + "%"),
						cb.like(root.get("remark"), "%" + paginatedRequest.getSearchKey() + "%"),
						cb.like(root.get("dlNo"), "%" + paginatedRequest.getSearchKey() + "%"));

				predicate = cb.and(predicate, searchPredicate);
			}

			cq.orderBy(cb.desc(root.get("eventServerCreateTime")));

			return predicate;
		};
	}

	Page<Event> findAll(Specification<Event> parentData, Pageable pageable);

	default Page<Event> findAll(PaginatedRequestDto paginatedRequest, Pageable pageable) throws Exception {
		try {
			return findAll(search(paginatedRequest), pageable);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error occurred while fetching data.");

		}
	}

}
