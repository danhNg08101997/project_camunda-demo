package org.camunda.bpm.getstarted.loanapproval.controller;


import org.camunda.bpm.getstarted.loanapproval.camunda.CamundaService;
import org.camunda.bpm.getstarted.loanapproval.camunda.CamundaTask;
import org.camunda.bpm.getstarted.loanapproval.model.request.CamundaRequest;
import org.camunda.bpm.getstarted.loanapproval.model.response.BaseResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("camunda")
public class CamundaController {
	
	@Autowired
	private CamundaService camundaService;
	
	@PostMapping(value = "/startProcess", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> insertProcess(@RequestBody CamundaRequest request) {
		BaseResponse response = new BaseResponse();
		JSONObject result = camundaService.startProcess(request.getProcessKey(), request.getBusinessKey());
		response.setData(result.toString());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping(value = "/getActiveTask", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> findByCondition(@RequestBody CamundaRequest request) {
		List<CamundaTask> result = camundaService.getActiveTask(request.getProcessInstanceId());
		BaseResponse response = new BaseResponse();
		if(result != null && !result.isEmpty()) {
			response.setData(result.get(0));
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
    }

	@PostMapping(value = "/postCompleteTask", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> postCompleteTask(@RequestBody CamundaRequest request) {
		camundaService.postCompleteTask(request.getTaskId(), request.getVars());
//		BaseResponse response = new BaseResponse();
//		if(result != null && !result.isEmpty()) {
//			response.setData(result.get(0));
//		}
		return null;
	}
	
}
