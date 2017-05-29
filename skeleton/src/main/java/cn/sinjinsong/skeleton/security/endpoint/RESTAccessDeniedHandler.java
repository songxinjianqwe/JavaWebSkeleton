package cn.sinjinsong.skeleton.security.endpoint;

import cn.sinjinsong.common.exception.base.BaseRESTException;
import cn.sinjinsong.common.exception.domain.RESTError;
import cn.sinjinsong.common.util.JsonUtil;
import cn.sinjinsong.skeleton.exception.user.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by SinjinSong on 2017/5/9.
 */
@Component
public class RESTAccessDeniedHandler implements AccessDeniedHandler {
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BaseRESTException exception = new AccessDeniedException(authentication.getAuthorities().toString());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(exception.getStatus().value());
        response.getWriter().append(JsonUtil.json(new RESTError(exception.getStatus(),exception.getCode(),exception.getErrors(),exception.getMoreInfoURL())));
    }
}
