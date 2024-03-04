package org.camunda.bpm.getstarted.loanapproval.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    private String actionType = "UPDATE";
    private String module = "FILE_SERVICE";

    public ServiceException() {
        super();
        this.code = String.valueOf(status.value());
        this.message = "Data invalid";
    }

    public ServiceException(String message) {
        super(message);
        this.code = String.valueOf(status.value());
        this.message = message;
    }

    public ServiceException(String code, String message, HttpStatus status) {
        super();
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public ServiceException(String message, HttpStatus status) {
        super();
        this.message = message;
        this.status = status;
        this.code = String.valueOf(status.value());
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
