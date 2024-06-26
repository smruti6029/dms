package com.watsoo.dms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.watsoo.dms.entity.CommandType;

@Repository
public interface CommandTypeRepository extends JpaRepository<CommandType, Long> {

	Optional<CommandType> findByName(String name);
}
