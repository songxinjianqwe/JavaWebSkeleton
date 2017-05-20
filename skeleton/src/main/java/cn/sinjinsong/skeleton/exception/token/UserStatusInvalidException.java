package cn.sinjinsong.skeleton.exception.token;

import cn.sinjinsong.common.exception.annotation.RESTField;
import cn.sinjinsong.common.exception.annotation.RESTResponseStatus;
import cn.sinjinsong.common.exception.base.BaseRESTException;
import org.springframework.http.HttpStatus;

/**
 * Created by SinjinSong on 2017/4/28.
 */
@RESTResponseStatus(value = HttpStatus.UNAUTHORIZED,code = 3)
@RESTField("userStatus")
public class UserStatusInvalidException extends BaseRESTException {
    public UserStatusInvalidException(String userStatus){
        super(userStatus);
    }
}
