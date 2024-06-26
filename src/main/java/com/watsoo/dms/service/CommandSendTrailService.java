package com.watsoo.dms.service;

import com.watsoo.dms.dto.CommanddetalisSendDto;
import com.watsoo.dms.dto.Response;

public interface CommandSendTrailService {

	void saveManualCommand(CommanddetalisSendDto commanddetalisSendDto);

	Response<?> getAllCommandByVechileId(Long vechileId);

}
