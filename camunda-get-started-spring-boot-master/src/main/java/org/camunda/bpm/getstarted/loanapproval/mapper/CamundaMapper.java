package org.camunda.bpm.getstarted.loanapproval.mapper;

import org.camunda.bpm.getstarted.loanapproval.model.response.CamundaHisDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CamundaMapper {

	List<CamundaHisDTO> getCamundaHistoryList(CamundaHisDTO request);

	CamundaHisDTO getLastestHistory(CamundaHisDTO request);
	
}
