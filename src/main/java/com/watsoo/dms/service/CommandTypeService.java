package com.watsoo.dms.service;


import com.watsoo.dms.dto.CommandTypeDTO;
import com.watsoo.dms.dto.Response;

public interface CommandTypeService {
	Response<?> createCommandType(CommandTypeDTO commandTypeDTO);
	Response<?> updateCommandType(Integer id, CommandTypeDTO commandTypeDTO);
	Response<?> getCommandTypeById(Integer id);
	Response<?> getAllCommandTypes();
}
