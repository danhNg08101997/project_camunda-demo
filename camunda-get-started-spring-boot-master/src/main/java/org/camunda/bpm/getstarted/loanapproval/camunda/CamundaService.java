package org.camunda.bpm.getstarted.loanapproval.camunda;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.getstarted.loanapproval.exception.ServiceException;
import org.camunda.bpm.getstarted.loanapproval.model.request.CommonRequest;
import org.camunda.bpm.getstarted.loanapproval.restTemplate.RestTemplateUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CamundaService {

    @Value("${card.process.service}")
    private String processDomain;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public JSONObject startProcess(String processKey, String businessKey) {

        JSONObject req = new JSONObject();
        req = req.put("businessKey", businessKey);
        req = req.put("content", "");
        req = req.put("contentEn", "");
        req = req.put("sendNotif", "0");
        JSONObject test = null;
        try {
            test = restTemplateUtil.post(req,
                    String.format("%s/engine-rest/process-definition/key/%s/start", processDomain, processKey),
                    httpServletRequest);
//            test = restTemplateUtil.post(req,
//                    String.format("http://localhost:8809/api/process/engine-rest/process-definition/key/card_service_complaint/start", processKey),
//                    httpServletRequest);
        }  catch (Exception e) {
            e.printStackTrace();
        }


        return test;
    }

    public List<CamundaTask> getActiveTask(String processInstanceId) {
        String url = String.format("%s/engine-rest/task?processInstanceId=%s&active=true", processDomain,
                processInstanceId);
        JSONArray datas = null;
        int count = 0;
        try {
            do {
                datas = restTemplateUtil.getReturnArray(null, url, httpServletRequest);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                count++;
            } while ((datas == null || datas.isEmpty()) && count < 5);
            if (datas == null) {
                return new ArrayList<>();
            }

            Type type = new TypeToken<List<CamundaTask>>() {
            }.getType();
            return new Gson().fromJson(datas.toString(), type);
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public List<IdentityLink> getIdentityLinks(String taskId) {
        String url = String.format("%s/engine-rest/task/%s/identity-links", processDomain, taskId);
        JSONArray datas = restTemplateUtil.getReturnArray(null, url, httpServletRequest);
        Type type = new TypeToken<List<IdentityLink>>() {
        }.getType();
        return new Gson().fromJson(datas.toString(), type);
    }

    private JSONObject initTaskVar(Map<String, Object> variables) {
        JSONObject result = new JSONObject();
        JSONObject vals = new JSONObject();

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            vals.put(entry.getKey(), new JSONObject().put("value", entry.getValue()));
        }
        result.put("variables", vals);
        return result;
    }

    public void postCompleteTask(String taskId, Map<String, Object> variables) throws ServiceException {
        try {
            String url = String.format("%s/engine-rest/task/%s/complete", processDomain, taskId);

            JSONObject obj = restTemplateUtil.post(initTaskVar(variables), url, httpServletRequest);
            System.out.println("obj - " + obj);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    public boolean postCompleteTaskWithHandle(String taskId, Map<String, Object> variables) {
        try {
            String url = String.format("%s/engine-rest/task/%s/complete", processDomain, taskId);
            restTemplateUtil.post(initTaskVar(variables), url, httpServletRequest);
            return true;
        } catch (Exception e) {
            if (e.getMessage().contains("No outgoing sequence")) {
            	return true;
            }
            return false;
        }
    }

    public void putVariables(String processId, String varName, Object value) {
        String url = String.format("%s/engine-rest/process-instance/%s/variables/%s", processDomain,
                processId, varName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CommonRequest request = new CommonRequest();
        request.setValue(value);
        restTemplate.put(url, request);
    }

    public void putVariables(String processId, String varName, String type, Object value) {
        String url = String.format("%s/engine-rest/process-instance/%s/variables/%s", processDomain,
                processId, varName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CommonRequest request = new CommonRequest();
        request.setType(type);
        request.setValue(value);
        restTemplate.put(url, request);
    }

}
