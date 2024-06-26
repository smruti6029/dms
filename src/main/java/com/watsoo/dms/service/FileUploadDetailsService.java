package com.watsoo.dms.service;

import java.util.List;

import com.watsoo.dms.entity.CommandSendDetails;

public interface FileUploadDetailsService {

	void saveFileDetalis(List<CommandSendDetails> saveAllCommandDetalis);

	void updateFlleDetalis(int reCallCount);
	void updateFlleDetalisV2();

}
