package com.watsoo.dms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watsoo.dms.entity.CommandSendTrail;

public interface CommandSendTrailRepository extends JpaRepository<CommandSendTrail, Long> {

	List<CommandSendTrail> findByVechileId(Long vechileId);

}
