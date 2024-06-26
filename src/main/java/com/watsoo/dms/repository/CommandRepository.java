package com.watsoo.dms.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watsoo.dms.dto.PaginatedRequestDto;
import com.watsoo.dms.entity.Command;

import javax.persistence.criteria.Predicate;

public interface CommandRepository extends JpaRepository<Command, Long> {

	@Query("SELECT c FROM Command c WHERE c.vehicleNumber IN :vehicleNumbers AND c.description = :description")
	List<Command> findCommandsByVehicleNumbers(@Param("vehicleNumbers") Set<String> vehicleNumbers,
			@Param("description") String description);

	Command findByBaseCommandAndVechileId(String command, Long vechileId);

	public static Specification<Command> search(PaginatedRequestDto paginatedRequest) {
		return (root, cq, cb) -> {
			Predicate predicate = cb.conjunction();

			if (paginatedRequest.getDeviceModel() != null) {
				predicate = cb.and(predicate,
						cb.equal(root.get("ddevicModelNumber"), paginatedRequest.getDeviceModel()));
			}
			if (paginatedRequest.getVechileId() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("vechileId"), paginatedRequest.getVechileId()));
			}

			if (paginatedRequest.getImeiNumber() != null) {

				predicate = cb.and(predicate, cb.equal(root.get("imeiNumber"), paginatedRequest.getImeiNumber()));
			}
			return predicate;
		};
	}

	Page<Command> findAll(Specification<Command> parentData, Pageable pageable);

	default Page<Command> findAll(PaginatedRequestDto paginatedRequest, Pageable pageable) throws Exception {
		try {
			return findAll(search(paginatedRequest), pageable);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error occurred while fetching data.");

		}
	}

}
