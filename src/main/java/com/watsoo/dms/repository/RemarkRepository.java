package com.watsoo.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watsoo.dms.entity.Remark;

public interface RemarkRepository extends JpaRepository<Remark, Integer> {

	Remark findByStatus(String status);

}
