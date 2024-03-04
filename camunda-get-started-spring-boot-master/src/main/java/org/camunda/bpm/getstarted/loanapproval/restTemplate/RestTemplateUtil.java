package org.camunda.bpm.getstarted.loanapproval.restTemplate;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.camunda.bpm.getstarted.loanapproval.exception.ServiceException;
import org.camunda.bpm.getstarted.loanapproval.model.response.BaseListResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RestTemplateUtil {

	@Autowired
	private RestTemplate restTemplate;

//    @Autowired
//    private LogService logService;

	private final static Logger LOGGER = Logger.getLogger(RestTemplateUtil.class.getName());

//    public <T> JSONObject callService(T entity, String urlPath, String authen) {
//        JSONObject responseHR = new JSONObject();
//        HttpHeaders headers = new HttpHeaders();
//        try {
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set("Authorization", authen);
//            HttpEntity<T> rq = new HttpEntity<T>(entity, headers);
//            String result = restTemplate.postForObject(urlPath, rq, String.class);
//            if (result != null) {
//                responseHR = new JSONObject(result);
//            }
//        } catch (Exception e) {
//            logService.insertException(e);
//        }
//        return responseHR;
//
//    }

	private String configDomain;
	private String processDomain;
	private String portalDomain;
	private String hrDomain;
	private String notificationDomain;
	private String financeDomain;
	private String bussinessDomain;

	public String getBussinessDomain() {
		return bussinessDomain;
	}

	public void setBussinessDomain(String bussinessDomain) {
		this.bussinessDomain = bussinessDomain;
	}

	public String getFinanceDomain() {
		return financeDomain;
	}

	public void setFinanceDomain(String financeDomain) {
		this.financeDomain = financeDomain;
	}

	public String getNotificationDomain() {
		return notificationDomain;
	}

	public void setNotificationDomain(String notificationDomain) {
		this.notificationDomain = notificationDomain;
	}

	public String getConfigDomain() {
		return configDomain;
	}

	public void setConfigDomain(String configDomain) {
		this.configDomain = configDomain;
	}

	public String getHrDomain() {
		return hrDomain;
	}

	public void setHrDomain(String hrDomain) {
		this.hrDomain = hrDomain;
	}

	public String getProcessDomain() {
		return processDomain;
	}

	public void setProcessDomain(String processDomain) {
		this.processDomain = processDomain;
	}

	public String getPortalDomain() {
		return portalDomain;
	}

	public void setPortalDomain(String portalDomain) {
		this.portalDomain = portalDomain;
	}

	public RestTemplateUtil(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public <T> JSONObject callService(T entity, String urlPath, HttpServletRequest request) {
		JSONObject response = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", request.getHeader("Authorization"));
			HttpEntity<T> rq = new HttpEntity<T>(entity, headers);
			String result = restTemplate.postForObject(urlPath, rq, String.class);
			if (result != null) {
				response = new JSONObject(result);
			}
		} catch (Exception e) {
//            logService.insertException(e);
			throw new ServiceException(e.getMessage());
		}
		return response;

	}

	public <T> JSONObject postUsingModal(T entity, String urlPath, HttpServletRequest request) {
		JSONObject response = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", request.getHeader("Authorization"));
			HttpEntity<String> rq = new HttpEntity<String>(new Gson().toJson(entity), headers);
			ResponseEntity<String> result = restTemplate.postForEntity(urlPath, rq, String.class);
			if (result != null) {

				response = Strings.isNullOrEmpty(result.getBody()) ? null : new JSONObject(result.getBody());
			}
		} catch (Exception e) {
//            logService.insertException(e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	public <T> JSONObject post(JSONObject entity, String urlPath, HttpServletRequest request) throws ServiceException {
		JSONObject response = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", request.getHeader("Authorization"));
			HttpEntity<String> rq = new HttpEntity<String>(entity.toString(), headers);

			ResponseEntity<String> result = restTemplate.postForEntity(urlPath, rq, String.class);
			if (result != null) {

				response = Strings.isNullOrEmpty(result.getBody()) ? null : new JSONObject(result.getBody());
			}
		} catch (Exception e) {
//            logService.insertException(e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	public <T, R> List<T> postForList(Class<T> clazz, R requestBody, String urlPath, HttpServletRequest request) {
		ObjectMapper objectMapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		try {
			String body;
			if (requestBody instanceof JSONObject) {
				body = ((JSONObject) requestBody).toString();
			} else {
				body = new Gson().toJson(requestBody);
			}
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", request.getHeader("Authorization"));
			HttpEntity<String> rq = new HttpEntity<String>(body, headers);
			ResponseEntity<String> result = restTemplate.postForEntity(urlPath, rq, String.class);
			JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
			return readValue(result, javaType);
		} catch (Exception e) {
//            logService.insertException(e);
			throw new ServiceException(e.getMessage());
		}
	}

	public <T, R> T postForObject(Class<T> clazz, R entity, String urlPath, HttpServletRequest request) {
		ObjectMapper objectMapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();

		String body;
		if (entity instanceof JSONObject) {
			body = ((JSONObject) entity).toString();
		} else {
			body = new Gson().toJson(entity);
		}
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			if (request != null) {
				headers.set("Authorization", request.getHeader("Authorization"));
			}
			HttpEntity<String> rq = new HttpEntity<String>(body, headers);
			ResponseEntity<String> result = restTemplate.postForEntity(urlPath, rq, String.class);
			JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
			return readValue(result, javaType);
		} catch (Exception e) {
//            logService.insertException(e);
			throw new ServiceException(e.getMessage());
		}
	}

	private <T> T readValue(ResponseEntity<String> response, JavaType javaType) {
		ObjectMapper objectMapper = new ObjectMapper();
		T result = null;
		if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
			try {
				BaseListResponse bodyData = objectMapper.readValue(response.getBody(), BaseListResponse.class);
				result = objectMapper.convertValue(bodyData.getData(), javaType);
			} catch (IOException e) {
//                logService.insertException(e);
			}
		} else {
			throw new ServiceException("Restemplate failed");
		}
		return result;
	}

	public <T> JSONArray getReturnArray(T entity, String urlPath, HttpServletRequest request) {
		JSONArray response = new JSONArray();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", request.getHeader("Authorization"));
			HttpEntity<T> rq = new HttpEntity<T>(entity, headers);
			String result = restTemplate.getForObject(urlPath, String.class);
			if (result != null) {
				response = new JSONArray(result);
			}
		} catch (Exception e) {
//            logService.insertException(e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	public <T> JSONObject callServiceAuth(T entity, String urlPath, String token) {
		JSONObject response = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", token);
			HttpEntity<T> rq = new HttpEntity<T>(entity, headers);
			String result = restTemplate.postForObject(urlPath, rq, String.class);
			if (result != null) {
				response = new JSONObject(result);
			}
		} catch (Exception e) {
//            logService.insertException(e);
//			throw new ServiceException(e.getMessage());
		}
		return response;

	}

	public <T> JSONObject callServiceNoAuth(T entity, String urlPath) {
		JSONObject response = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<T> rq = new HttpEntity<T>(entity, headers);
			String result = restTemplate.postForObject(urlPath, rq, String.class);
			if (result != null) {
				response = new JSONObject(result);
			}
		} catch (Exception e) {
//            logService.insertException(e);
		}
		return response;

	}

//	public FileTemplate postMultipartFile(MultipartFile file, String urlPath, HttpServletRequest httpServletRequest) {
//		ObjectMapper objectMapper = new ObjectMapper();
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//		headers.set("Authorization", httpServletRequest.getHeader("Authorization"));
//
////        LinkedMultiValueMap<String, Object> requestEntity = new LinkedMultiValueMap<>();
////        requestEntity.add("file", file);
//
//		MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
//		ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
//				.filename(file.getOriginalFilename()).build();
//		try {
//
//			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
//			HttpEntity<byte[]> fileEntity = new HttpEntity<>(file.getBytes(), fileMap);
//			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//			body.add("file", fileEntity);
//
//			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//			ResponseEntity<String> result = restTemplate.exchange(urlPath, HttpMethod.POST, requestEntity,
//					String.class);
//			JavaType javaType = objectMapper.getTypeFactory().constructType(FileTemplate.class);
//			return readValue(result, javaType);
//		} catch (Exception e) {
////            logService.insertException(e);
//		}
//		return null;
//	}
}
