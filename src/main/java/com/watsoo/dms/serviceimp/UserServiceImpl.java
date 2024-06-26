package com.watsoo.dms.serviceimp;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.watsoo.dms.dto.LoginRequest;
import com.watsoo.dms.dto.LoginResponse;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.dto.UserDto;
import com.watsoo.dms.entity.CredentialMaster;
import com.watsoo.dms.entity.User;
import com.watsoo.dms.repository.CredentialMasterRepository;
import com.watsoo.dms.repository.UserRepository;
import com.watsoo.dms.security.JwtTokenUtil;
import com.watsoo.dms.security.JwtUserDetailsService;
import com.watsoo.dms.service.UserService;
import com.watsoo.dms.validation.Validation;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private CredentialMasterRepository credentialMasterRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public Response<?> login(LoginRequest loginRequest) throws Exception {
		try {
			Response<?> validationResponse = Validation.checkLoginRequest(loginRequest);
			if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
				LoginResponse loginResponse = new LoginResponse();

				UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

				if (userDetails != null) {

					Optional<CredentialMaster> credentialMasterOptional = credentialMasterRepository
							.findByEmail(loginRequest.getEmail());

					if (credentialMasterOptional.isPresent()) {

						CredentialMaster credentialMaster = credentialMasterOptional.get();

						if (credentialMaster.passwordMatches(loginRequest.getPassword())) {
							loginResponse.setUserId(credentialMaster.getUser().getId());
							loginResponse.setEmail(credentialMaster.getEmail());
							loginResponse.setToken(jwtTokenUtil.generateToken(userDetails));
							return new Response<>("Login success.", loginResponse, HttpStatus.OK.value());
						}
						return new Response<>("INVALID CREDENTIALS", null, HttpStatus.BAD_REQUEST.value());
					} else {
						return new Response<>("INVALID CREDENTIALS", null, HttpStatus.BAD_REQUEST.value());
					}
				} else {
					return new Response<>("INVALID CREDENTIALS", null, HttpStatus.BAD_REQUEST.value());
				}
			} else {
				return validationResponse;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Something went wrong", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> saveUser(UserDto userDto) {
		try {
			Response<?> validationResponse = Validation.checkValidUser(userDto);
			if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
				Optional<User> existingUser = userRepository.findAllByPhoneNoEmail(userDto.getPhone(),
						userDto.getEmail());
				if (existingUser.isPresent() && (existingUser.get().getEmail().equals(userDto.getEmail())
						|| existingUser.get().getPhone().equals(userDto.getPhone()))) {
					return new Response<>("Email and phone number cannot be duplicate !!!", null,
							HttpStatus.BAD_REQUEST.value());
				}

				User user = UserDto.convertDtoToEntity(userDto);
				user.setCreatedOn(new Date());
				user.setUpdatedOn(new Date());
				user.setIsActive(true);
				User userSave = userRepository.save(user);

				CredentialMaster credentialMaster = new CredentialMaster();
				credentialMaster.setUser(userSave);
				credentialMaster.setEmail(userDto.getEmail());
				credentialMaster.setCreatedOn(new Date());
				credentialMaster.setUpdatedOn(new Date());
				credentialMaster.setIsActive(true);

				credentialMaster.setPassword("1234");

				CredentialMaster savedCredentialMaster = credentialMasterRepository.save(credentialMaster);

				if (savedCredentialMaster != null) {
					return new Response<>("User Registered Succefully  !!!", savedCredentialMaster,
							HttpStatus.OK.value());
				} else {
					return new Response<>("Failed in User Registeration!!!", null, HttpStatus.BAD_REQUEST.value());
				}
			} else {
				return validationResponse;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Register user service goes wrong.", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> getById(long userId) {
		try {
			Optional<User> user = userRepository.findById(userId);
			if (user != null && user.isPresent()) {
				return new Response<>("success", user.get(), HttpStatus.OK.value());
			}
			return new Response<>("User not found", null, HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Something went wrong", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> searchByName(String userName) {

		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
