package cn.sinjinsong.skeleton.controller.user.handler.impl;


import cn.sinjinsong.skeleton.controller.user.handler.QueryUserHandler;
import cn.sinjinsong.skeleton.domain.entity.user.UserDO;
import cn.sinjinsong.skeleton.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by SinjinSong on 2017/5/6.
 */
@Component("QueryUserHandler.email")
public class QueryUserByEmailHandler implements QueryUserHandler {
    @Autowired
    private UserService userService;
    @Override
    public UserDO handle(String key) {
        return userService.findByEmail(key);
    }
}
