package org.camunda.bpm.getstarted.loanapproval.model.response;

public class BaseResponse {
	private Object data;
	private String errorCode;
	private String errorDesc;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public BaseResponse() {
        this.data = null;
        this.errorDesc = null;
        this.errorCode = null;
    }

	public BaseResponse(Object data) {
		this.data = data;
	}

	public BaseResponse(String errCode, String errDesc) {
		super();
		this.errorCode = errCode;
		this.errorDesc = errDesc;
	}
	public BaseResponse(Object data, String errCode, String errDesc) {
		super();
		this.data = data;
		this.errorCode = errCode;
		this.errorDesc = errDesc;
	}
	
	
}
