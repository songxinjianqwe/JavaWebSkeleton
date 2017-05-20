package cn.sinjinsong.common.exception;

import cn.sinjinsong.common.exception.annotation.RESTResponseStatus;
import cn.sinjinsong.common.exception.base.BaseRESTException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@RESTResponseStatus(value= HttpStatus.BAD_REQUEST,code =1)
public class ValidationException extends BaseRESTException {
    public ValidationException(List<FieldError> errors) {
		super(errors);
	}
}
