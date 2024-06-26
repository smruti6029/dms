package com.watsoo.dms.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watsoo.dms.dto.PaginatedRequestDto;
import com.watsoo.dms.entity.Driver;
import com.watsoo.dms.entity.Event;
import com.watsoo.dms.enums.EventType;

import javax.persistence.criteria.Predicate;

public interface DriverRepository extends JpaRepository<Driver, Long> {
	List<Driver> findByIsActiveTrue();

	Driver findByDlNumber(String name);

	@Query(value = "SELECT COUNT(id) FROM driver e WHERE e.is_active :", nativeQuery = true)
	long countEventsExcludingType(@Param("excludedEventType") String excludedEventType);

	public static Specification<Driver> search(PaginatedRequestDto paginatedRequest) {
		return (root, cq, cb) -> {
			Predicate predicate = cb.conjunction();

			if (paginatedRequest.getDlNumber() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("dlNumber"), paginatedRequest.getDlNumber()));
			}
			return predicate;
		};
	}

	Page<Driver> findAll(Specification<Driver> parentData, Pageable pageable);

	default Page<Driver> findAll(PaginatedRequestDto paginatedRequest, Pageable pageable) throws Exception {
		try {
			return findAll(search(paginatedRequest), pageable);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error occurred while fetching data.");

		}
	}

}
