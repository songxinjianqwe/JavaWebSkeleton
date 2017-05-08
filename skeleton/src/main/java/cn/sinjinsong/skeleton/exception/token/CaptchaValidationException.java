package cn.sinjinsong.skeleton.exception.token;

import cn.sinjinsong.common.exception.annotation.RESTField;
import cn.sinjinsong.common.exception.annotation.RESTResponseStatus;
import cn.sinjinsong.common.exception.base.BaseRESTException;
import org.springframework.http.HttpStatus;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@RESTResponseStatus(value = HttpStatus.UNAUTHORIZED, code = 2)
@RESTField("captcha")
public class CaptchaValidationException extends BaseRESTException {
    public CaptchaValidationException(String value){
        super(value);
    }
}
