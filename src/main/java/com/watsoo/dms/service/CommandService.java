package com.watsoo.dms.service;


import com.watsoo.dms.dto.CommandDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.entity.Command;

public interface CommandService {
	Response<?> getAllCommands(int pageSize, int pageNo, String deviceModel, Long vechileId, String imeiNumber);
	Response<?> getCommandById(Long id);
	Response<?> createCommand(CommandDto commandDto);
	Response<?> updateCommand(Long id, CommandDto commandDto);
}

