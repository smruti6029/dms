package com.watsoo.dms.serviceimp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watsoo.dms.dto.CommandSendTrailDto;
import com.watsoo.dms.dto.CommanddetalisSendDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.dto.UserDto;
import com.watsoo.dms.entity.CommandSendTrail;
import com.watsoo.dms.entity.User;
import com.watsoo.dms.repository.CommandSendTrailRepository;
import com.watsoo.dms.repository.UserRepository;
import com.watsoo.dms.service.CommandSendTrailService;

@Service
public class CommandSendTrailServiceImp implements CommandSendTrailService {

	@Autowired
	private CommandSendTrailRepository commandSendTrailRepository;

	@Autowired
	private UserRepository userRepository;;

	@Override
	public void saveManualCommand(CommanddetalisSendDto commanddetalisSendDto) {
		
		try {
		CommandSendTrail commandSendTrail = new CommandSendTrail();

		commandSendTrail.setCommand(commanddetalisSendDto.getCommand());
		commandSendTrail.setVechileId(commanddetalisSendDto.getVechileId());
		commandSendTrail.setCreatedOn(new Date());
		commandSendTrail.setUserId(commanddetalisSendDto.getUserId());
		commandSendTrail.setDescription(commanddetalisSendDto.getDescription());
		commandSendTrail.setUseCommand(commanddetalisSendDto.getUseCommand());
		commandSendTrailRepository.save(commandSendTrail);
		}catch (Exception e) {
		}

	}

	@Override
	public Response<?> getAllCommandByVechileId(Long vechileId) {
		
		try {
		if (vechileId == null) {
			return new Response<>("Vechile Id Required", null, 400);
		}

		List<CommandSendTrail> findByVechileId = commandSendTrailRepository.findByVechileId(vechileId);

		List<CommandSendTrailDto> listOFCommandendTrail = new ArrayList<>();

		if (findByVechileId != null && findByVechileId.size() > 0) {
			Set<Long> allUserId = findByVechileId.stream().map(x -> x.getUserId()).collect(Collectors.toSet());
			List<User> findByIdIn = userRepository.findByIdIn(allUserId);
			Map<Long, User> collectUserDetalis = findByIdIn.stream()
					.collect(Collectors.toMap(User::getId, user -> user));

			for (CommandSendTrail commandSendTrail : findByVechileId) {
				CommandSendTrailDto entityToDto = CommandSendTrailDto.entityToDto(commandSendTrail);

				entityToDto
						.setUserDto(UserDto.convertEntityToDto(collectUserDetalis.get(commandSendTrail.getUserId())));
				listOFCommandendTrail.add(entityToDto);

			}

			Collections.sort(listOFCommandendTrail, Comparator.comparing(CommandSendTrailDto::getCreatedOn).reversed());

		}

		return new Response<>("Success", listOFCommandendTrail, 200);
		}catch (Exception e) {
			return new Response<>("Something went wrong", null, 400);
		}

	}

}
