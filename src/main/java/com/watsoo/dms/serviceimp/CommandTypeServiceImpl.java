package com.watsoo.dms.serviceimp;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watsoo.dms.dto.CommandTypeDTO;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.entity.CommandType;
import com.watsoo.dms.repository.CommandTypeRepository;
import com.watsoo.dms.service.CommandTypeService;

@Service
public class CommandTypeServiceImpl implements CommandTypeService {

	@Autowired
	private CommandTypeRepository commandTypeRepository;

	@Override
	public Response<?> createCommandType(CommandTypeDTO commandTypeDTO) {

		if (commandTypeDTO == null || commandTypeDTO.getName() == null) {

			return new Response<>("CommandType filed Can't be Empty", null, 400);
		}

		Optional<CommandType> existingCommandType = commandTypeRepository.findByName(commandTypeDTO.getName());
		if (existingCommandType.isPresent()) {
			return new Response<>("CommandType name already exists", null, 400);
		}
		CommandType commandType = new CommandType();
		commandType.setName(commandTypeDTO.getName());
		commandType.setCreateOn(new Date());
		commandType.setUpdatedOn(new Date());

		CommandType savedCommandType = commandTypeRepository.save(commandType);

		return new Response<>("CommandType created successfully", null, 200);
	}

	@Override
	public Response<?> updateCommandType(Integer id, CommandTypeDTO commandTypeDTO) {

		return null;
	}

	@Override
	public Response<?> getCommandTypeById(Integer id) {
		return null;
	}

	@Override
	public Response<?> getAllCommandTypes() {
		List<CommandType> commandTypes = commandTypeRepository.findAll();
		List<CommandTypeDTO> collect = commandTypes.stream().map(CommandTypeDTO::convertToDTO)
				.collect(Collectors.toList());
		return new Response<>("Success", collect, 200);
	}

}
