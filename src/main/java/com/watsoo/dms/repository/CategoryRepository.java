package com.watsoo.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watsoo.dms.entity.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
