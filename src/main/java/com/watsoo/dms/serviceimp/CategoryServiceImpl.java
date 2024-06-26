package com.watsoo.dms.serviceimp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.watsoo.dms.dto.CategoryDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.entity.Category;
import com.watsoo.dms.repository.CategoryRepository;
import com.watsoo.dms.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Response<?> getAllCategory() {
		try {

			List<Category> categories = categoryRepository.findAll();
			List<CategoryDto> categoryList = categories.stream().map(CategoryDto::toDTO).collect(Collectors.toList());
			return new Response<>("Error occurred while fetching categories: ", categoryList, 200);
		} catch (Exception e) {
			return new Response<>("Error occurred while fetching categories: ", null, 400);
		}
	}
}
