package com.watsoo.dms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.watsoo.dms.entity.CredentialMaster;


public interface CredentialMasterRepository extends JpaRepository<CredentialMaster, Long> {

	Optional<CredentialMaster> findByEmail(String username);

	@Query(value = "select * from credential_master where email=?1", nativeQuery = true)
	List<CredentialMaster> findAllByPhoneNoEmail(String email);

	@Query(value = "select * from credential_master where user_code=?1", nativeQuery = true)
	Optional<CredentialMaster> getByUserCode(String userCode);

}
