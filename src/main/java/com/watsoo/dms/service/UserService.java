package com.watsoo.dms.service;


import com.watsoo.dms.dto.LoginRequest;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.dto.UserDto;

public interface UserService {

	Response<?> login(LoginRequest loginRequest) throws Exception;

	Response<?> searchByName(String userName);

	Response<?> getById(long userId);

	Response<?> saveUser(UserDto userDto);
	
	
}
