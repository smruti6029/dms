package com.watsoo.dms.service;

import java.util.List;
import java.util.Map;

import com.watsoo.dms.dto.CommanddetalisSendDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.entity.Event;

public interface CommandSendDetalisService {

	void saveCommandDetalis(List<Event> saveAll, Map<Long, String> devicePositionProtocol);

	void sendCommand(int reCallCount, int processSleepTime);


	Response<?> sendCommandManually(CommanddetalisSendDto commanddetalisSendDto);

	Response<?> getAllCommandDetalis(int pageSize, int pageNo);


	Long updateCommandResponse(Long batchSize, Long positionID);

	

}
