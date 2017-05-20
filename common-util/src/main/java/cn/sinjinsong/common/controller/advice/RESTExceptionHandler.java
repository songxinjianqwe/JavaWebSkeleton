package cn.sinjinsong.common.controller.advice;


import cn.sinjinsong.common.exception.base.BaseRESTException;
import cn.sinjinsong.common.exception.domain.RESTError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RESTExceptionHandler {
	
	@ExceptionHandler(BaseRESTException.class)
	public ResponseEntity<RESTError> handle(BaseRESTException e) {
		return new ResponseEntity<>(new RESTError(e.getStatus(), e.getCode(), e.getErrors(), e.getMoreInfoURL()), e.getStatus());
	}
	
}
