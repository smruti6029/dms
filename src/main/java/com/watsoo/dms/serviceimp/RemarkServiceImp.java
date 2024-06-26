package com.watsoo.dms.serviceimp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watsoo.dms.dto.RemarkDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.entity.Remark;
import com.watsoo.dms.repository.RemarkRepository;
import com.watsoo.dms.service.RemarkService;

@Service
public class RemarkServiceImp implements RemarkService {

	@Autowired
	private RemarkRepository remarkRepository;

	@Override
	public Response<?> getAllRemark() {
		List<RemarkDto> remarkList = new ArrayList<>();
		List<Remark> remarks = remarkRepository.findAll();
		if (remarks != null && remarks.size() > 0) {
			remarkList = remarks.stream().map(RemarkDto::convertToDTO).collect(Collectors.toList());
		}
		return new Response<>("Succes", remarkList, 200);
	}

}
