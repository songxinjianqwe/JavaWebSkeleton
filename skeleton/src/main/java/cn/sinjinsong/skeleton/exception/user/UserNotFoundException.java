package cn.sinjinsong.skeleton.exception.user;

import cn.sinjinsong.common.exception.annotation.RESTField;
import cn.sinjinsong.common.exception.annotation.RESTResponseStatus;
import cn.sinjinsong.common.exception.base.BaseRESTException;
import org.springframework.http.HttpStatus;

/**
 * Created by SinjinSong on 2017/5/6.
 */
@RESTResponseStatus(value = HttpStatus.NOT_FOUND,code=8)
@RESTField("queryKey")
public class UserNotFoundException extends BaseRESTException{
    public UserNotFoundException(String key){
        super(key);
    }
}
