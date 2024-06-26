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
import com.watsoo.dms.entity.CommandSendDetails;

import javax.persistence.criteria.Predicate;

public interface CommandSendDetalisRepository extends JpaRepository<CommandSendDetails, Long> {

	@Query(value = "SELECT * FROM command_send_details c WHERE c.evidence_files IN :evidenceFiles", nativeQuery = true)
	List<CommandSendDetails> findAllByEvidenceFiles(@Param("evidenceFiles") List<String> evidenceFiles);

	List<CommandSendDetails> findAllByIdIn(Set<Long> commandSendIds);

	@Query(value = "SELECT * FROM command_send_details WHERE (no_of_file_uploaded IS NULL AND re_call_count < :recall) OR (re_call_count IS NULL AND no_of_file_uploaded IS NULL)", nativeQuery = true)
	List<CommandSendDetails> findByNoOfFileUploadedIsNullAndReCallCountLessThanRecallOrReCallCountIsNull(
			@Param("recall") Integer recall);

	public static Specification<CommandSendDetails> search(PaginatedRequestDto paginatedRequest) {
		return (root, cq, cb) -> {
			Predicate predicate = cb.conjunction();

			return predicate;
		};
	}

	Page<CommandSendDetails> findAll(Specification<CommandSendDetails> parentData, Pageable pageable);

	default Page<CommandSendDetails> findAll(PaginatedRequestDto paginatedRequest, Pageable pageable) throws Exception {
		try {
			return findAll(search(paginatedRequest), pageable);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error occurred while fetching data.");

		}
	}

}
