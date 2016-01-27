package com.xst.bigwhite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
public class RestRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -10000000011L;

	private Errors errors;
	private String code;

	public RestRuntimeException(String message){
		this("InvalidRequest",message);
	}
	
	public RestRuntimeException(String code,String message){
		super(message);
		this.code = code;
	}
	
    public RestRuntimeException(String code,String message, Errors errors) {
        this(code,message);
        this.errors = errors;
    }

    public Errors getErrors() { return errors; }
    
    public String getCode(){return code;}

}