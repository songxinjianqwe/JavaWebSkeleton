package cn.sinjinsong.skeleton.exception.mail;

import cn.sinjinsong.common.exception.annotation.RESTField;
import cn.sinjinsong.common.exception.annotation.RESTResponseStatus;
import cn.sinjinsong.common.exception.base.BaseRESTException;
import org.springframework.http.HttpStatus;

/**
 * Created by SinjinSong on 2017/5/5.
 */
@RESTResponseStatus(value= HttpStatus.NOT_FOUND,code=6)
@RESTField("mailTarget")
public class MailTargetNotFoundException extends BaseRESTException{
    public MailTargetNotFoundException(String target){
        super(target);
    }
}
