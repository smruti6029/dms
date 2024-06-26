package com.watsoo.dms.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import com.watsoo.dms.dto.CommandDto;
import com.watsoo.dms.dto.CommanddetalisSendDto;
import com.watsoo.dms.dto.EventDto;
import com.watsoo.dms.dto.LoginRequest;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.dto.UserDto;

public class Validation {

	public static Response<?> checkLoginRequest(LoginRequest loginRequest) {
		if (loginRequest.getEmail() == null || loginRequest.getEmail().isBlank()) {
			return new Response<>("Email cannot be empty", null, HttpStatus.BAD_REQUEST.value());
		} else if (!isValidEmail(loginRequest.getEmail())) {
			return new Response<>("Invalid email format", null, HttpStatus.BAD_REQUEST.value());
		} else if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
			return new Response<>("Password cannot be empty", null, HttpStatus.BAD_REQUEST.value());
		} else {
			return new Response<>("success", null, HttpStatus.OK.value());
		}
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		String phoneRegex = "^[0-9]{10}$";
		Pattern pattern = Pattern.compile(phoneRegex);
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.matches();
	}

	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static Response<?> checkValidUser(UserDto userDto) {

		if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
			return new Response<>("Email cannot be empty", null, HttpStatus.BAD_REQUEST.value());
		} else if (!isValidEmail(userDto.getEmail())) {
			return new Response<>("Invalid email format", null, HttpStatus.BAD_REQUEST.value());
		} else if (userDto.getPhone() == null || userDto.getPhone().isBlank()) {
			return new Response<>("Phone Number cannot be empty", null, HttpStatus.BAD_REQUEST.value());
		} else if (userDto.getName() == null || userDto.getName().isBlank()) {
			return new Response<>("Name cannot be empty", null, HttpStatus.BAD_REQUEST.value());
		} else {
			return new Response<>("success", null, HttpStatus.OK.value());
		}
	}

	public static Response<?> validateCommandDto(CommandDto command) {

		if (command.getCommand() == null || command.getCommand().isEmpty()) {
		    return new Response<>("Command is required", null, 400);
		} else if (!command.getCommand().endsWith("#")) {
		    return new Response<>("Command must end with #", null, 400);
		}

		if (command.getVechile_id() == null) {
			return new Response<>("Vechile id is required", null, 400);
		}

		if (command.getImeiNumber() == null) {
			return new Response<>("Imei Number is required", null, 400);
		}

		if (command.getDescription() == null) {
			return new Response<>("Description is required", null, 400);

		}

		if (command.getVechileNumber() == null) {
			return new Response<>("Vechile Number  is required", null, 400);

		}
		return null;
	}

	public static Response<?> checkCommandDetalis(CommanddetalisSendDto command) {

		if (command.getVechileId() == null) {
			return new Response<>("Vechile id is required", null, 400);
		}

		if (command.getUserId() == null) {
			return new Response<>("User  is required", null, 400);
		}

		if (command.getCommand() == null && command.getCommand().isEmpty()) {
			return new Response<>("Command Can't be Empty", null, 400);
		}

		if (command.getDescription() == null) {
			return new Response<>("Description is required", null, 400);

		}

		if (command.getUseCommand() == null) {
			return new Response<>("The 'useCommand' field is required.", null, 400);

		}

		if (command.getDeviceId() == null)
			return new Response<>("Device id is required", null, 400);
		return null;

	}

	public static Response<?> checkEventUpdateDto(EventDto eventDto) {

		if (eventDto.getId() == null) {
			return new Response<>("Event id is required", null, 400);
		}

		if (eventDto.getRemarkId() == null) {
			return new Response<>("Remark id is required", null, 400);
		}
		if (eventDto.getRemark() == null) {
			return new Response<>("Remark  is required", null, 400);
		}
		return null;
	}

}
