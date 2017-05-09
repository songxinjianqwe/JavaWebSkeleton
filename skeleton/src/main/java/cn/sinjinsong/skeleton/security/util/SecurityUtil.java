package cn.sinjinsong.skeleton.security.util;

import cn.sinjinsong.skeleton.security.domain.JWTUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by SinjinSong on 2017/5/9.
 */
public class SecurityUtil {
    public static Long currentUserId(){
        return ((JWTUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
    
    public static String currentUsername(){
         return ((JWTUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
    
    
}
