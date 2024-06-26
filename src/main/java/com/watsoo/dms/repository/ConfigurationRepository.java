package com.watsoo.dms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watsoo.dms.entity.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

	Optional<Configuration> findByKey(String string);

}
