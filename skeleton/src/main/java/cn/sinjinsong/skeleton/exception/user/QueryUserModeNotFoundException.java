package cn.sinjinsong.skeleton.exception.user;

import cn.sinjinsong.common.exception.annotation.RESTField;
import cn.sinjinsong.common.exception.annotation.RESTResponseStatus;
import cn.sinjinsong.common.exception.base.BaseRESTException;
import org.springframework.http.HttpStatus;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@RESTResponseStatus(value = HttpStatus.NOT_FOUND,code =1)
@RESTField("queryMode")
public class QueryUserModeNotFoundException extends BaseRESTException {
    public QueryUserModeNotFoundException(String mode){
        super(mode);
    }
}
